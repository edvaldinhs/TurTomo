package com.example.turtomo.HomeScreen.RoomFrag;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ItemSpacingDecoration extends RecyclerView.ItemDecoration {

    private int mSpacing;

    public ItemSpacingDecoration(int spacing) {
        mSpacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpacing;
        outRect.right = mSpacing;
    }
}
