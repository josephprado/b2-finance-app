package com.b2.b2data.repository;

import com.b2.b2data.model.Element;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * A data access object for interacting with {@link Element} records
 */
@Repository
public interface ElementDAO extends PagingAndSortingRepository<Element,Integer> {

    @Override
    Optional<Element> findById(Integer id);
    Optional<Element> findByName(String name);
    List<Element> findAllByOrderById();
}
