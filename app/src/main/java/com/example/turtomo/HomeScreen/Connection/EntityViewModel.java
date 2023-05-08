package com.example.turtomo.HomeScreen.Connection;

import androidx.lifecycle.ViewModel;

public class EntityViewModel extends ViewModel {
    private Entity entity;

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

}
