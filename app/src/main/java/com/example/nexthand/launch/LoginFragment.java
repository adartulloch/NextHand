package com.example.nexthand.launch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nexthand.MainActivity;
import com.example.nexthand.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    public static final String TAG = "LoginFragment";
    private Context mContext;
    private LinearProgressIndicator lpiLoading;
    private TextInputEditText mEtUsername;
    private TextInputEditText mEtPassword;
    private MaterialButton mBtnSignIn;
    private MaterialButton mBtnSignUp;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mContext = getContext();
        lpiLoading = view.findViewById(R.id.lpiLoading);
        mEtUsername = view.findViewById(R.id.etUsername);
        mEtPassword = view.findViewById(R.id.etPassword);
        mBtnSignIn = view.findViewById(R.id.btnSignIn);
        mBtnSignUp = view.findViewById(R.id.btnSignUp);

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        mBtnSignUp.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, RegisterFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        });

        mBtnSignIn.setOnClickListener(v -> {
            lpiLoading.setVisibility(View.VISIBLE);
            String username = mEtUsername.getText().toString();
            String password = mEtPassword.getText().toString();
            loginUser(username, password);
        });

        return view;
    }

    private void loginUser(String username, String password) {
        Log.i(TAG,"Attempting to login " + username);

        if (username.isEmpty()) {
            Toast.makeText(mContext, "Your username cannot be empty!", Toast.LENGTH_LONG);
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(mContext, "Your username cannot be empty!", Toast.LENGTH_LONG);
            return;
        }

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.i(TAG, "Issue with login", e);
                    return;
                }
                lpiLoading.setVisibility(View.GONE);
                goMainActivity();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(mContext, MainActivity.class);
        startActivity(i);
        getActivity().finish();
    }
}