package com.b2.b2data.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a single line of a parent {@link Transaction}
 */
@Entity
@Table(name = "gl_transaction_line")
@IdClass(TransactionLineId.class)
@NamedEntityGraphs({
        @NamedEntityGraph(name = TransactionLine.WITH_PLAYER, attributeNodes = {@NamedAttributeNode("player")}),
        @NamedEntityGraph(name = TransactionLine.WITH_ACCOUNT, attributeNodes = {@NamedAttributeNode("account")}),
        @NamedEntityGraph(
                name = TransactionLine.WITH_ALL_FIELDS,
                attributeNodes = {
                        @NamedAttributeNode(value = "account", subgraph = "account.all"),
                        @NamedAttributeNode("player")
                },
                subgraphs = @NamedSubgraph(
                        name = "account.all",
                        attributeNodes = {@NamedAttributeNode("element"), @NamedAttributeNode("player")}
                )
        )
})
public class TransactionLine extends Entry {

    public static final String WITH_PLAYER = "graph.transactionLine.player";
    public static final String WITH_ACCOUNT = "graph.transactionLine.account";
    public static final String WITH_ALL_FIELDS = "graph.transactionLine.all";

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "transaction_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk__gl_transaction_line__gl_transaction_id"),
            nullable = false
    )
    private Transaction transaction;

    @Id
    @Column(name = "line_id")
    private Integer lineId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "account_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk__gl_transaction_line__gl_account_id"),
            nullable = false
    )
    @NotNull
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "player_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk__gl_transaction_line__player_id")
    )
    private Player player;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "memo")
    private String memo;

    @Column(name = "date_reconciled")
    private LocalDate dateReconciled;

    public TransactionLine() {
    }

    public TransactionLine(Transaction transaction, Integer lineId,
                           Account account, double amount) {

        this.transaction = transaction;
        this.lineId = lineId;
        this.account = account;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof TransactionLine that))
            return false;
        return Double.compare(that.amount, amount) == 0
                && transaction.equals(that.transaction)
                && lineId.equals(that.lineId)
                && account.equals(that.account)
                && Objects.equals(player, that.player)
                && Objects.equals(memo, that.memo)
                && Objects.equals(dateReconciled, that.dateReconciled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaction, lineId, account, player, amount, memo, dateReconciled);
    }

    @Override
    public String toString() {
        return "TransactionLine{" +
                "transaction=" + transaction.getId() +
                ", lineId=" + lineId +
                ", account=" + account.getId() +
                ", player=" + player.getId() +
                ", amount=" + amount +
                ", memo='" + memo + '\'' +
                ", dateReconciled=" + dateReconciled +
                '}';
    }

    @JsonBackReference
    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Integer getLineId() {
        return lineId;
    }

    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    @JsonBackReference
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @JsonBackReference
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public LocalDate getDateReconciled() {
        return dateReconciled;
    }

    public void setDateReconciled(LocalDate dateReconciled) {
        this.dateReconciled = dateReconciled;
    }
}
