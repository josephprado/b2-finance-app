package com.b2.b2data.repository;

import com.b2.b2data.domain.Account;
import com.b2.b2data.domain.Element;
import com.b2.b2data.domain.Player;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account,Integer> {

    @EntityGraph(value = Account.WITH_ALL, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Account> findByNumber(String number);

    @EntityGraph(value = Account.WITH_ALL, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Account> findByName(String name);

    @EntityGraph(value = Account.WITH_ALL, type = EntityGraph.EntityGraphType.LOAD)
    List<Account> findAllByOrderByNumber();

    @EntityGraph(value = Account.WITH_PLAYER, type = EntityGraph.EntityGraphType.LOAD)
    List<Account> findAllByElementOrderByNumber(Element element);

    @EntityGraph(value = Account.WITH_ELEMENT, type = EntityGraph.EntityGraphType.LOAD)
    List<Account> findAllByPlayerOrderByNumber(Player player);
}
