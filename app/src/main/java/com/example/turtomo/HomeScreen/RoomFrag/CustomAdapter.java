package com.example.turtomo.HomeScreen.RoomFrag;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.turtomo.HomeScreen.RoomFrag.ItemAct.MainActivity;
import com.example.turtomo.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Room> rooms = new ArrayList<>();

    public CustomAdapter(Context context, ArrayList<Room> rooms){
        mContext = context;
        this.rooms = rooms;
    }

    @Override
    public int getCount() {
        return rooms.size();
    }

    @Override
    public Object getItem(int i) {
        return rooms.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.listview_room,viewGroup, false);
        }
        Room tempRoom = (Room) getItem(i);

        Button tvRoom = (Button)view.findViewById(R.id.tvRoom);

        tvRoom.setText("Sala "+tempRoom.getRoomNumber());

        tvRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("blockId", tempRoom.getBlockId());
                intent.putExtra("id", tempRoom.getId());
                intent.putExtra("roomNumber", tempRoom.getRoomNumber());
                mContext.startActivity(intent);
            }
        });

        return view;
    }
}
