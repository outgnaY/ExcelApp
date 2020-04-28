package com.sjtu.ExcelApp.Customize;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sjtu.ExcelApp.R;
import com.sjtu.ExcelApp.Util.ComputeUtil;

public class CustomToolbar extends RelativeLayout {
    private String PREFIX = "[CustomToolbar]";
    private View view;
    private ImageView backIcon;
    private TextView backTextView;
    private TextView titleTextView;
    private RelativeLayout wrapper;
    private LinearLayout returnArea;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        titleTextView.setText(title);
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        titleTextView.setTextColor(titleColor);
    }

    public float getTitleSize() {
        return titleSize;
    }

    public void setTitleSize(float titleSize) {
        this.titleSize = titleSize;
        titleTextView.setTextSize(titleSize);
    }

    public String getBackText() {
        return backText;
    }

    public void setBackText(String backText) {
        this.backText = backText;
        backTextView.setText(backText);
    }

    public int getBackTextColor() {
        return backTextColor;
    }

    public void setBackTextColor(int backTextColor) {
        this.backTextColor = backTextColor;
        backTextView.setTextColor(backTextColor);
    }

    public float getBackTextSize() {
        return backTextSize;
    }

    public void setBackTextSize(float backTextSize) {
        this.backTextSize = backTextSize;
        backTextView.setTextSize(backTextSize);
    }

    public int getBackIconResource() {
        return backIconResource;
    }

    public void setBackIconResource(int backIconResource) {
        this.backIconResource = backIconResource;
        backIcon.setImageResource(backIconResource);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        wrapper.setBackgroundColor(backgroundColor);
    }

    public boolean getShowBack() {
        return showBack;
    }

    public void setShowBack(boolean showBack) {
        this.showBack = showBack;
    }

    private String title;
    private int titleColor;
    private float titleSize;
    private String backText;
    private int backTextColor;
    private float backTextSize;
    private int backIconResource;
    private int backgroundColor;
    private boolean showBack;

    private String defaultTitle;
    private int defaultTitleColor;
    private float defaultTitleSize;
    private String defaultBackText;
    private int defaultBackTextColor;
    private float defaultBackTextSize;
    private int defaultBackIconResource;
    private int defaultBackgroundColor;
    private boolean defaultShowBack;

    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_TITLE = "title";
    private static final String INSTANCE_TITLE_COLOR = "title_color";
    private static final String INSTANCE_TITLE_SIZE = "title_size";
    private static final String INSTANCE_BACKTEXT = "backtext";
    private static final String INSTANCE_BACKTEXT_COLOR = "backtext_color";
    private static final String INSTANCE_BACKTEXT_SIZE = "backtext_size";
    private static final String INSTANCE_BACKICON = "backicon";
    private static final String INSTANCE_BACKGROUND = "background";
    private static final String INSTANCE_SHOWBACK = "showback";
    public CustomToolbar(Context context) {
        this(context, null);
    }
    public CustomToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public CustomToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        view = LayoutInflater.from(context).inflate(R.layout.custom_toolbar, this);
        backIcon = view.findViewById(R.id.custom_toolbar_back_icon);
        backTextView = view.findViewById(R.id.custom_toolbar_back_text);
        titleTextView = view.findViewById(R.id.custom_toolbar_title);
        wrapper = view.findViewById(R.id.custom_toolbar_wrapper);
        returnArea = view.findViewById(R.id.custom_toolbar_return_area);

        defaultTitle = "";
        defaultTitleColor = Color.rgb(255, 255, 255);
        defaultTitleSize = ComputeUtil.sp2px(getResources(), 20);
        defaultBackText = "返回";
        defaultBackTextColor = Color.rgb(255, 255, 255);
        defaultBackTextSize = ComputeUtil.sp2px(getResources(), 15);
        defaultBackIconResource = R.mipmap.arrow_left;
        defaultBackgroundColor = getResources().getColor(R.color.departmentBackground);
        defaultShowBack = false;
        // defaultBackgroundColor = Color.rgb(0, 0, 0);
        // Log.e(PREFIX, String.valueOf(defaultBackgroundColor));
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomToolbar, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();
    }
    private void initByAttributes(TypedArray attributes) {
        title = attributes.getString(R.styleable.CustomToolbar_custom_toolbar_title);
        titleColor = attributes.getColor(R.styleable.CustomToolbar_custom_toolbar_title_color, defaultTitleColor);
        titleSize = attributes.getDimension(R.styleable.CustomToolbar_custom_toolbar_title_size, defaultTitleSize);
        backText = attributes.getString(R.styleable.CustomToolbar_custom_toolbar_backtext);;
        backTextColor = attributes.getColor(R.styleable.CustomToolbar_custom_toolbar_backtext_color, defaultBackTextColor);;
        backTextSize = attributes.getDimension(R.styleable.CustomToolbar_custom_toolbar_backtext_size, defaultBackTextSize);
        backIconResource = attributes.getResourceId(R.styleable.CustomToolbar_custom_toolbar_backicon, defaultBackIconResource);
        backgroundColor = attributes.getColor(R.styleable.CustomToolbar_custom_toolbar_background, defaultBackgroundColor);
        showBack = attributes.getBoolean(R.styleable.CustomToolbar_custom_toolbar_showback, defaultShowBack);
        Log.e(PREFIX, String.valueOf(backgroundColor));
        titleTextView.setTextColor(titleColor);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        if(!TextUtils.isEmpty(title)) {
            titleTextView.setText(title);
        }
        backTextView.setTextColor(backTextColor);
        backTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, backTextSize);
        if(showBack && !TextUtils.isEmpty(backText)) {
            backTextView.setText(backText);
            backIcon.setImageResource(backIconResource);
            returnArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Activity)view.getContext()).finish();
                }
            });
        }
        wrapper.setBackgroundColor(backgroundColor);
    }
    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putString(INSTANCE_TITLE, getTitle());
        bundle.putInt(INSTANCE_TITLE_COLOR, getTitleColor());
        bundle.putFloat(INSTANCE_TITLE_SIZE, getTitleSize());
        bundle.putString(INSTANCE_BACKTEXT, getBackText());
        bundle.putInt(INSTANCE_BACKTEXT_COLOR, getBackTextColor());
        bundle.putFloat(INSTANCE_BACKTEXT_SIZE, getBackTextSize());
        bundle.putInt(INSTANCE_BACKICON, getBackIconResource());
        bundle.putInt(INSTANCE_BACKGROUND, getBackgroundColor());
        bundle.putBoolean(INSTANCE_SHOWBACK, getShowBack());
        return bundle;
    }
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            title = bundle.getString(INSTANCE_TITLE);
            titleColor = bundle.getInt(INSTANCE_TITLE_COLOR);
            titleSize = bundle.getFloat(INSTANCE_TITLE_SIZE);
            backText = bundle.getString(INSTANCE_BACKTEXT);
            backTextColor = bundle.getInt(INSTANCE_BACKTEXT_COLOR);
            backTextSize = bundle.getFloat(INSTANCE_BACKTEXT_SIZE);
            backIconResource = bundle.getInt(INSTANCE_BACKICON);
            backgroundColor = bundle.getInt(INSTANCE_BACKGROUND);
            showBack = bundle.getBoolean(INSTANCE_SHOWBACK);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

}
