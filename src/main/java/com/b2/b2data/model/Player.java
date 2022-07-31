package com.b2.b2data.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Represents a person, company, or other entity (such as a vendor, bank, customer, etc.)
 */
@Entity
@Table(name = "player")
public class Player extends Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank
    private String name;

    @Column(name = "is_bank", nullable = false)
    private boolean isBank;

    public Player() {
    }

    public Player(String name, boolean isBank) {
        this.name = name;
        this.isBank = isBank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Player player))
            return false;

        return isBank == player.isBank
                && id.equals(player.id)
                && name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isBank);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isBank=" + isBank +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBank() {
        return isBank;
    }

    public void setBank(boolean bank) {
        isBank = bank;
    }
}
