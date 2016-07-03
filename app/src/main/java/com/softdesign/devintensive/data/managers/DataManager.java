package com.softdesign.devintensive.data.managers;

/**
 * Класс-синглтон для работы с менеджерами данных
 * Синглтон реализован через вложенный nested-класс чтобы можно было работать с синглтоном из
 * не UI-потоков и, как бонус, ленивая инициализация класса при первом обращении
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
