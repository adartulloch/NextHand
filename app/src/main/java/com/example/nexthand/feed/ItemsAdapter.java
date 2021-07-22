package com.example.nexthand.feed;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nexthand.R;
import com.example.nexthand.models.Item;
import com.example.nexthand.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    private OnClickListener mOnClickListener;
    private Context mContext;
    private List<Item> mItems;

    public ItemsAdapter(Context context, List<Item> items, OnClickListener onClickListener) {
        this.mContext = context;
        this.mItems = items;
        this.mOnClickListener = onClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemsAdapter.ViewHolder holder, int position) {
        Item item = mItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void clear() {
        mItems.clear();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPost;
        private TextView tvTitle;

        public ViewHolder(@NotNull View itemView) {
            super(itemView);
            ivPost = itemView.findViewById(R.id.ivPost);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(v -> {
                mOnClickListener.onItemClicked(getAdapterPosition());
            });
        }

        public void bind(Item item) {
            Glide.with(mContext).load(item.getImage().getUrl()).into(ivPost);
            tvTitle.setText(item.getTitle());
        }
    }
}
