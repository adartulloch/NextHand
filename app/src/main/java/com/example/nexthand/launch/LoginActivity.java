package com.example.nexthand.launch;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nexthand.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, LoginFragment.newInstance())
                    .commit();
        }
    }
}