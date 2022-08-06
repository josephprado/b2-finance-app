package com.b2.b2data.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "player")
public class Player extends Entry {

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

    public Player() {
    }

    public Player(String name, Boolean isBank) {
        this.name = name;
        this.isBank = isBank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Player player))
            return false;

        return Objects.equals(id, player.id)
                && name.equals(player.name)
                && isBank.equals(player.isBank);
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getBank() {
        return isBank;
    }

    public void setBank(Boolean bank) {
        isBank = bank;
    }
}
