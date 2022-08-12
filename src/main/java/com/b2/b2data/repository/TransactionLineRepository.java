package com.b2.b2data.repository;

import com.b2.b2data.domain.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionLineRepository
        extends PagingAndSortingRepository<TransactionLine, TransactionLineId>,
        JpaSpecificationExecutor<TransactionLine> {

    @EntityGraph(value = TransactionLine.WITH_ALL, type = EntityGraph.EntityGraphType.LOAD)
    List<TransactionLine> findAllByOrderByTransactionDateDesc();

    @EntityGraph(value = TransactionLine.WITHOUT_TRANSACTION, type = EntityGraph.EntityGraphType.LOAD)
    List<TransactionLine> findAllByTransactionIdOrderByLineAsc(Integer transaction);

    @EntityGraph(value = TransactionLine.WITHOUT_ACCOUNT, type = EntityGraph.EntityGraphType.LOAD)
    List<TransactionLine> findAllByAccountNumberOrderByTransactionDateDesc(String accountNumber);

    @EntityGraph(value = TransactionLine.WITHOUT_PLAYER, type = EntityGraph.EntityGraphType.LOAD)
    List<TransactionLine> findAllByPlayerNameOrderByTransactionDateDesc(String playerName);

    @EntityGraph(value = TransactionLine.WITH_ALL, type = EntityGraph.EntityGraphType.LOAD)
    List<TransactionLine> findAllByMemoLikeOrderByTransactionDateDesc(String memoPattern);

    @EntityGraph(value = TransactionLine.WITH_ALL, type = EntityGraph.EntityGraphType.LOAD)
    @Override
    List<TransactionLine> findAll(Specification<TransactionLine> specification, Sort sort);
}
