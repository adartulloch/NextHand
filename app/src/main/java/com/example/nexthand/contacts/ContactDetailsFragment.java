package com.example.nexthand.contacts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.nexthand.R;
import com.example.nexthand.models.Contact;
import com.example.nexthand.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactDetailsFragment extends Fragment {

    private Context mContext;
    private Contact mContact;
    private ImageView mIvProfile;
    private TextView mTvName;
    private TextView mTvPhone;
    private View mVPalette;
    private FloatingActionButton mFab;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContactDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactDetailsFragment newInstance(String param1, String param2) {
        ContactDetailsFragment fragment = new ContactDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_details, container, false);
        mContext = getContext();
        Bundle args = getArguments();
        if (args != null) { mContact = (Contact) args.getSerializable(Contact.TAG); }
        mIvProfile = view.findViewById(R.id.ivProfile);
        mTvName = view.findViewById(R.id.tvName);
        mTvPhone = view.findViewById(R.id.tvPhone);
        mVPalette = view.findViewById(R.id.vPalette);
        mFab = view.findViewById(R.id.fab);
        Glide.with(mContext).asBitmap().load(mContact.getUser().getParseFile(User.KEY_PROFILEPIC).getUrl()).centerCrop().into(getBitmap());

        mFab.setOnClickListener(v -> {
            String uri = "tel:" + mContact.getUser().getString(User.KEY_PHONE_NUMBER);
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        });

        mTvName.setText(mContact.getUser().getString(User.KEY_FIRSTNAME));
        return view;
    }

    private CustomTarget<Bitmap> getBitmap() {
        return new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Glide.with(mContext).load(resource).into(mIvProfile);
                Palette palette = Palette.from(resource).generate();
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                if (vibrant != null) {
                    mVPalette.setBackgroundColor(vibrant.getRgb());
                    mTvName.setTextColor(vibrant.getTitleTextColor());
                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) { }
        };
    }
}