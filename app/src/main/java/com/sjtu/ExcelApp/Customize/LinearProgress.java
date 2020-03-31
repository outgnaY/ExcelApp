package com.sjtu.ExcelApp.Customize;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.github.lzyzsd.circleprogress.Utils;
import com.sjtu.ExcelApp.R;

import java.text.AttributedCharacterIterator;

public class LinearProgress extends View {
    private Paint textPaint;
    private Paint progressPaint;
    private Paint backgroundPaint;

    private boolean showText;
    private float progress;
    private String text;
    private int progressBackgroundColor;
    private int progressColor;
    private int textColor;
    private float textSize;
    private float strokeWidth;

    private int defaultProgressBackgroundColor = Color.rgb(60, 63, 65);
    private int defaultProgressColor = Color.rgb(62, 134, 160);
    private float defaultTextSize;
    private int defaultTextColor = Color.WHITE;
    private float defaultStrokeWidth;

    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_TEXT_COLOR = "text_color";
    private static final String INSTANCE_TEXT_SIZE = "text_size";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_PROGRESS_BACKGROUND_COLOR = "background_color";
    private static final String INSTANCE_PROGRESS_COLOR = "progress_color";
    private static final String INSTANCE_TEXT = "text";
    private static final String INSTANCE_STROKE_WIDTH = "stroke_width";

    private Paint paint = new Paint();


    public LinearProgress(Context context) {
        this(context, null);
    }
    public LinearProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public LinearProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultTextSize = Utils.sp2px(getResources(), 12);
        defaultStrokeWidth = Utils.dp2px(getResources(), 5);
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LinearProgress, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();

        initPainters();
    }
    protected void initByAttributes(TypedArray attributes) {
        progressBackgroundColor = attributes.getColor(R.styleable.LinearProgress_background_color, defaultProgressBackgroundColor);
        progressColor = attributes.getColor(R.styleable.LinearProgress_progress_color, defaultProgressColor);

        showText = attributes.getBoolean(R.styleable.LinearProgress_show_text, true);
        setProgress(attributes.getFloat(R.styleable.LinearProgress_progress, 0));

        if(showText) {
            if(attributes.getString(R.styleable.LinearProgress_text) != null) {
                text = attributes.getString(R.styleable.LinearProgress_text);
                Log.d("a", text);
            }
            textColor = attributes.getColor(R.styleable.LinearProgress_text_color, defaultTextColor);
            textSize = attributes.getFloat(R.styleable.LinearProgress_text_size, defaultTextSize);
        }
        strokeWidth = attributes.getFloat(R.styleable.LinearProgress_stroke_width, defaultStrokeWidth);

    }
    protected void initPainters() {
        if(showText) {
            textPaint = new TextPaint();
            textPaint.setColor(textColor);
            textPaint.setTextSize(textSize);
            textPaint.setAntiAlias(true);
        }
        progressPaint = new Paint();
        progressPaint.setColor(progressColor);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeWidth(strokeWidth);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(progressBackgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStrokeWidth(strokeWidth);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);
    }
    @Override
    public void invalidate() {
        initPainters();
        super.invalidate();
    }
    public boolean isShowText() {
        return showText;
    }
    public void setShowText(boolean showText) {
        this.showText = showText;
        this.invalidate();
    }
    public float getProgress() {
        return progress;
    }
    public void setProgress(float progress) {
        this.progress = progress;
        this.invalidate();
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
        this.invalidate();
    }
    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        this.invalidate();
    }
    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.invalidate();
    }
    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        this.invalidate();
    }
    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        this.invalidate();
    }

    public int getProgressBackgroundColor() {
        return progressBackgroundColor;
    }

    public void setProgressBackgroundColor(int progressBackgroundColor) {
        this.progressBackgroundColor = progressBackgroundColor;
        this.invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(strokeWidth, getHeight() * 0.8f, getWidth() - strokeWidth, getHeight() * 0.8f, backgroundPaint);
        canvas.drawLine(strokeWidth, getHeight() * 0.8f, (getWidth() - strokeWidth) * getProgress() / 100f, getHeight() * 0.8f, progressPaint);
        if(showText) {
            String text = this.text;
            Log.d("a", text);
            // float textHeight = textPaint.descent() + textPaint.ascent();
            // canvas.drawText(text, (getWidth() - textPaint.measureText(text)) * 0.5f, (getHeight() - textHeight) * 0.5f, textPaint);
            canvas.drawText(text, (getWidth() - textPaint.measureText(text)) * 0.5f, getHeight() * 0.5f, textPaint);
        }
    }
    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor());
        bundle.putFloat(INSTANCE_TEXT_SIZE, getTextSize());
        bundle.putFloat(INSTANCE_PROGRESS, getProgress());
        bundle.putInt(INSTANCE_PROGRESS_BACKGROUND_COLOR, getProgressBackgroundColor());
        bundle.putInt(INSTANCE_PROGRESS_COLOR, getProgressColor());
        bundle.putString(INSTANCE_TEXT, getText());
        bundle.putFloat(INSTANCE_STROKE_WIDTH, getStrokeWidth());
        return bundle;
    }
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            textColor = bundle.getInt(INSTANCE_TEXT_COLOR);
            textSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
            progress = bundle.getFloat(INSTANCE_PROGRESS);
            progressBackgroundColor = bundle.getInt(INSTANCE_PROGRESS_BACKGROUND_COLOR);
            progressColor = bundle.getInt(INSTANCE_PROGRESS_COLOR);
            text = bundle.getString(INSTANCE_TEXT);
            strokeWidth = bundle.getFloat(INSTANCE_STROKE_WIDTH);
            return;
        }
        super.onRestoreInstanceState(state);
    }







}
