package com.example.turtomo.HomeScreen.HomeFrag;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Block {

    private String blockId;
    private String blockName;
    private int primeiraSala;
    private int ultimaSala;

    public Block() {
    }

    public Block(String blockId, String blockName) {
        this.blockId = blockId;
        this.blockName = blockName;
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

    public int getPrimeiraSala() {
        return primeiraSala;
    }

    public void setPrimeiraSala(int primeiraSala) {
        this.primeiraSala = primeiraSala;
    }

    public int getUltimaSala() {
        return ultimaSala;
    }

    public void setUltimaSala(int ultimaSala) {
        this.ultimaSala = ultimaSala;
    }

    public void save(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("blocks").child(getBlockId()).setValue(this);
    }
}
