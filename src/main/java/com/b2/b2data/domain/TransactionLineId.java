package com.b2.b2data.domain;

import java.io.Serializable;
import java.util.Objects;

public class TransactionLineId implements Serializable {

    // field names
    public static final String TRANSACTION = "transaction";
    public static final String LINE = "line";

    private Integer transaction;
    private Integer line;

    public TransactionLineId() {
    }

    public TransactionLineId(Integer transaction, Integer line) {
        this.transaction = transaction;
        this.line = line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof TransactionLineId that))
            return false;

        return transaction.equals(that.transaction) && line.equals(that.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaction, line);
    }
}
