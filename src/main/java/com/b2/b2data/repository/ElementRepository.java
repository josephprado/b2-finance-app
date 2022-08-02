package com.b2.b2data.repository;

import com.b2.b2data.domain.Element;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ElementRepository extends PagingAndSortingRepository<Element,Integer> {

    Optional<Element> findByNumber(Integer number);
    Optional<Element> findByName(String name);
    List<Element> findAllByOrderByNumber();
}
