package com.socialnetwork.map_toysocialnetwork.Repository;

import com.socialnetwork.map_toysocialnetwork.Domain.Entity;

import java.util.Optional;

public interface Repo<ID,E extends Entity<ID>> {
    /**
     * @param e
     * @return
     */
    Optional<E> add(E e);

    /**
     * @param e
     * @return
     */
    Optional<E> update(E e);

    /**
     * @param e
     * @return
     */
    Optional<E>delete(E e);

    /**
     * @param id
     * @return
     */
    Optional<E> find(ID id);

    /**
     * @return
     */
    Iterable<E> getAll();

    Iterable<E> getFromIndexToOffset(Long index,Long offset);

}
