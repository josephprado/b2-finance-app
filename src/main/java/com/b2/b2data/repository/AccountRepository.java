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

/**
 * Provides CRUD operations for {@link Account} objects in the database
 */
@Repository
public interface AccountRepository
        extends PagingAndSortingRepository<Account,Integer>, JpaSpecificationExecutor<Account> {

    /**
     * Finds the account with the given number
     *
     * @param number An account number
     * @return An optional containing the account with the given number, if it exists
     */
    @EntityGraph(value = Account.WITH_ALL, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Account> findByNumber(String number);

    /**
     * Finds the account with the given name
     *
     * @param name An account name
     * @return An optional containing the account with the given name, if it exists
     */
    @EntityGraph(value = Account.WITH_ALL, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Account> findByName(String name);

    /**
     * Finds all accounts with the given element number
     *
     * @param elementNumber An element number
     * @return A list of accounts with the given element number, sorted by number ascending
     */
    @EntityGraph(value = Account.WITH_PLAYER, type = EntityGraph.EntityGraphType.LOAD)
    List<Account> findAllByElementNumberOrderByNumberAsc(Integer elementNumber);

    /**
     * Finds all accounts with the given player name
     *
     * @param playerName A player name
     * @return A list of accounts with the given player name, sorted by number ascending
     */
    @EntityGraph(value = Account.WITH_ELEMENT, type = EntityGraph.EntityGraphType.LOAD)
    List<Account> findAllByPlayerNameOrderByNumberAsc(String playerName);

    /**
     * Finds all accounts
     *
     * @return A list of accounts sorted by number ascending
     */
    @EntityGraph(value = Account.WITH_ALL, type = EntityGraph.EntityGraphType.LOAD)
    List<Account> findAllByOrderByNumberAsc();

    /**
     * Finds all accounts matching the given specification
     *
     * @param specification can be {@literal null}.
     * @param sort must not be {@literal null}.
     * @return A list of accounts matching the given specification, sorted by the given sort
     */
    @EntityGraph(value = Account.WITH_ALL, type = EntityGraph.EntityGraphType.LOAD)
    @Override
    List<Account> findAll(Specification<Account> specification, Sort sort);
}
