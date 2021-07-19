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
    private TextInputEditText mEtUsername;
    private TextInputEditText mEtPassword;
    private MaterialButton mBtnSignIn;
    private MaterialButton mBtnSignUp;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mContext = getContext();
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
                    .replace(R.id.container, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        });

        mBtnSignIn.setOnClickListener(v -> {
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
                goMainActivity();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(mContext, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
    }
}