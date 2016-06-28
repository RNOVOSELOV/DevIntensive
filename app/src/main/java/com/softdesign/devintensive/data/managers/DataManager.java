package com.softdesign.devintensive.data.managers;

/**
 * Created by roman on 28.06.16.
 */
public class DataManager {

    private static DataManager INSTANCE = null;
    private PreferencesManager mPreferenceManager;

    private DataManager() {
        mPreferenceManager = new PreferencesManager();
    }

    public static DataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public PreferencesManager getPreferenceManager () {
        return mPreferenceManager;
    }
}
