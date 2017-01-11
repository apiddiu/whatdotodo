package com.aldo.whatdotodo;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ToDoController {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(ToDoController.class);

    @Autowired ToDoItemRepository repo;

    @RequestMapping(method = RequestMethod.GET)
    public List<ToDoItem> items() {
        return repo.findAll();
    }

    @RequestMapping(value = "{itemId}",method = RequestMethod.GET)
    public ToDoItem item(@PathVariable Long itemId) {
        ToDoItem item = repo.findOne(itemId);

        if(item!=null){
            return item;
        }
        throw new ResourceNotFoundException();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ToDoItem add(@RequestBody ToDoItem item){
        return repo.save(item);
    }

    @RequestMapping(value = "{itemId}", method = RequestMethod.DELETE)
    public ToDoItem delete(@PathVariable Long itemId){
        ToDoItem item = repo.findOne(itemId);
        if(item!=null){
            repo.delete(itemId);
            return item;
        }
        throw new ResourceNotFoundException();
    }

    @RequestMapping(value = "{itemId}", method = RequestMethod.PUT)
    public ToDoItem update(@PathVariable Long itemId, @RequestBody ToDoItem modifiedItem){
        //TODO Check url itemId and object itemId match
        ToDoItem item = repo.findOne(itemId);
        if(item!=null){
            item.setTitle(modifiedItem.getTitle());
            item.setDescription(modifiedItem.getDescription());
            item.setStatus(modifiedItem.getStatus());
            repo.save(item);
            return modifiedItem;
        }
        throw new ResourceNotFoundException();
    }

    @RequestMapping(value = "{itemId}/status", method = RequestMethod.PUT)
    public Status updateStatus(@PathVariable Long itemId, @RequestBody Status status){
        log.info("ItemId: {}", itemId);
        ToDoItem item = repo.findOne(itemId);
        if(item!=null){
            item.setStatus(status);
            repo.save(item);
            return item.getStatus();
        }
        throw new ResourceNotFoundException();
    }
}
