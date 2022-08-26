package com.b2.b2data.controller;

import com.b2.b2data.domain.Entry;
import com.b2.b2data.dto.DTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.ValidationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A base class for all Controllers
 *
 * @param <T> An entry
 * @param <U> A DTO
 * @param <V> The public key type of U
 */
public abstract class Controller<T extends Entry, U extends DTO, V> {

    /**
     * Transfers the given DTO's values into the given entry
     *
     * @param dto A DTO; must not be null
     * @param entry An entry; must not be null
     * @return An entry with field values matching the DTO
     */
    protected abstract T convertDtoToEntry(U dto, T entry);

    /**
     * Handles NoSuchElementExceptions
     *
     * @param e A NoSuchElementException
     * @return A 404 Not Found response entity
     */
    @ExceptionHandler({NoSuchElementException.class})
    private ResponseEntity<Response<U>> handleException(NoSuchElementException e) {
        return responseCodeNotFound(e.getMessage());
    }

    /**
     * Handles HttpMessageNotReadableExceptions
     *
     * @param e An HttpMessageNotReadableException
     * @return A 400 Bad Request response entity
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    private ResponseEntity<Response<U>> handleException(HttpMessageNotReadableException e) {
        return responseCodeBadRequest(e.getMessage());
    }

    /**
     * Handles ValidationExceptions
     *
     * @param e A ValidationException
     * @return A 400 Bad Request response entity
     */
    @ExceptionHandler({ValidationException.class})
    private ResponseEntity<Response<U>> handleException(ValidationException e) {
        return responseCodeBadRequest(e.getMessage());
    }

    /**
     * Handles MethodArgumentNotValidExceptions
     *
     * @param e A MethodArgumentNotValidException
     * @return A 400 Bad Request response entity
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    private ResponseEntity<Response<U>> handleException(MethodArgumentNotValidException e) {
        return responseCodeBadRequest(e.getMessage());
    }

    /**
     * Handles SQLIntegrityConstraintViolationExceptions
     *
     * @param e A SQLIntegrityConstraintViolationException
     * @return A 400 Bad Request response entity
     */
    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    private ResponseEntity<Response<U>> handleException(SQLIntegrityConstraintViolationException e) {
        return responseCodeBadRequest(e.getMessage());
    }

    /**
     * Creates a response entity indicating that the request was successful
     *
     * @param data A list of data to send in the response body
     * @return A 200 OK response entity
     */
    public ResponseEntity<Response<U>> responseCodeOk(List<U> data) {
        HttpStatus status = HttpStatus.OK;
        return responseEntity(status, status.name(), data, null);
    }

    /**
     * Creates a response entity indicating that the request was successful
     *
     * @param data A list of data to send in the response body
     * @param oldPath The old URI path id of an updated resource
     * @param newPath The new URI path id of an updated resource
     * @return A 200 OK response entity with a location header
     */
    public ResponseEntity<Response<U>> responseCodeOk(List<U> data, String oldPath, String newPath) {
        HttpStatus status = HttpStatus.OK;
        return responseEntity(
                status,
                status.name(),
                data,
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .toUriString()
                        .replace(oldPath, "")
                        +newPath
        );
    }

    /**
     * Creates a response entity indicating that a new resource has been created
     *
     * @param data A list of data to send in the response body
     * @param pathId The URI path id of a new resource
     * @return A 201 Created response entity with a location header
     */
    public ResponseEntity<Response<U>> responseCodeCreated(List<U> data, String pathId) {
        HttpStatus status = HttpStatus.CREATED;
        return responseEntity(
                status,
                status.name(),
                data,
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .toUriString()
                        +pathId
        );
    }

    /**
     * Creates a response entity indicating that there is no content to return
     *
     * @return A 204 No Content response entity
     */
    public ResponseEntity<Response<U>> responseCodeNoContent() {
        HttpStatus status = HttpStatus.NO_CONTENT;
        return responseEntity(status, status.name(), null, null);
    }

    /**
     * Creates a response entity indicating a bad request
     *
     * @param message An error message
     * @return A 400 Bad Request response entity
     */
    public ResponseEntity<Response<U>> responseCodeBadRequest(String message) {
        return responseEntity(HttpStatus.BAD_REQUEST, message, null, null);
    }

    /**
     * Creates a response entity indicating that the requested resource does not exist
     *
     * @param message An error message
     * @return A 404 Not Found response entity
     */
    public ResponseEntity<Response<U>> responseCodeNotFound(String message) {
        return responseEntity(HttpStatus.NOT_FOUND, message, null, null);
    }

    /**
     * Creates a response entity
     *
     * @param status An HTTP status
     * @param message A response message
     * @param data A list of data to send in the response body
     * @param pathId The URI path id of a new or updated resource
     * @return A response entity
     */
    private ResponseEntity<Response<U>> responseEntity(HttpStatus status, String message,
                                                       List<U> data, String pathId) {
        if (pathId != null) {
            return ResponseEntity
                    .status(status)
                    .header("Location", pathId)
                    .body(response(status, message, data));
        }
        return ResponseEntity
                .status(status)
                .body(response(status, message, data));
    }

    /**
     * Creates a response
     *
     * @param status An HTTP status
     * @param message A response message
     * @param data A list of data to send in the response body
     * @return A response
     */
    private Response<U> response(HttpStatus status, String message, List<U> data) {
        Response<U> response = new Response<>();
        response.setStatus(status.value());
        response.setMessage(message);
        response.setData(data);
        response.setPath(ServletUriComponentsBuilder.fromCurrentRequest().toUriString());

        return response;
    }
}
