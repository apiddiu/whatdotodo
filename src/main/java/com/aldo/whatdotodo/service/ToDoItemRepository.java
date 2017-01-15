package com.aldo.whatdotodo.service;

import com.aldo.whatdotodo.model.ToDoItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
public interface ToDoItemRepository extends CrudRepository<ToDoItem, Long> {
    List<ToDoItem> findAll();
    List<ToDoItem> findByUsername(String username);
    ToDoItem findByIdAndUsername(Long id, String username);
    void deleteByUsername(String username);
}
