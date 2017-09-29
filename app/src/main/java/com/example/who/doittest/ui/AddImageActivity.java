package com.example.who.doittest.ui;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.who.doittest.R;
import com.example.who.doittest.interfaces.IAddImageView;
import com.example.who.doittest.presenter.AddImageActivityPresenter;
import com.example.who.doittest.utils.PermissionUtils;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pugman.com.simplelocationgetter.SimpleLocationGetter;

public class AddImageActivity extends AppCompatActivity implements IAddImageView, SimpleLocationGetter.OnLocationGetListener {

    @BindView(R.id.etDescription)
    EditText etDescription;
    @BindView(R.id.etHashtag)
    EditText etHashtag;
    @BindView(R.id.ivPlaceholder)
    ImageView ivPlaceholder;

    private AddImageActivityPresenter presenter;
    private Uri imageUri;
    private Location myLocation;

    public static Intent getNewIntent(Context context) {
        Intent intent = new Intent(context, AddImageActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        ButterKnife.bind(this);
        setCustomBar();
        presenter = new AddImageActivityPresenter(AddImageActivity.this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStorageEnabled();
        PermissionUtils.checkLocationPermissions(this);
        SimpleLocationGetter getter = new SimpleLocationGetter(this, this);
        getter.getLastLocation();
    }

    private void setCustomBar() {
        if (getSupportActionBar() != null) {
            ActionBar bar = getSupportActionBar();
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            bar.setDisplayShowCustomEnabled(true);
            bar.setCustomView(R.layout.custom_bar_add_image);
            View view = bar.getCustomView();
            ImageView plus = (ImageView) view.findViewById(R.id.ivAccept);
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addNewImage();
                }
            });
        }
    }

    private void addNewImage() {
        MultipartBody.Part body = null;
        float latitude = 0.0f;
        float longitude = 0.0f;
        if (imageUri != null) {
            File file = new File(imageUri.getPath());
            body = MultipartBody.Part.createFormData("avatar", file.getName().trim(), RequestBody.create(MediaType.parse("image/*"), file));
            if (myLocation != null) {
                latitude = (float) myLocation.getLatitude();
                longitude = (float) myLocation.getLongitude();
            } else {
                try {
                    ExifInterface exifInterface = new ExifInterface(imageUri.getPath());
                    latitude = Float.parseFloat(exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
                    longitude = Float.parseFloat(exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String description = etDescription.getText().toString();
            String hashtag = etHashtag.getText().toString();

        }
    }

    @OnClick(R.id.ivPlaceholder)
    void getImage() {
        presenter.getPhotoFromSD();
    }

    @Override
    public void takePhoto(Intent intent, int i) {
        startActivityForResult(intent, i);
    }

    @Override
    public void setStorageEnabled() {
        PermissionUtils.checkStoragePermissions(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void updatePlaceholder(Uri uri) {
        imageUri = uri;
        Glide.with(this)
                .load(uri)
                .into(ivPlaceholder);
    }

    @Override
    public void onAddImageSuccess() {

    }

    @Override
    public void onAddImageFailure() {

    }

    @Override
    public void onLocationReady(Location location) {
        if (location != null) myLocation = location;
    }

    //SimpleLocationGetter.OnLocationGetListener
    @Override
    public void onError(String s) {

    }
}
