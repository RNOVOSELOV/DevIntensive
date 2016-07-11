package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.EditText;

import com.softdesign.devintensive.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends BaseActivity {

    @BindView(R.id.auth_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.auth_et_email)
    EditText mLogin;
    @BindView(R.id.auth_et_password)
    EditText mPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.auth_tv_remember)
    public void onRememberPasswordClicked() {
        rememberPassword();
    }

    @OnClick(R.id.auth_login_button)
    public void onSignInClicked() {
        loginSuccess();
    }

    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void rememberPassword() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(intent);
    }

    private void loginSuccess() {
        showSnackBar("Вошли");
    }
}
