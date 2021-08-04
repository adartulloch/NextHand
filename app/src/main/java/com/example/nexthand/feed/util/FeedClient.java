package com.example.nexthand.feed.util;
import android.location.Location;
import android.util.Log;

import com.example.nexthand.models.Item;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.Collections;
import java.util.List;

/**
 * Simple API responsible of communicating with the Parse
 * to create a home feed. Handles in-memory cache via the ItemCache
 * instance. Handles offline loading of items via the Parse localDatabase.
 */

public class FeedClient {

    public interface CallbackHandler {
        void onSuccess(List<Item> items, ParseException e);
    }

    public static final String TAG = "FeedClient";
    public static final int QUERY_LIMIT = 20;

    private ParseUser mUser;
    private CallbackHandler mCallbackHandler;

    public FeedClient(ParseUser user, CallbackHandler callbackHandler) {
        this.mUser = user;
        this.mCallbackHandler = callbackHandler;
    }

    /**
     * Retrieves Items from either local database,
     * ItemCache, or Parse. The order is to first check the cache,
     * then the local DB, and eventually fetch from Parse if
     * necessary. Items become available via {@link #mCallbackHandler}
     * @param location
     */
    public void queryPosts(Location location) {
        List<Item> cachedItems = ItemCache.getInstance().loadItemsFromCache();
        if (!cachedItems.isEmpty()) {
            mCallbackHandler.onSuccess(cachedItems, null);
        } else {
            ParseQuery<Item> localQuery = buildQuery(location);
            localQuery.fromLocalDatastore();
            localQuery.findInBackground((localItems, e1) -> {
                if (e1 != null) {
                    Log.e(TAG, "Error fetching offline items", e1);
                } else {
                    if (!localItems.isEmpty()) {
                        mCallbackHandler.onSuccess(localItems, null);
                    } else {
                        //Get fresh items from server
                        ParseQuery<Item> serverQuery = buildQuery(location);
                        serverQuery.findInBackground((serverItems, e2) -> {
                            if (e2 != null) {
                                Log.e(TAG, "Error fetching server items", e2);
                            } else {
                                cacheItems(serverItems);
                                mCallbackHandler.onSuccess(serverItems, null);
                            }
                        });
                    }
                }
            });
        }
    }

    public void queryPosts(Location location, boolean isForcedFetch) {
        ParseQuery<Item> query = buildQuery(location);
        query.findInBackground((items, e) -> {
            if (e != null) {
                Log.e(TAG, "Error force-fetching from Parse");
            } else {
                cacheItems(items);
                mCallbackHandler.onSuccess(items, null);
            }
        });
    }

    public void getFromDatabase(Location location) {
        ParseQuery<Item> query = buildQuery(location);
        query.fromLocalDatastore();
        query.findInBackground((items, e) -> {
            mCallbackHandler.onSuccess(items, e);
        });
    }

    private ParseQuery<Item> buildQuery(Location location) {
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.setLimit(QUERY_LIMIT);
        query.include(Item.KEY_AUTHOR);
        query.whereNotEqualTo(Item.KEY_AUTHOR, mUser);
        query.whereNotContainedIn(Item.KEY_USERS_INQUIRED, Collections.singleton(ParseUser.getCurrentUser()));
        query.whereWithinMiles(Item.KEY_LOCATION,  new ParseGeoPoint(location.getLatitude(), location.getLongitude()),3000);
        return query;
    }

    private void cacheItems(List<Item> items) {
        ItemCache.getInstance().saveItemsToCache(items);
    }

}
