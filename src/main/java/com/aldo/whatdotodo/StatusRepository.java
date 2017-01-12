package com.aldo.whatdotodo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StatusRepository extends CrudRepository<Status, String> {
    List<Status> findAll();

}
