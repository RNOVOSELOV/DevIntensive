package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.widget.CheckBox;
import android.widget.EditText;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.managers.PreferencesManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.model.Repository;
import com.softdesign.devintensive.data.storage.model.RepositoryDao;
import com.softdesign.devintensive.data.storage.model.User;
import com.softdesign.devintensive.data.storage.model.UserDao;
import com.softdesign.devintensive.utils.AppConfig;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.NetworkHelper;

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
    @BindView(R.id.auth_chb_save_login)
    CheckBox saveLoginCheckBox;

    private DataManager mDataManager;
    private RepositoryDao mRepositoryDao;
    private UserDao mUserDao;

    boolean isTokenFailed;  // FALSE, если в процессе работы токен аутотентификации стал не валидным
    // пользователя выкидывает на активити авторизации, появляется соответствующее сообщение

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        mDataManager = DataManager.getInstance();
        mUserDao = mDataManager.getDaoSession().getUserDao();
        mRepositoryDao = mDataManager.getDaoSession().getRepositoryDao();

        Intent intent = getIntent();
        isTokenFailed = intent.getBooleanExtra(ConstantManager.USER_AUTORIZATION_FAILED, false);

        PreferencesManager preferencesManager = mDataManager.getPreferenceManager();
        boolean isLoginSaved = preferencesManager.isLoginSaved();
        saveLoginCheckBox.setChecked(isLoginSaved);
        if (isLoginSaved) {
            mLogin.setText(preferencesManager.getLogin());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTokenFailed) {
            showSnackBar("Произошла ошибка работы с сетью, необходима повторная аутотентификация.");
            isTokenFailed = true;
        }
    }

    /**
     * Метод обрабатывает клик по {@link android.widget.TextView} "Забыли пароль?"
     */
    @OnClick(R.id.auth_tv_remember)
    public void onRememberPasswordClicked() {
        rememberPassword();
    }

    /**
     * Метод обрабатывает клик по кнопке "Войти"
     */
    @OnClick(R.id.auth_login_button)
    public void onSignInClicked() {
        signIn();
    }

    /**
     * Метод вызывает всплывающее сообщение в {@link Snackbar}
     *
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
     *
     * @param response ответ сервера
     */
    private void loginSuccess(UserModelRes response) {
        PreferencesManager preferencesManager = mDataManager.getPreferenceManager();
        preferencesManager.saveAuthToken(response.getData().getToken());
        preferencesManager.saveUserId(response.getData().getUser().getId());

        if (saveLoginCheckBox.isChecked()) {
            preferencesManager.setLoginSavedFlag(true);
            preferencesManager.saveLogin(mLogin.getText().toString());
        } else {
            preferencesManager.setLoginSavedFlag(false);
            preferencesManager.saveLogin();                     // Затираем логин в SP, если флаг сохранения не стоит
        }

        saveUserValues(response);
        saveUserProfileValues(response);
        saveUsersInDb();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AuthActivity.this.hideProgress();
                Intent loginIntent = new Intent(AuthActivity.this, MainActivity.class);
                startActivity(loginIntent);
                ActivityCompat.finishAfterTransition(AuthActivity.this);
            }
        }, AppConfig.START_DELAY);
    }

    /**
     * Метод выполняет запрос к REST API на получение токена и информации о пользователе
     */
    private void signIn() {
        if (NetworkHelper.isNetworkAvailable(this)) {
            showProgress();

            Call<UserModelRes> call = DataManager.getInstance().loginUser(new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        try {
                            loginSuccess(response.body());
                        } catch (NullPointerException e) {
                            hideProgress();
                            e.printStackTrace();
                        }
                    } else if (response.code() == 404) {
                        hideProgress();
                        showSnackBar("Неверный логин или пароль");
                    } else {
                        hideProgress();
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

    private void saveUsersInDb() {
        Call<UserListRes> call = DataManager.getInstance().getUsersListFromNetwork();
        call.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                try {
                    if (response.code() == 200) {
                        List<Repository> allRepos = new ArrayList<Repository>();
                        List<User> allUsers = new ArrayList<User>();

                        for (UserListRes.UserData userRes : response.body().getData()) {
                            allRepos.addAll(getRepositoriesListFromUserRes(userRes));
                            allUsers.add(new User(userRes));
                        }

                        mRepositoryDao.insertOrReplaceInTx(allRepos);
                        mUserDao.insertOrReplaceInTx(allUsers);

                    } else {
                        showSnackBar("Список пользователей не может быть получен.");
                    }
                } catch (NullPointerException e) {
                    showSnackBar("Непорядок!");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                showSnackBar("Ошибочка вышла!");
            }
        });
    }

    private List<Repository> getRepositoriesListFromUserRes(UserListRes.UserData userData) {
        final String userId = userData.getId();

        List<Repository> repositories = new ArrayList<>();
        for (UserListRes.Repo repo : userData.getRepositories().getRepo()) {
            repositories.add(new Repository(repo, userId));
        }

        return repositories;
    }
}
