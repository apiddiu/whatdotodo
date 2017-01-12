package com.aldo.whatdotodo.controller.service;

import com.aldo.whatdotodo.controller.model.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StatusRepository extends CrudRepository<Status, String> {
    List<Status> findAll();

}
