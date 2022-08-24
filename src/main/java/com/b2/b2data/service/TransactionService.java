package com.b2.b2data.service;

import com.b2.b2data.domain.Transaction;
import com.b2.b2data.domain.TransactionLine;
import com.b2.b2data.repository.TransactionLineRepository;
import com.b2.b2data.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides services for handling {@link Transaction} objects
 */
@Service
public class TransactionService {

    private final TransactionRepository REPO;
    private final TransactionLineRepository LINE_REPO;

    /**
     * Constructs a new transaction service
     *
     * @param repo A transaction repository
     * @param lineRepo A transaction line repository
     */
    @Autowired
    public TransactionService(TransactionRepository repo, TransactionLineRepository lineRepo) {
        REPO = repo;
        LINE_REPO = lineRepo;
    }

    /**
     * Finds the transaction with the given id
     *
     * @param id A transaction id
     * @return The transaction with the given id, or null if it does not exist
     */
    public Transaction findById(Integer id) {
        return REPO.findById(id).orElse(null);
    }

    /**
     * Finds all transactions
     *
     * @return A list of transactions sorted by date descending
     */
    public List<Transaction> findAll() {
        return REPO.findAll(null, Sort.by(Transaction.DATE).descending());
    }

    /**
     * Finds all transactions matching the given parameters
     *
     * @param from A minimum bounding date
     * @param to A maximum bounding date
     * @param memoPattern A memo pattern
     * @return A list of transactions matching the given parameters, sorted by transaction date descending
     */
    public List<Transaction> findAll(LocalDate from, LocalDate to, String memoPattern) {
        return REPO.findAll(
                Specification
                        .where(dateOnOrAfter(from))
                        .and(dateOnOrBefore(to))
                        .and(memoLike(memoPattern)),
                Sort.by(Transaction.DATE).descending()
        );
    }

    /**
     * Saves the given transaction to the database
     *
     * @param transaction A transaction to save
     * @return The transaction saved in the database
     */
    @Transactional
    @Modifying
    public Transaction save(Transaction transaction) {
        return REPO.save(transaction);
    }

    /**
     * Saves the given transaction with transaction lines to the database
     *
     * @param transaction A transaction to save
     * @param lines A list of transaction lines
     * @return The transaction saved in the database
     */
    @Transactional
    @Modifying
    public Transaction save(Transaction transaction, List<TransactionLine> lines) {
        Transaction tranToSave = REPO.save(transaction);
        List<TransactionLine> linesToSave = new ArrayList<>(lines.size());

        for (int i = 0; i < lines.size(); i++) {
            TransactionLine line = lines.get(i);
            line.setTransaction(tranToSave);
            line.setLineId(i+1);
            linesToSave.add(line);
        }
        List<TransactionLine> existingLines = LINE_REPO.findAllByTransactionIdOrderByLineIdAsc(tranToSave.getId());

        // new lines will overwrite existing ones, so if there are more existing lines than new lines,
        // the excess existing lines will not get overwritten, so they must be deleted
        if (existingLines.size() > linesToSave.size())
            LINE_REPO.deleteAll(existingLines);

        LINE_REPO.saveAll(linesToSave);

        return tranToSave;
    }

    /**
     * Deletes the given transaction and all associated transaction lines from the database
     *
     * @param transaction A transaction to delete
     */
    @Transactional
    @Modifying
    public void delete(Transaction transaction) {
        LINE_REPO.deleteAll(LINE_REPO.findAllByTransactionIdOrderByLineIdAsc(transaction.getId()));
        REPO.delete(transaction);
    }

    //region SPECIFICATIONS

    /**
     * Creates a specification for a transaction with a date >= the given from date
     *
     * @param from A minimum bounding date
     * @return A specification for a transaction with a date >= the given from date,
     *         or an always true specification if from is null
     */
    private Specification<Transaction> dateOnOrAfter(LocalDate from) {
        return ((root, query, criteriaBuilder) ->
                from == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.greaterThanOrEqualTo(root.get(Transaction.DATE), from)
        );
    }

    /**
     * Creates a specification for a transaction with a date <= the given to date
     *
     * @param to A maximum bounding date
     * @return A specification for a transaction with a date <= the given to date,
     *         or an always true specification if to is null
     */
    private Specification<Transaction> dateOnOrBefore(LocalDate to) {
        return ((root, query, criteriaBuilder) ->
                to == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.lessThanOrEqualTo(root.get(Transaction.DATE), to)
        );
    }

    /**
     * Creates a specification for a transaction with a memo matching the given memo patten
     *
     * @param memoPattern A memo pattern
     * @return A specification for a transaction with a memo matching the given memo pattern,
     *         or an always true specification if the memo pattern is null
     */
    private Specification<Transaction> memoLike(String memoPattern) {
        return ((root, query, criteriaBuilder) ->
                memoPattern == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.like(root.get(Transaction.MEMO), memoPattern)
        );
    }
    //endregion
}
