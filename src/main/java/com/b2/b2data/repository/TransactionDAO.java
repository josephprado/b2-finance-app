package com.b2.b2data.repository;

import com.b2.b2data.model.Transaction;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * A data access object for interacting with {@link Transaction} records
 */
@Repository
public interface TransactionDAO extends PagingAndSortingRepository<Transaction,Long> {

    List<Transaction> findAllByOrderByDateDesc();

    List<Transaction> findAllByMemoLikeIgnoreCaseOrderByDateDesc(String memo);

    List<Transaction> findAllByDateBetweenOrderByDateDesc(LocalDate from, LocalDate to);
}
