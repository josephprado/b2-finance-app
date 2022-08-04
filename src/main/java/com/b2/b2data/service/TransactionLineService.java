package com.b2.b2data.service;

import com.b2.b2data.domain.*;
import com.b2.b2data.repository.TransactionLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public TransactionLine findById(int transactionId, int lineId) {
        return REPO.findById(new TransactionLineId(transactionId, lineId)).orElse(null);
    }

    public List<TransactionLine> findAll() {
        return REPO.findAllByOrderByTransactionDateDesc();
    }

    public List<TransactionLine> findAll(Transaction transaction) {
        return REPO.findAllByTransactionOrderByLineAsc(transaction);
    }

    public List<TransactionLine> findAll(Account account) {
        return REPO.findAllByAccountOrderByTransactionDateDesc(account);
    }

    public List<TransactionLine> findAll(Player player) {
        return REPO.findAllByPlayerOrderByTransactionDateDesc(player);
    }

    public List<TransactionLine> findAll(String memoPattern) {
        return REPO.findAllByMemoLikeOrderByTransactionDateDesc(memoPattern);
    }

    @Transactional
    @Modifying
    public TransactionLine save(TransactionLine line) {
        return REPO.save(line);
    }

    @Transactional
    @Modifying
    public void delete(TransactionLine line) {
        REPO.delete(line);
    }
}
