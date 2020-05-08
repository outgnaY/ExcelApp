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

public class SemiCircleProgress extends View {
    private String PREFIX = "[SemiCircleProgress]";
    // private Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "DINAlternateBold.ttf");
    private Typeface scMedium = Typeface.createFromAsset(getContext().getAssets(), "NotoSansSC-Medium.ttf");
    private Typeface scRegular = Typeface.createFromAsset(getContext().getAssets(), "NotoSansSC-Regular.ttf");
    private Typeface numMedium = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Medium.ttf");
    private Typeface numRegular = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Regular.ttf");
    // painters
    private Paint finishedPaint;
    private Paint unfinishedPaint;
    private Paint innerCirclePaint;
    private Paint topTextPaint;
    private Paint midSubTextPaint;
    private Paint midTextPaint;
    private Paint bottom1TextPaint;
    private Paint bottom2TextPaint;
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

    public float getTopTextSize() {
        return topTextSize;
    }

    public void setTopTextSize(float topTextSize) {
        this.topTextSize = topTextSize;
        this.invalidate();
    }

    public int getTopTextColor() {
        return topTextColor;
    }

    public void setTopTextColor(int topTextColor) {
        this.topTextColor = topTextColor;
        this.invalidate();
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

    public float getBottom1TextSize() {
        return bottom1TextSize;
    }

    public void setBottom1TextSize(float bottom1TextSize) {
        this.bottom1TextSize = bottom1TextSize;
        this.invalidate();
    }

    public int getBottom1TextColor() {
        return bottom1TextColor;
    }

    public void setBottom1TextColor(int bottom1TextColor) {
        this.bottom1TextColor = bottom1TextColor;
        this.invalidate();
    }

    public float getBottom2TextSize() {
        return bottom2TextSize;
    }

    public void setBottom2TextSize(float bottom2TextSize) {
        this.bottom2TextSize = bottom2TextSize;
        this.invalidate();
    }

    public int getBottom2TextColor() {
        return bottom2TextColor;
    }

    public void setBottom2TextColor(int bottom2TextColor) {
        this.bottom2TextColor = bottom2TextColor;
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
    public String getTopText() {
        return topText;
    }

    public void setTopText(String topText) {
        this.topText = topText;
        this.invalidate();
    }

    public String getMidText() {
        return midText;
    }

    public void setMidText(String midText) {
        this.midText = midText;
        this.invalidate();
    }

    public String getBottom1Text() {
        return bottom1Text;
    }

    public void setBottom1Text(String bottom1Text) {
        this.bottom1Text = bottom1Text;
        this.invalidate();
    }

    public String getBottom2Text() {
        return bottom2Text;
    }

    public void setBottom2Text(String bottom2Text) {
        this.bottom2Text = bottom2Text;
        this.invalidate();
    }
    public float getMidSubTextSize() {
        return midSubTextSize;
    }

    public void setMidSubTextSize(float midSubTextSize) {
        this.midSubTextSize = midSubTextSize;
    }
    public int getMidSubTextColor() {
        return midSubTextColor;
    }

    public void setMidSubTextColor(int midSubTextColor) {
        this.midSubTextColor = midSubTextColor;
    }
    public String getMidSubText() {
        return midSubText;
    }

    public void setMidSubText(String midSubText) {
        this.midSubText = midSubText;
    }
    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        this.invalidate();
    }
    /*
    public int getStartDegree() {
        return startDegree;
    }
    public void setStartDegree(int startDegree) {
        this.startDegree = startDegree;
        this.invalidate();
    }
    */
    private float getProgressAngle() {
        return getProgress() / 100 * 180f;
    }

    private int finishedColor;
    private int unfinishedColor;
    private int innerBackgroundColor;

    private boolean showText;

    private float topTextSize;
    private int topTextColor;
    private String topText = "";

    private float midSubTextSize;
    private int midSubTextColor;
    private String midSubText = "";

    private float midTextSize;
    private int midTextColor;
    private String midText = "";

    private float bottom1TextSize;
    private int bottom1TextColor;
    private String bottom1Text = "";

    private float bottom2TextSize;
    private int bottom2TextColor;
    private String bottom2Text = "";
    private float finishedStrokeWidth;
    private float unfinishedStrokeWidth;

    private float progress = 0;
    // private int startDegree;

    // default values
    private float defaultTopTextSize;
    private int defaultTopTextColor;
    private float defaultMidSubTextSize;
    private int defaultMidSubTextColor;
    private float defaultMidTextSize;
    private int defaultMidTextColor;
    private float defaultBottom1TextSize;
    private int defaultBottom1TextColor;
    private float defaultBottom2TextSize;
    private int defaultBottom2TextColor;
    private float defaultStrokeWidth;
    // private int defaultStartDegree;
    private int defaultFinishedColor;
    private int defaultUnfinishedColor;
    private int defaultInnerBackgroundColor;


    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_TOP_TEXT = "top_text";
    private static final String INSTANCE_TOP_TEXT_SIZE = "top_text_size";
    private static final String INSTANCE_TOP_TEXT_COLOR = "top_text_color";
    private static final String INSTANCE_TOP_SUB_TEXT = "top_sub_text";
    private static final String INSTANCE_TOP_SUB_TEXT_SIZE = "top_sub_text_size";
    private static final String INSTANCE_TOP_SUB_TEXT_COLOR = "top_sub_text_color";
    private static final String INSTANCE_MID_TEXT = "mid_text";
    private static final String INSTANCE_MID_TEXT_SIZE = "mid_text_size";
    private static final String INSTANCE_MID_TEXT_COLOR = "mid_text_color";
    private static final String INSTANCE_BOTTOM1_TEXT = "bottom1_text";
    private static final String INSTANCE_BOTTOM1_TEXT_SIZE = "bottom1_text_size";
    private static final String INSTANCE_BOTTOM1_TEXT_COLOR = "bottom1_text_color";
    private static final String INSTANCE_BOTTOM2_TEXT = "bottom2_text";
    private static final String INSTANCE_BOTTOM2_TEXT_SIZE = "bottom2_text_size";
    private static final String INSTANCE_BOTTOM2_TEXT_COLOR = "bottom2_text_color";
    private static final String INSTANCE_FINISHED_COLOR = "finished_color";
    private static final String INSTANCE_UNFINISHED_COLOR = "unfinished_color";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_BACKGROUND_COLOR = "inner_background_color";
    private static final String INSTANCE_FINISHED_STROKE_WIDTH = "finished_stroke_width";
    private static final String INSTANCE_UNFINISHED_STROKE_WIDTH = "unfinished_stroke_width";
    // private static final String INSTANCE_START_DEGREE = "start_degree";


    public SemiCircleProgress(Context context) {
        this(context, null);
    }
    public SemiCircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public SemiCircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultTopTextSize = ComputeUtil.sp2px(getResources(), 15);
        defaultTopTextColor = Color.rgb(0, 0, 0);
        defaultMidSubTextSize = ComputeUtil.sp2px(getResources(), 15);
        defaultMidSubTextColor = Color.rgb(0, 0, 0);
        defaultMidTextSize = ComputeUtil.sp2px(getResources(), 15);
        defaultMidTextColor = Color.rgb(0, 0, 0);
        defaultBottom1TextSize = ComputeUtil.sp2px(getResources(), 15);
        defaultBottom1TextColor = Color.rgb(0, 0, 0);
        defaultBottom2TextSize = ComputeUtil.sp2px(getResources(), 15);
        defaultBottom2TextColor = Color.rgb(0, 0, 0);
        defaultStrokeWidth = ComputeUtil.dp2px(getResources(), 10);
        // defaultStartDegree = 0;
        defaultInnerBackgroundColor = Color.rgb(80, 145, 245);
        defaultFinishedColor = Color.rgb(125, 251, 236);
        defaultUnfinishedColor = Color.rgb(60, 109, 185);

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SemiCircleProgress, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();
        initPainters();
    }
    protected void initByAttributes(TypedArray attributes) {
        setProgress(attributes.getFloat(R.styleable.SemiCircleProgress_semicircle_progress, 0));
        showText = attributes.getBoolean(R.styleable.SemiCircleProgress_semicircle_show_text, false);
        if(showText) {
            topText = attributes.getString(R.styleable.SemiCircleProgress_semicircle_top_text);
            topTextColor = attributes.getColor(R.styleable.SemiCircleProgress_semicircle_top_text_color, defaultTopTextColor);
            topTextSize = attributes.getDimension(R.styleable.SemiCircleProgress_semicircle_top_text_size, defaultTopTextSize);

            midSubText = attributes.getString(R.styleable.SemiCircleProgress_semicircle_mid_sub_text);
            midSubTextColor = attributes.getColor(R.styleable.SemiCircleProgress_semicircle_mid_sub_text_color, defaultMidSubTextColor);
            midSubTextSize = attributes.getDimension(R.styleable.SemiCircleProgress_semicircle_mid_sub_text_size, defaultMidSubTextSize);

            midText = attributes.getString(R.styleable.SemiCircleProgress_semicircle_mid_text);
            midTextColor = attributes.getColor(R.styleable.SemiCircleProgress_semicircle_mid_text_color, defaultMidTextColor);
            midTextSize = attributes.getDimension(R.styleable.SemiCircleProgress_semicircle_mid_text_size, defaultMidTextSize);

            bottom1Text = attributes.getString(R.styleable.SemiCircleProgress_semicircle_bottom1_text);
            bottom1TextColor = attributes.getColor(R.styleable.SemiCircleProgress_semicircle_bottom1_text_color, defaultBottom1TextColor);
            bottom1TextSize = attributes.getDimension(R.styleable.SemiCircleProgress_semicircle_bottom1_text_size, defaultBottom1TextSize);

            bottom2Text = attributes.getString(R.styleable.SemiCircleProgress_semicircle_bottom2_text);
            bottom2TextColor = attributes.getColor(R.styleable.SemiCircleProgress_semicircle_bottom2_text_color, defaultBottom2TextColor);
            bottom2TextSize = attributes.getDimension(R.styleable.SemiCircleProgress_semicircle_bottom2_text_size, defaultBottom2TextSize);
        }
        finishedStrokeWidth = attributes.getDimension(R.styleable.SemiCircleProgress_semicircle_finished_stroke_width, defaultStrokeWidth);
        unfinishedStrokeWidth = attributes.getDimension(R.styleable.SemiCircleProgress_semicircle_unfinished_stroke_width, defaultStrokeWidth);
        finishedColor = attributes.getColor(R.styleable.SemiCircleProgress_semicircle_finished_color, defaultFinishedColor);
        unfinishedColor = attributes.getColor(R.styleable.SemiCircleProgress_semicircle_unfinished_color, defaultUnfinishedColor);
        // startDegree = attributes.getInt(R.styleable.CircleProgress_start_degree, defaultStartDegree);
        innerBackgroundColor = attributes.getColor(R.styleable.SemiCircleProgress_semicircle_circle_background_color, defaultInnerBackgroundColor);

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
        // canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, innerCircleRadius, innerCirclePaint);
        Log.e(PREFIX, String.valueOf(getProgressAngle()));
        // canvas.drawArc(unfinishedOuterRect, -180 + getProgressAngle(), 180 - getProgressAngle(), false, unfinishedPaint);
        canvas.drawArc(unfinishedOuterRect, -180, 180, false, unfinishedPaint);
        canvas.drawArc(finishedOuterRect, -180, getProgressAngle(), false, finishedPaint);
        if(showText) {
            if(!TextUtils.isEmpty(topText)) {
                float topTextHeight = topTextPaint.descent() + topTextPaint.ascent();
                canvas.drawText(topText, (getWidth() - topTextPaint.measureText(topText)) * 0.5f, (getWidth() - topTextHeight) * 0.19f, topTextPaint);
            }
            if(!TextUtils.isEmpty(midText)) {
                float midTextHeight = midTextPaint.descent() + midTextPaint.ascent();
                float midSubTextHeight = midSubTextPaint.descent() + midSubTextPaint.ascent();
                canvas.drawText(midText, (getWidth() - midTextPaint.measureText(midText)) * 0.5f, (getWidth() - midTextHeight) * 0.35f, midTextPaint);
                canvas.drawText(midSubText, (getWidth() + midTextPaint.measureText(midText)) * 0.5f, (getWidth() - midTextHeight) * 0.35f, midSubTextPaint);
            }
            if(!TextUtils.isEmpty(bottom1Text)) {
                float bottom1TextHeight = bottom1TextPaint.descent() + bottom1TextPaint.ascent();
                canvas.drawText(bottom1Text, (getWidth() - bottom1TextPaint.measureText(bottom1Text)) * 0.5f, (getWidth() - bottom1TextHeight) * 0.48f, bottom1TextPaint);
            }
            if(!TextUtils.isEmpty(bottom2Text)) {
                float bottom2TextHeight = bottom2TextPaint.descent() + bottom2TextPaint.ascent();
                canvas.drawText(bottom2Text, (getWidth() - bottom2TextPaint.measureText(bottom2Text)) * 0.5f, (getWidth() - bottom2TextHeight) * 0.56f, bottom2TextPaint);
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
            topTextPaint = new TextPaint();
            topTextPaint.setAntiAlias(true);
            topTextPaint.setColor(topTextColor);
            topTextPaint.setTextSize(topTextSize);
            topTextPaint.setTypeface(scRegular);

            midSubTextPaint = new TextPaint();
            midSubTextPaint.setAntiAlias(true);
            midSubTextPaint.setColor(midSubTextColor);
            midSubTextPaint.setTextSize(midSubTextSize);
            midSubTextPaint.setTypeface(numMedium);

            midTextPaint = new TextPaint();
            midTextPaint.setAntiAlias(true);
            midTextPaint.setColor(midTextColor);
            midTextPaint.setTextSize(midTextSize);
            midTextPaint.setTypeface(numMedium);

            bottom1TextPaint = new TextPaint();
            bottom1TextPaint.setAntiAlias(true);
            bottom1TextPaint.setColor(bottom1TextColor);
            bottom1TextPaint.setTextSize(bottom1TextSize);
            bottom1TextPaint.setTypeface(scRegular);

            bottom2TextPaint = new TextPaint();
            bottom2TextPaint.setAntiAlias(true);
            bottom2TextPaint.setColor(bottom2TextColor);
            bottom2TextPaint.setTextSize(bottom2TextSize);
            bottom2TextPaint.setTypeface(numRegular);
        }
        // init progress painters
        finishedPaint = new Paint();
        finishedPaint.setColor(finishedColor);
        finishedPaint.setStyle(Paint.Style.STROKE);
        finishedPaint.setAntiAlias(true);
        finishedPaint.setStrokeWidth(finishedStrokeWidth);
        finishedPaint.setStrokeCap(Paint.Cap.ROUND);

        // finishedPaint.setShader()

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
        bundle.putString(INSTANCE_TOP_TEXT, getTopText());
        bundle.putFloat(INSTANCE_TOP_TEXT_SIZE, getTopTextSize());
        bundle.putInt(INSTANCE_TOP_TEXT_COLOR, getTopTextColor());
        bundle.putString(INSTANCE_TOP_SUB_TEXT, getMidSubText());
        bundle.putFloat(INSTANCE_TOP_SUB_TEXT_SIZE, getMidSubTextSize());
        bundle.putInt(INSTANCE_TOP_SUB_TEXT_COLOR, getMidSubTextColor());
        bundle.putString(INSTANCE_MID_TEXT, getMidText());
        bundle.putFloat(INSTANCE_MID_TEXT_SIZE, getMidTextSize());
        bundle.putInt(INSTANCE_MID_TEXT_COLOR, getMidTextColor());
        bundle.putString(INSTANCE_BOTTOM1_TEXT, getBottom1Text());
        bundle.putFloat(INSTANCE_BOTTOM1_TEXT_SIZE, getBottom1TextSize());
        bundle.putInt(INSTANCE_BOTTOM1_TEXT_COLOR, getBottom1TextColor());
        bundle.putString(INSTANCE_BOTTOM2_TEXT, getBottom2Text());
        bundle.putFloat(INSTANCE_BOTTOM2_TEXT_SIZE, getBottom2TextSize());
        bundle.putInt(INSTANCE_BOTTOM2_TEXT_COLOR, getBottom2TextColor());
        bundle.putFloat(INSTANCE_PROGRESS, getProgress());
        bundle.putFloat(INSTANCE_FINISHED_STROKE_WIDTH, getFinishedStrokeWidth());
        bundle.putFloat(INSTANCE_UNFINISHED_STROKE_WIDTH, getUnfinishedStrokeWidth());
        bundle.putInt(INSTANCE_BACKGROUND_COLOR, getInnerBackgroundColor());
        bundle.putInt(INSTANCE_FINISHED_COLOR, getFinishedColor());
        bundle.putInt(INSTANCE_UNFINISHED_COLOR, getUnfinishedColor());
        // bundle.putInt(INSTANCE_START_DEGREE, getStartDegree());
        return bundle;
    }
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            topText = bundle.getString(INSTANCE_TOP_TEXT);
            topTextSize = bundle.getFloat(INSTANCE_TOP_TEXT_SIZE);
            topTextColor = bundle.getInt(INSTANCE_TOP_TEXT_COLOR);
            midSubText = bundle.getString(INSTANCE_TOP_SUB_TEXT);
            midSubTextSize = bundle.getFloat(INSTANCE_TOP_SUB_TEXT_SIZE);
            midSubTextColor = bundle.getInt(INSTANCE_TOP_SUB_TEXT_COLOR);
            midText = bundle.getString(INSTANCE_MID_TEXT);
            midTextSize = bundle.getFloat(INSTANCE_MID_TEXT_SIZE);
            midTextColor = bundle.getInt(INSTANCE_MID_TEXT_COLOR);
            bottom1Text = bundle.getString(INSTANCE_BOTTOM1_TEXT);
            bottom1TextSize = bundle.getFloat(INSTANCE_BOTTOM1_TEXT_SIZE);
            bottom1TextColor = bundle.getInt(INSTANCE_BOTTOM1_TEXT_COLOR);
            bottom2Text = bundle.getString(INSTANCE_BOTTOM2_TEXT);
            bottom2TextSize = bundle.getFloat(INSTANCE_BOTTOM2_TEXT_SIZE);
            bottom2TextColor = bundle.getInt(INSTANCE_BOTTOM2_TEXT_COLOR);
            progress = bundle.getFloat(INSTANCE_PROGRESS);
            finishedStrokeWidth = bundle.getFloat(INSTANCE_FINISHED_STROKE_WIDTH);
            unfinishedStrokeWidth = bundle.getFloat(INSTANCE_UNFINISHED_STROKE_WIDTH);
            innerBackgroundColor = bundle.getInt(INSTANCE_BACKGROUND_COLOR);
            finishedColor = bundle.getInt(INSTANCE_FINISHED_COLOR);
            unfinishedColor = bundle.getInt(INSTANCE_UNFINISHED_COLOR);
            // startDegree = bundle.getInt(INSTANCE_START_DEGREE);
            initPainters();
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

}


