package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileUserActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.user_photo_img)
    ImageView mProfileImage;
    @BindView(R.id.et_about)
    EditText mUserBio;
    @BindView(R.id.main_tv_raiting)
    TextView mRaiting;
    @BindView(R.id.main_tv_code_lines)
    TextView mCodeLines;
    @BindView(R.id.main_tv_projects)
    TextView mProjects;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.repositories_list)
    ListView mRepoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        ButterKnife.bind(this);
        setupToolbar();
        initProfileData();
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

        int baseListViewHeight = getResources().getDimensionPixelSize(R.dimen.size_bigger_80);
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
