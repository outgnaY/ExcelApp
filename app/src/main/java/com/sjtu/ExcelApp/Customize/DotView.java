package com.sjtu.ExcelApp.Customize;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import com.sjtu.ExcelApp.R;
import com.sjtu.ExcelApp.Util.ComputeUtil;

public class DotView extends View {
    private String PREFIX = "[DotView]";
    private Paint paint;

    @Override
    public void invalidate() {
        initPainters();
        super.invalidate();
    }
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        invalidate();
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
        invalidate();
    }

    private int color;
    private float radius;
    private float length;

    private int defaultColor;
    private float defaultRadius;
    private float defaultLength;

    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_COLOR = "color";
    private static final String INSTANCE_RADIUS = "radius";
    private static final String INSTANCE_LENGTH = "length";

    public DotView(Context context) {
        this(context, null);
    }
    public DotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public DotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultColor = Color.rgb(159, 195, 247);
        defaultRadius = ComputeUtil.dp2px(getResources(), 8);
        defaultLength = ComputeUtil.dp2px(getResources(), 8);
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DotView, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();
        initPainters();
    }
    protected void initPainters() {
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(radius);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }
    protected void initByAttributes(TypedArray attributes) {
        color = attributes.getColor(R.styleable.DotView_dotview_color, defaultColor);
        radius = attributes.getDimension(R.styleable.DotView_dotview_radius, defaultRadius);
        length = attributes.getDimension(R.styleable.DotView_dotview_length, defaultLength);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(radius, getHeight() * 0.5f, radius + length, getHeight() * 0.5f, paint);
    }
    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(INSTANCE_COLOR, getColor());
        bundle.putFloat(INSTANCE_RADIUS, getRadius());
        bundle.putFloat(INSTANCE_LENGTH, getLength());
        return bundle;
    }
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            color = bundle.getInt(INSTANCE_COLOR);
            radius = bundle.getFloat(INSTANCE_RADIUS);
            length = bundle.getFloat(INSTANCE_LENGTH);
            initPainters();
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
