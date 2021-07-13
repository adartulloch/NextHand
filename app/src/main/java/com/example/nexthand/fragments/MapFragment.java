package com.example.nexthand.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nexthand.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //Initialize Map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        //Load map async
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        // Set the map coordinates to Kyoto Japan.
        LatLng kyoto = new LatLng(35.00116, 135.7681);
        // Set the map type to Hybrid.
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // Add a marker on the map coordinates.
        googleMap.addMarker(new MarkerOptions()
                .position(kyoto)
                .title("Kyoto"));
        // Move the camera to the map coordinates and zoom in closer.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(kyoto));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        // Display traffic.
        googleMap.setTrafficEnabled(true);
    }
}
