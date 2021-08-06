package com.example.nexthand.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.nexthand.R;
import com.example.nexthand.feed.util.ItemCache;
import com.example.nexthand.launch.LoginActivity;
import com.example.nexthand.models.Contact;
import com.example.nexthand.models.Inquiry;
import com.example.nexthand.models.Item;
import com.example.nexthand.models.User;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ProfileFragment extends Fragment implements InquiriesAdapter.OnClickListener {

    public static final String TAG = "ProfileFragment";
    public static final int QUERY_LIMIT = 20;

    private Context mContext;
    private TextView mTvWelcome;
    private ImageView mIvProfileImage;
    private Button btnEditProfilePic;
    private RecyclerView mRvInquiries;
    private InquiriesAdapter mInquiriesAdapter;
    private ExtendedFloatingActionButton mFabLogout;
    private File mProfilePic;
    private String mPhotoFileName = "profile.jpg";
    private final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 24;
    private List<Inquiry> mInquiries;

    public ProfileFragment() {}

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = getContext();
        mTvWelcome = view.findViewById(R.id.tvWelcome);
        mIvProfileImage = view.findViewById(R.id.ivProfileImage);
        setProfilePic();
        btnEditProfilePic = view.findViewById(R.id.btnEditProfilePic);
        btnEditProfilePic.setOnClickListener(v -> {
            editProfilePic();
        });
        mFabLogout = view.findViewById(R.id.fabLogout);
        mFabLogout.setOnClickListener(v -> {
            ParseUser currentUser = ParseUser.getCurrentUser();
            ItemCache.getInstance().clearCache();
            ParseObject.unpinAllInBackground(e -> {
                currentUser.logOut();
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            });
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

    private void setProfilePic() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground((users, e) -> {
            if (e == null) {
                for(ParseUser user1 : users) {
                    insertProfilePic(user1.getParseFile(User.KEY_PROFILEPIC).getUrl());
                }
            } else {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertProfilePic(String url) {
        Glide.with(mContext)
                .load(url)
                .circleCrop()
                .into(mIvProfileImage);
    }

    private void editProfilePic() {
        launchCamera();
    }

    private void launchCamera() {
        mProfilePic = getPhotoFileUri(mPhotoFileName);
        Uri fileProvider = FileProvider.getUriForFile(getContext(),
                "com.nexthand.fileprovider",
                getPhotoFileUri(mPhotoFileName));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(mProfilePic.getAbsolutePath());
                Glide.with(mContext)
                        .load(takenImage)
                        .circleCrop()
                        .into(mIvProfileImage);
                saveProfilePic();
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveProfilePic() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            currentUser.put("profilePic", new ParseFile(mProfilePic));
            currentUser.saveInBackground(e -> {
                if(e==null){
                    Toast.makeText(mContext, "Updated your profile pic!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
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

    /***
     * Deletes an inquiry and its associated item from the DB.
     * Asynchronously deletes an inquiry which triggers another async call to delete its item.
     * @param position
     */
    @Override
    public void onInquiryAccepted(int position) {
            Contact contact = new Contact();
            contact.setUser(mInquiries.get(position).getSender());
            contact.setRecipient(ParseUser.getCurrentUser());
            contact.saveInBackground(e -> {
                if (e == null) {
                    Toast.makeText(mContext,
                             mInquiries.get(position).getSender().getUsername() + " has been saved as a contact!",
                            Toast.LENGTH_SHORT).show();
                    Inquiry removed = mInquiries.get(position);
                    mInquiries.remove(position);
                    mInquiriesAdapter.notifyItemRemoved(position);
                    deleteInquiry(removed, true);
                }
            });
    }

    @Override
    public void onInquiryCanceled(int position) {
        Inquiry removed = mInquiries.get(position);
        mInquiries.remove(position);
        mInquiriesAdapter.notifyItemRemoved(position);
        deleteInquiry(removed, false);
    }

    public void deleteItem(ParseObject object) {
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.getInBackground(object.getObjectId(), (item, e) -> {
            if (e == null) {
                item.deleteInBackground(e2 -> {
                    if(e2 !=null){
                        Toast.makeText(mContext, "Error: "+e2.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                item.unpinInBackground();
            } else {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteInquiry(ParseObject object, Boolean itemIsToBeDeleted) {
        ParseQuery<Inquiry> query = ParseQuery.getQuery(Inquiry.class);
        query.getInBackground(object.getObjectId(), (inquiry, e) -> {
            if (e == null) {
                inquiry.deleteInBackground(e2 -> {
                    if(e2==null){
                       if (itemIsToBeDeleted) deleteItem(inquiry.getItem());
                    }else{
                        Toast.makeText(mContext, "Error: "+e2.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
