package com.b2.b2data.service;

import com.b2.b2data.domain.Transaction;
import com.b2.b2data.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository REPO;

    @Autowired
    public TransactionService(TransactionRepository repo) {
        REPO = repo;
    }

    public Transaction findById(int id) {
        return REPO.findById(id).orElse(null);
    }

    public List<Transaction> findAll() {
        return REPO.findAllByOrderByDateDesc();
    }

    public List<Transaction> findAll(LocalDate from, LocalDate to) {
        return REPO.findAllByDateBetweenOrderByDateDesc(from, to);
    }

    public List<Transaction> findAll(String memoPattern) {
        return REPO.findAllByMemoLikeOrderByDateDesc(memoPattern);
    }

    @Transactional
    @Modifying
    public Transaction save(Transaction transaction) {
        return REPO.save(transaction);
    }

    @Transactional
    @Modifying
    public void delete(Transaction transaction) {
        REPO.delete(transaction);
    }
}
