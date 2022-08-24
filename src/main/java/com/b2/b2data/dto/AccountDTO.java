package com.b2.b2data.dto;

import com.b2.b2data.domain.Account;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * A data transfer object for transmitting {@link Account} entities to and from the client
 */
public class AccountDTO extends DTO {

    @NotBlank
    private String number;

    @NotBlank
    private String name;

    @NotNull
    private Integer elementNumber;
    private String playerName;

    /**
     * Constructs a new account DTO
     */
    public AccountDTO() {
    }

    /**
     * Constructs a new account DTO
     *
     * @param account An account; must not be null
     * @throws IllegalArgumentException If the account is null
     */
    public AccountDTO(Account account) throws IllegalArgumentException {
        if (account == null)
            throw new IllegalArgumentException("account must not be null.");

        number = account.getNumber();
        name = account.getName();

        if (account.getElement() != null)
            elementNumber = account.getElement().getNumber();

        if (account.getPlayer() != null)
            playerName = account.getPlayer().getName();
    }

    /**
     * Checks the equality of two account DTOs
     *
     * @param o The other account DTO to compare with this account DTO
     * @return True if the other account DTO is equal to this account DTO, or false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof AccountDTO that))
            return false;

        return Objects.equals(number, that.number)
                && Objects.equals(name, that.name)
                && Objects.equals(elementNumber, that.elementNumber)
                && Objects.equals(playerName, that.playerName);
    }

    /**
     * Returns a hash code value for the account DTO
     *
     * @return A hash code value for the account DTO
     */
    @Override
    public int hashCode() {
        return Objects.hash(number, name, elementNumber, playerName);
    }

    /**
     * Returns a string representation of the account DTO
     *
     * @return A string representation of the account DTO in the following format:
     * <br/><br/>AccountDTO{number='number', name='name', elementNumber=elementNumber, playerName='playerName'}
     */
    @Override
    public String toString() {
        return "AccountDTO{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", elementNumber=" + elementNumber +
                ", playerName='" + playerName + '\'' +
                '}';
    }

    /**
     * Gets the number of the account DTO
     *
     * @return The number of the account DTO
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the number of the account DTO
     *
     * @param number A unique alphanumeric number
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Gets the name of the account DTO
     *
     * @return The name of the account DTO
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the account DTO
     *
     * @param name A unique name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the number of the element associated with the account DTO
     *
     * @return The number of the element associated with the account DTO
     */
    public Integer getElementNumber() {
        return elementNumber;
    }

    /**
     * Sets the element number of the account DTO
     *
     * @param elementNumber An element number
     */
    public void setElementNumber(Integer elementNumber) {
        this.elementNumber = elementNumber;
    }

    /**
     * Gets the name of the player associated with the account DTO
     *
     * @return The name of the player associated with the account DTO
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets the player name of the account DTO
     *
     * @param playerName A player name
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
