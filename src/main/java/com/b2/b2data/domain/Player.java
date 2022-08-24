package com.b2.b2data.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Represents a person, company, or other entity (such as a vendor, bank, customer, etc.)
 */
@Entity
@Table(name = "player")
public class Player extends Entry {

    // field names
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String IS_BANK = "isBank";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", unique = true, nullable = false)
    @NotBlank
    private String name;

    @Column(name = "is_bank", nullable = false)
    @NotNull
    private Boolean isBank;

    /**
     * Constructs a new player
     */
    public Player() {
    }

    /**
     * Constructs a new player
     *
     * @param name A unique name
     * @param isBank True if the player is a bank, or false otherwise
     * @see #setBank(Boolean) Definiton of a bank
     */
    public Player(String name, Boolean isBank) {
        this.name = name;
        this.isBank = isBank;
    }

    /**
     * Checks the equality of two players
     *
     * @param o The other player to compare with this player
     * @return True if the other player is equal to this player, or false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Player player))
            return false;

        return Objects.equals(id, player.id)
                && Objects.equals(name, player.name)
                && Objects.equals(isBank, player.isBank);
    }

    /**
     * Returns a hash code value for the player
     *
     * @return a hash code value for the player
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, isBank);
    }

    /**
     * Returns a string representation of the player
     *
     * @return A string representation of the player in the following format:
     * <br/><br/>Player{id=id, name='name', isBank=isBank}
     */
    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isBank=" + isBank +
                '}';
    }

    /**
     * Gets the id of the player
     *
     * @return The id of the player
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the player
     *
     * @param id A unique identifier
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the name of the player
     *
     * @return The name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the player
     *
     * @param name A unique name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the isBank status of the player
     *
     * @return True if the player is a bank, or false otherwise. See {@link #setBank}
     *         for the definition of a bank
     */
    public Boolean getBank() {
        return isBank;
    }

    /**
     * Sets the isBank status of the player
     *
     * @param bank True if the player is a bank. The term 'bank' refers to any
     *             financial institution where the user owns a monetary account.
     *             Use false if the player is a literal financial institution but
     *             holds no accounts owned by the user.
     */
    public void setBank(Boolean bank) {
        isBank = bank;
    }
}
