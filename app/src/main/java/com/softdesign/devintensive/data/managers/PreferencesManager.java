package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с {@link SharedPreferences}
 */
public class PreferencesManager {

    private SharedPreferences mSharedPreferences;
    private static final String[] USER_FIELDS = {
            ConstantManager.USER_PHONE_KEY,
            ConstantManager.USER_EMAIL_KEY,
            ConstantManager.USER_VK_KEY,
            ConstantManager.USER_GITHUB_KEY,
            ConstantManager.USER_ABOUT_KEY};

    private static final String[] USER_VALUES = {
            ConstantManager.USER_RAITING_VALUE,
            ConstantManager.USER_CODE_LINES_COUNT,
            ConstantManager.USER_PROJECT_VALUES};

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
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY, DataManager.getInstance().getContext().getString(R.string.default_phone)));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_EMAIL_KEY, DataManager.getInstance().getContext().getString(R.string.default_email)));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY, DataManager.getInstance().getContext().getString(R.string.default_vk)));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_GITHUB_KEY, DataManager.getInstance().getContext().getString(R.string.default_github)));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_ABOUT_KEY, DataManager.getInstance().getContext().getString(R.string.default_about)));
        return userFields;
    }

    public void saveAuthToken(String authToken) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOKEN, authToken);
        editor.apply();
    }

    public String getAuthToken() {
        return mSharedPreferences.getString(ConstantManager.AUTH_TOKEN, "null");
    }

    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY, userId);
        editor.apply();
    }

    public String getUserId() {
        return mSharedPreferences.getString(ConstantManager.USER_ID_KEY, "null");
    }

    public void saveUserProfileValues(int[] userValues) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_VALUES.length; i++) {
            editor.putString(USER_VALUES[i], String.valueOf(userValues[i]));
        }
        editor.apply();
    }

    public List<String> loadUserProfileValues () {
        List<String> userFields = new ArrayList<>();
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_RAITING_VALUE, "0"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_CODE_LINES_COUNT, "0"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PROJECT_VALUES, "0"));
        return userFields;
    }

    public void saveUserName (String name) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_FULL_NAME, name);
        editor.apply();
    }

    public String getUserName() {
        return mSharedPreferences.getString(ConstantManager.USER_FULL_NAME, "null");
    }

    public void saveUserPhoto(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }

    public Uri loadUserPhoto() {
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY, "android.resource://com.softdesign.devintensive/drawable/user_bg"));
    }

    public void saveUserAvatar(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_AVATAR_KEY, uri.toString());
        editor.apply();
    }

    public Uri getUserAvatar() {
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_AVATAR_KEY, "android.resource://com.softdesign.devintensive/drawable/photo"));
    }

    public void savePhotoLastChangedTime(String authToken) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_DOWNLOAD_DATE_KEY, authToken);
        editor.apply();
    }

    public String getPhotoLastChangedTime() {
        return mSharedPreferences.getString(ConstantManager.USER_PHOTO_DOWNLOAD_DATE_KEY, "null");
    }
}
