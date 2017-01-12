package com.aldo.whatdotodo;

import lombok.Data;

import javax.persistence.*;

import java.io.Serializable;

@Data
@Entity
@Table(name = "TODOITEM")

public class ToDoItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String title;
    @Column
    private String description;

    @Column
    private Status status;

    public ToDoItem(){}

    public ToDoItem(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public ToDoItem(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
