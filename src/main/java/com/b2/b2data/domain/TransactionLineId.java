package com.b2.b2data.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the primary key of a transaction line
 */
public class TransactionLineId implements Serializable {

    // field names
    public static final String TRANSACTION = "transaction";
    public static final String LINE = "line";

    private Integer transaction;
    private Integer line;

    /**
     * Constructs a new transaction line id
     */
    public TransactionLineId() {
    }

    /**
     * Constructs a new transaction line id
     *
     * @param transaction A transaction id
     * @param line A line id
     */
    public TransactionLineId(Integer transaction, Integer line) {
        this.transaction = transaction;
        this.line = line;
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

        return Objects.equals(transaction, that.transaction) && Objects.equals(line, that.line);
    }

    /**
     * Returns a hash code value for the transaction line id
     *
     * @return A hash code value for the transaction line id
     */
    @Override
    public int hashCode() {
        return Objects.hash(transaction, line);
    }
}
