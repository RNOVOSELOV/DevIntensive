package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.model.UserDto;
import com.softdesign.devintensive.ui.adapters.RepositoryAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.StringTokenizer;


public class ProfileUserActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageView mProfileImage;
    private EditText mUserBio;
    private TextView mRaiting;
    private TextView mCodeLines;
    private TextView mProjects;
    private ListView mRepoList;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        mToolbar = ((Toolbar) findViewById(R.id.toolbar));
        mCollapsingToolbar = ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar));
        mRepoList = ((ListView) findViewById(R.id.repositories_list));
        mProjects = ((TextView) findViewById(R.id.main_tv_projects));
        mCodeLines = ((TextView) findViewById(R.id.main_tv_code_lines));
        mRaiting = ((TextView) findViewById(R.id.main_tv_raiting));
        mUserBio = ((EditText) findViewById(R.id.et_about));
        mProfileImage = ((ImageView) findViewById(R.id.user_photo_img));
        mCoordinatorLayout = ((CoordinatorLayout) findViewById(R.id.main_coordinator_container));
        setupToolbar();
        initProfileData();
    }

    public boolean deviceHaveIntentActivity(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activitiesList = packageManager.queryIntentActivities(intent, 0);
        return activitiesList.size() > 0;
    }

    private void initProfileData() {
        UserDto userDto = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);
        final List<String> repo = userDto.getmRepositories();

        final RepositoryAdapter repositoryAdapter = new RepositoryAdapter(this, repo);
        mRepoList.setAdapter(repositoryAdapter);

        mRepoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String url = "https://" + repo.get(i);
                Intent githubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (deviceHaveIntentActivity(githubIntent)) {
                    startActivity(githubIntent);
                } else {
                    Snackbar.make(mCoordinatorLayout, R.string.string_warning_does_not_match_activity, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        int baseListViewHeight = getResources().getDimensionPixelSize(R.dimen.size_big_72);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mRepoList.getLayoutParams();
        lp.height = baseListViewHeight * repo.size();
        mRepoList.setLayoutParams(lp);

        mUserBio.setText(userDto.getmBio());

        mRaiting.setText(userDto.getmRating());
        mCodeLines.setText(userDto.getmCodeLines());
        mProjects.setText(userDto.getmProjects());
        mCollapsingToolbar.setTitle(userDto.getmFullName());

        Picasso.with(this)
                .load(userDto.getmPhoto())
                .placeholder(R.drawable.user_bg)
                .error(R.drawable.user_bg)
                .into(mProfileImage);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
