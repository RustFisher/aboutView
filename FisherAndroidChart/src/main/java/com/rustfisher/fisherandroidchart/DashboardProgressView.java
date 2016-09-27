package com.rustfisher.fisherandroidchart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class DashboardProgressView extends View {

    private static final String TAG = DashboardProgressView.class.getSimpleName();

    private int mRadius; // 圆弧半径
    private int mStartAngle; // 起始角度
    private int mSweepAngle; // 绘制角度
    private int mBigSliceCount; // 大份数
    private int mSliceCountInOneBigSlice; // 划分一大份长的小份数
    private int mBgArcColor;
    private int mBgOuterElementColor;
    private int mMeasureTextSize; // 刻度字体大小
    private int mTextColor; // 字体颜色
    private String mHeaderTitle = "";
    private int mHeaderTextSize; // 表头字体大小
    private int mCircleRadius; // 中心圆半径
    private int mMinValue; // 最小值
    private int mMaxValue; // 最大值
    private float mRealTimeValue; // 实时值
    private int mStripeWidth; // 色条宽度
    private int mBigSliceRadius; // 较长刻度半径
    private int mSmallSliceRadius; // 较短刻度半径
    private int mNumMeaRadius; // 数字刻度半径
    private int mBgColor; // 整个View的背景色

    private StripeMode mStripeMode = StripeMode.DOT_BG;
    private int mModeType;// 圆圈外的背景：点；条状；无

    private int mArcWidth = dpToPx(8);// Arc: bg and real time arc
    private int mProgressArcColor = Color.BLUE;

    private int mViewWidth; // 控件宽度
    private int mViewHeight; // 控件高度
    private float mCenterX;
    private float mCenterY;

    private Paint mPaintArc;
    private Paint mPaintBackgroundText;
    private Paint mPaintValue;
    private Paint mPaintStripe;

    private RectF mBackgroundRectArc;
    private RectF mRectStripe;
    private Rect mRectMeasures;
    private Rect mRectRealText;

    private float mBigSliceAngle; // 大刻度等分角度

    private String[] mGraduations; // 等分的刻度值
    private float initAngle;
    private boolean mAnimEnable; // 是否播放动画
    private ViewHandler mHandler;
    private long duration = 500; // 动画默认时长

    public DashboardProgressView(Context context) {
        this(context, null);
    }

    public DashboardProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashboardProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DashboardProgressView, defStyleAttr, 0);

        // Set default values here
        mRadius = a.getDimensionPixelSize(R.styleable.DashboardProgressView_radius, dpToPx(80));
        mStartAngle = a.getInteger(R.styleable.DashboardProgressView_startAngle, 180);
        mSweepAngle = a.getInteger(R.styleable.DashboardProgressView_sweepAngle, 180);
        mBigSliceCount = a.getInteger(R.styleable.DashboardProgressView_bigSliceCount, 10);
        mSliceCountInOneBigSlice = a.getInteger(R.styleable.DashboardProgressView_sliceCountInOneBigSlice, 5);
        mBgArcColor = a.getColor(R.styleable.DashboardProgressView_bgArcColor, Color.BLACK);
        mBgOuterElementColor = a.getColor(R.styleable.DashboardProgressView_bgOuterElementColor, Color.BLACK);
        mProgressArcColor = a.getColor(R.styleable.DashboardProgressView_progressArcColor, Color.BLUE);
        mMeasureTextSize = a.getDimensionPixelSize(R.styleable.DashboardProgressView_measureTextSize, spToPx(7));
        mTextColor = a.getColor(R.styleable.DashboardProgressView_textColor, Color.WHITE);
        mHeaderTitle = a.getString(R.styleable.DashboardProgressView_headerTitle);
        if (mHeaderTitle == null) mHeaderTitle = "";
        mHeaderTextSize = a.getDimensionPixelSize(R.styleable.DashboardProgressView_headerTextSize, spToPx(16));
        mCircleRadius = a.getDimensionPixelSize(R.styleable.DashboardProgressView_circleRadius, mRadius / 17);
        mMinValue = a.getInteger(R.styleable.DashboardProgressView_minValue, 0);
        mMaxValue = a.getInteger(R.styleable.DashboardProgressView_maxValue, 100);
        mRealTimeValue = a.getFloat(R.styleable.DashboardProgressView_realTimeValue, 0.0f);
        mStripeWidth = a.getDimensionPixelSize(R.styleable.DashboardProgressView_stripeWidth, 0);
        mModeType = a.getInt(R.styleable.DashboardProgressView_stripeMode, 0);
        mBgColor = a.getColor(R.styleable.DashboardProgressView_bgColor, 0);
        mArcWidth = a.getDimensionPixelSize(R.styleable.DashboardProgressView_arcWidth, dpToPx(8));

        a.recycle();

        initObjects();
        initSizes();
    }

    private String[] getMeasureNumbers() {
        String[] strings = new String[mBigSliceCount + 1];
        for (int i = 0; i <= mBigSliceCount; i++) {
            if (i == 0) {
                strings[i] = String.valueOf(mMinValue);
            } else if (i == mBigSliceCount) {
                strings[i] = String.valueOf(mMaxValue);
            } else {
                strings[i] = String.valueOf(((mMaxValue - mMinValue) / mBigSliceCount) * i);
            }
        }
        return strings;
    }

    private void initObjects() {
        mPaintArc = new Paint();
        mPaintArc.setAntiAlias(true);
        mPaintArc.setColor(mBgArcColor);
        mPaintArc.setStyle(Paint.Style.STROKE);
        mPaintArc.setStrokeCap(Paint.Cap.ROUND);

        mPaintBackgroundText = new Paint();
        mPaintBackgroundText.setAntiAlias(true);
        mPaintBackgroundText.setColor(mTextColor);
        mPaintBackgroundText.setStyle(Paint.Style.STROKE);

        mPaintStripe = new Paint();
        mPaintStripe.setAntiAlias(true);
        mPaintStripe.setStyle(Paint.Style.STROKE);
        mPaintStripe.setStrokeWidth(mStripeWidth);

        mRectMeasures = new Rect();
        mRectRealText = new Rect();

        mPaintValue = new Paint();
        mPaintValue.setAntiAlias(true);
        mPaintValue.setColor(mTextColor);
        mPaintValue.setStyle(Paint.Style.STROKE);
        mPaintValue.setTextAlign(Paint.Align.CENTER);
        mPaintValue.setTextSize(mHeaderTextSize);
        mPaintValue.getTextBounds(trimFloat(mRealTimeValue), 0, trimFloat(mRealTimeValue).length(), mRectRealText);

        mHandler = new ViewHandler();
    }

    private void initSizes() {
        if (mSweepAngle > 360)
            throw new IllegalArgumentException("sweepAngle must less than 360 degree");

        mSmallSliceRadius = mRadius - dpToPx(8);
        mBigSliceRadius = mSmallSliceRadius - dpToPx(4);
        mNumMeaRadius = mBigSliceRadius - dpToPx(3);

        mBigSliceAngle = mSweepAngle / (float) mBigSliceCount;
        mGraduations = getMeasureNumbers();

        switch (mModeType) {
            case -1:
                mStripeMode = StripeMode.NONE;
                break;
            case 0:
                mStripeMode = StripeMode.DOT_BG;
                break;
            case 1:
                mStripeMode = StripeMode.RECT_BG;
                break;
        }

        switch (mStripeMode) {
            case DOT_BG:
                mArcWidth = dpToPx(8);
                break;
            case RECT_BG:
                mArcWidth = dpToPx(2);
                break;
            default:
                break;
        }
        int totalRadius = mRadius;

        mCenterX = mCenterY = 0.0f;
        if (mStartAngle <= 180 && mStartAngle + mSweepAngle >= 180) {
            mViewWidth = totalRadius * 2 + getPaddingLeft() + getPaddingRight() + dpToPx(2) * 2;
        } else {
            float[] point1 = getCoordinatePoint(totalRadius, mStartAngle);
            float[] point2 = getCoordinatePoint(totalRadius, mStartAngle + mSweepAngle);
            float max = Math.max(Math.abs(point1[0]), Math.abs(point2[0]));
            mViewWidth = (int) (max * 2 + getPaddingLeft() + getPaddingRight() + dpToPx(2) * 2);
        }
        if ((mStartAngle <= 90 && mStartAngle + mSweepAngle >= 90) ||
                (mStartAngle <= 270 && mStartAngle + mSweepAngle >= 270)) {
            mViewHeight = totalRadius * 2 + getPaddingTop() + getPaddingBottom() + dpToPx(2) * 2;
        } else {
            float[] point1 = getCoordinatePoint(totalRadius, mStartAngle);
            float[] point2 = getCoordinatePoint(totalRadius, mStartAngle + mSweepAngle);
            float max = Math.max(Math.abs(point1[1]), Math.abs(point2[1]));
            mViewHeight = (int) (max * 2 + getPaddingTop() + getPaddingBottom() + dpToPx(2) * 2);
        }

        mCenterX = mViewWidth / 2.0f;
        mCenterY = mViewHeight / 2.0f;

        int bgOffset = dpToPx(12);// Get some space for dots
        mBackgroundRectArc = new RectF(mCenterX - mRadius + bgOffset, mCenterY - mRadius + bgOffset,
                mCenterX + mRadius - bgOffset, mCenterY + mRadius - bgOffset);

        int r = 0;
        if (mStripeWidth > 0) {
            mRectStripe = new RectF(mCenterX - r, mCenterY - r, mCenterX + r, mCenterY + r);
        }

        initAngle = getAngleFromResult(mRealTimeValue);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mViewWidth = widthSize;
        } else {
            if (widthMode == MeasureSpec.AT_MOST)
                mViewWidth = Math.min(mViewWidth, widthSize);
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mViewHeight = heightSize;
        } else {
            int totalRadius = mRadius;
            if (mStartAngle >= 180 && mStartAngle + mSweepAngle <= 360) {
                mViewHeight = totalRadius + mCircleRadius + dpToPx(2) +
                        getPaddingTop() + getPaddingBottom() + mRectRealText.height();
            } else {
                float[] point1 = getCoordinatePoint(totalRadius, mStartAngle);
                float[] point2 = getCoordinatePoint(totalRadius, mStartAngle + mSweepAngle);
                float maxY = Math.max(Math.abs(point1[1]) - mCenterY, Math.abs(point2[1]) - mCenterY);
                float f = mCircleRadius + dpToPx(2) + mRectRealText.height();
                float max = Math.max(maxY, f);
                mViewHeight = (int) (max + totalRadius + getPaddingTop() + getPaddingBottom() + dpToPx(2) * 2);
            }
            if (widthMode == MeasureSpec.AT_MOST)
                mViewHeight = Math.min(mViewHeight, widthSize);
        }

        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBgColor != 0) canvas.drawColor(mBgColor);

//        drawStripe(canvas);
        drawBackground(canvas);
        drawText(canvas);
        drawProgressArc(canvas);
    }

    /**
     * Background includes arc, dots and dials text
     */
    private void drawBackground(Canvas canvas) {
        mPaintArc.setStrokeWidth(dpToPx(2));
        for (int i = 0; i <= mBigSliceCount; i++) {
            float angle = i * mBigSliceAngle + mStartAngle;
            if (StripeMode.DOT_BG.equals(mStripeMode)) {
                // Draw outer dots here
                int dotsOffset = dpToPx(2);
                float[] dot = getCoordinatePoint(mRadius - dotsOffset, angle);
                mPaintArc.setColor(mBgOuterElementColor);
                if (i % 5 == 0) {
                    mPaintArc.setStrokeWidth(dpToPx(6));
                } else {
                    mPaintArc.setStrokeWidth(dpToPx(3));
                }
                canvas.drawPoint(dot[0], dot[1], mPaintArc);
            } else if (StripeMode.RECT_BG.equals(mStripeMode)) {
                // Draw outer short lines
                float[] point1 = getCoordinatePoint(mRadius, angle);
                if (i % 5 != 0) {
                    point1 = getCoordinatePoint(mRadius - dpToPx(3), angle);
                }
                float[] point2 = getCoordinatePoint(mBigSliceRadius + dpToPx(6), angle);
                mPaintArc.setColor(mBgOuterElementColor);// Arc color
                canvas.drawLine(point1[0], point1[1], point2[0], point2[1], mPaintArc);
            }

            // Draw text in dials
            mPaintBackgroundText.setTextSize(mMeasureTextSize);
            String number = mGraduations[i];
            mPaintBackgroundText.getTextBounds(number, 0, number.length(), mRectMeasures);
            if (angle % 360 > 135 && angle % 360 < 225) {
                mPaintBackgroundText.setTextAlign(Paint.Align.LEFT);
            } else if ((angle % 360 >= 0 && angle % 360 < 45) || (angle % 360 > 315 && angle % 360 <= 360)) {
                mPaintBackgroundText.setTextAlign(Paint.Align.RIGHT);
            } else {
                mPaintBackgroundText.setTextAlign(Paint.Align.CENTER);
            }
            float[] numberPoint = getCoordinatePoint(((int) mBackgroundRectArc.width() / 2 - mMeasureTextSize), angle);
            if (i == 0 || i == mBigSliceCount) {
                canvas.drawText(number, numberPoint[0], numberPoint[1] + (mRectMeasures.height() / 2), mPaintBackgroundText);
            } else {
                canvas.drawText(number, numberPoint[0], numberPoint[1] + mRectMeasures.height(), mPaintBackgroundText);
            }
        }
        drawBackgroundArc(canvas);
    }

    private void drawBackgroundArc(Canvas canvas) {
        mPaintArc.setStrokeWidth(mArcWidth);// Set arc width, don't be too thin
        if (mStripeMode == StripeMode.DOT_BG) {
            mPaintArc.setColor(mBgArcColor);
            canvas.drawArc(mBackgroundRectArc, mStartAngle, mSweepAngle, false, mPaintArc);
        } else if (StripeMode.RECT_BG.equals(mStripeMode)) {
            mPaintArc.setColor(mBgArcColor);
            canvas.drawArc(mBackgroundRectArc, mStartAngle, mSweepAngle, false, mPaintArc);
        } else if (StripeMode.NONE.equals(mStripeMode)) {
            mPaintArc.setColor(mBgArcColor);
            mPaintArc.setStrokeWidth((float) (mArcWidth / 2.0));// back arc is thinner
            canvas.drawArc(mBackgroundRectArc, mStartAngle, mSweepAngle, false, mPaintArc);
        }
    }

    private void drawProgressArc(Canvas canvas) {
        switch (mStripeMode) {
            case NONE:
                mPaintArc.setStrokeWidth(mArcWidth);
                mPaintArc.setColor(mProgressArcColor);
                canvas.drawArc(mBackgroundRectArc, mStartAngle, (mRealTimeValue / mMaxValue * 180), false, mPaintArc);
                break;
            case DOT_BG:
                mPaintArc.setStrokeWidth(mArcWidth);
                mPaintArc.setColor(mProgressArcColor);
                canvas.drawArc(mBackgroundRectArc, mStartAngle, (mRealTimeValue / mMaxValue * 180), false, mPaintArc);
                break;
            case RECT_BG:
                mPaintArc.setStrokeWidth((float) (mArcWidth * 2));
                mPaintArc.setColor(mProgressArcColor);
                canvas.drawArc(mBackgroundRectArc, mStartAngle, (mRealTimeValue / mMaxValue * 180), false, mPaintArc);
                if (mRealTimeValue == 0) {
                    return;// If 0, don't draw bg
                }
                mPaintArc.setStrokeWidth((float) (mArcWidth * 1.5));
                drawRealTimeRectBg(canvas);
                break;
        }
    }

    /**
     * Only deal with RectMode background rect
     */
    private void drawRealTimeRectBg(Canvas canvas) {
        if (!mStripeMode.equals(StripeMode.RECT_BG)) {
            return;
        }
        int level = (int) (mRealTimeValue / 10);
        for (int i = 0; i <= level; i++) {
            float angle = i * mBigSliceAngle + mStartAngle;
            float[] point1 = getCoordinatePoint(mRadius, angle);
            if (i % 5 != 0) {
                // Shorter line
                point1 = getCoordinatePoint(mRadius - dpToPx(3), angle);
            }
            float[] point2 = getCoordinatePoint(mBigSliceRadius + dpToPx(6), angle);
            canvas.drawLine(point1[0], point1[1], point2[0], point2[1], mPaintArc);
        }
    }

    /**
     * Draw real time text and header text
     */
    private void drawText(Canvas canvas) {
        mPaintValue.setTextSize(mHeaderTextSize);
        switch (mStripeMode) {
            case NONE:
            case DOT_BG:
                canvas.drawText(mHeaderTitle, mCenterX, mCenterY + dpToPx(2), mPaintValue);
                mPaintValue.setTextSize(mHeaderTextSize + spToPx(10));
                canvas.drawText(trimFloat(mRealTimeValue), mCenterX, mCenterY - dpToPx(20), mPaintValue);
                break;
            case RECT_BG:
                mPaintValue.setTextSize(mHeaderTextSize + spToPx(30));
                canvas.drawText(trimFloat(mRealTimeValue), mCenterX, mCenterY, mPaintValue);
                break;
        }
    }

    /**
     * 依圆心坐标，半径，扇形角度，计算出扇形终射线与圆弧交叉点的xy坐标
     */
    public float[] getCoordinatePoint(int radius, float cirAngle) {
        float[] point = new float[2];

        double arcAngle = Math.toRadians(cirAngle); //将角度转换为弧度
        if (cirAngle < 90) {
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (cirAngle == 90) {
            point[0] = mCenterX;
            point[1] = mCenterY + radius;
        } else if (cirAngle > 90 && cirAngle < 180) {
            arcAngle = Math.PI * (180 - cirAngle) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (cirAngle == 180) {
            point[0] = mCenterX - radius;
            point[1] = mCenterY;
        } else if (cirAngle > 180 && cirAngle < 270) {
            arcAngle = Math.PI * (cirAngle - 180) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        } else if (cirAngle == 270) {
            point[0] = mCenterX;
            point[1] = mCenterY - radius;
        } else {
            arcAngle = Math.PI * (360 - cirAngle) / 180.0;
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        }

        return point;
    }

    /**
     * 通过数值得到角度位置
     */
    private float getAngleFromResult(float result) {
        if (result > mMaxValue)
            return mMaxValue;
        return mSweepAngle * (result - mMinValue) / (mMaxValue - mMinValue) + mStartAngle;
    }

    /**
     * float类型如果小数点后为零则显示整数否则保留
     */
    public static String trimFloat(float value) {
        if (Math.round(value) - value == 0) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int radius) {
        mRadius = dpToPx(radius);
        initSizes();
        invalidate();
    }

    public int getStartAngle() {
        return mStartAngle;
    }

    public void setStartAngle(int startAngle) {
        mStartAngle = startAngle;
        initSizes();
        invalidate();
    }

    public int getSweepAngle() {
        return mSweepAngle;
    }

    public void setSweepAngle(int sweepAngle) {
        mSweepAngle = sweepAngle;
        initSizes();
        invalidate();
    }

    public int getBigSliceCount() {
        return mBigSliceCount;
    }

    public void setBigSliceCount(int bigSliceCount) {
        mBigSliceCount = bigSliceCount;
        initSizes();
        invalidate();
    }

    public int getSliceCountInOneBigSlice() {
        return mSliceCountInOneBigSlice;
    }

    public void setSliceCountInOneBigSlice(int sliceCountInOneBigSlice) {
        mSliceCountInOneBigSlice = sliceCountInOneBigSlice;
        initSizes();
        invalidate();
    }

    public int getArcColor() {
        return mBgArcColor;
    }

    public void setArcColor(int arcColor) {
        mBgArcColor = arcColor;
        mPaintArc.setColor(arcColor);
        invalidate();
    }

    public int getMeasureTextSize() {
        return mMeasureTextSize;
    }

    public void setMeasureTextSize(int measureTextSize) {
        mMeasureTextSize = spToPx(measureTextSize);
        initSizes();
        invalidate();
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        mPaintBackgroundText.setColor(textColor);
        mPaintValue.setColor(textColor);
        invalidate();
    }

    public String getHeaderTitle() {
        return mHeaderTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.mHeaderTitle = headerTitle;
        invalidate();
    }

    public int getHeaderTextSize() {
        return mHeaderTextSize;
    }

    public void setHeaderTextSize(int headerTextSize) {
        mHeaderTextSize = spToPx(headerTextSize);
        initSizes();
        invalidate();
    }

    public void setHeaderRadius(int headerRadius) {
        initSizes();
        invalidate();
    }

    public void setPointerRadius(int pointerRadius) {
        initSizes();
        invalidate();
    }

    public int getCircleRadius() {
        return mCircleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        mCircleRadius = dpToPx(circleRadius);
        initSizes();
        invalidate();
    }

    public int getMinValue() {
        return mMinValue;
    }

    public void setMinValue(int minValue) {
        mMinValue = minValue;
        initSizes();
        invalidate();
    }

    public int getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(int maxValue) {
        mMaxValue = maxValue;
        initSizes();
        invalidate();
    }

    public float getRealTimeValue() {
        return mRealTimeValue;
    }

    public void setRealTimeValue(float realTimeValue) {
        mRealTimeValue = realTimeValue;
        initSizes();
        if (!mAnimEnable)
            invalidate();
    }

    public void setRealTimeValue(float realTimeValue, boolean animEnable) {
        mHandler.preValue = mRealTimeValue;
        mAnimEnable = animEnable;
        initSizes();
        if (!mAnimEnable) {
            invalidate();
        } else {
            mRealTimeValue = realTimeValue;
            mHandler.endValue = realTimeValue;
            mHandler.deltaValue = Math.abs(mHandler.endValue - mHandler.preValue);
            mHandler.sendEmptyMessage(0);
        }
    }

    public void setRealTimeValue(float realTimeValue, boolean animEnable, long duration) {
        mHandler.preValue = mRealTimeValue;
        mAnimEnable = animEnable;
        initSizes();
        if (!mAnimEnable) {
            invalidate();
        } else {
            this.duration = duration;
            mRealTimeValue = realTimeValue;
            mHandler.endValue = realTimeValue;
            mHandler.deltaValue = Math.abs(mHandler.endValue - mHandler.preValue);
            mHandler.sendEmptyMessage(0);
        }
    }

    public int getStripeWidth() {
        return mStripeWidth;
    }

    public void setStripeWidth(int stripeWidth) {
        mStripeWidth = dpToPx(stripeWidth);
        initSizes();
        invalidate();
    }

    public StripeMode getStripeMode() {
        return mStripeMode;
    }

    public void setStripeMode(StripeMode mStripeMode) {
        this.mStripeMode = mStripeMode;
        switch (mStripeMode) {
            case DOT_BG:
                mModeType = 0;
                break;
        }
        initSizes();
        invalidate();
    }

    public int getBigSliceRadius() {
        return mBigSliceRadius;
    }

    public void setBigSliceRadius(int bigSliceRadius) {
        mBigSliceRadius = dpToPx(bigSliceRadius);
        initSizes();
        invalidate();
    }

    public int getSmallSliceRadius() {
        return mSmallSliceRadius;
    }

    public void setSmallSliceRadius(int smallSliceRadius) {
        mSmallSliceRadius = dpToPx(smallSliceRadius);
        initSizes();
        invalidate();
    }

    public int getNumMeaRadius() {
        return mNumMeaRadius;
    }

    public void setNumMeaRadius(int numMeaRadius) {
        mNumMeaRadius = dpToPx(numMeaRadius);
        initSizes();
        invalidate();
    }

    public enum StripeMode {
        NONE, DOT_BG, RECT_BG
    }

    public int getBgColor() {
        return mBgColor;
    }

    public void setBgColor(int mBgColor) {
        this.mBgColor = mBgColor;
        invalidate();
    }

    public boolean isAnimEnable() {
        return mAnimEnable;
    }

    public void setAnimEnable(boolean animEnable) {
        mAnimEnable = animEnable;
        if (mAnimEnable) {
            mHandler.endValue = mRealTimeValue;
            mHandler.sendEmptyMessage(0);
        }
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private int spToPx(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    private class ViewHandler extends Handler {

        float preValue;
        float endValue;
        float deltaValue;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (preValue > endValue) {
                    preValue -= 1;
                } else if (preValue < endValue) {
                    preValue += 1;
                }
                if (Math.abs(preValue - endValue) > 1) {
                    mRealTimeValue = preValue;
                    long t = (long) (duration / deltaValue);
                    sendEmptyMessageDelayed(0, t);
                } else {
                    mRealTimeValue = endValue;
                }
                initAngle = getAngleFromResult(mRealTimeValue);
                invalidate();
            }
        }
    }

}