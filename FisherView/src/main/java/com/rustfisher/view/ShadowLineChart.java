package com.rustfisher.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Locale;

/**
 * 带有阴影的折线图
 * Created by Rust on 2018/6/4.
 */
public class ShadowLineChart extends View {

    private int mViewHeight;
    private int mViewWidth;
    private int mBgColorInt = Color.parseColor("#202a30");
    private int mTextColorInt = Color.parseColor("#757c85");
    private int mDataColorInt = Color.parseColor("#fe6270");
    private int mAxisWid = 7;           // 坐标轴线条宽度
    private int mMarkLineLength = 88;   // 刻度长度
    private float mDataMax = 2048;
    private float mDataMin = -2048;      // 图表显示数据的下限
    private float mMark1 = 1000;  // 刻度1
    private float mMark2 = -1000; // 刻度2
    private float mTextSizePx = 28;
    private float mMarkTextCoorGapPx = 15;// 坐标刻度文字和轴间距
    private Rect mTextTmpRect;
    private int mTextRectHeight = 30;
    private DashPathEffect mDashPathEffect;
    private Paint mBgPaint;
    private Paint mTextPaint;
    private Paint mDataPaint;
    private Paint shadowPaint;
    private Path mLinePath;
    private Path mShadowPath;

    private float mCoorOriginX; // 坐标原点 x
    private float mCoorOriginY;

    private int mMaxDataCount = 256; // 横轴显示的最大数据个数
    private float[] mDataInDraw = new float[1];

    public ShadowLineChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChart(context, attrs, defStyleAttr);
    }

    private void initChart(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShadowLineChart, defStyleAttr, 0);
        mDataColorInt = a.getColor(R.styleable.ShadowLineChart_dataColor, mDataColorInt);
        a.recycle();

        mDataInDraw = new float[mMaxDataCount];
        mBgPaint = new Paint();
        mDataPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint = new Paint();
        mLinePath = new Path();
        mShadowPath = new Path();
        mDataPaint.setStyle(Paint.Style.STROKE);
        mDataPaint.setStrokeWidth(4);
        mBgPaint.setStyle(Paint.Style.FILL);
        mDashPathEffect = new DashPathEffect(new float[]{44, 21}, 0);
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        mTextTmpRect = new Rect(0, 1, 2, 3);
    }

    public float[] getDataInDraw() {
        return mDataInDraw;
    }

    public float getDataMax() {
        return mDataMax;
    }

    public void setDataMax(float max) {
        this.mDataMax = max;
        invalidate();
    }

    public void setDataMin(float min) {
        this.mDataMin = min;
        invalidate();
    }

    public void setMark1(float mark) {
        mMark1 = mark;
    }

    public void setMark2(float mark) {
        mMark2 = mark;
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
        mCoorOriginX = mViewWidth * 0.1f;
        mCoorOriginY = mViewHeight / 2.0f;
        drawBg(canvas);
        drawData(canvas);
    }

    public void inputData(float[] inputData) {
        if (null == inputData || inputData.length == 0) {
            return;
        }
        int inputLen = inputData.length;
        int maxLen = mDataInDraw.length;
        if (inputLen < maxLen) {
            // 复制原有数据到前面去
            System.arraycopy(mDataInDraw, inputLen, mDataInDraw, 0, maxLen - inputLen);
            System.arraycopy(inputData, 0, mDataInDraw, maxLen - inputLen, inputLen);
        } else {
            System.arraycopy(inputData, inputLen - maxLen, mDataInDraw, 0, maxLen);
        }
        invalidate();
    }

    /**
     * 绘制背景  坐标 刻度 文字 等等
     */
    private void drawBg(Canvas canvas) {
        setLayerType(LAYER_TYPE_SOFTWARE, mBgPaint); // 关闭硬件加速
        mBgPaint.setColor(mBgColorInt);
        mBgPaint.setStrokeWidth(mAxisWid);
        mBgPaint.setPathEffect(null);
        canvas.drawLine(mCoorOriginX, 0, mCoorOriginX, mViewHeight, mBgPaint); // y轴
        float markY1 = mCoorOriginY * (1 - mMark1 / mDataMax);
        float markY2 = mCoorOriginY * (mMark2 / mDataMin) + mCoorOriginY;
        canvas.drawLine(mCoorOriginX, markY1, mCoorOriginX + mMarkLineLength, markY1, mBgPaint);
        canvas.drawLine(mCoorOriginX, markY2, mCoorOriginX + mMarkLineLength, markY2, mBgPaint);

        mBgPaint.setPathEffect(mDashPathEffect); // 虚线效果
        mBgPaint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(mCoorOriginX, mCoorOriginY, mViewWidth, mCoorOriginY, mBgPaint); // x轴

        // 绘制刻度文字
        mTextPaint.setColor(mTextColorInt);
        mTextPaint.setTextSize(mTextSizePx);
        float markTextX = mCoorOriginX - mMarkTextCoorGapPx;

        // 先确定字体所在的矩形
        mTextTmpRect.set(0, (int) (mCoorOriginY - mTextRectHeight), (int) markTextX, (int) (mCoorOriginY + mTextRectHeight));
        // 绘制文字
        canvas.drawText("0", markTextX, calTextBaselineY(mTextTmpRect), mTextPaint);
        mTextTmpRect.set(0, (int) markY1 - mTextRectHeight, (int) markTextX, (int) markY1 + mTextRectHeight);
        canvas.drawText(String.format(Locale.CHINA, "%.0f", mMark1), markTextX, calTextBaselineY(mTextTmpRect), mTextPaint);
        mTextTmpRect.set(0, (int) markY2 - mTextRectHeight, (int) markTextX, (int) markY2 + mTextRectHeight);
        canvas.drawText(String.format(Locale.CHINA, "%.0f", mMark2), markTextX, calTextBaselineY(mTextTmpRect), mTextPaint);
    }

    private void drawData(Canvas canvas) {
        mDataPaint.setColor(mDataColorInt);
        mLinePath.reset();
        mShadowPath.reset();
        int dataCount = mDataInDraw.length;
        float xStep = (mViewWidth - mCoorOriginX) / (dataCount * 1.0f - 1); // 每个数据占的x宽度
        float perDataDy = (float) mViewHeight / (mDataMax - mDataMin);
        float maxDataValue = mDataInDraw[0];
        for (int i = 0; i < dataCount; i++) {
            if (mDataInDraw[i] > maxDataValue) {
                maxDataValue = mDataInDraw[i]; // 找出最大的数值
            }
            float pointX = mCoorOriginX + xStep * i;
            float pointY = calDataYCoor(mDataInDraw[i], perDataDy);
            if (i == 0) {
                mLinePath.moveTo(pointX, pointY);
                mShadowPath.moveTo(pointX, pointY);
            } else {
                mLinePath.lineTo(pointX, pointY);
                mShadowPath.lineTo(pointX, pointY);
                if (i == dataCount - 1) {
                    mShadowPath.lineTo(pointX, mViewHeight);
                    mShadowPath.lineTo(mCoorOriginX, mViewHeight);
                    mShadowPath.close();
                }
            }
        }
        shadowPaint.setColor(mDataColorInt);
        shadowPaint.setShader(new LinearGradient(0, calDataYCoor(maxDataValue, perDataDy) + mDataPaint.getStrokeWidth(),
                0, mViewHeight, shadowPaint.getColor(), Color.TRANSPARENT, Shader.TileMode.CLAMP));
        canvas.drawPath(mShadowPath, shadowPaint);
        canvas.drawPath(mLinePath, mDataPaint);
    }

    private float calDataYCoor(float dataValue, float perDataDy) {
        return mCoorOriginY - dataValue * perDataDy;
    }

    /**
     * @param textRect 让字体显示在这个矩形的正中间
     * @return 文字的baseline
     */
    private int calTextBaselineY(Rect textRect) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top; // 为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom; // 为基线到字体下边框的距离,即上图中的bottom
        return (int) (textRect.centerY() - top / 2 - bottom / 2);
    }

}
