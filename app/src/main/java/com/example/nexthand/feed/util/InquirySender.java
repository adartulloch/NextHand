package com.example.nexthand.feed.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.nexthand.models.Inquiry;
import com.example.nexthand.models.Item;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;

/**
 * Static class to make inquiry requests.
 */

public class InquirySender {

    public static final String TAG = "InquirySender";

     public static void send(Context context, Item item, ParseUser user) {
        sendInquiry(context, item, user);
        updateItem(context, item, user);
    }

    private static void sendInquiry(Context context, Item item, ParseUser user) {
        Inquiry inquiry = new Inquiry();
        inquiry.setItem(item);
        inquiry.setSender(user);
        inquiry.setRecipient(item.getAuthor());
        inquiry.saveInBackground(e -> {
            if (e == null) {
                Toast.makeText(context, "You have successfully placed an inquiry about the item", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "Failed to save", e);
            }
        });
    }

    private static void updateItem(Context context, Item item, ParseUser user) {
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.getInBackground(item.getObjectId(), (object, e) -> {
            if (e == null) {
                JSONArray arr = object.getJSONArray(Item.KEY_USERS_INQUIRED);
                if (arr == null) {
                    arr = new JSONArray(); //Default value is not an empty JSONArray
                }
                ItemCache.getInstance().evictItem(item); // Parse will update the local version, however we still need to manually evict from the cache
                arr.put(user);
                object.put(Item.KEY_USERS_INQUIRED, arr);
                object.saveInBackground();
            } else {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
