package com.b2.b2data.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "gl_transaction_line")
@IdClass(TransactionLineId.class)
public class TransactionLine extends Entry {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "gl_transaction_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk__gl_transaction_line__gl_transaction_id"),
            nullable = false
    )
    @NotNull
    private Transaction transaction;

    @Id
    @Column(name = "line_id")
    @NotNull
    private Integer line;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "gl_account_id",
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

    public TransactionLine(Transaction transaction, Integer line, Account account, double amount) {
        this.transaction = transaction;
        this.line = line;
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
                && line.equals(that.line)
                && account.equals(that.account)
                && Objects.equals(player, that.player)
                && Objects.equals(memo, that.memo)
                && Objects.equals(dateReconciled, that.dateReconciled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaction, line, account, player, amount, memo, dateReconciled);
    }

    @Override
    public String toString() {
        return "TransactionLine{" +
                "transaction=" + transaction.getId() +
                ", line=" + line +
                ", account='" + account.getName() + '\'' +
                ", player='" + (player != null ? player.getName() : "null") + '\'' +
                ", amount=" + amount +
                ", memo='" + memo + '\'' +
                ", dateReconciled=" + dateReconciled +
                '}';
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

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
