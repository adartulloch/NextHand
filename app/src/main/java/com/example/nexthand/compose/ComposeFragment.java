package com.example.nexthand.compose;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.nexthand.MainActivity;
import com.example.nexthand.R;
import com.example.nexthand.models.Item;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import permissions.dispatcher.NeedsPermission;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeFragment extends Fragment {

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public static final String TAG = "ComposeFragment";

    private Context mContext;
    private File mPhotoFile;
    private EditText mEtTitle;
    private EditText mEtDescription;
    private ExtendedFloatingActionButton mFabSubmit;
    private Button mBtnCaptureImage;
    private ImageView mIvPostImage;
    private Switch swDonation;
    public String mPhotoFileName = "photo.jpg";
    private FusedLocationProviderClient mLocationClient;
    private Location mLocation;

    public ComposeFragment() { }

    public static ComposeFragment newInstance() {
        ComposeFragment fragment = new ComposeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        mEtTitle = view.findViewById(R.id.etTitle);
        mFabSubmit = view.findViewById(R.id.fabSubmit);
        mIvPostImage = view.findViewById(R.id.ivPostImage);
        mBtnCaptureImage = view.findViewById(R.id.btnCaptureImage);
        mEtDescription = view.findViewById(R.id.etDescription);
        mLocationClient = new FusedLocationProviderClient(mContext);
        swDonation = view.findViewById(R.id.swDonation);

        mBtnCaptureImage.setOnClickListener(v -> launchCamera());

        mFabSubmit.setOnClickListener(v -> {
            String title = mEtTitle.getText().toString();
            if (title.isEmpty()) {
                Toast.makeText(getContext(), "Description can't be empty", Toast.LENGTH_LONG).show();
            }
            String description = mEtDescription.getText().toString();
            if (description.isEmpty()) {
                Toast.makeText(getContext(), "Description can't be empty", Toast.LENGTH_LONG).show();
            }
            if (mPhotoFile == null || mIvPostImage.getDrawable() == null) {
                Toast.makeText(getContext(), "There is no image!", Toast.LENGTH_SHORT).show();
            }
            Boolean isDonation = swDonation.isChecked();
            ParseUser currentUser = ParseUser.getCurrentUser();
            getMyLocationAndPost(title, description, currentUser, isDonation);
        });
    }

    @SuppressLint("MissingPermission")
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private void getMyLocationAndPost(String title, String description, ParseUser currentUser, Boolean isDonation) {
        mLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        mLocation = location;
                        savePost(title, description, currentUser, mPhotoFile, isDonation, mLocation);
                    } else {
                        Log.i(TAG, "Location is null");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG,"Unable to get the user's location", e);
                });
    }

    private void launchCamera() {
        mPhotoFile = getPhotoFileUri(mPhotoFileName);
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
                Bitmap takenImage = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());
                mIvPostImage.setImageBitmap(takenImage);
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void savePost(String title, String caption, ParseUser currentUser, File photoFile, Boolean isDonation, Location location) {
        Item item = new Item();
        item.setTitle(title);
        item.setCaption(caption);
        item.setAuthor(currentUser);
        item.setImage(new ParseFile(photoFile));
        item.setDonation(isDonation);
        item.setIsAvailable(true);
        item.setLocation(new ParseGeoPoint(location.getLatitude(), location.getLongitude()));
        item.saveEventually(e -> {
            if (e != null) {
                Log.e(TAG, "Error while saving " + e);
                Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_LONG).show();
            }
            Log.i(TAG, "Post save was successful!");
            mEtDescription.setText("");
            mEtTitle.setText("");
            mIvPostImage.setImageResource(0);
            Toast.makeText(mContext, "Your item has been posted!", Toast.LENGTH_SHORT).show();
        });
    }
}