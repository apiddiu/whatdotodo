package com.aldo.whatdotodo.controller;

import com.aldo.whatdotodo.model.ResourceNotFoundException;
import com.aldo.whatdotodo.model.Status;
import com.aldo.whatdotodo.model.ToDoItem;
import com.aldo.whatdotodo.service.StatusRepository;
import com.aldo.whatdotodo.service.ToDoItemRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration
    .EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@EnableGlobalMethodSecurity(securedEnabled = true)
public class ToDoController {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(ToDoController.class);

    @Autowired ToDoItemRepository toDoItemRepository;
    @Autowired StatusRepository statusRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<ToDoItem> items(Principal principal) {
        return toDoItemRepository.findByUsername(principal.getName());
    }

    @RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
    public ToDoItem item(@PathVariable Long itemId, Principal principal) {
        ToDoItem item = toDoItemRepository.findByIdAndUsername(itemId, principal.getName());

        if (item != null) {
            return item;
        }
        throw todoItemNotFound(itemId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ToDoItem add(@RequestBody ToDoItem item, Principal principal) {
        item.setUsername(principal.getName());
        return toDoItemRepository.save(item);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @Secured("ROLE_ADMIN")
    @Transactional
    public ResponseEntity<Void> clearItems(Principal principal) {
        toDoItemRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{itemId}", method = RequestMethod.DELETE)
    @Transactional
    public ToDoItem delete(@PathVariable Long itemId, Principal principal) {
        ToDoItem item = toDoItemRepository.findByIdAndUsername(itemId, principal.getName());
        if (item != null) {
            toDoItemRepository.delete(itemId);
            return item;
        }
        throw todoItemNotFound(itemId);
    }

    @RequestMapping(value = "/{itemId}", method = RequestMethod.PUT)
    @Transactional
    public ToDoItem update(@PathVariable Long itemId, @RequestBody ToDoItem modifiedItem, Principal principal) {
        //TODO Check url itemId and object itemId match
        ToDoItem item = toDoItemRepository.findByIdAndUsername(itemId, principal.getName());

        if (item != null) {
            item.setTitle(modifiedItem.getTitle());
            item.setDescription(modifiedItem.getDescription());
            item.setStatusObj(item.getStatusObj());

            toDoItemRepository.save(item);
            return item;
        }
        throw todoItemNotFound(itemId);
    }

    @RequestMapping(value = "/{itemId}/status", method = RequestMethod.PUT)
    @Transactional
    public Status updateStatus(@PathVariable Long itemId, @RequestBody Status status, Principal principal) {
        ToDoItem item = toDoItemRepository.findByIdAndUsername(itemId, principal.getName());
        if (item != null) {
            Status newStatus = statusRepository.findOne(status.getName());
            if (newStatus == null) {
                throw statusNotFound(status.getName());
            }
            item.setStatusObj(status);

            return toDoItemRepository.save(item).getStatusObj();
        }
        throw todoItemNotFound(itemId);
    }

    private ResourceNotFoundException todoItemNotFound(Long itemId) {
        return new ResourceNotFoundException(ToDoItem.class.getName(), itemId.toString());
    }

    private ResourceNotFoundException statusNotFound(String status) {
        return new ResourceNotFoundException(Status.class.getName(), status);
    }
}
