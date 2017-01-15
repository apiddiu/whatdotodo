package com.aldo.whatdotodo.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private final String username;

    public User(String username) {
        this.username = username;
    }
}
