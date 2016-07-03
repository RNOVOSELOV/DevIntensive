package com.softdesign.devintensive.ui.view.behaviors;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;

/**
 * Created by roman on 02.07.16.
 */
public class StatisticPanelBehavior extends CoordinatorLayout.Behavior<LinearLayout> {

    // Максимально возможный паддинг панели статистики (24dp)
    int maxPanelPadding;
    // Высота статус бара и экшн бара
    float panelIndent;

    public StatisticPanelBehavior(Context context, AttributeSet attrs) {
        maxPanelPadding = context.getResources().getDimensionPixelSize(R.dimen.padding_xxlarge_24);
        int statusBarHeight = 0;
        int statusBarId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (statusBarId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(statusBarId);
        }

        float actionBarHeight = 0;
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        actionBarHeight = styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        panelIndent = statusBarHeight + actionBarHeight;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        final CoordinatorLayout.LayoutParams lp = ((CoordinatorLayout.LayoutParams) child.getLayoutParams());
        if (lp.getAnchorId() != dependency.getId()) {
            return false;
        }
        // Шаг высоты аппара, при прохождении которого необходимо на еденицу изменить паддинг2
        float panelStep = dependency.getHeight() / maxPanelPadding;
        int paddingTopAndBottom;

        // В крайних положениях минимальный и максимальный паддинг устанавливаем, если попали в последний либо первый шаг
        // Для всех остальных случаев высчитываем
        if (dependency.getBottom() - panelIndent <= panelStep) {
            paddingTopAndBottom = 0;
        } else if ((dependency.getBottom() - panelIndent) >= dependency.getHeight() - panelStep) {
            paddingTopAndBottom = maxPanelPadding;
        } else {
            paddingTopAndBottom = (int) ((dependency.getBottom() - panelIndent) / panelStep);
        }
        child.setPadding(0, paddingTopAndBottom, 0, paddingTopAndBottom);
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
