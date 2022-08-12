package com.b2.b2data.repository;

import com.b2.b2data.domain.Transaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository
        extends PagingAndSortingRepository<Transaction,Integer>, JpaSpecificationExecutor<Transaction> {

    @Override
    List<Transaction> findAll(Specification<Transaction> specification, Sort sort);
}
