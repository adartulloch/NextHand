package com.example.nexthand;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.example.nexthand.feed.util.FeedClient;
import com.example.nexthand.feed.util.ItemCache;
import com.example.nexthand.models.Item;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class LocalDatabaseManager {

    public static final String TAG = "LocalDatabaseManager";

    @SuppressLint("MissingPermission")
    public static void writeItemsToCache(Context context) {
        FusedLocationProviderClient locationClient = new FusedLocationProviderClient(context);
        locationClient.getLastLocation().addOnSuccessListener(location -> {
            FeedClient feedClient = new FeedClient(ParseUser.getCurrentUser(), (items, e) -> {
                if (e != null) {
                    Log.e(TAG, "Unable to write to local database");
                } else {
                    ItemCache.getInstance().saveItemsToCache(items);
                }
            });
            feedClient.getFromDatabase(location);
        });

    }

    public static void writeItemsToLocalDatabase() {
        ParseObject.pinAllInBackground(ItemCache.getInstance().loadItemsFromCache());
    }
}
