package com.b2.b2data.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
        @NamedEntityGraph(name = Account.WITH_ELEMENT, attributeNodes = @NamedAttributeNode("element")),
        @NamedEntityGraph(name = Account.WITH_PLAYER, attributeNodes =  @NamedAttributeNode("player")),
        @NamedEntityGraph(
                name = Account.WITH_ALL_FIELDS,
                attributeNodes = {@NamedAttributeNode("element"), @NamedAttributeNode("player")}
        )
})
public class Account extends Entry {

    public static final String WITH_ELEMENT = "graph.account.element";
    public static final String WITH_PLAYER = "graph.account.player";
    public static final String WITH_ALL_FIELDS = "graph.account.all";

    @Id
    @Column(name = "id")
    @NotBlank
    private String id;

    @Column(name = "name", nullable = false, unique = true)
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

    public Account() {
    }

    public Account(String id, String name, Element element) {
        this.id = id;
        this.name = name;
        this.element = element;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Account account))
            return false;

        return Objects.equals(id, account.id)
                && Objects.equals(name, account.name)
                && Objects.equals(element, account.element)
                && Objects.equals(player, account.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, element, player);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", element=" + element +
                ", player=" + player +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonBackReference
    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    @JsonBackReference
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
