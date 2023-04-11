package com.example.turtomo.HomeScreen.RoomFrag;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.turtomo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Room {

    String id;
    int roomNumber;

    public Room() {
    }

    public Room(String id, int roomNumber) {
        this.id = id;
        this.roomNumber = roomNumber;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void save(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("rooms").child(getId()).setValue(this);
    }

}