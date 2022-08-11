package com.b2.b2data.service;

import com.b2.b2data.domain.Account;
import com.b2.b2data.domain.Player;
import com.b2.b2data.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository REPO;

    @Autowired
    public AccountService(AccountRepository repo) {
        REPO = repo;
    }

    public Account findById(Integer id) {
        return REPO.findById(id).orElse(null);
    }

    public Account findByNumber(String number) {
        return REPO.findByNumber(number).orElse(null);
    }

    public Account findByName(String name) {
        return REPO.findByName(name).orElse(null);
    }

    public List<Account> findAll() {
        return REPO.findAll(null, Sort.by(Account.NUMBER).ascending()).stream().toList();
    }

    public List<Account> findAll(Integer element, String player, Boolean isBank) {
        return REPO.findAll(
                Specification
                        .where(elementNumberEquals(element))
                        .and(playerNameEquals(player))
                        .and(isBankEquals(isBank)),
                Sort.by(Account.NUMBER).ascending()
        );
    }

    @Transactional
    @Modifying
    public Account save(Account account) {
        return REPO.save(account);
    }

    @Transactional
    @Modifying
    public boolean delete(Account account) {

        if (account == null || !REPO.existsById(account.getId()))
            return false;

        REPO.delete(account);
        return !REPO.existsById(account.getId());
    }

    /********************************************************************************
     *                              SPECIFICATIONS
     ********************************************************************************/

    private Specification<Account> elementNumberEquals(Integer number) {
        return ((root, query, criteriaBuilder) ->
                number == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get(Account.ELEMENT), number)
        );
    }

    private Specification<Account> playerNameEquals(String name) {
        return ((root, query, criteriaBuilder) ->
                name == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get(Account.PLAYER).get(Player.NAME), name)
        );
    }

    private Specification<Account> isBankEquals(Boolean isBank) {
        return ((root, query, criteriaBuilder) -> {

            if (isBank == null)
                return criteriaBuilder.conjunction();

            Expression<Boolean> playerIsBank = root.join(Account.PLAYER, JoinType.LEFT).get(Player.IS_BANK);

            return isBank
                    ? criteriaBuilder.isTrue(playerIsBank)
                    : criteriaBuilder.or(
                            criteriaBuilder.isNull(playerIsBank),
                            criteriaBuilder.isFalse(playerIsBank));
        });
    }
}
