package com.b2.b2data.dto;

import com.b2.b2data.domain.Element;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * A data transfer object for transmitting {@link Element} entities to and from the client
 */
public class ElementDTO extends DTO {

    @NotNull
    private Integer number;

    @NotBlank
    private String name;

    /**
     * Constructs a new element DTO
     */
    public ElementDTO() {
    }

    /**
     * Constructs a new element DTO
     *
     * @param element An element; must not be null
     * @throws IllegalArgumentException If the element is null
     */
    public ElementDTO(Element element) throws IllegalArgumentException {
        if (element == null)
            throw new IllegalArgumentException("element must not be null.");

        number = element.getNumber();
        name = element.getName();
    }

    /**
     * Checks the equality of two element DTOs
     *
     * @param o The other element DTO to compare with this element DTO
     * @return True if the other element DTO is equal to this element DTO, or false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof ElementDTO that))
            return false;

        return Objects.equals(number, that.number) && Objects.equals(name, that.name);
    }

    /**
     * Returns a hash code value for the element DTO
     *
     * @return A hash code value for the element DTO
     */
    @Override
    public int hashCode() {
        return Objects.hash(number, name);
    }

    /**
     * Returns a string representation of the element DTO
     *
     * @return A string representation of the element DTO in the following format:
     * <br/><br/>ElementDTO{number=number, name='name'}
     */
    @Override
    public String toString() {
        return "ElementDTO{" +
                "number=" + number +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * Gets the number of the element DTO
     *
     * @return The number of the element DTO
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * Sets the number of the element DTO
     *
     * @param number A unique number
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * Gets the name of the element DTO
     *
     * @return The name of the element DTO
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the element DTO
     *
     * @param name A unique name
     */
    public void setName(String name) {
        this.name = name;
    }
}
