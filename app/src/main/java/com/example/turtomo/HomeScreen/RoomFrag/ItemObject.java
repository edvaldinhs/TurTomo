package com.example.turtomo.HomeScreen.RoomFrag;

public class ItemObject {

    private String tomoId;
    private String itemName;

    public ItemObject() {
    }

    public ItemObject(String tomoId, String itemName, boolean permanence) {
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
}
