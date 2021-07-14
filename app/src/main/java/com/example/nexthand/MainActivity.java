package com.example.nexthand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.nexthand.compose.ComposeFragment;
import com.example.nexthand.feed.HomeFragment;
import com.example.nexthand.map.MapFragment;
import com.example.nexthand.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.nav_home:
                    selectedFragment = new com.example.nexthand.feed.HomeFragment();
                    break;
                case R.id.nav_map:
                    selectedFragment = new MapFragment();
                    break;
                case R.id.nav_compose:
                    selectedFragment = new ComposeFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };
}