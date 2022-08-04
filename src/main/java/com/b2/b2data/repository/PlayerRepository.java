package com.b2.b2data.repository;

import com.b2.b2data.domain.Player;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends PagingAndSortingRepository<Player,Integer> {

    Optional<Player> findByName(String name);
    List<Player> findAllByOrderByName();
    List<Player> findAllByIsBankOrderByName(boolean isBank);
}
