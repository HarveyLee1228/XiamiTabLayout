package com.harvey.xiamidemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

/**
 * <pre>
 *     author : Harvey
 *     time   : 2018/05/16
 *     desc   :
 * </pre>
 */
public class MusicWaveView extends View {

    private Paint mPaint;

    private Path mPath;

    private float mDrawHeight;

    private float mDrawWidth;

    private float mViewHeight;

    private float mViewWidth;

    private float mCenterPointX;

    private float mCenterPointY;

    private float mAmplitude;//振幅

    private static final int PERIOD = 10;
    private double mPhase; // 相位
    private float divider = 4f;


    private Timer timer;
    private TimerTask task;

    public MusicWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#f58822"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = measureWidth(widthMeasureSpec);
        heightMeasureSpec = measureHeight(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        initOthers();
    }

    private void initOthers() {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        mDrawWidth = mViewWidth - paddingLeft - paddingRight;
        mDrawHeight = mViewHeight - paddingTop - paddingBottom;

        mCenterPointX = paddingLeft + mDrawWidth / 2f;
        mCenterPointY = paddingTop + mDrawHeight / 2f;

        mAmplitude = mDrawHeight / divider;
    }

    public void setAmplitudeDiv(float div) {
        this.divider = div;
        invalidate();
    }

    private int measureWidth(int spec) {
        int mode = MeasureSpec.getMode(spec);
        if (mode == MeasureSpec.UNSPECIFIED) {
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            spec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        } else if (mode == MeasureSpec.AT_MOST) {
            int value = MeasureSpec.getSize(spec);
            spec = MeasureSpec.makeMeasureSpec(value, MeasureSpec.EXACTLY);
        }
        return spec;
    }

    private int measureHeight(int spec) {
        int mode = MeasureSpec.getMode(spec);
        if (mode == MeasureSpec.EXACTLY) {
            return spec;
        }

        int height = (int) dip2px(50); // 其他模式下的最大高度

        if (mode == MeasureSpec.AT_MOST) {
            int preValue = MeasureSpec.getSize(spec);
            if (preValue < height) {
                height = preValue;
            }
        }
        spec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        return spec;
    }

    private float dip2px(float dp) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
    }

    private double sine(float x, int period, float drawWidth, double phase) {
        return Math.sin(2 * Math.PI * period * (x + phase) / drawWidth);
    }

    private void drawSine(Canvas canvas, Path path, Paint paint, int period,
                          float drawWidth, float amplitude, double phase) {
        float halfDrawWidth = drawWidth / 2f;
        path.reset();
        path.moveTo(-halfDrawWidth, 0);
        float y;
        double scaling;
        for (float x = -halfDrawWidth; x <= halfDrawWidth; x++) {
            scaling = 1 - Math.pow(x / halfDrawWidth, 2);// 对y进行缩放
            y = (float) (sine(x, period, drawWidth, phase) * amplitude * (1) * Math
                    .pow(scaling, 3));
            path.lineTo(x, y);
        }
        canvas.drawPath(path, paint);
        canvas.save();
        canvas.restore();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mCenterPointX, mCenterPointY);
        drawSine(canvas, mPath, mPaint, PERIOD, mDrawWidth, mAmplitude, mPhase);
    }
}

