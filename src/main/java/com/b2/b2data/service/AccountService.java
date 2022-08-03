package com.b2.b2data.service;

import com.b2.b2data.domain.Account;
import com.b2.b2data.domain.Element;
import com.b2.b2data.domain.Player;
import com.b2.b2data.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository REPO;

    @Autowired
    public AccountService(AccountRepository repo) {
        REPO = repo;
    }

    public Account findById(int id) {
        return REPO.findById(id).orElse(null);
    }

    public Account findByNumber(String number) {
        return REPO.findByNumber(number).orElse(null);
    }

    public Account findByName(String name) {
        return REPO.findByName(name).orElse(null);
    }

    public List<Account> findAll() {
        return REPO.findAllByOrderByNumber();
    }

    public List<Account> findAll(Element element) {
        return REPO.findAllByElementOrderByNumber(element);
    }

    public List<Account> findAll(Player player) {
        return REPO.findAllByPlayerOrderByNumber(player);
    }

    @Transactional
    @Modifying
    public Account save(Account account) {
        return REPO.save(account);
    }

    @Transactional
    @Modifying
    public void delete(Account account) {
        REPO.delete(account);
    }
}
