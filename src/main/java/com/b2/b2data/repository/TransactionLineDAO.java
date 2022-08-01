package com.b2.b2data.repository;

import com.b2.b2data.model.Transaction;
import com.b2.b2data.model.TransactionLine;
import com.b2.b2data.model.TransactionLineId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * A data access object for interacting with {@link TransactionLine} records
 */
@Repository
public interface TransactionLineDAO extends PagingAndSortingRepository<TransactionLine,TransactionLineId> {

    @EntityGraph(value = TransactionLine.WITH_ALL_FIELDS, type = EntityGraph.EntityGraphType.LOAD)
    List<TransactionLine> findAllByOrderByTransactionDescLineIdAsc();

    @EntityGraph(value = TransactionLine.WITH_ALL_FIELDS, type = EntityGraph.EntityGraphType.LOAD)
    List<TransactionLine> findAllByTransactionOrderByLineId(Transaction transaction);

    @EntityGraph(value = TransactionLine.WITH_ALL_FIELDS, type = EntityGraph.EntityGraphType.LOAD)
    List<TransactionLine> findAllByMemoLikeOrderByTransactionDescLineIdAsc(String memoPattern);
}
