package com.guido.dao;

import java.util.List;

/**
 * DAO methods for entities
 */
public interface DAO<E> {

    public E findById(int id);

    public List<E> findAll();

    public void persist(E entity);

    public void persistBatch(List<E> entities);

}
