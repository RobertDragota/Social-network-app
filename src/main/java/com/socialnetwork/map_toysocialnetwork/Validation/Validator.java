package com.socialnetwork.map_toysocialnetwork.Validation;

public interface Validator<E> {
    /**
     * @param entity
     * @throws ValidException
     */
    void validate(E entity) throws ValidException;
}
