package com.b2.b2data.repository;

import com.b2.b2data.model.Account;
import com.b2.b2data.model.Element;
import com.b2.b2data.model.Player;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * A data access object for interacting with {@link Account} records
 */
@Repository
public interface AccountDAO extends PagingAndSortingRepository<Account,String> {

    @Override
    Optional<Account> findById(String id);

    Optional<Account> findByName(String name);

    @EntityGraph(value = Account.WITH_ALL_FIELDS, type = EntityGraph.EntityGraphType.LOAD)
    List<Account> findAllByOrderById();

    @EntityGraph(value = Account.WITH_PLAYER, type = EntityGraph.EntityGraphType.LOAD)
    List<Account> findAllByElementOrderById(Element element);

    @EntityGraph(value = Account.WITH_ELEMENT, type = EntityGraph.EntityGraphType.LOAD)
    List<Account> findAllByPlayerOrderById(Player player);
}
