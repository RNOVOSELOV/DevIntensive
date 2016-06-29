package com.softdesign.devintensive.data.managers;

/**
 * Created by roman on 28.06.16.
 */
public class DataManager {

    private PreferencesManager mPreferenceManager;

    private DataManager() {
        mPreferenceManager = new PreferencesManager();
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
}
