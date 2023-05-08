package com.example.turtomo.HomeScreen.Connection;

import androidx.lifecycle.ViewModel;

public class Entity {
    private String name;
    private String entityID;

    public Entity() {
    }

    public Entity(String name, String entityID) {
        this.name = name;
        this.entityID = entityID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }
}
