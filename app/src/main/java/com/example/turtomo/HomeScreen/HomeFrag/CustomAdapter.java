package com.example.turtomo.HomeScreen.HomeFrag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.turtomo.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Block> blocks = new ArrayList<>();

    public CustomAdapter(Context context, ArrayList<Block> blocks){
        mContext = context;
        this.blocks = blocks;
    }

    @Override
    public int getCount() {
        return blocks.size();
    }

    @Override
    public Object getItem(int i) {
        return blocks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.listview_block,viewGroup, false);
        }
        Block tempblock = (Block) getItem(i);

        Button tvBlock = (Button)view.findViewById(R.id.tvBlock);

        tvBlock.setText(tempblock.getBlockName());

        tvBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        return view;
    }
}
