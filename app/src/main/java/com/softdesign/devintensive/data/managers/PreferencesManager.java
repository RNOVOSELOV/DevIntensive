package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;

import com.softdesign.devintensive.utils.ContentManager;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 28.06.16.
 */
public class PreferencesManager {

    private SharedPreferences mSharedPreferences;
    private static final String[] USER_FIELDS = {ContentManager.USER_PHONE_KEY, ContentManager.USER_EMAIL_KEY, ContentManager.USER_VK_KEY, ContentManager.USER_GITHUB_KEY, ContentManager.USER_ABOUT_KEY};

    public PreferencesManager() {
        mSharedPreferences = DevIntensiveApplication.getsSharedPreferences();
    }

    public void saveUserProfileData(List<String> data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], data.get(i));
        }
        editor.apply();
    }

    public List<String> loadUserProfileData() {
        List<String> userFields = new ArrayList<>();
        userFields.add(mSharedPreferences.getString(ContentManager.USER_PHONE_KEY, ""));
        userFields.add(mSharedPreferences.getString(ContentManager.USER_EMAIL_KEY, ""));
        userFields.add(mSharedPreferences.getString(ContentManager.USER_VK_KEY, ""));
        userFields.add(mSharedPreferences.getString(ContentManager.USER_GITHUB_KEY, ""));
        userFields.add(mSharedPreferences.getString(ContentManager.USER_ABOUT_KEY, ""));
        return userFields;
    }

}
