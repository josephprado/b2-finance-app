package com.b2.b2data.service;

import com.b2.b2data.domain.Player;
import com.b2.b2data.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Provides services for handling {@link Player} objects
 */
@Service
public class PlayerService {

    private final PlayerRepository REPO;

    /**
     * Constructs a new player
     *
     * @param repo A player repository
     */
    @Autowired
    public PlayerService(PlayerRepository repo) {
        REPO = repo;
    }

    /**
     * Finds the player with the given id
     *
     * @param id A player id
     * @return The element with the given id, or null if it does not exist
     */
    public Player findById(Integer id) {
        return REPO.findById(id).orElse(null);
    }

    /**
     * Finds the player with the given name
     *
     * @param name A player name
     * @return The player with the given name, or null if it does not exist
     */
    public Player findByName(String name) {
        return REPO.findByName(name).orElse(null);
    }

    /**
     * Finds all players
     *
     * @return A list of players sorted by name ascending
     */
    public List<Player> findAll() {
        return REPO.findAllByOrderByNameAsc();
    }

    /**
     * Finds all players with the given isBank status
     *
     * @param isBank True if the player is a bank
     * @return A list of players with the given isBank status, sorted by name ascending
     */
    public List<Player> findAllByBankStatus(Boolean isBank) {
        return REPO.findAllByIsBankOrderByNameAsc(isBank);
    }

    /**
     * Saves the given player to the database
     *
     * @param player A player to save
     * @return The player saved in the database
     */
    @Transactional
    @Modifying
    public Player save(Player player) {
        return REPO.save(player);
    }

    /**
     * Deletes the given player from the database
     *
     * @param player A player to delete
     * @return True if the delete operation was successful, or false if
     *         unsuccessful or if the player is null or does not exist
     */
    @Transactional
    @Modifying
    public boolean delete(Player player) {

        if (player == null || !REPO.existsById(player.getId()))
            return false;

        REPO.delete(player);
        return !REPO.existsById(player.getId());
    }
}
