package com.example.nexthand.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexthand.R;
import com.example.nexthand.launch.LoginActivity;
import com.example.nexthand.models.Inquiry;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements InquiriesAdapter.OnClickListener {
    public static final String TAG = "ProfileFragment";
    public static final int QUERY_LIMIT = 20;

    private Context mContext;
    private TextView mTvWelcome;
    private RecyclerView mRvInquiries;
    private InquiriesAdapter mInquiriesAdapter;
    private ExtendedFloatingActionButton mFabLogout;
    private List<Inquiry> mInquiries;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = getContext();
        mTvWelcome = view.findViewById(R.id.tvWelcome);
        mFabLogout = view.findViewById(R.id.fabLogout);
        mFabLogout.setOnClickListener(v -> {
            //User will logout of the application
            ParseUser currentUser = ParseUser.getCurrentUser();
            currentUser.logOut();
            Intent i = new Intent(getContext(), LoginActivity.class);
            startActivity(i);
            getActivity().finish();
        });
        mTvWelcome.setText(getString(R.string.welcome, ParseUser.getCurrentUser().getUsername()));
        mInquiries = new ArrayList();
        mRvInquiries = view.findViewById(R.id.rvInquiries);
        mInquiriesAdapter = new InquiriesAdapter(mContext, mInquiries, this);
        mRvInquiries.setAdapter(mInquiriesAdapter);
        mRvInquiries.setLayoutManager(new LinearLayoutManager(mContext));
        getInquiries();
        return view;
    }

    private void getInquiries() {
        ParseQuery<Inquiry> query = ParseQuery.getQuery(Inquiry.class);
        query.setLimit(QUERY_LIMIT);
        query.include(Inquiry.KEY_ITEM);
        query.include(Inquiry.KEY_SENDER);
        query.include(Inquiry.KEY_SENDER);
        query.whereEqualTo(Inquiry.KEY_RECIPIENT, ParseUser.getCurrentUser());
        query.findInBackground((inquiries, e) -> {
            if (e == null) {
                mInquiriesAdapter.clear();
                mInquiries.addAll(inquiries);
                mInquiriesAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onInquiryAccepted(int position) {
        //TODO
    }

    @Override
    public void onInquiryCanceled(int position) {
        //TODO
    }
}
