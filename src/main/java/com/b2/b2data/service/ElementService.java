package com.b2.b2data.service;

import com.b2.b2data.domain.Element;
import com.b2.b2data.repository.ElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ElementService {

    private final ElementRepository REPO;

    @Autowired
    public ElementService(ElementRepository repo) {
        REPO = repo;
    }

    public Element findById(Integer id) {
        return REPO.findById(id).orElse(null);
    }

    public Element findByNumber(Integer number) {
        return REPO.findByNumber(number).orElse(null);
    }

    public Element findByName(String name) {
        return REPO.findByName(name).orElse(null);
    }

    public List<Element> findAll() {
        return REPO.findAllByOrderByNumber();
    }

    @Transactional
    @Modifying
    public Element save(Element element) {
        return REPO.save(element);
    }

    @Transactional
    @Modifying
    public boolean delete(Element element) {

        if (element == null || !REPO.existsById(element.getId()))
            return false;

        REPO.delete(element);
        return !REPO.existsById(element.getId());
    }
}
