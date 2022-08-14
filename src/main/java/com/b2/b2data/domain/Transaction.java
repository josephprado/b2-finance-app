package com.b2.b2data.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a monetary transaction on the general ledger
 */
@Entity
@Table(name = "gl_transaction")
public class Transaction extends Entry {

    // field names
    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String MEMO = "memo";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "date_entered", nullable = false)
    @NotNull
    private LocalDate date;

    @Column(name = "memo")
    private String memo;

    /**
     * Constructs a new transaction
     */
    public Transaction() {
    }

    /**
     * Constructs a new transaction
     *
     * @param date The date of the transaction
     * @param memo A memo describing the transaction
     */
    public Transaction(LocalDate date, String memo) {
        this.date = date;
        this.memo = memo;
    }

    /**
     * Checks the equality of two transactions
     *
     * @param o The other transaction to compare with this transaction
     * @return True if the other transaction is equal to this transaction, or false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Transaction that))
            return false;

        return Objects.equals(id, that.id)
                && date.equals(that.date)
                && Objects.equals(memo, that.memo);
    }

    /**
     * Returns a hash code value for the transaction
     *
     * @return A hash code value for the transaction
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, date, memo);
    }

    /**
     * Returns a string representation of the transaction
     *
     * @return A string representation of the transaction in the following format:
     * <br/><br/>Transaction{id=id, date=date, memo='memo'}
     */
    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date=" + date +
                ", memo='" + memo + '\'' +
                '}';
    }

    /**
     * Gets the id of the transaction
     *
     * @return The id of the transaction
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the transaction
     *
     * @param id A unique identifier
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the date of the transaction
     *
     * @return The date of the transaction
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the transaction
     *
     * @param date The date of the transaction
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the memo of the transaction
     *
     * @return The memo of the transaction
     */
    public String getMemo() {
        return memo;
    }

    /**
     * Sets the memo of the transaction
     *
     * @param memo A memo describing the transaction
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }
}
