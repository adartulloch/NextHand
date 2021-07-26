package com.example.nexthand.contacts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.nexthand.R;
import com.example.nexthand.models.Contact;
import com.example.nexthand.models.User;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.VH> {
    private Context mContext;
    private List<Contact> mContacts;
    private OnClickListener mOnClickListener;

    public void clear() {
        mContacts.clear();
    }

    public interface OnClickListener {
        void onContactClicked(int position);
    }

    public ContactsAdapter(Context mContext, List<Contact> mContacts, OnClickListener mOnClickListener) {
        this.mContext = mContext;
        this.mContacts = mContacts;
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        Contact contact = mContacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    // Provide a reference to the views for each contact item
    public class VH extends RecyclerView.ViewHolder {
        final View rootView;
        final ImageView ivProfile;
        final TextView tvName;
        final View vPalette;

        public VH(View itemView) {
            super(itemView);
            rootView = itemView;
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvName = itemView.findViewById(R.id.tvName);
            vPalette = itemView.findViewById(R.id.vPalette);

            itemView.setOnClickListener(v -> mOnClickListener.onContactClicked(getAdapterPosition()));
        }

        public void bind(Contact contact) {
            rootView.setTag(contact);
            tvName.setText(contact.getUser().getUsername());
            CustomTarget<Bitmap> target = new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    Glide.with(mContext).load(resource).into(ivProfile);
                    Palette palette = Palette.from(resource).generate();
                    Palette.Swatch vibrant = palette.getVibrantSwatch();
                    if (vibrant != null) {
                        vPalette.setBackgroundColor(vibrant.getRgb());
                        tvName.setTextColor(vibrant.getTitleTextColor());
                    }
                }

                @Override
                public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) { }
            };
            String username = contact.getUser().getUsername();
            Glide.with(mContext).asBitmap().load(contact.getUser().getParseFile(User.KEY_PROFILEPIC).getUrl()).centerCrop().into(target);
        }
    }
}
