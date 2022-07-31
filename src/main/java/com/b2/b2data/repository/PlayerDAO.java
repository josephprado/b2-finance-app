package com.b2.b2data.repository;

import com.b2.b2data.model.Player;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * A data access object for interacting with {@link Player} records
 */
@Repository
public interface PlayerDAO extends PagingAndSortingRepository<Player,Long> {

    @Override
    Optional<Player> findById(Long id);

    Optional<Player> findByName(String name);

    List<Player> findAllByOrderById();
}
