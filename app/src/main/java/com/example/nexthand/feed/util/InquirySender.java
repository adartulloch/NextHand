package com.example.nexthand.feed.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.nexthand.models.Inquiry;
import com.example.nexthand.models.Item;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

public class InquirySender {

    public static final String TAG = "InquirySender";
    private Item mItem;
    private Context mContext;

    public InquirySender(Item item, Context context) {
        this.mItem = item;
        this.mContext = context;
    }

    public void send() {
        sendInquiry();
        updateItem();
    }

    public void setItem(Item item) {
        this.mItem = item;
    }

    private void sendInquiry() {
        Inquiry inquiry = new Inquiry();
        inquiry.setItem(mItem);
        inquiry.setSender(ParseUser.getCurrentUser());
        inquiry.setRecipient(mItem.getAuthor());
        inquiry.saveInBackground(e -> {
            if (e == null) {
                Toast.makeText(mContext, "You have successfully placed an inquiry about the item", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "Failed to save", e);
            }
        });
    }

    private void updateItem() {
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.getInBackground(mItem.getObjectId(), (object, e) -> {
            if (e == null) {
                JSONArray arr = object.getJSONArray(Item.KEY_USERS_INQUIRED);
                if (arr == null) {
                    arr = new JSONArray(); //Default value is not an empty JSONArray
                }
                arr.put(ParseUser.getCurrentUser());
                object.put(Item.KEY_USERS_INQUIRED, arr);
                object.saveInBackground();
            } else {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
