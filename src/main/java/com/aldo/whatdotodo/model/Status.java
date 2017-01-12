package com.aldo.whatdotodo.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "STATUS")
public class Status implements Serializable{
    @Id
    private String status;

    public Status(){}

    public Status(String status){
        this.status = status;
    }
}
