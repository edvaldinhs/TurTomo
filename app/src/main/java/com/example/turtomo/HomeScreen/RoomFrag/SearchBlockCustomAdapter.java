package com.example.turtomo.HomeScreen.RoomFrag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.turtomo.HomeScreen.HomeFrag.Block;
import com.example.turtomo.R;

import java.util.ArrayList;

import javax.annotation.Nonnull;

public class SearchBlockCustomAdapter extends RecyclerView.Adapter<SearchBlockCustomAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Block> blocks = new ArrayList<>();

    public SearchBlockCustomAdapter(Context context, ArrayList<Block> blocks){
        mContext = context;
        this.blocks = blocks;
    }

    @Nonnull
    @Override
    public ViewHolder onCreateViewHolder(@Nonnull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.listview_searchbyblock, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@Nonnull ViewHolder holder, int position) {
        Block tempBlock = blocks.get(position);
        if(!tempBlock.getBlockName().isEmpty()){
            holder.tvSearchBlock.setText(tempBlock.getBlockName());
        }
    }

    @Override
    public int getItemCount() {
        return blocks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Button tvSearchBlock;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSearchBlock = itemView.findViewById(R.id.tvSearchBlock);
        }
    }
}

