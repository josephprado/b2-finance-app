package com.b2.b2data.repository;

import com.b2.b2data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionLineRepository extends PagingAndSortingRepository<TransactionLine, TransactionLineId> {

    @EntityGraph(value = TransactionLine.WITH_ALL, type = EntityGraph.EntityGraphType.LOAD)
    List<TransactionLine> findAllByOrderByTransactionDateDesc();

    @EntityGraph(value = TransactionLine.WITHOUT_TRANSACTION, type = EntityGraph.EntityGraphType.LOAD)
    List<TransactionLine> findAllByTransactionOrderByLineAsc(Transaction transaction);

    @EntityGraph(value = TransactionLine.WITHOUT_ACCOUNT, type = EntityGraph.EntityGraphType.LOAD)
    List<TransactionLine> findAllByAccountOrderByTransactionDateDesc(Account account);

    @EntityGraph(value = TransactionLine.WITHOUT_PLAYER, type = EntityGraph.EntityGraphType.LOAD)
    List<TransactionLine> findAllByPlayerOrderByTransactionDateDesc(Player player);

    @EntityGraph(value = TransactionLine.WITH_ALL, type = EntityGraph.EntityGraphType.LOAD)
    List<TransactionLine> findAllByMemoLikeOrderByTransactionDateDesc(String memoPattern);
}
