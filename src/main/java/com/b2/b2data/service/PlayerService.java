package com.b2.b2data.service;

import com.b2.b2data.model.Player;
import com.b2.b2data.repository.PlayerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Provides services for manipulating {@link Player} records
 */
@Service
public class PlayerService {

    private final PlayerDAO DAO;

    @Autowired
    public PlayerService(PlayerDAO DAO) {
        this.DAO = DAO;
    }

    /**
     * Finds the player with the given id
     *
     * @param id The id of a player
     * @return The player with the given id, or null if it does not exist
     */
    public Player findById(long id) {
        return DAO.findById(id).orElse(null);
    }

    /**
     * Finds the player with the given name
     *
     * @param name The name of a player
     * @return The player with the given name, or null if it does not exist
     */
    public Player findByName(String name) {
        return DAO.findByName(name).orElse(null);
    }

    /**
     * Finds all players in the database
     *
     * @return A list of players ordered by id
     */
    public List<Player> findAll() {
        return DAO.findAllByOrderById();
    }

    /**
     * Saves the player to the database
     *
     * @param player A player to save
     * @return The saved player
     */
    public Player save(Player player) {
        return DAO.save(player);
    }

    /**
     * Deletes the given player
     *
     * @param player A player to delete
     */
    public void delete(Player player) {
        DAO.delete(player);
    }
}
