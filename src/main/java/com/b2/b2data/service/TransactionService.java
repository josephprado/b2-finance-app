package com.b2.b2data.service;

import com.b2.b2data.repository.TransactionDAO;
import com.b2.b2data.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Provides services for manipulating {@link Transaction} records
 */
@Service
public class TransactionService {

    private final TransactionDAO DAO;

    @Autowired
    public TransactionService(TransactionDAO DAO) {
        this.DAO = DAO;
    }

    /**
     * Finds the transaction with the given id
     *
     * @param id The id of a transaction
     * @return The transaction with the given id, or null if it does not exist
     */
    public Transaction findById(long id) {
        return DAO.findById(id).orElse(null);
    }

    /**
     * Finds all transactions in the database
     *
     * @return A list of transactions ordered by date descending
     */
    public List<Transaction> findAll() {
        return DAO.findAllByOrderByDateDesc();
    }

    /**
     * Finds all transactions with a memo matching the given pattern (case-insensitive)
     *
     * @return A list of transactions with a memo matching the given pattern, ordered by date descending
     */
    public List<Transaction> findAll(String memoPattern) {
        return DAO.findAllByMemoLikeIgnoreCaseOrderByDateDesc(memoPattern);
    }

    /**
     * Finds all transactions with the given dates
     *
     * @return A list of transactions within the given dates, ordered by date descending
     */
    public List<Transaction> findAll(LocalDate from, LocalDate to) {
        return DAO.findAllByDateBetweenOrderByDateDesc(from, to);
    }

    /**
     * Saves the transaction to the database
     *
     * @param transaction A transaction to save
     * @return The saved transaction
     */
    public Transaction save(Transaction transaction) {
        return DAO.save(transaction);
    }

    /**
     * Deletes the given transaction
     *
     * @param transaction A transaction to delete
     */
    public void delete(Transaction transaction) {
        DAO.delete(transaction);
    }
}
