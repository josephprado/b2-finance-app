package com.b2.b2data.controller;

import com.b2.b2data.domain.Element;
import com.b2.b2data.dto.ElementDTO;
import com.b2.b2data.service.ElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

/**
 * Controls requests for {@link Element} resources
 */
@RestController
@RequestMapping("/api/elements")
public class ElementController extends Controller<Element, ElementDTO, Integer> {

    @Autowired
    private ElementService svc;

    /**
     * Gets all elements from the database
     *
     * @return A response entity containing a list of element DTOs, sorted by number ascending
     */
    @GetMapping("")
    public ResponseEntity<Response<ElementDTO>> getAll() {
        List<ElementDTO> data = svc.findAll().stream().map(ElementDTO::new).toList();
        return responseCodeOk(data);
    }

    /**
     * Gets the element with the given number
     *
     * @param number An element number
     * @return A response entity containing a DTO of the requested element, or an error message
     *         if it does not exist
     */
    @GetMapping("/{number}")
    public ResponseEntity<Response<ElementDTO>> getByNumber(@PathVariable(name = "number") Integer number) {
        Element element;

        try {
            element = getExistingEntry(number);

        } catch (ValidationException e) {
            return responseCodeNotFound(e.getMessage());
        }
        return responseCodeOk(List.of(new ElementDTO(element)));
    }

    /**
     * Creates a new element in the database
     *
     * @param dto An element DTO
     * @return A response entity containing a DTO of the newly created element, or an error message
     *         if the creation was unsuccessful
     */
    @PostMapping("")
    public ResponseEntity<Response<ElementDTO>> createOne(@Valid @RequestBody ElementDTO dto) {
        Element element;

        try {
            element = svc.save(convertDtoToEntry(dto, new Element()));

        } catch (Exception e) {
            return responseCodeBadRequest(e.getMessage());
        }
        return responseCodeCreated(
                List.of(new ElementDTO(element)),
                "/"+element.getNumber()
        );
    }

    /**
     * Updates an existing element in the database
     *
     * @param number An element number
     * @param dto An element DTO containing the updates to be saved
     * @return A response entity containing a DTO of the updated element, or an error message
     *         if the update was unsuccessful
     */
    @PatchMapping("/{number}")
    public ResponseEntity<Response<ElementDTO>> updateOne(@PathVariable(name = "number") Integer number,
                                                          @Valid @RequestBody ElementDTO dto) {
        Element element;

        try {
            element = getExistingEntry(number);

        } catch (ValidationException e) {
            return responseCodeNotFound(e.getMessage());

        } try {
            element = svc.save(convertDtoToEntry(dto, element));

        } catch (Exception e) {
            return responseCodeBadRequest(e.getMessage());
        }
        return responseCodeOk(
                List.of(new ElementDTO(element)),
                "/"+number,
                "/"+element.getNumber()
        );
    }

    /**
     * Deletes an existing element from the database
     *
     * @param number An element number
     * @return A response entity containing the result of the deletion
     */
    @DeleteMapping("/{number}")
    public ResponseEntity<Response<ElementDTO>> deleteOne(@PathVariable(name = "number") Integer number) {
        Element element;

        try {
            element = getExistingEntry(number);

        } catch (ValidationException e) {
            return responseCodeNotFound(e.getMessage());

        } try {
            svc.delete(element);

        } catch (Exception e) {
            return responseCodeBadRequest(e.getMessage());
        }
        return responseCodeNoContent();
    }

    /**
     * Returns the element with the given number
     *
     * @param number An element number
     * @return The element with the given number
     * @throws ValidationException If the element number does not exist
     */
    @Override
    protected Element getExistingEntry(Integer number) throws ValidationException {
        Element element = svc.findByNumber(number);

        if (element == null)
            throw new ValidationException("Element number="+number+" does not exist.");

        return element;
    }

    /**
     * Transfers the given DTO's values into the given element
     *
     * @param dto An element DTO; must not be null
     * @param element An element; must not be null
     * @return An element with field values matching the element DTO
     */
    @Override
    protected Element convertDtoToEntry(ElementDTO dto, Element element) {
        assert dto != null;
        assert element != null;

        // to avoid mutating element parameter
        Integer id = element.getId();
        element = new Element();
        element.setId(id);

        element.setNumber(dto.getNumber());
        element.setName(dto.getName());

        return element;
    }
}
