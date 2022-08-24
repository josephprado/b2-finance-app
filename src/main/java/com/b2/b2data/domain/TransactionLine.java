package com.b2.b2data.domain;

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
        @NamedEntityGraph(
                name = TransactionLine.WITH_ALL,
                attributeNodes = {
                        @NamedAttributeNode(TransactionLine.TRANSACTION),
                        @NamedAttributeNode(value = TransactionLine.ACCOUNT, subgraph = "account.all"),
                        @NamedAttributeNode(TransactionLine.PLAYER)
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "account.all",
                                attributeNodes = {
                                        @NamedAttributeNode(Account.ELEMENT),
                                        @NamedAttributeNode(Account.PLAYER)
                                }
                        )
                }),
        @NamedEntityGraph(
                name = TransactionLine.WITHOUT_TRANSACTION,
                attributeNodes = {
                        @NamedAttributeNode(value = TransactionLine.ACCOUNT, subgraph = "account.all"),
                        @NamedAttributeNode(TransactionLine.PLAYER)
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "account.all",
                                attributeNodes = {
                                        @NamedAttributeNode(Account.ELEMENT),
                                        @NamedAttributeNode(Account.PLAYER)
                                }
                        )
                }),
        @NamedEntityGraph(
                name = TransactionLine.WITHOUT_ACCOUNT,
                attributeNodes = {
                        @NamedAttributeNode(TransactionLine.TRANSACTION),
                        @NamedAttributeNode(TransactionLine.PLAYER)
                }
        ),
        @NamedEntityGraph(
                name = TransactionLine.WITHOUT_PLAYER,
                attributeNodes = {
                        @NamedAttributeNode(TransactionLine.TRANSACTION),
                        @NamedAttributeNode(value = TransactionLine.ACCOUNT, subgraph = "account.all")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "account.all",
                                attributeNodes = {
                                        @NamedAttributeNode(Account.ELEMENT),
                                        @NamedAttributeNode(Account.PLAYER)
                                }
                        )
                })
})
public class TransactionLine extends Entry {

    // entity graphs
    public static final String WITH_ALL = "graph.transactionLine.all";
    public static final String WITHOUT_TRANSACTION = "graph.transactionLine.withoutTransaction";
    public static final String WITHOUT_ACCOUNT = "graph.transactionLine.withoutAccount";
    public static final String WITHOUT_PLAYER = "graph.transactionLine.withoutPlayer";

    // field names
    public static final String TRANSACTION = "transaction";
    public static final String LINE = "line";
    public static final String ACCOUNT = "account";
    public static final String PLAYER = "player";
    public static final String AMOUNT = "amount";
    public static final String MEMO = "memo";
    public static final String DATE_RECONCILED = "dateReconciled";

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
    @NotNull
    private Double amount;

    @Column(name = "memo")
    private String memo;

    @Column(name = "date_reconciled")
    private LocalDate dateReconciled;

    /**
     * Constructs a new transaction line
     */
    public TransactionLine() {
    }

    /**
     * Constructs a new transaction line
     *
     * @param transaction The parent transaction which owns the line
     * @param line A line number (unique among the lines of the parent transaction)
     * @param account The account associated with the transaction line
     * @param amount The monetary value of the transaction line
     */
    public TransactionLine(Transaction transaction, Integer line, Account account, Double amount) {
        this.transaction = transaction;
        this.line = line;
        this.account = account;
        this.amount = amount;
    }

    /**
     * Checks the equality of two transaction lines
     *
     * @param o The other transaction line to compare with this transaction line
     * @return True if the other transaction line is equal to this transaction line, or false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof TransactionLine that))
            return false;

        return Objects.equals(transaction, that.transaction)
                && Objects.equals(line, that.line)
                && Objects.equals(account, that.account)
                && Objects.equals(player, that.player)
                && Objects.equals(amount, that.amount)
                && Objects.equals(memo, that.memo)
                && Objects.equals(dateReconciled, that.dateReconciled);
    }

    /**
     * Returns a hash code value for the transaction line
     *
     * @return A hash code value for the transaction line
     */
    @Override
    public int hashCode() {
        return Objects.hash(transaction, line, account, player, amount, memo, dateReconciled);
    }

    /**
     * Returns a string representation of the transaction line
     *
     * @return A string representation of the transaction line in the following format:
     * <br/><br/>TransactionLine{transaction=transactionId, line=line, account='accountNumber',
     *           player='playerName', amount=amount, memo='memo', dateReconciled=dateReconciled}
     */
    @Override
    public String toString() {
        return "TransactionLine{" +
                "transaction=" + transaction.getId() +
                ", line=" + line +
                ", account='" + account.getNumber() + '\'' +
                ", player=" + (player != null ? '\'' + player.getName() + '\'' : "null") +
                ", amount=" + amount +
                ", memo='" + memo + '\'' +
                ", dateReconciled=" + dateReconciled +
                '}';
    }

    /**
     * Gets the parent transaction of the transaction line
     *
     * @return The parent transaction owning the transaction line
     */
    public Transaction getTransaction() {
        return transaction;
    }

    /**
     * Sets the parent transaction of the transaction line
     *
     * @param transaction The parent transaction owning the transaction line
     */
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    /**
     * Gets the line number of the transaction line
     *
     * @return The line number of the transaction line
     */
    public Integer getLine() {
        return line;
    }

    /**
     * Sets the line number of the transaction line
     *
     * @param line A line number (must be unique among the lines owned by parent transaction)
     */
    public void setLine(Integer line) {
        this.line = line;
    }

    /**
     * Gets the account associated with the transaction line
     *
     * @return The account associated with the transaction line
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Sets the account of the transaction line
     *
     * @param account An account
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * Gets the player associated with the transaction line
     *
     * @return The player associated with the transaction line
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player of the transaction line
     *
     * @param player A player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Gets the amount of the transaction line
     *
     * @return The amount of the transaction line
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the transaction line
     *
     * @param amount A positive or negative amount
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Gets the memo of the transaction line
     *
     * @return The memo of the transaction line
     */
    public String getMemo() {
        return memo;
    }

    /**
     * Sets the memo of the transaction line
     *
     * @param memo A memo describing the transaction line
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * Gets the reconciliation date of the transaction line
     *
     * @return The reconciliation date of the transaction line
     */
    public LocalDate getDateReconciled() {
        return dateReconciled;
    }

    /**
     * Sets the reconciliation date of the transaction line
     *
     * @param dateReconciled The reconciliation date of the transaction line
     */
    public void setDateReconciled(LocalDate dateReconciled) {
        this.dateReconciled = dateReconciled;
    }
}
