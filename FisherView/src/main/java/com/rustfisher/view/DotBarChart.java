package com.rustfisher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;

/**
 * 柱形图或点图
 * Created by Rust on 2018/8/11.
 */
public class DotBarChart extends View {
    private static final String TAG = "rustAppDotBar";

    public enum TYPE {
        DOT, BAR
    }

    private TYPE mShowType = TYPE.DOT;
    private int mViewHeight;
    private int mViewWidth;
    private int mDotColor = Color.BLACK;
    private int mBarColor = Color.BLUE; // todo 增加柱形图
    private int mDotSizePx = 15;

    private float mShowDataMax = 20;
    private float mShowDataMin = 0;

    private Paint mDataPaint;

    private SparseIntArray mMarkIndexColorMap = new SparseIntArray();
    private float[] mDataInDraw = new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    public DotBarChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotBarChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChart();
    }

    private void initChart() {
        mDataPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDataPaint.setStyle(Paint.Style.FILL);
        mDataPaint.setStrokeWidth(4);
    }

    public float[] getDataInDraw() {
        return mDataInDraw;
    }

    public float getDataMax() {
        return mShowDataMax;
    }

    public void setShowDataMax(float max) {
        this.mShowDataMax = max;
        invalidate();
    }

    public void setShowDataMin(float min) {
        this.mShowDataMin = min;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewHeight = h - getPaddingTop() - getPaddingBottom();
        mViewWidth = w - getPaddingStart() - getPaddingEnd();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawData(canvas);
    }

    public void setData(float[] inputData) {
        mDataInDraw = inputData.clone();
        invalidate();
    }

    public void setData(int[] data, SparseIntArray indexColorMap) {
        mDataInDraw = new float[data.length];
        for (int i = 0; i < data.length; i++) {
            mDataInDraw[i] = data[i];
        }
        if (null != indexColorMap) {
            mMarkIndexColorMap = indexColorMap.clone();
        } else {
            mMarkIndexColorMap.clear();
        }
        invalidate();
    }

    private void drawData(Canvas canvas) {
        int dataCount = mDataInDraw.length;
        float xStep = mViewWidth / (dataCount * 1.0f + 1); // 每个数据占的x宽度
        switch (mShowType) {
            case DOT:
                float perDataDy = (float) (mViewHeight - mDotSizePx) / (mShowDataMax - mShowDataMin);
                mDataPaint.setStyle(Paint.Style.FILL);
                mDataPaint.setStrokeWidth(mDotSizePx);

                for (int i = 0; i < mDataInDraw.length; i++) {
                    mDataPaint.setColor(mMarkIndexColorMap.get(i, mDotColor));
                    canvas.drawCircle(xStep * (i + 1), calDataYCoordinate(mDataInDraw[i], perDataDy) - mDotSizePx, mDotSizePx / 2, mDataPaint);
                }
                break;
            case BAR:

                break;
        }
    }

    private float calDataYCoordinate(float dataValue, float perDataDy) {
        return mViewHeight - dataValue * perDataDy;
    }

}
