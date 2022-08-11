package com.b2.b2data.repository;

import com.b2.b2data.domain.Account;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository
        extends PagingAndSortingRepository<Account,Integer>, JpaSpecificationExecutor<Account> {

    @EntityGraph(value = Account.WITH_ALL, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Account> findByNumber(String number);

    @EntityGraph(value = Account.WITH_ALL, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Account> findByName(String name);

    @EntityGraph(value = Account.WITH_ALL, type = EntityGraph.EntityGraphType.LOAD)
    @Override
    List<Account> findAll(Specification<Account> specification, Sort sort);
}
