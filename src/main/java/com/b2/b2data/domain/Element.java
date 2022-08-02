package com.b2.b2data.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "element")
public class Element extends Entry {

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

    public Element() {
    }

    public Element(Integer number, String name) {
        this.number = number;
        this.name = name;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(id, number, name);
    }

    @Override
    public String toString() {
        return "Element{" +
                "id=" + id +
                ", number=" + number +
                ", name='" + name + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
