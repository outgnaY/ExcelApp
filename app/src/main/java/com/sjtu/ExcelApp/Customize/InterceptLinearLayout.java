package com.sjtu.ExcelApp.Customize;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class InterceptLinearLayout extends LinearLayout {
    private String PREFIX = "[InterceptLinearLayout]";
    public InterceptLinearLayout(Context context) {
        super(context);
    }
    public InterceptLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }
    public InterceptLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    // intercept touch event
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
