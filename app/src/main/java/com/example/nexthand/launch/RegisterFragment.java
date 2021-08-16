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
import com.example.nexthand.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    public static final String TAG = "RegisterFragment";
    private Context mContext;
    private TextInputEditText mEtFirstName;
    private TextInputEditText mEtUsername;
    private TextInputEditText mEtPassword;
    private TextInputEditText mEtPhoneNumber;
    private TextInputEditText mEtEmailAddress;
    private MaterialButton mBtnRegister;
    private MaterialButton mBtnCancel;


    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mContext = getContext();
        mEtFirstName = view.findViewById(R.id.etFirstName);
        mEtUsername = view.findViewById(R.id.etUsername);
        mEtPassword = view.findViewById(R.id.etPassword);
        mEtPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        mEtEmailAddress = view.findViewById(R.id.etEmailAddress);
        mBtnRegister = view.findViewById(R.id.btnRegister);
        mBtnCancel = view.findViewById(R.id.btnCancel);
        mBtnRegister = view.findViewById(R.id.btnRegister);

        mBtnCancel.setOnClickListener(v -> getParentFragmentManager()
                .popBackStack());

        mBtnRegister.setOnClickListener(v -> {
            String firstname = mEtFirstName.getText().toString();
            String username = mEtFirstName.getText().toString();
            String password = mEtPassword.getText().toString();
            String phoneNumber = mEtPhoneNumber.getText().toString();
            String emailAddress = mEtEmailAddress.getText().toString();

            createUser(firstname, username, password, phoneNumber, emailAddress);

        });

        return view;
    }

    private void createUser(String firstname, String username, String password, String phoneNumber, String emailAddress) {
        Log.i(TAG,"Registering the user " + username);

        //TODO: More robust input validation
        if (username.isEmpty()) {
            Toast.makeText(mContext, "Your username cannot be empty!", Toast.LENGTH_LONG);
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(mContext, "Your username cannot be empty!", Toast.LENGTH_LONG);
            return;
        }

        ParseUser user = new ParseUser();
        user.put(User.KEY_FIRSTNAME, firstname);
        user.setUsername(username);
        user.setPassword(username);
        user.setEmail(emailAddress);
        user.put(User.KEY_PHONE_NUMBER, phoneNumber);

        user.signUpInBackground(e -> {
            if (e != null) {
                Log.i(TAG, "Issue with login", e);
                return;
            }
            goMainActivity();
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(mContext, MainActivity.class);
        startActivity(i);
        getActivity().finish();
    }
}