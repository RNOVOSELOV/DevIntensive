package com.softdesign.devintensive.ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ContentManager;
import com.softdesign.devintensive.utils.RoundedAvatarDrawable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    public static final String TAG = ContentManager.TAG_PREFIX + MainActivity.class.getSimpleName();
    private boolean mCurrentEditMode = false;

    @BindView(R.id.navigation_drawer)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @BindViews({R.id.et_phone, R.id.et_email, R.id.et_vk, R.id.et_github, R.id.et_about})
    List<EditText> mUserInfoList;

    /**
     * Метод вызывается при старте активити
     * В данном методе инициализируются/производятся:
     * UI пользовательский интерфейс;
     * инициализация статических даных;
     * связь данных со списками (инициализация адаптеров).
     * <p/>
     * Не запускать длительные операции по работе с данными.
     *
     * @param savedInstanceState объект со значениями, сохраненными в {@link Bundle} - состояние UI
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupToolbar();
        setupDrawer();
        loadUserInfoValues();

        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_stat_panel);
        if (savedInstanceState == null) {
            mCurrentEditMode = false;

        } else {
            mCurrentEditMode = savedInstanceState.getBoolean(ContentManager.EDIT_MODE_KEY, false);
            setEditMode(mCurrentEditMode);
            int llPadding = savedInstanceState.getInt(ContentManager.STAT_PANEL_PADDING_KEY, getResources().getDimensionPixelSize(R.dimen.padding_large_24));
            if (ll != null) {
                ll.setPadding(0, llPadding, 0, llPadding);
            }
        }
    }

    /**
     * Метод сохранения пользовательских данных при пересоздании Активити
     *
     * @param outState обьект типа {@link Bundle}, в который сохраняются пользовательские данные
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ContentManager.EDIT_MODE_KEY, mCurrentEditMode);

        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_stat_panel);
        outState.putInt(ContentManager.STAT_PANEL_PADDING_KEY, ll.getPaddingTop());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Метод вызывается при старте активити перед тем, как UI станет доступен пользователю
     * как правило в этом методе происходит регистрация подписки на события, овтановка которых
     * была произведена в onStop()
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Метод вызывается когда активити становится доступной пользователю ля взаимодействия
     * в данном методе происходит запуск анимаций/аудио/видео BrodcastReceiver необходимых
     * для реализации UI-логики/запуск выполнения потоков и т.д.
     * Метод должен быть максимально легковесным для максимальной отзывчивости UI
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Метод вызывается, когда текщая активити теряет фокус, но остается видимойс (всплытие диалогового
     * окна/частичное перекрытие другой активити и т.п.)
     * <p/>
     * В данном методе реализуют сохранение легковесных UI-данных, остановку анимаций/аудио/видео.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveUserInfoValues();
    }

    /**
     * Метод вызывается, когда активити становится невидимой для пользователя.
     * В данно методе необходимо отписываться от событий, останавливать сложные анимации, выполнять сложные операции по
     * сохраненю данных/прерывание запущенных потоков и т.д.
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Метод вызывается при окончании работы активити (когда это происходит системно или при вызове finish())
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Метод вызывается при рестарте активити/возобновлении работы после выхова метода onStop()
     * В данном методе реализуется специфическая бизнес-логика, которая должна быть реализована именно при
     * рестарте активити - например запрос к серверу, которые необходимо выхзывать при выходе из другой активити
     * (обновление данных, подписка на определнное событие проинициализированное на другом экране/
     * специфичная бизнес-логика, завязанная именно на перезапуск активити)
     */
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Метод отображает сообщение в {@link Snackbar}
     *
     * @param message сообщение
     */
    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Метод настраивает туллбар при запуске прложения
     */
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Метод настраивает {@link NavigationView} при запуске приложения
     */
    private void setupDrawer() {
        NavigationView navigationView = ((NavigationView) findViewById(R.id.navigation_view));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.photo);
        RoundedAvatarDrawable roundedAvatarDrawable = new RoundedAvatarDrawable(bitmap);
        ImageView iv = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_avatar);
        if (iv != null) {
            iv.setImageDrawable(roundedAvatarDrawable);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackBar(item.getTitle().toString());
                item.setChecked(true);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    @OnClick(R.id.fab)
    public void onFabClick(FloatingActionButton button) {
        mCurrentEditMode = !mCurrentEditMode;
        setEditMode(mCurrentEditMode);
    }

    /**
     * Метод пеерключает режим редактирования информации о пользователе
     *
     * @param mode если true, то переключает в режим редактирования, если false, в режим просмотра
     */
    private void setEditMode(boolean mode) {
        for (EditText userValue : mUserInfoList) {
            userValue.setEnabled(mode);
            userValue.setFocusable(mode);
            userValue.setFocusableInTouchMode(mode);
        }
        int fabIcon;
        if (mode) {
            fabIcon = R.drawable.ic_done_black_24dp;
        } else {
            fabIcon = R.drawable.ic_create_black_24dp;
            saveUserInfoValues();
        }
        mFab.setImageResource(fabIcon);
    }

    /**
     * Метод чтения информации о пользователе из {@link android.content.SharedPreferences}
     */
    private void loadUserInfoValues() {
        List<String> userData = DataManager.getInstance().getPreferenceManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoList.get(i).setText(userData.get(i));
        }
    }

    /**
     * Метод записи информации о пользователе в {@link android.content.SharedPreferences}
     */
    private void saveUserInfoValues() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfoList) {
            userData.add(userFieldView.getText().toString());
        }
        DataManager.getInstance().getPreferenceManager().saveUserProfileData(userData);
    }
}
