package com.b2.b2data.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Represents an accounting element
 */
@Entity
@Table(name = "element")
public class Element extends Entry {

    // field names
    public static final String ID = "id";
    public static final String NUMBER = "number";
    public static final String NAME = "name";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "number", unique = true, nullable = false)
    @NotNull
    private Integer number;

    @Column(name = "name", unique = true, nullable = false)
    @NotBlank
    private String name;

    /**
     * Constructs a new element
     */
    public Element() {
    }

    /**
     * Constructs a new element
     *
     * @param number A unique number
     * @param name A unique name
     */
    public Element(Integer number, String name) {
        this.number = number;
        this.name = name;
    }

    /**
     * Checks the equality of two elements
     *
     * @param o The other element to compare with this element
     * @return True if the other element is equal to this element, or false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Element element))
            return false;

        return Objects.equals(id, element.id)
                && number.equals(element.number)
                && name.equals(element.name);
    }

    /**
     * Returns a hash code value for the element
     *
     * @return A hash code value for the element
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, number, name);
    }

    /**
     * Returns a string representation of the element
     *
     * @return A string representation of the element in the following format:
     * <br/><br/>Element{id=id, number=number, name='name'}
     */
    @Override
    public String toString() {
        return "Element{" +
                "id=" + id +
                ", number=" + number +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * Gets the id of the element
     *
     * @return The id of the element
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the element
     *
     * @param id A unique identifier
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the number of the element
     *
     * @return The number of the element
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * Sets the number of the element
     *
     * @param number A unique number
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * Gets the name of the element
     *
     * @return The name of the element
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the element
     *
     * @param name A unique name
     */
    public void setName(String name) {
        this.name = name;
    }
}
