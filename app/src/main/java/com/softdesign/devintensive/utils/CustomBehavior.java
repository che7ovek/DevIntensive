package com.softdesign.devintensive.utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class CustomBehavior extends CoordinatorLayout.Behavior<LinearLayout>{

    private static final String TAG = ConstantManager.TAG_PREFIX + "Custom Behavior";

    public CustomBehavior(){}

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, LinearLayout child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        Log.d(TAG, "onMeasureChild");
        return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        Log.d(TAG, "layoutDependsOn");
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        Log.d(TAG, "onDependentViewChanged");
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
