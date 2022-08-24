package com.b2.b2data.repository;

import com.b2.b2data.domain.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Provides CRUD operations for {@link TransactionLine} objects in the database
 */
@Repository
public interface TransactionLineRepository
        extends PagingAndSortingRepository<TransactionLine, TransactionLineId>,
        JpaSpecificationExecutor<TransactionLine> {

    /**
     * Finds all transaction lines
     *
     * @return A list of transaction lines sorted by transaction date descending
     */
    @EntityGraph(value = TransactionLine.WITH_ALL, type = EntityGraph.EntityGraphType.LOAD)
    List<TransactionLine> findAllByOrderByTransactionDateDesc();

    /**
     * Finds all transaction lines with the given transaction id
     *
     * @param transactionId A transaction id
     * @return A list of transaction lines with the given transaction id, sorted by line id ascending
     */
    @EntityGraph(value = TransactionLine.WITHOUT_TRANSACTION, type = EntityGraph.EntityGraphType.LOAD)
    List<TransactionLine> findAllByTransactionIdOrderByLineIdAsc(Integer transactionId);

    /**
     * Finds all transaction lines with the given account number
     *
     * @param accountNumber An account number
     * @return A list of transaction lines with the given account number, sorted by transaction date descending
     */
    @EntityGraph(value = TransactionLine.WITHOUT_ACCOUNT, type = EntityGraph.EntityGraphType.LOAD)
    List<TransactionLine> findAllByAccountNumberOrderByTransactionDateDesc(String accountNumber);

    /**
     * Finds all transaction lines with the given player name
     *
     * @param playerName A player name
     * @return A list of transaction lines with the given player name, sorted by transaction date descending
     */
    @EntityGraph(value = TransactionLine.WITHOUT_PLAYER, type = EntityGraph.EntityGraphType.LOAD)
    List<TransactionLine> findAllByPlayerNameOrderByTransactionDateDesc(String playerName);

    /**
     * Finds all transaction lines matching the given specification
     *
     * @param specification can be {@literal null}.
     * @param sort must not be {@literal null}.
     * @return A list of transaction lines matching the given specification, sorted by the given sort
     */
    @EntityGraph(value = TransactionLine.WITH_ALL, type = EntityGraph.EntityGraphType.LOAD)
    @Override
    List<TransactionLine> findAll(Specification<TransactionLine> specification, Sort sort);
}
