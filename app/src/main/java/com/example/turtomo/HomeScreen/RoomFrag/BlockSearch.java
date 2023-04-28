package com.example.turtomo.HomeScreen.RoomFrag;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BlockSearch {

    private String blockId;
    private String blockName;
    private boolean isSelected;

    public BlockSearch() {
    }

    public BlockSearch(String blockId, String blockName, boolean isSelected) {
        this.blockId = blockId;
        this.blockName = blockName;
        this.isSelected = isSelected;
    }


    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void save(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("blocks").child(getBlockId()).setValue(this);
    }
}
