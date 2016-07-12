package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.EditText;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        signIn();
    }

    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void rememberPassword() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(intent);
    }

    private void loginSuccess(UserModelRes response) {
        DataManager.getInstance().getPreferenceManager().saveAuthToken(response.getData().getToken());
        DataManager.getInstance().getPreferenceManager().saveUserId(response.getData().getUser().getId());

        saveUserValues(response);
        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
        ActivityCompat.finishAfterTransition(this);
    }

    private void signIn() {
        if (NetworkStatusChecker.isNetworkAvailable(this)) {

            Call<UserModelRes> call = DataManager.getInstance().loginUser(new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        loginSuccess(response.body());
                    } else if (response.code() == 404) {
                        showSnackBar("Неверный логин или пароль");
                    } else {
                        showSnackBar("Беда");
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    showSnackBar("Ошибка входа, попробуйте позже");
                }
            });
        } else {
            showSnackBar("Сеть недоступна, попробуйте позже");
        }
    }

    private void saveUserValues(UserModelRes userModelRes) {
        int[] userValues = {
                userModelRes.getData().getUser().getProfileValues().getRaiting(),
                userModelRes.getData().getUser().getProfileValues().getLinesCode(),
                userModelRes.getData().getUser().getProfileValues().getProjects()
        };
        DataManager.getInstance().getPreferenceManager().saveUserProfileValues(userValues);
    }
}
