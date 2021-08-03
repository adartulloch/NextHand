package com.example.nexthand.feed;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.nexthand.R;
import com.example.nexthand.feed.util.InquirySender;
import com.example.nexthand.models.Inquiry;
import com.example.nexthand.models.Item;
import com.example.nexthand.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    private static final String ARG_ITEM = "Item";
    private static final String ARG_LOCATION = "Location";

    private Item mItem;
    private Context mContext;
    private ImageView mIvItem;
    private TextView mTvName;
    private TextView mTvDescription;
    private TextView mTvMilesAway;
    private ImageView mIvProfileImage;
    private View mVPalette;
    private Location mLocation;
    private ExtendedFloatingActionButton mFab;

    public DetailsFragment() {}

    public static DetailsFragment newInstance(Item item, Location location) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, item);
        args.putParcelable(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (getArguments() != null) {
            mItem = (Item) args.getSerializable(ARG_ITEM);
            mLocation = args.getParcelable(ARG_LOCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_details, container, false);

        mContext = getContext();
        mIvItem = view.findViewById(R.id.ivItem);
        mTvName = view.findViewById(R.id.tvName);
        mTvDescription = view.findViewById(R.id.tvDescription);
        mVPalette = view.findViewById(R.id.vPalette);
        mTvMilesAway = view.findViewById(R.id.tvMilesAway);
        mIvProfileImage = view.findViewById(R.id.ivProfileImage);
        mFab = view.findViewById(R.id.fabInquiry);

        CustomTarget<Bitmap> target = new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Glide.with(getActivity()).load(resource).into(mIvItem);
                Palette palette = Palette.from(resource).generate();
                Palette.Swatch vibrant = palette.getLightMutedSwatch();
                if (vibrant != null) {
                    mVPalette.setBackgroundColor(vibrant.getRgb());
                    mTvName.setTextColor(vibrant.getTitleTextColor());
                    mTvMilesAway.setTextColor(vibrant.getTitleTextColor());
                }
            }
            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {}
        };
        Glide.with(this).asBitmap().load(mItem.getImage().getUrl()).centerCrop().into(target);
        Glide.with(mContext).load(mItem.getAuthor().getParseFile(User.KEY_PROFILEPIC).getUrl()).circleCrop().into(mIvProfileImage);
        mTvName.setText(mItem.getTitle());
        mTvDescription.setText(mItem.getCaption());
        mTvMilesAway.setText(mItem.milesAway(new ParseGeoPoint(mLocation.getLatitude(), mLocation.getLongitude())));
        if (mItem.getAuthor().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) { mFab.setVisibility(View.GONE); }
        mFab.setText(getString(R.string.inquire_user, mItem.getAuthor().getUsername()));
        mFab.setOnClickListener(v -> { InquirySender.send(mContext, mItem, ParseUser.getCurrentUser()); });
        return view;
    }
}