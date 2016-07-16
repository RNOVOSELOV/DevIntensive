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
import com.softdesign.devintensive.data.managers.PreferencesManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.List;

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

    boolean isTokenFailed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        Intent intent = getIntent();
        isTokenFailed = intent.getBooleanExtra(ConstantManager.USER_AUTORIZATION_FAILED, false);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTokenFailed) {
            showSnackBar("Произошла ошибка работы с сетью, необходима повторная аутотентификация.");
            isTokenFailed = true;
        }
    }

    @OnClick(R.id.auth_tv_remember)
    public void onRememberPasswordClicked() {
        rememberPassword();
    }

    @OnClick(R.id.auth_login_button)
    public void onSignInClicked() {
        signIn();
    }

    /**
     * Метод вызывает всплывающее сообщение в {@link Snackbar}
     * @param message текст сообщения
     */
    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Метод вызывается, если пользователь нажал на "Забыли пароль?"
     */
    private void rememberPassword() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(intent);
    }

    /**
     * Метод выполняется при удачном выполнении запроса к API
     * @param response ответ сервера
     */
    private void loginSuccess(UserModelRes response) {
        DataManager.getInstance().getPreferenceManager().saveAuthToken(response.getData().getToken());
        DataManager.getInstance().getPreferenceManager().saveUserId(response.getData().getUser().getId());

        saveUserValues(response);
        saveUserProfileValues(response);
        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
        ActivityCompat.finishAfterTransition(this);
    }

    /**
     * Метод выполняет запрос на получение токена и информации о пользователе
     */
    private void signIn() {
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            showProgress();

            Call<UserModelRes> call = DataManager.getInstance().loginUser(new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    hideProgress();
                    if (response.code() == 200) {
                        try {
                            loginSuccess(response.body());
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    } else if (response.code() == 404) {
                        showSnackBar("Неверный логин или пароль");
                    } else {
                        showSnackBar("Видимо что-то случилось");
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    hideProgress();
                    showSnackBar("Ошибка входа, попробуйте позже");
                }
            });
        } else {
            showSnackBar("Сеть недоступна, попробуйте позже");
        }
    }

    /**
     * Метод записи информации о активности пользователя в {@link android.content.SharedPreferences}
     *
     * @param userModelRes POJO обьект модели с данными
     */
    private void saveUserValues(UserModelRes userModelRes) {
        PreferencesManager preferencesManager = DataManager.getInstance().getPreferenceManager();
        int[] userValues = {
                userModelRes.getData().getUser().getProfileValues().getRaiting(),
                userModelRes.getData().getUser().getProfileValues().getLinesCode(),
                userModelRes.getData().getUser().getProfileValues().getProjects()
        };
        preferencesManager.saveUserProfileValues(userValues);

        StringBuilder name = new StringBuilder();
        name.append(userModelRes.getData().getUser().getSecondName())
                .append(" ")
                .append(userModelRes.getData().getUser().getFirstName());
        preferencesManager.saveUserName(name.toString());

        preferencesManager.saveUserPhoto(Uri.parse(userModelRes.getData().getUser().getPublicInfo().getPhoto()));
        preferencesManager.saveUserAvatar(Uri.parse(userModelRes.getData().getUser().getPublicInfo().getAvatar()));
    }

    /**
     * Метод записи информации о профиле пользователя в {@link android.content.SharedPreferences}
     *
     * @param userModelRes POJO обьект модели с данными
     */
    private void saveUserProfileValues(UserModelRes userModelRes) {
        List<String> userData = new ArrayList<>();
        userData.add(userModelRes.getData().getUser().getContacts().getPhone());
        userData.add(userModelRes.getData().getUser().getContacts().getEmail());
        userData.add(userModelRes.getData().getUser().getContacts().getVk());
        userData.add(userModelRes.getData().getUser().getRepositories().getRepo().get(0).getGit());
        userData.add(userModelRes.getData().getUser().getPublicInfo().getBio());
        DataManager.getInstance().getPreferenceManager().saveUserProfileData(userData);
    }
}
