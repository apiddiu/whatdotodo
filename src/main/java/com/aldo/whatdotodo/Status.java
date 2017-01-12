package com.aldo.whatdotodo;

public enum Status {


    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    CLOSED("Closed");

    private String name;

    Status(String name){
        this.name = name;
    }
}
