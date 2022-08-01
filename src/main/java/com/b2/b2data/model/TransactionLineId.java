package com.b2.b2data.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * A composite primary key for a {@link TransactionLine}
 */
public class TransactionLineId implements Serializable {

    private Long transaction;
    private Integer lineId;

    public TransactionLineId() {
    }

    public TransactionLineId(Long transaction, Integer lineId) {
        this.transaction = transaction;
        this.lineId = lineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof TransactionLineId that))
            return false;

        return transaction.equals(that.transaction)
                && lineId.equals(that.lineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaction, lineId);
    }
}
