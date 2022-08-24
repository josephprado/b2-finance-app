package com.b2.b2data.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the primary key of a {@link TransactionLine}
 */
public class TransactionLineId implements Serializable {

    // field names
    public static final String TRANSACTION_ID = "transactionId";
    public static final String LINE_ID = "lineID";

    // field names must match transaction line field names
    private Integer transaction;
    private Integer lineId;

    /**
     * Constructs a new transaction line id
     */
    public TransactionLineId() {
    }

    /**
     * Constructs a new transaction line id
     *
     * @param transactionId A transaction id
     * @param lineId A line id
     */
    public TransactionLineId(Integer transactionId, Integer lineId) {
        this.transaction = transactionId;
        this.lineId = lineId;
    }

    /**
     * Checks the equality of two transaction line ids
     *
     * @param o The other transaction line id to compare with this transaction line id
     * @return True if the other transaction line id is equal to this transaction line id,
     *         or false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof TransactionLineId that))
            return false;

        return Objects.equals(transaction, that.transaction) && Objects.equals(lineId, that.lineId);
    }

    /**
     * Returns a hash code value for the transaction line id
     *
     * @return A hash code value for the transaction line id
     */
    @Override
    public int hashCode() {
        return Objects.hash(transaction, lineId);
    }

    /**
     * Returns a string representation of the transaction line id
     *
     * @return A string representation of the transaction line id in the following format:
     * <br/><br/>TransactionLineId{transactionId=transactionId, lineId=lineId}
     */
    @Override
    public String toString() {
        return "TransactionLineId{" +
                "transactionId=" + transaction +
                ", lineId=" + lineId +
                '}';
    }

    /**
     * Gets the transaction id of the transaction line id
     *
     * @return The transaction id of the transaction line id
     */
    public Integer getTransactionId() {
        return transaction;
    }

    /**
     * Sets the transaction id of the transaction line id
     *
     * @param transactionId A transaction id
     */
    public void setTransactionId(Integer transactionId) {
        this.transaction = transactionId;
    }

    /**
     * Gets the line id of the transaction line id
     *
     * @return The line id of the transaction line id
     */
    public Integer getLineId() {
        return lineId;
    }

    /**
     * Sets the line id of the transaction line id
     *
     * @param lineId A line id
     */
    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }
}
