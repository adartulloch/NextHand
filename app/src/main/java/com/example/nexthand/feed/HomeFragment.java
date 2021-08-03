package com.example.nexthand.feed;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nexthand.R;
import com.example.nexthand.compose.ComposeFragment;
import com.example.nexthand.feed.util.FeedClient;
import com.example.nexthand.feed.util.InquirySender;
import com.example.nexthand.feed.util.ItemCache;
import com.example.nexthand.models.Inquiry;
import com.example.nexthand.models.Item;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements ItemsAdapter.OnClickListener {

    public static final String TAG = "HomeFragment";

    private Context mContext;
    private List<Item> mItems;
    private ItemsAdapter mItemsAdapter;
    private FeedClient mClient;
    private LinearProgressIndicator lpiLoading;
    private RecyclerView mRvItems;
    private FusedLocationProviderClient mLocationClient;
    private Location mLocation;

    public HomeFragment() {}

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        mContext = getContext();
        mItems = ItemCache.getInstance().loadItemsFromCache();
        mLocationClient = new FusedLocationProviderClient(mContext);
        mClient = new FeedClient(ParseUser.getCurrentUser());
        mItemsAdapter = new ItemsAdapter(mContext, mItems, this);
        mRvItems = view.findViewById(R.id.rvItems);
        lpiLoading = view.findViewById(R.id.lpiLoading);
        mRvItems.setAdapter(mItemsAdapter);
        mRvItems.setLayoutManager(new LinearLayoutManager(mContext));

        getItemTouchHelper().attachToRecyclerView(mRvItems);
        sendQuery();

        return view;
    }

    @SuppressLint("MissingPermission")
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private void sendQuery() {
        mLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        mLocation = location;
                        mClient.queryPosts(mLocation, itemCallback);
                    } else {
                        Log.i(TAG, "Loocation is null");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG,"Unable to get the user's location", e);
                });
    }

    private FindCallback<Item> itemCallback = (items, e) -> {
        if (e != null) {
            Log.e(TAG, "Error fetching items!", e);
        } else {
            mItemsAdapter.clear();
            ItemCache.getInstance().saveItemsToCache(items);
            mItems.addAll(items);
            mItemsAdapter.notifyDataSetChanged();
            lpiLoading.setVisibility(View.GONE);
        }
    };

    @Override
    public void onItemClicked(int position) {
        if (position != RecyclerView.NO_POSITION) {
            openDetails(mItems.get(position));
        }
    }

    private void openDetails(Item item) {
        Fragment details = DetailsFragment.newInstance(item, mLocation);
        getParentFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.fragment_container, details)
                .addToBackStack(null)
                .commit();
    }

    private ItemTouchHelper getItemTouchHelper() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Item swipedItem = mItems.get(position);
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        InquirySender.send(mContext, swipedItem, ParseUser.getCurrentUser());
                        mItems.remove(position);
                        mItemsAdapter.notifyItemRemoved(position);
                        break;
                }
            }

            @Override
            public void onChildDraw(@NotNull Canvas c, @NotNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                Drawable icon = ContextCompat.getDrawable(mContext,
                        R.drawable.outline_waving_hand_black_24dp);
                ColorDrawable background = new ColorDrawable(Color.CYAN);

                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20; //so background is behind the rounded corners of itemView

                int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                int iconTop = itemView.getTop() + iconMargin;
                int iconBottom = iconTop + icon.getIntrinsicHeight();

                if (dX < 0) { // Swiping to the left
                    int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                    int iconRight = itemView.getRight() - iconMargin;
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                    background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                            itemView.getTop(), itemView.getRight(), itemView.getBottom());
                } else { // View un-swiped
                    icon.setBounds(0, 0, 0, 0); //otherwise icon will still appear on the screen if user cancels swipe
                    background.setBounds(0, 0, 0, 0);
                }

                background.draw(c);
                icon.draw(c);
            }
        });
    }
}
