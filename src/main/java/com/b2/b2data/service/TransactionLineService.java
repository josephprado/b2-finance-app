package com.b2.b2data.service;

import com.b2.b2data.domain.*;
import com.b2.b2data.repository.TransactionLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Provides services for handling {@link TransactionLine} objects
 */
@Service
public class TransactionLineService {

    private final TransactionLineRepository REPO;

    /**
     * Constructs a new transaction line service
     *
     * @param repo A transaction line repository
     */
    @Autowired
    public TransactionLineService(TransactionLineRepository repo) {
        REPO = repo;
    }

    /**
     * Finds the transaction line with the given transaction line id
     *
     * @param id A transaction line id
     * @return The transaction line with the given transaction line id
     * @throws NoSuchElementException If the transaction line does not exist
     */
    public TransactionLine findById(TransactionLineId id) throws NoSuchElementException {
        return REPO.findById(id)
                    .orElseThrow(() -> new NoSuchElementException(
                            "Transaction line id="+id.getTransactionId()+"-"+id.getLineId()+" does not exist."));
    }

    /**
     * Finds all transaction lines with the given transaction id
     *
     * @param transactionId A transaction id
     * @return A list of transaction lines with the given transaction id, sorted by line id ascending
     */
    public List<TransactionLine> findAllByTransactionId(Integer transactionId) {
        return REPO.findAllByTransactionIdOrderByLineIdAsc(transactionId);
    }

    /**
     * Finds all transaction lines with the given account number
     *
     * @param accountNumber An account number
     * @return A list of transaction lines with the given account number, sorted by transaction date descending
     */
    public List<TransactionLine> findAllByAccountNumber(String accountNumber) {
        return REPO.findAllByAccountNumberOrderByTransactionDateDesc(accountNumber);
    }

    /**
     * Finds all transaction lines with the given player name
     *
     * @param playerName A player name
     * @return A list of transaction lines with the given player name, sorted by transaction date descending
     */
    public List<TransactionLine> findAllByPlayerName(String playerName) {
        return REPO.findAllByPlayerNameOrderByTransactionDateDesc(playerName);
    }

    /**
     * Finds all transaction lines
     *
     * @return A list of transaction lines sorted by transaction date descending
     */
    public List<TransactionLine> findAll() {
        return REPO.findAllByOrderByTransactionDateDesc();
    }

    // TODO: sort by transaction date descending
    /**
     * Finds all transaction lines matching the given parameters
     *
     * @param transactionId A transaction id
     * @param accountNumber An account number
     * @param playerName A player name
     * @param memoPattern A memo pattern
     * @param isReconciled True if the reconciled date is not null
     * @param from A minimum bounding date
     * @param to A maximum bounding date
     * @return A list of transaction lines matching the given parameters, sorted by transaction date descending
     */
    public List<TransactionLine> findAll(Integer transactionId, String accountNumber, String playerName,
                                         String memoPattern, Boolean isReconciled, LocalDate from, LocalDate to) {

        List<TransactionLine> lines =
                REPO.findAll(
                        Specification
                                .where(transactionIdEquals(transactionId))
                                .and(accountNumberEquals(accountNumber))
                                .and(playerNameEquals(playerName))
                                .and(memoLike(memoPattern))
                                .and(reconciledIs(isReconciled))
                                .and(dateOnOrAfter(from))
                                .and(dateOnOrBefore(to)),
                        Sort.by(TransactionLine.TRANSACTION).descending()
        );
        return lines
                .stream()
                .sorted((a,b) -> b.getTransaction().getDate().compareTo(a.getTransaction().getDate()))
                .toList();
    }

    /**
     * Saves the given transaction line to the database
     *
     * @param line A transaction line to save
     * @return The transaction line saved in the database
     */
    @Transactional
    @Modifying
    public TransactionLine save(TransactionLine line) {
        return REPO.save(line);
    }

    /**
     * Deletes the given transaction line from the database
     *
     * @param line A transaction line to delete
     */
    @Transactional
    @Modifying
    public void delete(TransactionLine line) {
        REPO.delete(line);
    }

    //region SPECIFICATIONS

    /**
     * Creates a specification for a transaction line with the given transaction id
     *
     * @param id A transaction id
     * @return A specification for a transaction line with the given transaction id,
     *         or an always true specification if the id is null
     */
    private Specification<TransactionLine> transactionIdEquals(Integer id) {
        return ((root, query, criteriaBuilder) ->
                id == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get(TransactionLine.TRANSACTION).get(Transaction.ID), id)
        );
    }

    /**
     * Creates a specification for a transaction line with the given account number
     *
     * @param accountNumber An account number
     * @return A specification for a transaction line with the given account number,
     *         or an always true specification if the account number is null
     */
    private Specification<TransactionLine> accountNumberEquals(String accountNumber) {
        return ((root, query, criteriaBuilder) ->
                accountNumber == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get(TransactionLine.ACCOUNT).get(Account.NUMBER), accountNumber)
        );
    }

    /**
     * Creates a specification for a transaction line with the given player name
     *
     * @param playerName A player name
     * @return A specification for a transaction line with the given player name,
     *         or an always true specification if the player name is null
     */
    private Specification<TransactionLine> playerNameEquals(String playerName) {
        return ((root, query, criteriaBuilder) ->
                playerName == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get(TransactionLine.PLAYER).get(Player.NAME), playerName)
        );
    }

    /**
     * Creates a specification for a transaction line with a memo matching the given memo pattern
     *
     * @param memoPattern A memo pattern
     * @return A specification for a transaction line with a memo matching the given memo pattern,
     *         or an always true specification if the memo pattern is null
     */
    private Specification<TransactionLine> memoLike(String memoPattern) {
        return ((root, query, criteriaBuilder) ->
                memoPattern == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.like(root.get(TransactionLine.MEMO), memoPattern)
        );
    }

    /**
     * Creates a specification for a transaction line that is reconciled
     *
     * @param isReconciled True if the reconciled date is not null
     * @return A specification for a transaction line that is reconciled,
     *         or an always true specification if isReconciled is null
     */
    private Specification<TransactionLine> reconciledIs(Boolean isReconciled) {
        return ((root, query, criteriaBuilder) -> {

            if (isReconciled == null)
                return criteriaBuilder.conjunction();

            if (isReconciled)
                return criteriaBuilder.isNotNull(root.get(TransactionLine.DATE_RECONCILED));

            return criteriaBuilder.isNull(root.get(TransactionLine.DATE_RECONCILED));
        });
    }

    /**
     * Creates a specification for a transaction line with a transaction date >= the given from date
     *
     * @param from A minimum bounding date
     * @return A specification for a transaction line with a transaction date >= the given from date,
     *         or an always true specification if from is null
     */
    private Specification<TransactionLine> dateOnOrAfter(LocalDate from) {
        return ((root, query, criteriaBuilder) ->
                from == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.greaterThanOrEqualTo(root.get(TransactionLine.TRANSACTION)
                                                                    .get(Transaction.DATE), from)
        );
    }

    /**
     * Creates a specification for a transaction line with a transaction date <= the given to date
     *
     * @param to A maximum bounding date
     * @return A specification for a transaction line with a transaction date <= the given to date,
     *         or an always true specification if to is null
     */
    private Specification<TransactionLine> dateOnOrBefore(LocalDate to) {
        return ((root, query, criteriaBuilder) ->
                to == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.lessThanOrEqualTo(root.get(TransactionLine.TRANSACTION)
                                                                .get(Transaction.DATE), to)
        );
    }
    //endregion
}
