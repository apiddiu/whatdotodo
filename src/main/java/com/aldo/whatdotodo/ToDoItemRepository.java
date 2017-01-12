package com.aldo.whatdotodo;

import org.springframework.data.repository.CrudRepository;

import javax.annotation.sql.DataSourceDefinition;
import java.util.List;
public interface ToDoItemRepository extends CrudRepository<ToDoItem, Long> {
    List<ToDoItem> findAll();

}
