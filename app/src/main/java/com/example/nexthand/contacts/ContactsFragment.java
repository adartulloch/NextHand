package com.example.nexthand.contacts;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nexthand.R;
import com.example.nexthand.feed.DetailsFragment;
import com.example.nexthand.feed.ItemsAdapter;
import com.example.nexthand.models.Contact;
import com.example.nexthand.models.Item;
import com.example.nexthand.models.User;
import com.example.nexthand.profile.ProfileFragment;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment implements ContactsAdapter.OnClickListener {


    public static final String TAG = "ContactsFragment";
    private static final int QUERY_LIMIT = 20;
    private Context mContext;
    private List<Contact> mContacts;
    private ContactsAdapter mContactsAdapter;
    private RecyclerView mRvContacts;
    private LinearProgressIndicator lpiLoading;

    public ContactsFragment() {}

    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        mContext = getContext();
        mContacts = new ArrayList();
        mContactsAdapter = new ContactsAdapter(mContext, mContacts, this);
        mRvContacts = view.findViewById(R.id.rvContacts);
        mRvContacts.setHasFixedSize(true);
        mRvContacts.setAdapter(mContactsAdapter);
        mRvContacts.setLayoutManager(new GridLayoutManager(mContext,2));
        getContacts();

        return view;
    }

    private void getContacts() {
        ParseQuery<Contact> query = ParseQuery.getQuery(Contact.class);
        query.setLimit(QUERY_LIMIT);
        query.include(Contact.KEY_USER);
        query.include(Contact.KEY_RECIPIENT);
        query.whereEqualTo(Contact.KEY_RECIPIENT, ParseUser.getCurrentUser());
        query.findInBackground((contacts, e) -> {
            if (e == null) {
                mContactsAdapter.clear();
                mContacts.addAll(contacts);
                mContactsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onContactClicked(int position) {
        if (position != RecyclerView.NO_POSITION) {
            openDetails(mContacts.get(position));
        }
    }

    private void openDetails(Contact contact) {
        Fragment details = ContactDetailsFragment.newInstance(contact);
        getParentFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.fragment_container, details)
                .addToBackStack(null)
                .commit();
    }
}