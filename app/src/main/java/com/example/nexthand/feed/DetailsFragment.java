package com.example.nexthand.feed;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.nexthand.R;
import com.example.nexthand.models.Item;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.Parse;
import com.parse.ParseGeoPoint;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "DetailsFragment";

    private Item mItem;
    private ImageView mIvProfile;
    private TextView mTvName;
    private TextView mTvDescription;
    private TextView mTvMilesAway;
    private View mVPalette;
    private Location mLocation;
    private FloatingActionButton mFab;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(String param1, String param2) {
        DetailsFragment fragment = new DetailsFragment();
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
        View view =  inflater.inflate(R.layout.fragment_details, container, false);
        Bundle args = getArguments();
        if (args != null) {
            mItem = (Item) args.getSerializable(Item.TAG);
            mLocation = args.getParcelable(Item.KEY_LOCATION);
        }

        mIvProfile = (ImageView) view.findViewById(R.id.ivProfile);
        mTvName = (TextView) view.findViewById(R.id.tvName);
        mTvDescription = (TextView) view.findViewById(R.id.tvDescription);
        mVPalette = view.findViewById(R.id.vPalette);
        mTvMilesAway = view.findViewById(R.id.tvMilesAway);
        mFab = view.findViewById(R.id.fab);

        //async listener for image loading
        CustomTarget<Bitmap> target = new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Glide.with(getActivity()).load(resource).into(mIvProfile);
                Palette palette = Palette.from(resource).generate();
                Palette.Swatch vibrant = palette.getLightVibrantSwatch();
                Log.i("Tag ",  "palette is " + palette);
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
        mTvName.setText(mItem.getTitle());
        mTvDescription.setText(mItem.getCaption());
        mTvMilesAway.setText(mItem.milesAway(new ParseGeoPoint(mLocation.getLatitude(), mLocation.getLongitude())));

        return view;
    }
}