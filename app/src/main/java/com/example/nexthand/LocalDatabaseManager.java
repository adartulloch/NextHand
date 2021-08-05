package com.example.nexthand;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import com.example.nexthand.feed.util.FeedClient;
import com.example.nexthand.feed.util.ItemCache;
import com.example.nexthand.models.Item;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;


public class LocalDatabaseManager {

    public static final String TAG = "LocalDatabaseManager";

    @SuppressLint("MissingPermission")
    public static void writeItemsToCache(Context context, Location queryLocation) {
        FeedClient feedClient = new FeedClient(ParseUser.getCurrentUser(), new FeedClient.CallbackHandler() {
                @Override
                public void onSuccess(List<Item> items, ParseException e) {
                    ItemCache.getInstance().saveItemsToCache(items);
                }

                @Override
                public void onFailure(List<Item> items, ParseException e) {
                    Log.e(TAG, "Error writing to local cache", e);
                }
            });

        feedClient.getFromDatabase(queryLocation);
    }

    public static void writeItemsToLocalDatabase() {
        ParseObject.pinAllInBackground(ItemCache.getInstance().loadItemsFromCache());
    }
}
