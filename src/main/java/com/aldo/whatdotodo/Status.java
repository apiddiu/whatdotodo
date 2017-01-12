package com.aldo.whatdotodo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Status {
    @Id
    private String status;
}
