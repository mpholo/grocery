package com.mpholo.project.grocery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenericRepository<T,ID extends  Integer> extends JpaRepository<T,ID> {

}
