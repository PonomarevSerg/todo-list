package com.ponomarev;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, K> {

    List<T> findAll();

    Optional<T> findById(K id);

    T save(T entity);

    boolean existsById(K id);

    void deleteById(K id);

    Iterable<T> saveAll(Iterable<T> entities);

    void deleteAll();
}
