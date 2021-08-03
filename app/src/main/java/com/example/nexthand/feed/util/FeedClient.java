package com.example.nexthand.feed.util;
import android.location.Location;
import com.example.nexthand.models.Item;
import com.parse.FindCallback;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.Collections;

/**
 * A client class responsible of communicating with the Parse API
 * to create a home feed. Handles in-memory cache via the ItemCache
 * instance. Handles offline loading of items via the Parse localDatabase.
 */
public class FeedClient {
    public static final String TAG = "FeedClient";
    public static final int QUERY_LIMIT = 20;

    private ParseUser mUser;

    public FeedClient(ParseUser user) {
        this.mUser = user;
    }

    public void queryPosts(Location location, FindCallback<Item> callback, boolean isFromLocalDatabase) {
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        if (isFromLocalDatabase) query.fromLocalDatabase();
        query.setLimit(QUERY_LIMIT);
        query.include(Item.KEY_AUTHOR);
        query.whereNotEqualTo(Item.KEY_AUTHOR, mUser);
        query.whereNotContainedIn(Item.KEY_USERS_INQUIRED, Collections.singleton(ParseUser.getCurrentUser()));
        query.whereWithinMiles(Item.KEY_LOCATION,  new ParseGeoPoint(location.getLatitude(), location.getLongitude()),3000);
        query.findInBackground(callback);
    }
    
    public void queryPostsFromDatabase(Location location, FindCallback<Item> callback) {
        queryPosts(location, callback, true);
    }
}
