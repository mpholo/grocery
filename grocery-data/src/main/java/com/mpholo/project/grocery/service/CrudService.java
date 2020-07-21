package com.mpholo.project.grocery.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T,ID> {

    Optional<T> findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
    void delete(T object);
    T save(T object);
    T edit(Integer id,T object);

}
