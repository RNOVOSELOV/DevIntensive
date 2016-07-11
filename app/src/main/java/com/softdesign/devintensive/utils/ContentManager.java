package com.softdesign.devintensive.utils;

/**
 * Created by Роман on 23.06.2016.
 */
public interface ContentManager {
    String TAG_PREFIX = "DEV_";
    String EDIT_MODE_KEY = "EDIT_MODE_KEY";
    String STAT_PANEL_PADDING_KEY = "STAT_PANEL_PADDING_KEY";

    String USER_PHONE_KEY = "USER_PHONE_KEY";
    String USER_EMAIL_KEY = "USER_EMAIL_KEY";
    String USER_VK_KEY = "USER_VK_KEY";
    String USER_GITHUB_KEY = "USER_GITHUB_KEY";
    String USER_ABOUT_KEY = "USER_ABOUT_KEY";
    String USER_PHOTO_KEY = "USER_PHOTO_KEY";

    int REQUEST_CAMERA_PHOTO = 100;
    int REQUEST_GALLERY_PICTURE = 101;
    int REQUEST_PERMISSION_CODE = 102;
    int CAMERA_REQUEST_PERMISSION_CODE = 103;
    int GALLERY_REQUEST_PERMISSION_CODE = 104;
}
