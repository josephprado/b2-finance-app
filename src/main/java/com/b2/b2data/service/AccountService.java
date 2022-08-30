package com.b2.b2data.service;

import com.b2.b2data.domain.Account;
import com.b2.b2data.domain.Player;
import com.b2.b2data.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Provides services for handling {@link Account} objects
 */
@Service
public class AccountService {

    private final AccountRepository REPO;

    /**
     * Constructs a new account service
     *
     * @param repo An account repository
     */
    @Autowired
    public AccountService(AccountRepository repo) {
        REPO = repo;
    }

    /**
     * Finds the account with the given id
     *
     * @param id An account id
     * @return The account with the given id
     * @throws NoSuchElementException If the account does not exist
     */
    public Account findById(Integer id) throws NoSuchElementException {
        return REPO.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Account id="+id+" does not exist."));
    }

    /**
     * Finds the account with the given number
     *
     * @param number An account number
     * @return The account with the given number
     * @throws NoSuchElementException If the account does not exist
     */
    public Account findByNumber(String number) throws NoSuchElementException {
        return REPO.findByNumber(number)
                    .orElseThrow(() -> new NoSuchElementException("Account number='"+number+"' does not exist."));
    }

    /**
     * Finds the account with the given name
     *
     * @param name An account name
     * @return The account with the given name
     * @throws NoSuchElementException If the account does not exist
     */
    public Account findByName(String name) throws NoSuchElementException {
        return REPO.findByName(name)
                    .orElseThrow(() -> new NoSuchElementException("Account name='"+name+"' does not exist."));
    }

    /**
     * Finds all accounts with the given element number
     *
     * @param elementNumber An element number
     * @return A list of accounts with the given element number, sorted by number ascending
     */
    public List<Account> findAllByElementNumber(Integer elementNumber) {
        return REPO.findAllByElementNumberOrderByNumberAsc(elementNumber);
    }

    /**
     * Finds all accounts with the given player name
     *
     * @param playerName A player name
     * @return A list of accounts with the given player name, sorted by number ascending
     */
    public List<Account> findAllByPlayerName(String playerName) {
        return REPO.findAllByPlayerNameOrderByNumberAsc(playerName);
    }

    /**
     * Finds all accounts
     *
     * @return A list of accounts sorted by number ascending
     */
    public List<Account> findAll() {
        return REPO.findAllByOrderByNumberAsc();
    }

    /**
     * Finds all accounts matching the given parameters
     *
     * @param elementNumber An element number
     * @param playerName A player name
     * @param isBank True if the account is associated with a bank
     * @return A list of accounts matching the given parameters, sorted by number ascending
     */
    public List<Account> findAll(Integer elementNumber, String playerName, Boolean isBank) {
        return REPO.findAll(
                Specification
                        .where(elementNumberEquals(elementNumber))
                        .and(playerNameEquals(playerName))
                        .and(isBankEquals(isBank)),
                Sort.by(Account.NUMBER).ascending()
        );
    }

    /**
     * Saves the given account to the database
     *
     * @param account An account to save
     * @return The account saved in the database
     */
    @Transactional
    @Modifying
    public Account save(Account account) {
        return REPO.save(account);
    }

    /**
     * Deletes the given account from the database
     *
     * @param account An account to delete
     */
    @Transactional
    @Modifying
    public void delete(Account account) {
        REPO.delete(account);
    }

    //region SPECIFICATIONS

    /**
     * Creates a specification for an account with the given element number
     *
     * @param elementNumber An element number
     * @return A specification for an account with the given element number,
     *         or an always true specification if the element number is null
     */
    private Specification<Account> elementNumberEquals(Integer elementNumber) {
        return ((root, query, criteriaBuilder) ->
                elementNumber == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get(Account.ELEMENT), elementNumber)
        );
    }

    /**
     * Creates a specification for an account with the given player name
     *
     * @param playerName A player name
     * @return A specification for an account with the given player name,
     *         or an always true specification if the player name is null
     */
    private Specification<Account> playerNameEquals(String playerName) {
        return ((root, query, criteriaBuilder) ->
                playerName == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get(Account.PLAYER).get(Player.NAME), playerName)
        );
    }

    /**
     * Creates a specification for an account with the given isBank status
     *
     * @param isBank True if the account is associated with a bank
     * @return A specification for an account with the given isBank status,
     *         or an always true specification if isBank is null
     */
    private Specification<Account> isBankEquals(Boolean isBank) {
        return ((root, query, criteriaBuilder) -> {

            if (isBank == null)
                return criteriaBuilder.conjunction();

            Expression<Boolean> playerIsBank = root.join(Account.PLAYER, JoinType.LEFT).get(Player.IS_BANK);

            return isBank
                    ? criteriaBuilder.isTrue(playerIsBank)
                    : criteriaBuilder.or(
                            criteriaBuilder.isNull(playerIsBank),
                            criteriaBuilder.isFalse(playerIsBank));
        });
    }
    //endregion
}
