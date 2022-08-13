package com.b2.b2data.repository;

import com.b2.b2data.domain.Player;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Provides CRUD operations for {@link Player} objects in the database
 */
@Repository
public interface PlayerRepository extends PagingAndSortingRepository<Player,Integer> {

    /**
     * Finds the player with the given name
     *
     * @param name A player name
     * @return An optional containing the player with the given name, if it exists
     */
    Optional<Player> findByName(String name);

    /**
     * Finds all players
     *
     * @return A list of players sorted by name ascending
     */
    List<Player> findAllByOrderByNameAsc();

    /**
     * Finds all players with the given isBank status
     *
     * @param isBank True if the player is a bank
     * @return A list of players matching the given isBank status, sorted by name ascending
     */
    List<Player> findAllByIsBankOrderByNameAsc(Boolean isBank);
}
