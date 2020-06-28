package com.mpholo.project.grocery.service;

import java.util.List;

public interface CrudService<T,ID> {

    T findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
    void delete(T object);
    void save(T object);

}
