package com.example.nexthand;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.nexthand.compose.ComposeFragment;
import com.example.nexthand.contacts.ContactsFragment;
import com.example.nexthand.feed.HomeFragment;
import com.example.nexthand.feed.util.ItemCache;
import com.example.nexthand.map.MapFragment;
import com.example.nexthand.profile.ProfileFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseObject;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private FragmentManager mFragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        mBottomNavigationView.setSelectedItemId(R.id.nav_home);
        //Write items from DB to Cache
        LocalDatabaseManager.writeItemsToCache(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalDatabaseManager.writeItemsToLocalDatabase();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.nav_home:
                        selectedFragment = HomeFragment.newInstance();
                        break;
                    case R.id.nav_map:
                        selectedFragment = MapFragment.newInstance();
                        break;
                    case R.id.nav_compose:
                        selectedFragment = ComposeFragment.newInstance();
                        break;
                    case R.id.nav_profile:
                        selectedFragment = ProfileFragment.newInstance();
                        break;
                    case R.id.nav_contacts:
                        selectedFragment = ContactsFragment.newInstance();
                        break;
                }
                mFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            };
}