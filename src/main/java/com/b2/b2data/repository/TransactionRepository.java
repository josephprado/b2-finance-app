package com.b2.b2data.repository;

import com.b2.b2data.domain.Transaction;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction,Integer> {

    List<Transaction> findAllByOrderByDateDesc();
    List<Transaction> findAllByDateBetweenOrderByDateDesc(LocalDate from, LocalDate to);
    List<Transaction> findAllByMemoLikeOrderByDateDesc(String memoPattern);
}
