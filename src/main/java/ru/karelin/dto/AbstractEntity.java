package ru.karelin.dto;

import java.util.UUID;



public abstract class AbstractEntity {

    private String id = UUID.randomUUID().toString();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
