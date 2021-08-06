package com.example.nexthand.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.nexthand.R;
import com.example.nexthand.models.Item;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.Parse;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MapFragment extends Fragment {

    public static final String TAG = "MapFragment";
    private static final String ARG_LOCATION = "ARG_LOCATION";

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Location mLocation;
    private Context mContext;

    public MapFragment() {}

    public static MapFragment newInstance(Location location) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mLocation = args.getParcelable(ARG_LOCATION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mContext = getContext();
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(map -> {
                loadMap(map);
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            });
        } else {
            Toast.makeText(mContext, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    protected void loadMap(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            sendQuery();
        } else {
            Toast.makeText(mContext, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    void sendQuery() {
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(mLocation.getLatitude(),
                        mLocation.getLongitude()), 15));
        queryPosts();
    }

    private void queryPosts() {
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        ParseUser currentUser = ParseUser.getCurrentUser();
        query.include(Item.KEY_AUTHOR);
        query.whereNotEqualTo(Item.KEY_AUTHOR, currentUser);
        query.whereNotContainedIn(Item.KEY_USERS_INQUIRED, Collections.singleton(currentUser));
        query.findInBackground((items, e) -> {
            for (Item item : items) {
                if (item.getIsAvailable()) {
                    addMarker(item);
                }
            }
        });
    }

    private void addMarker(Item item) {
        if (mMap != null) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(item.getLocation().getLatitude(), item.getLocation().getLongitude()))
                    .title(item.getTitle()));
        }
    }
}
