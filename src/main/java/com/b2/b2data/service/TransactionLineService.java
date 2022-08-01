package com.b2.b2data.service;

import com.b2.b2data.model.Transaction;
import com.b2.b2data.model.TransactionLine;
import com.b2.b2data.repository.TransactionLineDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Provides services for manipulating {@link TransactionLine} records
 */
@Service
public class TransactionLineService {

    private final TransactionLineDAO DAO;

    @Autowired
    public TransactionLineService(TransactionLineDAO DAO) {
        this.DAO = DAO;
    }

    /**
     * Finds all transaction lines in the database
     *
     * @return A list of transaction lines ordered by transaction (desc) and line id (asc)
     */
    public List<TransactionLine> findAll() {
        return DAO.findAllByOrderByTransactionDescLineIdAsc();
    }

    /**
     * Finds all transaction lines for the given transaction
     *
     * @return A list of transaction lines ordered by transaction (desc) and line id (asc)
     */
    public List<TransactionLine> findAll(Transaction transaction) {
        return DAO.findAllByTransactionOrderByLineId(transaction);
    }

    /**
     * Finds all transaction lines with a memo matching the given pattern (case-insensitive)
     *
     * @return A list of transaction lines with a memo matching the given pattern,
     *         ordered by transaction (desc) and line id (asc)
     */
    public List<TransactionLine> findAll(String memoPattern) {
        return DAO.findAllByMemoLikeOrderByTransactionDescLineIdAsc(memoPattern);
    }

    /**
     * Saves the transaction line to the database
     *
     * @param line A transaction line to save
     * @return The saved transaction line
     */
    public TransactionLine save(TransactionLine line) {
        return DAO.save(line);
    }

    /**
     * Deletes the given transaction line
     *
     * @param line A transaction line to delete
     */
    public void delete(TransactionLine line) {
        DAO.delete(line);
    }
}
