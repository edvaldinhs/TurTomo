package com.example.turtomo.HomeScreen.RoomFrag.ItemAct;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.turtomo.R;

import java.util.ArrayList;

public class CustomAdapterItem extends BaseAdapter {

    Context mContext;
    ArrayList<Item> items = new ArrayList<>();

    public CustomAdapterItem(Context context, ArrayList<Item> items){
        mContext = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.listview_item,viewGroup, false);
        }
        Item tempItem = (Item) getItem(i);

        TextView tvItem = (TextView)view.findViewById(R.id.tvItem);

        tvItem.setText(tempItem.getItemName());

        if(tempItem.isCheck()){
            tvItem.setBackgroundResource(R.drawable.round_gray_textbox_check);
        }else {
            tvItem.setBackgroundResource(R.drawable.round_gray_textbox);
        }


        return view;
    }
}
