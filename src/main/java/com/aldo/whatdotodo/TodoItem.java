package com.aldo.whatdotodo;

import lombok.Data;

@Data
public class TodoItem {

    private Long id;
    private String title;
    private String description;
    private Status status = Status.OPEN;

    public TodoItem(){}

    public TodoItem(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public TodoItem(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
