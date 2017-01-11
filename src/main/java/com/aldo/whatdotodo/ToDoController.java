package com.aldo.whatdotodo;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/items")
public class ToDoController {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(ToDoController.class);

    private final AtomicLong counter = new AtomicLong();
    private final Map<Long, TodoItem> items = new ConcurrentHashMap<>();

    @RequestMapping(method = RequestMethod.GET)
    public List<TodoItem> items() {
        return new LinkedList<>(items.values());
    }

    @RequestMapping(value = "{itemId}",method = RequestMethod.GET)
    public TodoItem item(@PathVariable Long itemId) {
        if(items.containsKey(itemId)){
            return items.get(itemId);
        }
        throw new ResourceNotFoundException();
    }

    @RequestMapping(method = RequestMethod.POST)
    public TodoItem add(@RequestBody TodoItem item){
        item.setId(counter.incrementAndGet());
        items.put(item.getId(), item);
        return item;
    }

    @RequestMapping(value = "{itemId}", method = RequestMethod.DELETE)
    public TodoItem delete(@PathVariable Long itemId){
        if(items.containsKey(itemId)){
            return items.remove(itemId);
        }
        throw new ResourceNotFoundException();
    }

    @RequestMapping(value = "{itemId}", method = RequestMethod.PUT)
    public TodoItem update(@PathVariable Long itemId, @RequestBody TodoItem modifiedItem){
        log.info("ItemId: {}", itemId);

        if(items.containsKey(itemId)){
            TodoItem item = items.get(itemId);

            item.setTitle(modifiedItem.getTitle());
            item.setDescription(modifiedItem.getDescription());
            item.setStatus(modifiedItem.getStatus());

            return item;
        }
        throw new ResourceNotFoundException();
    }

    @RequestMapping(value = "{itemId}/status", method = RequestMethod.PUT)
    public Status updateStatus(@PathVariable Long itemId, @RequestBody Status status){
        log.info("ItemId: {}", itemId);

        if(items.containsKey(itemId)){
            TodoItem item = items.get(itemId);
            item.setStatus(status);

            return item.getStatus();
        }
        throw new ResourceNotFoundException();
    }


    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public final class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException() {
            super();
        }
    }
}
