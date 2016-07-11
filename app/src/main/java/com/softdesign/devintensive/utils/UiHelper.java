package com.softdesign.devintensive.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;

/**
 * Created by roman on 10.07.16.
 */
public class UiHelper {

    public static int getStatusBarHeight() {
        Context context = DevIntensiveApplication.getAppContext();
        int statusBarHeight = 0;
        int statusBarId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (statusBarId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(statusBarId);
        }
        return statusBarHeight;
    }

    public static int getActionBarHeight() {
        Context context = DevIntensiveApplication.getAppContext();
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    /**
     * Метод возвращает высоту в пикселях на основании параметра friction
     *
     * @param start минимальная высота
     * @param end максимальная высота
     * @param friction текущее местоположение в диапазоне 0 ... 1
     * @return положение между положениями start и end на основании параметра friction
     */
    public static int lerp(int start, int end, float friction) {
        return (int) (start + (end - start) * friction);
    }

    /**
     * Метод подсчитвает на сколько сжался элемент относитоельно максимально возможной высоты
     * @param start минимальный размер
     * @param end максимальный размер
     * @param currentValue текущий размер
     * @return текущий размер элемента в диапазоне 0 ... 1, где 0 - минимальный размер; 1 - максимальный
     */
    public static float currentFriction(int start, int end, int currentValue) {
        return (float) (currentValue - start) / (end - start);
    }
}
