package com.b2.b2data.controller;

import com.b2.b2data.dto.DTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Represents an HTTP response object
 *
 * @param <T> A subtype of DTO
 */
public class Response<T extends DTO> {

    private LocalDateTime timestamp;
    private Integer status;
    private List<T> data;
    private String message;
    private String path;

    /**
     * Constructs a new response with the current date and time
     */
    public Response() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Checks the equality of two responses
     *
     * @param o The other response to compare with this response
     * @return True if the other response is equal to this response, or false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Response<?> response))
            return false;

        return Objects.equals(timestamp, response.timestamp)
                && Objects.equals(status, response.status)
                && Objects.equals(data, response.data)
                && Objects.equals(message, response.message)
                && Objects.equals(path, response.path);
    }

    /**
     * Returns a hash code value for the response
     *
     * @return A hash code value for the response
     */
    @Override
    public int hashCode() {
        return Objects.hash(timestamp, status, data, message, path);
    }

    /**
     * Returns a string representation of the response
     *
     * @return A string representation of the response
     */
    @Override
    public String toString() {
        return "Response{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", data=" + data +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    /**
     * Gets the date and time of the response
     *
     * @return The date and time of the response
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the data and time of the response
     *
     * @param timestamp A data time
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the HTTP status code of the response
     *
     * @return The HTTP status code of the response
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Sets the HTTP status code of the response
     *
     * @param status An HTTP status code
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Gets the data of the response
     *
     * @return A list of T objects
     */
    public List<T> getData() {
        return data;
    }

    /**
     * Sets the data of the response
     *
     * @param data A list of T objects
     */
    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * Gets the message of the response
     *
     * @return The message of the response
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message of the response
     *
     * @param message A description of the response
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the path of the response's resource
     *
     * @return A URL to the response's resource
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path of the response
     *
     * @param path A URL to the path's resource
     */
    public void setPath(String path) {
        this.path = path;
    }
}
