package com.b2.b2data.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Represents a general ledger account
 */
@Entity
@Table(name = "gl_account")
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = Account.WITH_ALL,
                attributeNodes = {@NamedAttributeNode(Account.ELEMENT), @NamedAttributeNode(Account.PLAYER)}
        ),
        @NamedEntityGraph(name = Account.WITH_ELEMENT, attributeNodes = @NamedAttributeNode(Account.ELEMENT)),
        @NamedEntityGraph(name = Account.WITH_PLAYER, attributeNodes = @NamedAttributeNode(Account.PLAYER))
})
public class Account extends Entry {

    // entity graphs
    public static final String WITH_ALL = "graph.account.all";
    public static final String WITH_ELEMENT = "graph.account.element";
    public static final String WITH_PLAYER = "graph.account.player";

    // field names
    public static final String ID = "id";
    public static final String NUMBER = "number";
    public static final String NAME = "name";
    public static final String ELEMENT = "element";
    public static final String PLAYER = "player";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "number", unique = true, nullable = false)
    @NotBlank
    private String number;

    @Column(name = "name", unique = true, nullable = false)
    @NotBlank
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "element_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk__gl_account__element_id"),
            nullable = false
    )
    @NotNull
    private Element element;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "player_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk__gl_account__player_id")
    )
    private Player player;

    /**
     * Constructs a new account
     */
    public Account() {
    }

    /**
     * Constructs a new account
     *
     * @param number A unique alphanumeric number
     * @param name A unique name
     * @param element The element associated with the account
     */
    public Account(String number, String name, Element element) {
        this.number = number;
        this.name = name;
        this.element = element;
    }

    /**
     * Checks the equality of two accounts
     *
     * @param o The other account to compare with this account
     * @return True if the other account is equal to this account, or false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Account account))
            return false;

        return Objects.equals(id, account.id)
                && number.equals(account.number)
                && name.equals(account.name)
                && element.equals(account.element)
                && Objects.equals(player, account.player);
    }

    /**
     * Returns a hash code value for the account
     *
     * @return A hash code value for the account
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, number, name, element, player);
    }

    /**
     * Returns a string representation of the account
     *
     * @return A string representation of the account in the following format:
     * <br/><br/>Account{id=id, number='number', name='name', element='elementName', player='playerName'}
     *
     */
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", element='" + element.getName() + '\'' +
                ", player='" + (player != null ? player.getName() : "null") + '\'' +
                '}';
    }

    /**
     * Gets the id of the account
     *
     * @return The id of the account
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the account
     *
     * @param id A unique identifier
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the number of the account
     *
     * @return The number of the account
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the number of the account
     *
     * @param number A unique number
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Gets the name of the account
     *
     * @return The name of the account
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the account
     *
     * @param name A unique name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the element associated with the account
     *
     * @return The element associated with the account
     */
    public Element getElement() {
        return element;
    }

    /**
     * Sets the element of the account
     *
     * @param element An element
     */
    public void setElement(Element element) {
        this.element = element;
    }

    /**
     * Gets the player associated with the account
     *
     * @return The player associated with the account
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player of the account
     *
     * @param player A player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
}
