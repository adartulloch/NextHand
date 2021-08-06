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
import com.parse.ParseFile;

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

        private ImageView ivProfilePic;
        private TextView tvName;
        private TextView tvBody;
        private Button btnAccept;
        private Button btnCancel;

        public ViewHolder(@NotNull View itemView) {
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvName = itemView.findViewById(R.id.tvName);
            tvBody = itemView.findViewById(R.id.tvBody);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }

        public void bind(Inquiry inquiry) {
            String itemTitle = inquiry.getItem().getString(Item.KEY_TITLE);
            Boolean isDonation = inquiry.getItem().getBoolean(Item.KEY_ISDONATION);
            tvName.setText(inquiry.getSender().getUsername());
            String body = isDonation ? itemView.getResources().getString(R.string.donation_inquiry, itemTitle) :
                    itemView.getResources().getString(R.string.borrow_inquiry, itemTitle);
            tvBody.setText(body);
            Glide.with(mContext)
                    .load(inquiry.getSender().getParseFile(User.KEY_PROFILEPIC).getUrl())
                    .circleCrop()
                    .into(ivProfilePic);
            btnAccept.setOnClickListener(v -> mOnClickListener.onInquiryAccepted(getAdapterPosition()));
            btnCancel.setOnClickListener(v -> mOnClickListener.onInquiryCanceled(getAdapterPosition()));
        }
    }
}
