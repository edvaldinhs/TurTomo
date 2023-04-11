package com.example.turtomo.HomeScreen.RoomFrag.ItemAct;

import android.os.Bundle;

import com.example.turtomo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Item {

    private String tomoId;
    private String itemName;

    public Item() {
    }

    public Item(String tomoId, String itemName) {
        this.tomoId = tomoId;
        this.itemName = itemName;
    }

    public String getTomoId() {
        return tomoId;
    }

    public void setTomoId(String tomoId) {
        this.tomoId = tomoId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void save(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("items").child(getTomoId()).setValue(this);
    }
}
