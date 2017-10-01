package com.example.who.doittest.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.who.doittest.R;
import com.example.who.doittest.interfaces.ILoginView;
import com.example.who.doittest.presenter.LoginActivityPresenter;
import com.example.who.doittest.presenter.SignupActivityPresenter;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.example.who.doittest.global.Constants.TXT_PLAIN;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    @BindView(R.id.input_email)
    EditText emailText;
    @BindView(R.id.input_password)
    EditText passwordText;
    @BindView(R.id.btn_login)
    Button loginButton;
    @BindView(R.id.progress)
    ProgressBar progress;

    private static final String TAG = LoginActivity.class.getSimpleName();
    private LoginActivityPresenter presenter;
    private static final int REQUEST_SIGNUP = 0;

    public static Intent getNewIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        presenter = new LoginActivityPresenter(LoginActivity.this, this);
    }

    @OnClick(R.id.link_signup)
    public void signUp() {
        startActivityForResult(SignupActivity.getNewIntent(this), REQUEST_SIGNUP);
    }


    @OnClick(R.id.btn_login)
    public void login() {
        Log.d(TAG, "Login");
        if (!validate()) {
            onLoginFailed(getString(R.string.not_valid));
            return;
        }
        loginButton.setEnabled(false);
        progress.setVisibility(View.VISIBLE);
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            final RequestBody emailBody = RequestBody.create(okhttp3.MediaType.parse(TXT_PLAIN), email);
            final RequestBody passwordBody = RequestBody.create(okhttp3.MediaType.parse(TXT_PLAIN), password);
            progress.setVisibility(View.VISIBLE);
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            presenter.loginUser(emailBody, passwordBody);
                        }
                    }, 1500);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                onLoginSuccess();
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(SplashActivity.getNewIntent(this, true));
    }

    public boolean validate() {
        boolean valid = true;
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError(getString(R.string.enter_carrect_email));
            valid = false;
        } else {
            emailText.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError(getString(R.string.password_hint));
            valid = false;
        } else {
            passwordText.setError(null);
        }
        return valid;
    }


    @Override
    public void onLoginSuccess() {
        progress.setVisibility(View.GONE);
        Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show();
        loginButton.setEnabled(true);
        startActivity(GalleryActivity.getNewIntent(this));
        finish();
    }

    @Override
    public void onLoginFailed(String why) {
        progress.setVisibility(View.GONE);
        Toast.makeText(getBaseContext(), why, Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }
}
