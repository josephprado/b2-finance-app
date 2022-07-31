package com.b2.b2data.service;

import com.b2.b2data.model.Element;
import com.b2.b2data.model.Player;
import com.b2.b2data.repository.AccountDAO;
import com.b2.b2data.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Provides services for manipulating {@link Account} records
 */
@Service
public class AccountService {

    private final AccountDAO DAO;

    @Autowired
    public AccountService(AccountDAO DAO) {
        this.DAO = DAO;
    }

    /**
     * Finds the account with the given id
     *
     * @param id The id of an account
     * @return The account with the given id, or null if it does not exist
     */
    public Account findById(String id) {
        return DAO.findById(id).orElse(null);
    }

    /**
     * Finds the account with the given name
     *
     * @param name The name of an account
     * @return The account with the given name, or null if it does not exist
     */
    public Account findByName(String name) {
        return DAO.findByName(name).orElse(null);
    }

    /**
     * Finds all accounts in the database
     *
     * @return A list of accounts ordered by id
     */
    public List<Account> findAll() {
        return DAO.findAllByOrderById();
    }

    /**
     * Finds all accounts with the given element
     *
     * @param element An element
     * @return A list of all accounts with the given element, sorted by id
     */
    public List<Account> findAll(Element element) {
        return DAO.findAllByElementOrderById(element);
    }

    /**
     * Finds all accounts with the given player
     *
     * @param player A player
     * @return A list of all accounts with the given player, sorted by id
     */
    public List<Account> findAll(Player player) {
        return DAO.findAllByPlayerOrderById(player);
    }

    /**
     * Saves the account to the database
     *
     * @param account An account to save
     * @return The saved account
     */
    public Account save(Account account) {
        return DAO.save(account);
    }

    /**
     * Deletes the given account
     *
     * @param account An account to delete
     */
    public void delete(Account account) {
        DAO.delete(account);
    }
}
