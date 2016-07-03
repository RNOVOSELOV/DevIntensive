package com.softdesign.devintensive.ui.view.behaviors;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by roman on 02.07.16.
 */
public class NestedScrollBehavior extends CoordinatorLayout.Behavior<NestedScrollView> {

    /**
     * Конструктор бихейвера, чтобы можно было его использовать из xml разметки
     *
     * @param context      контекст
     * @param attributeSet набор аттрибутов
     */
    public NestedScrollBehavior(Context context, AttributeSet attributeSet) {

    }

    /**
     * Метод указывает зависимость вью от родительской
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, NestedScrollView child, View dependency) {
        return dependency instanceof LinearLayout;
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
    public boolean onDependentViewChanged(CoordinatorLayout parent, NestedScrollView child, View dependency) {
        child.setY(dependency.getBottom());
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
