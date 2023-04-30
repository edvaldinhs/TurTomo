package com.example.turtomo.HomeScreen.HomeFrag;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

import com.example.turtomo.HomeScreen.RoomsFragment;
import com.example.turtomo.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Block> blocks = new ArrayList<>();
    private NavController navController;

    public CustomAdapter(Context context, ArrayList<Block> blocks, NavController navController){
        mContext = context;
        this.blocks = blocks;
        this.navController = navController;
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
        TextView txtBlock = (TextView)view.findViewById(R.id.txtSize);

        String blockId = tempblock.getBlockId();
        tvBlock.setText(tempblock.getBlockName());

        int firstRoom = tempblock.getPrimeiraSala();
        int lastRoom = tempblock.getUltimaSala();
        txtBlock.setText(firstRoom+" - "+lastRoom);


        tvBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("blockId", blockId);
                Log.d("Somethingaaa",blockId);

                RoomsFragment fragment = new RoomsFragment();
                fragment.setArguments(bundle);

                if (navController != null) {
                    navController.navigate(R.id.action_home_to_rooms, bundle);
                } else {
                    Log.e("CustomAdapter", "NavController is null");
                }
            }
        });

        return view;
    }
}
