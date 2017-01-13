package com.aldo.whatdotodo.controller;

import com.aldo.whatdotodo.model.ResourceNotFoundException;
import com.aldo.whatdotodo.model.Status;
import com.aldo.whatdotodo.model.ToDoItem;
import com.aldo.whatdotodo.service.StatusRepository;
import com.aldo.whatdotodo.service.ToDoItemRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ToDoController {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(ToDoController.class);

    @Autowired ToDoItemRepository toDoItemRepository;
    @Autowired StatusRepository statusRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<ToDoItem> items() {
        return toDoItemRepository.findAll();
    }

    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ToDoItem item(@PathVariable Long itemId) {
        ToDoItem item = toDoItemRepository.findOne(itemId);

        if (item != null) {
            return item;
        }
        throw todoItemNotFound(itemId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ToDoItem add(@RequestBody ToDoItem item) {
        return toDoItemRepository.save(item);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public List<ToDoItem> setItems(@RequestBody List<ToDoItem> items) {
        toDoItemRepository.deleteAll();
        if (!items.isEmpty()) {
            toDoItemRepository.save(items);
        }
        return toDoItemRepository.findAll();
    }

    @RequestMapping(value = "{itemId}", method = RequestMethod.DELETE)
    public ToDoItem delete(@PathVariable Long itemId) {
        ToDoItem item = toDoItemRepository.findOne(itemId);
        if (item != null) {
            toDoItemRepository.delete(itemId);
            return item;
        }
        throw todoItemNotFound(itemId);
    }

    @RequestMapping(value = "{itemId}", method = RequestMethod.PUT)
    public ToDoItem update(@PathVariable Long itemId, @RequestBody ToDoItem modifiedItem) {
        //TODO Check url itemId and object itemId match
        ToDoItem item = toDoItemRepository.findOne(itemId);

        if (item != null) {
            item.setTitle(modifiedItem.getTitle());
            item.setDescription(modifiedItem.getDescription());
            updateStatus(item, modifiedItem.getStatus());

            toDoItemRepository.save(item);
            return item;
        }
        throw todoItemNotFound(itemId);
    }

    @RequestMapping(value = "{itemId}/status", method = RequestMethod.PUT)
    public String updateStatus(@PathVariable Long itemId, @RequestBody Status status) {
        ToDoItem item = toDoItemRepository.findOne(itemId);
        if (item != null) {
            updateStatus(item, status.getName());
            toDoItemRepository.save(item);
            return item.getStatus();
        }
        throw todoItemNotFound(itemId);
    }

    private void updateStatus(ToDoItem item, String status) {
        if (status == null) {
            item.setStatusObj(null);
        } else {
            Status newStatus = statusRepository.findOne(status);
            if (newStatus == null) {
                throw statusNotFound(status);
            }
            item.setStatusObj(newStatus);
        }
    }

    private ResourceNotFoundException todoItemNotFound(Long itemId) {
        return new ResourceNotFoundException(ToDoItem.class.getName(), itemId.toString());
    }

    private ResourceNotFoundException statusNotFound(String status) {
        return new ResourceNotFoundException(Status.class.getName(), status);
    }
}
