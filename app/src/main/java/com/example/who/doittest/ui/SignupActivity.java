package com.example.who.doittest.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.who.doittest.R;
import com.example.who.doittest.interfaces.ISignupView;
import com.example.who.doittest.presenter.SignupActivityPresenter;
import com.example.who.doittest.utils.PermissionUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.example.who.doittest.global.Constants.IMG;
import static com.example.who.doittest.global.Constants.TXT_PLAIN;

public class SignupActivity extends AppCompatActivity implements ISignupView {

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
    @BindView(R.id.progress)
    ProgressBar progress;

    private static final String TAG = SignupActivity.class.getSimpleName();
    private SignupActivityPresenter presenter;
    private Uri imageUri = null;

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
        setStorageEnabled();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStorageEnabled();
    }

    @OnClick(R.id.link_login)
    public void GoToLogin() {
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
        progress.setVisibility(View.VISIBLE);
        final String name = nameText.getText().toString();
        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && imageUri != null) {
            final RequestBody nameBody = RequestBody.create(okhttp3.MediaType.parse(TXT_PLAIN), name);
            final RequestBody emailBody = RequestBody.create(okhttp3.MediaType.parse(TXT_PLAIN), email);
            final RequestBody passwordBody = RequestBody.create(okhttp3.MediaType.parse(TXT_PLAIN), password);
            File file = new File(imageUri.getPath());
            final MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName().trim(), RequestBody.create(MediaType.parse(IMG), file));
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            presenter.registerUser(nameBody, emailBody, passwordBody, body);
                            progress.setVisibility(View.GONE);
                        }
                    }, 3000);
        }
    }

    @Override
    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        Toast.makeText(this, "Login success", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK, null);
        finish();
    }

    @Override
    public void onSignupFailed() {
        Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
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
    public void updateAvatar(Uri image) {
        imageUri = image;
        Glide.with(this)
                .load(image)
                .into(avatar);

    }

    @Override
    public void takePhoto(Intent intent, int i) {
        startActivityForResult(intent, i);
    }

    @Override
    public void setStorageEnabled() {
        PermissionUtils.checkStoragePermissions(this);
    }
}
