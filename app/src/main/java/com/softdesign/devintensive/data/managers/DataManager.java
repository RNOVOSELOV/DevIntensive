package com.softdesign.devintensive.data.managers;

import android.content.ContentValues;
import android.content.Context;

import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UploadProfilePhotoRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Класс-синглтон для работы с менеджерами данных
 * Синглтон реализован через вложенный nested-класс чтобы можно было работать с синглтоном из
 * не UI-потоков и, как бонус, ленивая инициализация класса при первом обращении
 */
public class DataManager {

    private Context mContext;
    private PreferencesManager mPreferenceManager;
    private RestService mRestService;

    private DataManager() {
        mPreferenceManager = new PreferencesManager();
        mContext = DevIntensiveApplication.getAppContext();
        mRestService = ServiceGenerator.createService(RestService.class);
    }

    private static class DataManagerHolder {
        private final static DataManager INSTANCE = new DataManager();
    }

    public static DataManager getInstance() {
        return DataManagerHolder.INSTANCE;
    }

    public PreferencesManager getPreferenceManager() {
        return mPreferenceManager;
    }

    public Context getContext() {
        return mContext;
    }

    // region ============== NETWORK ==============

    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq) {
        return mRestService.loginUser(userLoginReq);
    }

    public Call<UploadProfilePhotoRes> uploadPhoto (String userId, File photoFile) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), photoFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", photoFile.getName(), requestFile);
        return mRestService.uploadPhoto(userId, body);
    }

    // endregion

    // region ============== DATABASE ==============
    // endregion
}
