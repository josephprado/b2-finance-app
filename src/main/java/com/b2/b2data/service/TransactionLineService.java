package com.b2.b2data.service;

import com.b2.b2data.domain.*;
import com.b2.b2data.repository.TransactionLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionLineService {

    private final TransactionLineRepository REPO;

    @Autowired
    public TransactionLineService(TransactionLineRepository repo) {
        REPO = repo;
    }

    public TransactionLine findById(Integer transactionId, Integer lineId) {
        return REPO.findById(new TransactionLineId(transactionId, lineId)).orElse(null);
    }

    public List<TransactionLine> findAllByTransaction(Integer transactionId) {
        return REPO.findAllByTransactionIdOrderByLineAsc(transactionId);
    }

    public List<TransactionLine> findAllByAccount(String accountNumber) {
        return REPO.findAllByAccountNumberOrderByTransactionDateDesc(accountNumber);
    }

    public List<TransactionLine> findAllByPlayer(String playerName) {
        return REPO.findAllByPlayerNameOrderByTransactionDateDesc(playerName);
    }

    public List<TransactionLine> findAllByMemo(String memoPattern) {
        return REPO.findAllByMemoLikeOrderByTransactionDateDesc(memoPattern);
    }

    public List<TransactionLine> findAll() {
        return REPO.findAllByOrderByTransactionDateDesc();
    }

    public List<TransactionLine> findAll(Integer transactionId, String accountNumber,
                                         String playerName, String memoPattern) {

        List<TransactionLine> lines =
                REPO.findAll(
                        Specification
                                .where(transactionIdEquals(transactionId))
                                .and(accountNumberEquals(accountNumber))
                                .and(playerNameEquals(playerName))
                                .and(memoLike(memoPattern)),
                        Sort.by(TransactionLine.TRANSACTION).descending()
        );
        return lines
                .stream()
                .sorted((a,b) -> b.getTransaction().getDate().compareTo(a.getTransaction().getDate()))
                .toList();
    }

    @Transactional
    @Modifying
    public TransactionLine save(TransactionLine line) {
        return REPO.save(line);
    }

    @Transactional
    @Modifying
    public boolean delete(TransactionLine line) {

        if (line == null ||
                !REPO.existsById(new TransactionLineId(line.getTransaction().getId(), line.getLine())))
            return false;

        REPO.delete(line);
        return !REPO.existsById(new TransactionLineId(line.getTransaction().getId(), line.getLine()));
    }

    /********************************************************************************
     *                              SPECIFICATIONS
     ********************************************************************************/

    private Specification<TransactionLine> transactionIdEquals(Integer id) {
        return ((root, query, criteriaBuilder) ->
                id == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get(TransactionLine.TRANSACTION).get(Transaction.ID), id)
        );
    }

    private Specification<TransactionLine> accountNumberEquals(String number) {
        return ((root, query, criteriaBuilder) ->
                number == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get(TransactionLine.ACCOUNT).get(Account.NUMBER), number)
        );
    }

    private Specification<TransactionLine> playerNameEquals(String name) {
        return ((root, query, criteriaBuilder) ->
                name == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get(TransactionLine.PLAYER).get(Player.NAME), name)
        );
    }

    private Specification<TransactionLine> memoLike(String memo) {
        return ((root, query, criteriaBuilder) ->
                memo == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.like(root.get(TransactionLine.MEMO), memo)
        );
    }
}
