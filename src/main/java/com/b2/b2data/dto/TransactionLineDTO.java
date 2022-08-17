package com.b2.b2data.dto;

import com.b2.b2data.domain.TransactionLine;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A data transfer object for transmitting {@link TransactionLine} entities to and from the client
 */
public class TransactionLineDTO extends DTO{

    @NotNull
    private Integer transaction;

    @NotNull
    private Integer line;

    @NotBlank
    private String account;
    private String player;

    @NotNull
    private Double amount;
    private String memo;
    private LocalDate dateReconciled;

    /**
     * Constructs a new transaction line DTO
     */
    public TransactionLineDTO() {
    }

    /**
     * Constructs a new transaction line DTO
     *
     * @param transactionLine A transaction line; must not be null
     * @throws IllegalArgumentException If the transaction line is null
     */
    public TransactionLineDTO(TransactionLine transactionLine) throws IllegalArgumentException {
        if (transactionLine == null)
            throw new IllegalArgumentException("transaction line must not be null.");

        if (transactionLine.getTransaction() != null)
            transaction = transactionLine.getTransaction().getId();

        line = transactionLine.getLine();

        if (transactionLine.getAccount() != null)
            account = transactionLine.getAccount().getNumber();

        if (transactionLine.getPlayer() != null)
            player = transactionLine.getPlayer().getName();

        amount = transactionLine.getAmount();
        memo = transactionLine.getMemo();
        dateReconciled = transactionLine.getDateReconciled();
    }

    /**
     * Checks the equality of two transaction line DTOs
     *
     * @param o The other transaction line DTO to compare with this transaction line DTO
     * @return True if the other transaction line DTO is equal to this transaction line DTO, or false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof TransactionLineDTO that))
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
     * Returns a hash code value for the transaction line DTO
     *
     * @return A hash code value for the transaction line DTO
     */
    @Override
    public int hashCode() {
        return Objects.hash(transaction, line, account, player, amount, memo, dateReconciled);
    }

    /**
     * Returns a string representation of the transaction line DTO
     *
     * @return A string representation of the transaction line DTO in the following format:
     * <br/><br/>TransactionLineDTO{transaction=transactionNumber, line=line, account='accountNumber',
     *           player='playerName', amount=amount, memo='memo', dateReconciled=dateReconciled}
     */
    @Override
    public String toString() {
        return "TransactionLineDTO{" +
                "transaction=" + transaction +
                ", line=" + line +
                ", account='" + account + '\'' +
                ", player='" + player + '\'' +
                ", amount=" + amount +
                ", memo='" + memo + '\'' +
                ", dateReconciled=" + dateReconciled +
                '}';
    }

    /**
     * Gets the number of the transaction owning the transaction line DTO
     *
     * @return The number of the transaction owning the transaction line DTO
     */
    public Integer getTransaction() {
        return transaction;
    }

    /**
     * Sets the transaction number of the transaction line DTO
     *
     * @param transaction A transaction number
     */
    public void setTransaction(Integer transaction) {
        this.transaction = transaction;
    }

    /**
     * Gets the line number of the transaction line DTO
     *
     * @return The line number of the transaction line DTO
     */
    public Integer getLine() {
        return line;
    }

    /**
     * Sets the line number of the transaction line DTO
     *
     * @param line A line number (must be unique among the lines owned by parent transaction DTO)
     */
    public void setLine(Integer line) {
        this.line = line;
    }

    /**
     * Gets the number of the account associated with the transaction line DTO
     *
     * @return The number of the account associated with the transaction line DTO
     */
    public String getAccount() {
        return account;
    }

    /**
     * Sets the account number of the transaction line DTO
     *
     * @param account An account number
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * Gets the name of the player associated with the transaction line DTO
     *
     * @return The name of the player associated with the transaction line DTO
     */
    public String getPlayer() {
        return player;
    }

    /**
     * Sets the player name of the transaction line DTO
     *
     * @param player A player name
     */
    public void setPlayer(String player) {
        this.player = player;
    }

    /**
     * Gets the amount of the transaction line DTO
     *
     * @return The amount of the transaction line DTO
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the transaction line DTO
     *
     * @param amount A positive or negative amount
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Gets the memo of the transaction line DTO
     *
     * @return The memo of the transaction line DTO
     */
    public String getMemo() {
        return memo;
    }

    /**
     * Sets the memo of the transaction line DTO
     *
     * @param memo A memo describing the transaction line DTO
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * Gets the reconciliation date of the transaction line DTO
     *
     * @return The reconciliation date of the transaction line DTO
     */
    public LocalDate getDateReconciled() {
        return dateReconciled;
    }

    /**
     * Sets the reconciliation date of the transaction line DTO
     *
     * @param dateReconciled The reconciliation date of the transaction line DTO
     */
    public void setDateReconciled(LocalDate dateReconciled) {
        this.dateReconciled = dateReconciled;
    }
}
