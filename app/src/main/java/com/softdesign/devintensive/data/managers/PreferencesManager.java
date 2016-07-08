package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ContentManager;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с {@link SharedPreferences}
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
        userFields.add(mSharedPreferences.getString(ContentManager.USER_PHONE_KEY, DevIntensiveApplication.getAppContext().getString(R.string.default_phone)));
        userFields.add(mSharedPreferences.getString(ContentManager.USER_EMAIL_KEY, DevIntensiveApplication.getAppContext().getString(R.string.default_email)));
        userFields.add(mSharedPreferences.getString(ContentManager.USER_VK_KEY, DevIntensiveApplication.getAppContext().getString(R.string.default_vk)));
        userFields.add(mSharedPreferences.getString(ContentManager.USER_GITHUB_KEY, DevIntensiveApplication.getAppContext().getString(R.string.default_github)));
        userFields.add(mSharedPreferences.getString(ContentManager.USER_ABOUT_KEY, DevIntensiveApplication.getAppContext().getString(R.string.default_about)));
        return userFields;
    }

    public void saveUserPhoto (Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ContentManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }

    public Uri loadUserPhoto () {
        return Uri.parse(mSharedPreferences.getString(ContentManager.USER_PHOTO_KEY, "android.resource://com.softdesign.devintensive/drawable/user_bg"));
    }
}
