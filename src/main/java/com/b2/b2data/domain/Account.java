package com.b2.b2data.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "gl_account")
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = Account.WITH_ALL,
                attributeNodes = {@NamedAttributeNode("element"), @NamedAttributeNode("player")}
        ),
        @NamedEntityGraph(name = Account.WITH_ELEMENT, attributeNodes = @NamedAttributeNode("element")),
        @NamedEntityGraph(name = Account.WITH_PLAYER, attributeNodes = @NamedAttributeNode("player"))
})
public class Account extends Entry {

    public static final String WITH_ALL = "graph.account.all";
    public static final String WITH_ELEMENT = "graph.account.element";
    public static final String WITH_PLAYER = "graph.account.player";

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

    public Account() {
    }

    public Account(String number, String name, Element element) {
        this.number = number;
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
                && number.equals(account.number)
                && name.equals(account.name)
                && element.equals(account.element)
                && Objects.equals(player, account.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, name, element, player);
    }

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
