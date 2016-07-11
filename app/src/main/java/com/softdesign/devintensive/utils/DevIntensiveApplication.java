package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by roman on 28.06.16.
 */
public class DevIntensiveApplication extends Application {

    private static Context sContext;
    public static SharedPreferences sSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        DevIntensiveApplication.sContext = getApplicationContext();
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public static SharedPreferences getsSharedPreferences() {
        return sSharedPreferences;
    }

    public static Context getAppContext() {
        return DevIntensiveApplication.sContext;
    }
}
