package com.example.nexthand.feed;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexthand.R;
import com.example.nexthand.models.Item;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;

public class HomeFragment extends Fragment implements ItemsAdapter.onClickListener{

    public static final String TAG = "HomeFragment";
    private static final int QUERY_LIMIT = 20;
    private Context mContext;
    private List<Item> mItems;
    private ItemsAdapter mItemsAdapter;
    private RecyclerView mRvItems;
    private FusedLocationProviderClient mLocationClient;
    private Location mLocation;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getContext();
        mItems = new ArrayList();
        mItemsAdapter = new ItemsAdapter(mContext, mItems, this);
        mRvItems = view.findViewById(R.id.rvItems);
        mRvItems.setAdapter(mItemsAdapter);
        mLocationClient = new FusedLocationProviderClient(mContext);
        mRvItems.setLayoutManager(new LinearLayoutManager(mContext));
        sendQuery();
        return view;
    }

    @SuppressLint("MissingPermission")
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private void sendQuery() {
        mLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        mLocation = location;
                        queryPosts();
                    } else {
                        Log.i(TAG, "Location is null");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG,"Unable to get the user's location", e);
                });
    }

    private void queryPosts() {
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.setLimit(QUERY_LIMIT);
        query.include(Item.KEY_AUTHOR);
        query.whereWithinMiles(Item.KEY_LOCATION,  new ParseGeoPoint(mLocation.getLatitude(), mLocation.getLongitude()),Integer.MAX_VALUE);
        query.findInBackground((items, e) -> {
            if (e != null) {
                Log.e(TAG, "Error fetching items!", e);
            }
            mItemsAdapter.clear();
            mItems.addAll(items);
            mItemsAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onItemClicked(int position) {
        if (position != RecyclerView.NO_POSITION) {
            openDetails(mItems.get(position));
        }
    }

    private void openDetails(Item item) {
        Bundle args = new Bundle();
        args.putSerializable(Item.TAG, item);
        Fragment details = new DetailsFragment();
        details.setArguments(args);
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, details)
                .addToBackStack(null)
                .commit();
    }
}
