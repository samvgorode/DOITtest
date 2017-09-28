package com.example.who.doittest.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.who.doittest.R;
import com.example.who.doittest.interfaces.ISignupView;
import com.example.who.doittest.presenter.SignupActivityPresenter;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity  implements ISignupView {

    private static final String TAG = "SignupActivity";
    private SignupActivityPresenter presenter;

    @BindView(R.id.input_name)
    EditText nameText;
    @BindView(R.id.avatar)
    de.hdodenhof.circleimageview.CircleImageView avatar;
    @BindView(R.id.input_email)
    EditText emailText;
    @BindView(R.id.input_password)
    EditText passwordText;
    @BindView(R.id.btn_signup)
    Button signupButton;

    public static Intent getNewIntent(Context context) {
        Intent intent = new Intent(context, SignupActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        presenter = new SignupActivityPresenter(SignupActivity.this, this);
        setStaregeEnabled();
    }

    @OnClick(R.id.link_login)
    public void GoToLogin(){
        startActivity(LoginActivity.getNewIntent(this));
        finish();
    }

    @OnClick(R.id.avatar)
    void getPhotoFromSD() {
        presenter.getPhotoFromSD();
    }

    @OnClick(R.id.btn_signup)
    public void signup() {
        Log.d(TAG, "Signup");
        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }
        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void logOut() {

    }

    @Override
    public void updateAvatar(String image) {
        if (image.contains("storage") && !image.contains("file")) {
            File file = new File(image);
            Uri fromFile = Uri.fromFile(file);
            Glide.with(this)
                    .load(fromFile)
                    .into(avatar);
        } else {
            Glide.with(this)
                    .load(image)
                    .into(avatar);
        }
    }

    @Override
    public void setLastName(String lastName) {

    }

    @Override
    public void setEmail(String email) {

    }

    @Override
    public void setPhoneNumber(String phoneNumber) {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void setFirstName(String firstName) {

    }

    @Override
    public void setMiddleName(String middleName) {

    }

    @Override
    public void takePhoto(Intent intent, int i) {
        startActivityForResult(intent, i);
    }


    @Override
    public void updateAvatar(Bitmap bitmap) {

    }

    @Override
    public void saveData() {

    }

    @Override
    public void setStaregeEnabled() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }
}
