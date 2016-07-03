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

    public NestedScrollBehavior(Context context, AttributeSet attributeSet) {

    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, NestedScrollView child, View dependency) {
        return dependency instanceof LinearLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, NestedScrollView child, View dependency) {
        child.setY(dependency.getBottom());
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
