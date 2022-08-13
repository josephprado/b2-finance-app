package com.b2.b2data.repository;

import com.b2.b2data.domain.Transaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Provides CRUD operations for {@link Transaction} objects in the database
 */
@Repository
public interface TransactionRepository
        extends PagingAndSortingRepository<Transaction,Integer>, JpaSpecificationExecutor<Transaction> {

    /**
     * Finds all transactions matching the given specification
     *
     * @param specification can be {@literal null}.
     * @param sort must not be {@literal null}.
     * @return A list of transactions matching the given specification, sorted by the given sort
     */
    @Override
    List<Transaction> findAll(Specification<Transaction> specification, Sort sort);
}
