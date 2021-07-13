package com.example.nexthand.fragments;

import android.content.ClipData;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nexthand.MainActivity;
import com.example.nexthand.R;
import com.example.nexthand.models.ItemToy;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.getSystemService;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";
    private static final int QUERY_LIMIT = 20;
    private List<Item> items;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    protected void queryPosts() {
        // TODO: Ensure that permissions have been approved for LOCATION_COARSE

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.include(Post.KEY_USER);
        query.setLimit(QUERY_LIMIT);
        withinMiles("location", new GeoPoint({latitude: location.getLatitude(), longitude: location.getLongitude()}) , 100, true);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                //Ensure there are no errors
                if (e != null) {
                    Log.e(TAG, "Error fetching posts", e);
                }

                //TODO: Save the Items to the list and notify the adapter of new data
            }
        });
    }
}
