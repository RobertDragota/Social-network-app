package com.socialnetwork.map_toysocialnetwork.Repository;

import com.socialnetwork.map_toysocialnetwork.Domain.Entity;
import com.socialnetwork.map_toysocialnetwork.Validation.ValidException;
import com.socialnetwork.map_toysocialnetwork.Validation.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepo<ID, E extends Entity<ID>> implements Repo<ID,E> {
    protected Validator<E> validator;
    protected Map<ID, E> Entitys;

    public InMemoryRepo(Validator<E> validator) {
        this.validator = validator;
        Entitys = new HashMap<>();
    }

    @Override
    public Optional<E> add(E e) {
        if (e == null)
            throw new ValidException("Error from InMemoryRepo:add(E) :E can't be NULL!");
        validator.validate(e);
        return Optional.ofNullable(Entitys.putIfAbsent(e.getId(),e));
    }

    @Override
    public Optional<E> update(E e) {
        if (e == null)
            throw new ValidException("Error from InMemoryRepo:update(E) :E can't be NULL!");
        validator.validate(e);
        return Optional.ofNullable(Entitys.put(e.getId(), e));
    }

    @Override
    public Optional<E> delete(E e) {
        if (e == null)
            throw new ValidException("Error from InMemoryRepo:delete(E) :E can't be NULL!");

        validator.validate(e);
        return Optional.ofNullable(Entitys.remove(e.getId()));
    }

    @Override
    public Optional<E> find(ID id) {
        if (id == null)
            throw new ValidException("Error from InMemoryRepo:find(ID) :ID can't be NULL!");
        return Optional.ofNullable(Entitys.get(id));
    }

    @Override
    public Iterable<E> getAll() {
        return new ArrayList<>(Entitys.values());
    }

    @Override
    public Iterable<E> getFromIndexToOffset(Long index, Long offset) {
        return null;
    }
}
