package com.aldo.whatdotodo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToOne
    @JsonIgnore
    private Status statusObj;

    public ToDoItem(){}

    public ToDoItem(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public ToDoItem(long id, String title, String description, Status statusObj) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.statusObj = statusObj;
    }

    public ToDoItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public ToDoItem(String title, String description, Status statusObj) {
        this.title = title;
        this.description = description;
        this.statusObj = statusObj;
    }

    public String getStatus(){
        return statusObj!=null?statusObj.getName():null;
    }

    public void setStatus(String status){
        statusObj = new Status(status);
    }
}
