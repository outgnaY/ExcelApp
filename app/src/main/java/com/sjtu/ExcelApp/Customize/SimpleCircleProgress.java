package com.sjtu.ExcelApp.Customize;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.sjtu.ExcelApp.R;
import com.sjtu.ExcelApp.Util.ComputeUtil;

public class SimpleCircleProgress extends View {
    private String PREFIX = "[SimpleCircleProgress]";
    // private Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "DINAlternateBold.ttf");
    private Typeface scMedium = Typeface.createFromAsset(getContext().getAssets(), "NotoSansSC-Medium.ttf");
    private Typeface scRegular = Typeface.createFromAsset(getContext().getAssets(), "NotoSansSC-Regular.ttf");
    private Typeface numMedium = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Medium.ttf");
    private Typeface numRegular = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Regular.ttf");
    // painters
    private Paint finishedPaint;
    private Paint unfinishedPaint;
    private Paint innerCirclePaint;
    private Paint midTextPaint;
    // outer rects
    private RectF finishedOuterRect = new RectF();
    private RectF unfinishedOuterRect = new RectF();

    public int getFinishedColor() {
        return finishedColor;
    }

    public void setFinishedColor(int finishedColor) {
        this.finishedColor = finishedColor;
        this.invalidate();
    }

    public int getUnfinishedColor() {
        return unfinishedColor;
    }

    public void setUnfinishedColor(int unfinishedColor) {
        this.unfinishedColor = unfinishedColor;
        this.invalidate();
    }

    public int getInnerBackgroundColor() {
        return innerBackgroundColor;
    }

    public void setInnerBackgroundColor(int innerBackgroundColor) {
        this.innerBackgroundColor = innerBackgroundColor;
        this.invalidate();
    }

    public boolean isShowText() {
        return showText;
    }

    public void setShowText(boolean showText) {
        this.showText = showText;
    }

    public float getMidTextSize() {
        return midTextSize;
    }

    public void setMidTextSize(float midTextSize) {
        this.midTextSize = midTextSize;
        this.invalidate();
    }

    public int getMidTextColor() {
        return midTextColor;
    }

    public void setMidTextColor(int midTextColor) {
        this.midTextColor = midTextColor;
        this.invalidate();
    }

    public float getFinishedStrokeWidth() {
        return finishedStrokeWidth;
    }

    public void setFinishedStrokeWidth(float finishedStrokeWidth) {
        this.finishedStrokeWidth = finishedStrokeWidth;
        this.invalidate();
    }

    public float getUnfinishedStrokeWidth() {
        return unfinishedStrokeWidth;
    }

    public void setUnfinishedStrokeWidth(float unfinishedStrokeWidth) {
        this.unfinishedStrokeWidth = unfinishedStrokeWidth;
        this.invalidate();
    }

    public String getMidText() {
        return midText;
    }

    public void setMidText(String midText) {
        this.midText = midText;
        this.invalidate();
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        this.invalidate();
    }
    public int getStartDegree() {
        return startDegree;
    }

    public void setStartDegree(int startDegree) {
        this.startDegree = startDegree;
        this.invalidate();
    }

    private float getProgressAngle() {
        return getProgress() / 100 * 360f;
    }

    private int finishedColor;
    private int unfinishedColor;
    private int innerBackgroundColor;

    private boolean showText;

    private float midTextSize;
    private int midTextColor;
    private String midText = "";

    private float finishedStrokeWidth;
    private float unfinishedStrokeWidth;

    private float progress = 0;
    private int startDegree;

    // default values
    private float defaultMidTextSize;
    private int defaultMidTextColor;
    private float defaultStrokeWidth;
    private int defaultStartDegree;
    private int defaultFinishedColor;
    private int defaultUnfinishedColor;
    private int defaultInnerBackgroundColor;


    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_MID_TEXT = "mid_text";
    private static final String INSTANCE_MID_TEXT_SIZE = "mid_text_size";
    private static final String INSTANCE_MID_TEXT_COLOR = "mid_text_color";
    private static final String INSTANCE_FINISHED_COLOR = "finished_color";
    private static final String INSTANCE_UNFINISHED_COLOR = "unfinished_color";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_BACKGROUND_COLOR = "inner_background_color";
    private static final String INSTANCE_FINISHED_STROKE_WIDTH = "finished_stroke_width";
    private static final String INSTANCE_UNFINISHED_STROKE_WIDTH = "unfinished_stroke_width";
    private static final String INSTANCE_START_DEGREE = "start_degree";


    public SimpleCircleProgress(Context context) {
        this(context, null);
    }
    public SimpleCircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public SimpleCircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultMidTextSize = ComputeUtil.sp2px(getResources(), 15);
        defaultMidTextColor = Color.rgb(0, 0, 0);
        defaultStrokeWidth = ComputeUtil.dp2px(getResources(), 10);
        defaultStartDegree = 0;
        defaultInnerBackgroundColor = Color.rgb(80, 145, 245);
        defaultFinishedColor = Color.rgb(125, 251, 236);
        defaultUnfinishedColor = Color.rgb(60, 109, 185);

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SimpleCircleProgress, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();
        initPainters();
    }
    protected void initByAttributes(TypedArray attributes) {
        setProgress(attributes.getFloat(R.styleable.SimpleCircleProgress_simple_circle_progress, 0));
        showText = attributes.getBoolean(R.styleable.SimpleCircleProgress_simple_circle_show_text, false);
        Log.e(PREFIX, "showText = " + showText);
        if(showText) {
            midText = attributes.getString(R.styleable.SimpleCircleProgress_simple_circle_mid_text);
            midTextColor = attributes.getColor(R.styleable.SimpleCircleProgress_simple_circle_mid_text_color, defaultMidTextColor);
            midTextSize = attributes.getDimension(R.styleable.SimpleCircleProgress_simple_circle_mid_text_size, defaultMidTextSize);
        }
        finishedStrokeWidth = attributes.getDimension(R.styleable.SimpleCircleProgress_simple_circle_finished_stroke_width, defaultStrokeWidth);
        unfinishedStrokeWidth = attributes.getDimension(R.styleable.SimpleCircleProgress_simple_circle_unfinished_stroke_width, defaultStrokeWidth);
        finishedColor = attributes.getColor(R.styleable.SimpleCircleProgress_simple_circle_finished_color, defaultFinishedColor);
        unfinishedColor = attributes.getColor(R.styleable.SimpleCircleProgress_simple_circle_unfinished_color, defaultUnfinishedColor);
        startDegree = attributes.getInt(R.styleable.SimpleCircleProgress_simple_circle_start_degree, defaultStartDegree);
        innerBackgroundColor = attributes.getColor(R.styleable.SimpleCircleProgress_simple_circle_background_color, defaultInnerBackgroundColor);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float delta = Math.min(finishedStrokeWidth, unfinishedStrokeWidth);
        finishedOuterRect.set(delta,
                delta,
                getWidth() - delta,
                getHeight() - delta);
        unfinishedOuterRect.set(delta,
                delta,
                getWidth() - delta,
                getHeight() - delta);

        float innerCircleRadius = (getWidth() - Math.min(finishedStrokeWidth, unfinishedStrokeWidth) + Math.abs(finishedStrokeWidth - unfinishedStrokeWidth)) / 2f;
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, innerCircleRadius, innerCirclePaint);

        // canvas.drawArc(unfinishedOuterRect, getStartDegree() + getProgressAngle(), 360 - getProgressAngle(), false, unfinishedPaint);
        canvas.drawArc(unfinishedOuterRect, 0, 360, false, unfinishedPaint);
        canvas.drawArc(finishedOuterRect, getStartDegree(), getProgressAngle(), false, finishedPaint);
        if(showText) {
            if(!TextUtils.isEmpty(midText)) {
                float midTextHeight = midTextPaint.descent() + midTextPaint.ascent();
                canvas.drawText(midText, (getWidth() - midTextPaint.measureText(midText)) * 0.5f, (getWidth() - midTextHeight) * 0.5f, midTextPaint);
            }
        }
    }
    @Override
    public void invalidate() {
        initPainters();
        super.invalidate();
    }
    protected void initPainters() {
        // init text painters
        if(showText) {
            midTextPaint = new TextPaint();
            midTextPaint.setAntiAlias(true);
            midTextPaint.setColor(midTextColor);
            midTextPaint.setTextSize(midTextSize);
            midTextPaint.setTypeface(numMedium);
        }
        // init progress painters
        finishedPaint = new Paint();
        finishedPaint.setColor(finishedColor);
        finishedPaint.setStyle(Paint.Style.STROKE);
        finishedPaint.setAntiAlias(true);
        finishedPaint.setStrokeWidth(finishedStrokeWidth);
        finishedPaint.setStrokeCap(Paint.Cap.ROUND);

        unfinishedPaint = new Paint();
        unfinishedPaint.setColor(unfinishedColor);
        unfinishedPaint.setStyle(Paint.Style.STROKE);
        unfinishedPaint.setAntiAlias(true);
        unfinishedPaint.setStrokeWidth(unfinishedStrokeWidth);
        unfinishedPaint.setStrokeCap(Paint.Cap.ROUND);

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(innerBackgroundColor);
        innerCirclePaint.setAntiAlias(true);
    }
    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putString(INSTANCE_MID_TEXT, getMidText());
        bundle.putFloat(INSTANCE_MID_TEXT_SIZE, getMidTextSize());
        bundle.putInt(INSTANCE_MID_TEXT_COLOR, getMidTextColor());
        bundle.putFloat(INSTANCE_PROGRESS, getProgress());
        bundle.putFloat(INSTANCE_FINISHED_STROKE_WIDTH, getFinishedStrokeWidth());
        bundle.putFloat(INSTANCE_UNFINISHED_STROKE_WIDTH, getUnfinishedStrokeWidth());
        bundle.putInt(INSTANCE_BACKGROUND_COLOR, getInnerBackgroundColor());
        bundle.putInt(INSTANCE_FINISHED_COLOR, getFinishedColor());
        bundle.putInt(INSTANCE_UNFINISHED_COLOR, getUnfinishedColor());
        bundle.putInt(INSTANCE_START_DEGREE, getStartDegree());
        return bundle;
    }
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            midText = bundle.getString(INSTANCE_MID_TEXT);
            midTextSize = bundle.getFloat(INSTANCE_MID_TEXT_SIZE);
            midTextColor = bundle.getInt(INSTANCE_MID_TEXT_COLOR);
            progress = bundle.getFloat(INSTANCE_PROGRESS);
            finishedStrokeWidth = bundle.getFloat(INSTANCE_FINISHED_STROKE_WIDTH);
            unfinishedStrokeWidth = bundle.getFloat(INSTANCE_UNFINISHED_STROKE_WIDTH);
            innerBackgroundColor = bundle.getInt(INSTANCE_BACKGROUND_COLOR);
            finishedColor = bundle.getInt(INSTANCE_FINISHED_COLOR);
            unfinishedColor = bundle.getInt(INSTANCE_UNFINISHED_COLOR);
            startDegree = bundle.getInt(INSTANCE_START_DEGREE);
            initPainters();
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

}

