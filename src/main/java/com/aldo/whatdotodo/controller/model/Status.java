package com.aldo.whatdotodo.controller.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "STATUS")
//@JsonSerialize(using = StatusSerializer.class)
//@JsonDeserialize(using = StatusDeserializer.class)
public class Status implements Serializable{
    @Id
    public String name;

    public Status(){}

    public Status(String status){
        this.name = status;
    }
}
