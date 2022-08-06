package com.b2.b2data.service;

import com.b2.b2data.domain.Player;
import com.b2.b2data.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository REPO;

    @Autowired
    public PlayerService(PlayerRepository repo) {
        REPO = repo;
    }

    public Player findById(int id) {
        return REPO.findById(id).orElse(null);
    }

    public Player findByName(String name) {
        return REPO.findByName(name).orElse(null);
    }

    public List<Player> findAll() {
        return REPO.findAllByOrderByName();
    }

    public List<Player> findAllBanks(boolean isBank) {
        return REPO.findAllByIsBankOrderByName(isBank);
    }

    @Transactional
    @Modifying
    public Player save(Player player) {
        return REPO.save(player);
    }

    @Transactional
    @Modifying
    public boolean delete(Player player) {

        if (player == null || !REPO.existsById(player.getId()))
            return false;

        REPO.delete(player);
        return !REPO.existsById(player.getId());
    }
}
