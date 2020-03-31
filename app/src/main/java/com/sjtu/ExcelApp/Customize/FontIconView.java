package com.sjtu.ExcelApp.Customize;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontIconView extends android.support.v7.widget.AppCompatTextView {
    public FontIconView(Context context) {
        super(context);
        init(context);
    }
    public FontIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public FontIconView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    private void init(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
        this.setTypeface(font);
    }

}
