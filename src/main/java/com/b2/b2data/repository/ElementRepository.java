package com.b2.b2data.repository;

import com.b2.b2data.domain.Element;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Provides CRUD operations for {@link Element} objects in the database
 */
@Repository
public interface ElementRepository extends PagingAndSortingRepository<Element,Integer> {

    /**
     * Finds the element with the given number
     *
     * @param number An element number
     * @return An optional containing the element with the given number, if it exists
     */
    Optional<Element> findByNumber(Integer number);

    /**
     * Finds the element with the given name
     *
     * @param name An element name
     * @return An optional containing the element with the given name, if it exists
     */
    Optional<Element> findByName(String name);

    /**
     * Finds all elements
     *
     * @return A list of elements sorted by number ascending
     */
    List<Element> findAllByOrderByNumberAsc();
}
