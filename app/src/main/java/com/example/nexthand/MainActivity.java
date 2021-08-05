package com.example.nexthand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import com.example.nexthand.compose.ComposeFragment;
import com.example.nexthand.contacts.ContactsFragment;
import com.example.nexthand.feed.HomeFragment;
import com.example.nexthand.map.MapFragment;
import com.example.nexthand.profile.ProfileFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.jetbrains.annotations.NotNull;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private BottomNavigationView mBottomNavigationView;
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        MainActivityPermissionsDispatcher.startAppWithPermissionCheck(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalDatabaseManager.writeItemsToLocalDatabase(); //Write cache items to DB
    }

    @SuppressWarnings({"MissingPermission"})
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void startApp() {
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);
        locationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        mCurrentLocation = location;
                        LocalDatabaseManager.writeItemsToCache(this, mCurrentLocation); // Write DB items to the Cache
                        setBottomNavigationView();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Error trying to get last GPS location");
                    e.printStackTrace();
                });
    }

    private void setBottomNavigationView() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        mBottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.nav_home:
                        selectedFragment = HomeFragment.newInstance(mCurrentLocation);
                        break;
                    case R.id.nav_map:
                        selectedFragment = MapFragment.newInstance(mCurrentLocation);
                        break;
                    case R.id.nav_compose:
                        selectedFragment = ComposeFragment.newInstance(mCurrentLocation);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}