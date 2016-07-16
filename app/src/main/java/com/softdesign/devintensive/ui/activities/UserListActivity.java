package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.managers.PreferencesManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.storage.model.UserDto;
import com.softdesign.devintensive.ui.adapters.UserAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends BaseActivity {

    private static String TAG = ConstantManager.TAG_PREFIX + " UserListActivity";

    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.user_list_navigation_drawer)
    DrawerLayout mNavigationDrawer;

    @BindView(R.id.user_list_recycler_view)
    RecyclerView mRecyclerView;

    private DataManager mDataManager;
    private UserAdapter mUserAdapter;
    private List<UserListRes.UserData> mUsers;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        ButterKnife.bind(this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);

        mDataManager = DataManager.getInstance();
        setupToolbar();
        setupDrawer();
        loadUsers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void loadUsers() {
        Call<UserListRes> call = mDataManager.getUsersList();
        showProgress();
        call.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                hideProgress();
                if (response.code() == 200) {
                    try {
                        mUsers = response.body().getData();
                        mUserAdapter = new UserAdapter(UserListActivity.this, mUsers, new UserAdapter.UserViewHolder.CustomClickListener() {
                            @Override
                            public void onUserItemClickListener(int position) {
                                UserDto user = new UserDto(mUsers.get(position));
                                Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                                profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, user);
                                startActivity(profileIntent);
                            }
                        });
                        mRecyclerView.setAdapter(mUserAdapter);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {
                    Intent intent = new Intent(UserListActivity.this, AuthActivity.class);
                    intent.putExtra(ConstantManager.USER_AUTORIZATION_FAILED, true);
                    startActivity(intent);
                    ActivityCompat.finishAfterTransition(UserListActivity.this);
                } else {
                    showSnackBar("Видимо что-то случилось");
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                showSnackBar("Ошибка загрузки, попробуйте позже");
                hideProgress();
            }
        });
    }

    private void setupDrawer() {
        PreferencesManager preferencesManager = DataManager.getInstance().getPreferenceManager();
        NavigationView navigationView = ((NavigationView) findViewById(R.id.navigation_view));
        ImageView iv = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_avatar);
        Picasso.with(this)
                .load(preferencesManager.getUserAvatar())
                .transform(new RoundedTransformation())
                .into(iv);

        TextView name = ((TextView) navigationView.getHeaderView(0).findViewById(R.id.user_name_txt));
        name.setText(preferencesManager.getUserName());
        TextView email = ((TextView) navigationView.getHeaderView(0).findViewById(R.id.user_e_mail_txt));
        String e_mail = preferencesManager.loadUserProfileData().get(1);
        email.setText(e_mail);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.user_profile_menu:
                        Intent intentProfile = new Intent(UserListActivity.this, MainActivity.class);
                        startActivity(intentProfile);
                        ActivityCompat.finishAfterTransition(UserListActivity.this);
                        break;
                    case R.id.team_menu:
                        Intent intentTeam = new Intent(UserListActivity.this, UserListActivity.class);
                        startActivity(intentTeam);
                        ActivityCompat.finishAfterTransition(UserListActivity.this);
                        break;
                }
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
