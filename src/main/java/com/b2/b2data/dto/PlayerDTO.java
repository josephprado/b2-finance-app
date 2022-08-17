package com.b2.b2data.dto;

import com.b2.b2data.domain.Player;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * A data transfer object for transmitting {@link Player} entities to and from the client
 */
public class PlayerDTO extends DTO {

    @NotBlank
    private String name;

    @NotNull
    private Boolean isBank;

    /**
     * Constructs a new player DTO
     */
    public PlayerDTO() {
    }

    /**
     * Constructs a new player DTO
     *
     * @param player A player; must not be null
     * @throws IllegalArgumentException If the player is null
     */
    public PlayerDTO(Player player) throws IllegalArgumentException {
        if (player == null)
            throw new IllegalArgumentException("player must not be null.");

        name = player.getName();
        isBank = player.getBank();
    }

    /**
     * Checks the equality of two player DTOs
     *
     * @param o The other player DTO to compare with this player DTO
     * @return True if the other player DTO is equal to this player DTO, or false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof PlayerDTO playerDTO))
            return false;

        return Objects.equals(name, playerDTO.name) && Objects.equals(isBank, playerDTO.isBank);
    }

    /**
     * Returns a hash code value for the player DTO
     *
     * @return A hash code value for the player DTO
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, isBank);
    }

    /**
     * Returns a string representation of the player DTO
     *
     * @return A string representation of the player DTO in the following format:
     * <br/><br/>PlayerDTO{name='name', isBank=isBank}
     */
    @Override
    public String toString() {
        return "PlayerDTO{" +
                "name='" + name + '\'' +
                ", isBank=" + isBank +
                '}';
    }

    /**
     * Gets the name of the player DTO
     *
     * @return The name of the player DTO
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the player DTO
     *
     * @param name A unique name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the isBank status of the player DTO
     *
     * @return True if the player is a bank, or false otherwise
     */
    public Boolean getBank() {
        return isBank;
    }

    /**
     * Sets the isBank status of the player DTO
     *
     * @param bank True if the player is a bank, or false otherwise
     */
    public void setBank(Boolean bank) {
        isBank = bank;
    }
}
