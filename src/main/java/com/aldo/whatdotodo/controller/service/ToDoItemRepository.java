package com.aldo.whatdotodo.controller.service;

import com.aldo.whatdotodo.controller.model.ToDoItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
public interface ToDoItemRepository extends CrudRepository<ToDoItem, Long> {
    List<ToDoItem> findAll();
}
