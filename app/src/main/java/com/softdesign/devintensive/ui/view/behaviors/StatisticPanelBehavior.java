package com.softdesign.devintensive.ui.view.behaviors;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;

/**
 * Created by roman on 02.07.16.
 */
public class StatisticPanelBehavior extends CoordinatorLayout.Behavior<LinearLayout> {

    // Максимально возможный паддинг панели статистики (24dp) в px
    int maxPanelPadding;
    // Высота на которую может максимально подняться панель статистики (высота статус бара и экшн бара)
    float panelIndent;

    /**
     * Конструктор бихейвера, чтобы можно было его использовать из xml разметки
     *
     * @param context контекст
     * @param attrs   набор аттрибутов
     */
    public StatisticPanelBehavior(Context context, AttributeSet attrs) {
        maxPanelPadding = context.getResources().getDimensionPixelSize(R.dimen.padding_large_24);
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

    /**
     * Метод вызывается каждый раз, когда изменяется вью (dependency), к которой привязан контролируемый (child),
     * а так же при прокрутке и появлении/исчезновении элементов
     *
     * @param parent     родительский {@link CoordinatorLayout }
     * @param child      контролируемое {@link View}, в нашем случае {@link LinearLayout}
     * @param dependency {@link View} от которого зависит контролируемое {@link View}, в нашем случае {@link android.support.v7.app.ActionBar}
     * @return
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        final CoordinatorLayout.LayoutParams lp = ((CoordinatorLayout.LayoutParams) child.getLayoutParams());
        if (lp.getAnchorId() != dependency.getId()) {
            return super.onDependentViewChanged(parent, child, dependency);
        }
        // Шаг высоты аппара, при прохождении которого необходимо на еденицу изменить паддинг
        float panelStep = dependency.getHeight() / maxPanelPadding;

        int currentTopAndBottomPadding;

        // В крайних положениях минимальный и максимальный паддинг устанавливаем, если попали в последний либо первый шаг
        // Для всех остальных случаев высчитываем
        if (dependency.getBottom() - panelIndent <= panelStep) {
            currentTopAndBottomPadding = 0;
        } else if ((dependency.getBottom() - panelIndent) >= dependency.getHeight() - panelStep) {
            currentTopAndBottomPadding = maxPanelPadding;
        } else {
            currentTopAndBottomPadding = (int) ((dependency.getBottom() - panelIndent) / panelStep);
        }
        child.setPadding(0, currentTopAndBottomPadding, 0, currentTopAndBottomPadding);
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
