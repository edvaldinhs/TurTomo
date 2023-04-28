package com.example.turtomo.HomeScreen.RoomFrag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.turtomo.HomeScreen.HomeFrag.Block;
import com.example.turtomo.HomeScreen.RoomsFragment;
import com.example.turtomo.R;

import java.util.ArrayList;

import javax.annotation.Nonnull;

public class SearchBlockCustomAdapter extends RecyclerView.Adapter<SearchBlockCustomAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<BlockSearch> blocks = new ArrayList<>();

    public SearchBlockCustomAdapter(Context context, ArrayList<BlockSearch> blocks){
        mContext = context;
        this.blocks = blocks;
    }

    @Nonnull
    @Override
    public ViewHolder onCreateViewHolder(@Nonnull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.listview_searchbyblock, parent, false);
        return new ViewHolder(view, blocks);
    }

    @Override
    public void onBindViewHolder(@Nonnull ViewHolder holder, int position) {
        BlockSearch tempBlock = blocks.get(position);
        holder.bind(tempBlock);
        if (tempBlock.isSelected()) {
            holder.tvSearchBlock.setBackgroundResource(R.drawable.blue_button);
        } else {
            holder.tvSearchBlock.setBackgroundResource(R.drawable.invisible_button);
        }
    }

    @Override
    public int getItemCount() {
        return blocks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Button tvSearchBlock;
        private BlockSearch block;
        private ArrayList<BlockSearch> blocks;


        public ViewHolder(View itemView, ArrayList<BlockSearch> blocks) {
            super(itemView);
            this.blocks = blocks;
            tvSearchBlock = itemView.findViewById(R.id.tvSearchBlock);
            tvSearchBlock.setOnClickListener(this);
        }

        public void bind(BlockSearch block) {
            this.block = block;
            if(!block.getBlockName().isEmpty()){
                tvSearchBlock.setText(block.getBlockName());
            }
            if (block.isSelected()) {
                tvSearchBlock.setBackgroundResource(R.drawable.blue_button);
            } else {
                tvSearchBlock.setBackgroundResource(R.drawable.invisible_button);
            }
        }
        public void colorBind(BlockSearch block){
            this.block = block;
            if (block.isSelected()) {
                tvSearchBlock.setBackgroundResource(R.drawable.blue_button);
            } else {
                tvSearchBlock.setBackgroundResource(R.drawable.invisible_button);
            }
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            switch (view.getId()) {
                case R.id.tvSearchBlock:
                    for (BlockSearch b : blocks) {
                        if (b.getBlockId() != block.getBlockId()) {
                            b.setSelected(false);
                        }
                    }
                    block.setSelected(true);
                    notifyDataSetChanged();
                    tvSearchBlock.setBackgroundResource(R.drawable.blue_button);
                    break;
                default:
                    break;
            }
        }
    }
}

