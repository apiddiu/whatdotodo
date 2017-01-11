package com.aldo.whatdotodo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ToDoItemRepository extends CrudRepository<ToDoItem, Long> {
    List<ToDoItem> findAll();

}
