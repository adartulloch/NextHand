package com.example.nexthand.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexthand.R;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";
    private static final int QUERY_LIMIT = 20;
    private RecyclerView rvItems;
    //private List<Item> items;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);



        return view;
    }

    /* protected void queryPosts() {
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
     */
}
