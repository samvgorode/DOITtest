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
import android.widget.ProgressBar;
import android.widget.Toast;

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

import static com.example.who.doittest.global.Constants.IMG;
import static com.example.who.doittest.global.Constants.TXT_PLAIN;
import static com.example.who.doittest.utils.LocationUtils.askForGps;

public class AddImageActivity extends AppCompatActivity implements IAddImageView, SimpleLocationGetter.OnLocationGetListener {

    @BindView(R.id.etDescription)
    EditText etDescription;
    @BindView(R.id.etHashtag)
    EditText etHashtag;
    @BindView(R.id.ivPlaceholder)
    ImageView ivPlaceholder;
    @BindView(R.id.progress)
    ProgressBar progress;

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
        askForGps(this);
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
        if (imageUri != null) {
            float latitude = 0.0f;
            float longitude = 0.0f;
            File file = new File(imageUri.getPath());
            final MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName().trim(), RequestBody.create(MediaType.parse(IMG), file));
            String description = etDescription.getText().toString();
            final RequestBody descriptionBody = RequestBody.create(okhttp3.MediaType.parse(TXT_PLAIN), description);
            String hashtag = etHashtag.getText().toString();
            final RequestBody hashtagBody = RequestBody.create(okhttp3.MediaType.parse(TXT_PLAIN), hashtag);
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
            final RequestBody latBody = RequestBody.create(okhttp3.MediaType.parse(TXT_PLAIN), String.valueOf(latitude));
            final RequestBody lonBody = RequestBody.create(okhttp3.MediaType.parse(TXT_PLAIN), String.valueOf(longitude));
            progress.setVisibility(View.VISIBLE);
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            presenter.addNewImage(body, descriptionBody, hashtagBody, latBody, lonBody);
                        }
                    }, 3000);

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
        progress.setVisibility(View.GONE);
        Toast.makeText(this, "Add image success", Toast.LENGTH_LONG).show();
        startActivity(GalleryActivity.getNewIntent(this));
        finish();
    }

    @Override
    public void onAddImageFailure() {
        progress.setVisibility(View.GONE);
        Toast.makeText(this, "Add image failure, try another one", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationReady(Location location) {
        if (location != null) myLocation = location;
    }

    //SimpleLocationGetter.OnLocationGetListener
    @Override
    public void onError(String s) {}
}
