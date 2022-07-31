package com.b2.b2data.service;

import com.b2.b2data.repository.ElementDAO;
import com.b2.b2data.model.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Provides services for manipulating {@link Element} records
 */
@Service
public class ElementService {

    private final ElementDAO DAO;

    /**
     * Constructs a new element service
     *
     * @param DAO An element DAO
     */
    @Autowired
    public ElementService(ElementDAO DAO) {
        this.DAO = DAO;
    }

    /**
     * Finds the element with the given id
     *
     * @param id The id of an element
     * @return The element with the given id, or null if it does not exist
     */
    public Element findById(int id) {
        return DAO.findById(id).orElse(null);
    }

    /**
     * Finds the element with the given name
     *
     * @param name The name of an element
     * @return The element with the given name, or null if it does not exist
     */
    public Element findByName(String name) {
        return DAO.findByName(name).orElse(null);
    }

    /**
     * Finds all elements in the database
     *
     * @return A list of elements ordered by id
     */
    public List<Element> findAll() {
        return DAO.findAllByOrderById();
    }

    /**
     * Saves the element to the database
     *
     * @param element An element to save
     * @return The saved element
     */
    public Element save(Element element) {
        return DAO.save(element);
    }

    /**
     * Deletes the given element
     *
     * @param element An element to delete
     */
    public void delete(Element element) {
        DAO.delete(element);
    }
}
