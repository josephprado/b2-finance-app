package com.b2.b2data.service;

import com.b2.b2data.domain.Transaction;
import com.b2.b2data.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    public Transaction findById(Integer id) {
        return REPO.findById(id).orElse(null);
    }

    public List<Transaction> findAll() {
        return REPO.findAll(null, Sort.by(Transaction.DATE).descending());
    }

    public List<Transaction> findAll(LocalDate from, LocalDate to, String memo) {
        return REPO.findAll(
                Specification
                        .where(dateOnOrAfter(from))
                        .and(dateOnOrBefore(to))
                        .and(memoLike(memo)),
                Sort.by(Transaction.DATE).descending()
        );
    }

    @Transactional
    @Modifying
    public Transaction save(Transaction transaction) {
        return REPO.save(transaction);
    }

    @Transactional
    @Modifying
    public boolean delete(Transaction transaction) {

        if (transaction == null || !REPO.existsById(transaction.getId()))
            return false;

        REPO.delete(transaction);
        return !REPO.existsById(transaction.getId());
    }

    /********************************************************************************
     *                              SPECIFICATIONS
     ********************************************************************************/

    private Specification<Transaction> dateOnOrAfter(LocalDate from) {
        return ((root, query, criteriaBuilder) ->
                from == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.greaterThanOrEqualTo(root.get(Transaction.DATE), from)
        );
    }

    private Specification<Transaction> dateOnOrBefore(LocalDate to) {
        return ((root, query, criteriaBuilder) ->
                to == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.lessThanOrEqualTo(root.get(Transaction.DATE), to)
        );
    }

    private Specification<Transaction> memoLike(String memo) {
        return ((root, query, criteriaBuilder) ->
                memo == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.like(root.get(Transaction.MEMO), memo)
        );
    }
}
