package com.example.nexthand.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nexthand.R;
import com.example.nexthand.feed.ItemsAdapter;
import com.example.nexthand.models.Inquiry;
import com.example.nexthand.models.Item;
import com.example.nexthand.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InquiriesAdapter extends RecyclerView.Adapter<InquiriesAdapter.ViewHolder>{

    public interface OnClickListener {
        void onInquiryAccepted(int position);
        void onInquiryCanceled(int position);
    }

    private OnClickListener mOnClickListener;
    private Context mContext;
    private List<Inquiry> mInquiries;

    public InquiriesAdapter(Context context, List<Inquiry> inquiries, OnClickListener onClickListener) {
        this.mContext = context;
        this.mInquiries = inquiries;
        this.mOnClickListener = onClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public InquiriesAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.inquiry_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull InquiriesAdapter.ViewHolder holder, int position) {
        Inquiry inquiry = mInquiries.get(position);
        holder.bind(inquiry);
    }

    @Override
    public int getItemCount() {
        return mInquiries.size();
    }

    public void clear() {
        mInquiries.clear();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPost;
        private TextView tvName;
        private Button btnAccept;
        private Button btnCancel;

        public ViewHolder(@NotNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }

        public void bind(Inquiry inquiry) {
            tvName.setText(inquiry.getSender().getUsername());
        }
    }
}
