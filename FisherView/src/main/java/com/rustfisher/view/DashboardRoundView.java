package com.rustfisher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆形的仪表盘
 * 内容居中显示
 * 假设整个View的宽高是250，中心圆直径是104，以此为基准按比例计算各部分的尺寸
 * Created on 2019-3-20
 */
public class DashboardRoundView extends View {
    private static final String TAG = "DRV";

    private String mHeaderTitle = "";
    private float mRealTimeValue; // 实时值
    private int mBgColor = Color.TRANSPARENT; // 整个View的背景色

    private int mViewWidth;     // 控件宽度
    private int mViewHeight;    // 控件高度
    private Point mCenterPoint; // 控件中心点

    private Handler mHandler = new Handler(Looper.getMainLooper());
    final int animDuration = 450; // 动画默认时长

    static final int mainColorOrange = Color.parseColor("#ff5722"); // 深橙色
    static final int mainColorYellow = Color.parseColor("#ff8f22"); // 有点黄
    static final int bgTextColor = Color.WHITE;

    Path centerCirclePath = new Path(); // 中心圆
    Paint centerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    final int[] centerCircleColors = {Color.TRANSPARENT, Color.TRANSPARENT, mainColorOrange};
    final float[] centerCircleColorStops = {0, 0.48f, 1};

    Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    final int circleBgColor = Color.argb(127, 255, 255, 255); // 环的颜色
    RectF c1 = new RectF(); // 用于画背景环
    final float bgTransparentLineWid = 2; // 背景中透明线条的宽度

    Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Rect textTmpRect = new Rect();
    int headerTextColor = Color.WHITE;
    RectF d1 = new RectF();

    private Paint mPaintValue = new Paint(Paint.ANTI_ALIAS_FLAG);
    float dataRingOuterR = 0; // 数据扇形的外半径
    float dataRingInnerR = 0;
    final int[] dataRingColors = {Color.TRANSPARENT, Color.parseColor("#80f65212"), Color.parseColor("#f65212")};
    final float[] dataRingStops = {0, 0.48f, 1};

    private Typeface typeface1;

    public DashboardRoundView(Context context) {
        this(context, null);
    }

    public DashboardRoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashboardRoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initObjects();
    }

    private void initObjects() {
        mCenterPoint = new Point();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewHeight = h;
        mViewWidth = w;
        mCenterPoint.set(mViewWidth / 2, mViewHeight / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(mBgColor);
        drawBackground(canvas);
        drawDataText(canvas);
        drawDataArc(canvas);
    }

    private void drawBackground(Canvas canvas) {
        final float centerCircleRadius = mViewHeight / 2.4f / 2; // 最中间的圆的半径
        centerCirclePath.reset();
        centerCirclePath.addCircle(mCenterPoint.x, mCenterPoint.y, centerCircleRadius, Path.Direction.CW);
        centerCirclePaint.setShader(new RadialGradient(mCenterPoint.x, mCenterPoint.y, centerCircleRadius, centerCircleColors, centerCircleColorStops, Shader.TileMode.CLAMP));
        canvas.drawPath(centerCirclePath, centerCirclePaint);

        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setColor(circleBgColor);

        final float ring1Wid = centerCircleRadius * 9 / 54f; // 最里面的环 计算根据UI设计图的比例
        final float r1 = centerCircleRadius + ring1Wid / 2 + bgTransparentLineWid;
        c1.bottom = mCenterPoint.y + r1;
        c1.top = mCenterPoint.y - r1;
        c1.left = mCenterPoint.x - r1;
        c1.right = mCenterPoint.x + r1;
        bgPaint.setStrokeWidth(ring1Wid);
        canvas.drawOval(c1, bgPaint);

        final float ring2Wid = ring1Wid * 1.1f; // 第二个环
        final float r2 = centerCircleRadius + ring1Wid + ring2Wid / 2 + bgTransparentLineWid * 2;
        c1.bottom = mCenterPoint.y + r2;
        c1.top = mCenterPoint.y - r2;
        c1.left = mCenterPoint.x - r2;
        c1.right = mCenterPoint.x + r2;
        bgPaint.setStrokeWidth(ring2Wid);
        canvas.drawOval(c1, bgPaint);

        final float numberRingWid = ring1Wid * 2.5f; // 数字显示区

        // 画第三个环 - 宽度和环1一样
        final float r3 = centerCircleRadius + ring1Wid + ring2Wid + numberRingWid + ring1Wid / 2 + bgTransparentLineWid * 2;
        c1.bottom = mCenterPoint.y + r3;
        c1.top = mCenterPoint.y - r3;
        c1.left = mCenterPoint.x - r3;
        c1.right = mCenterPoint.x + r3;
        bgPaint.setStrokeWidth(ring1Wid);
        canvas.drawOval(c1, bgPaint);

        // 第四个环，一节一节的
        final float r4 = centerCircleRadius + ring1Wid * 2 + ring2Wid + numberRingWid + ring1Wid / 2 + bgTransparentLineWid * 3;
        c1.bottom = mCenterPoint.y + r4;
        c1.top = mCenterPoint.y - r4;
        c1.left = mCenterPoint.x - r4;
        c1.right = mCenterPoint.x + r4;
        for (int i = 0; i < 24; i++) {
            canvas.drawArc(c1, 15 * i + 0.65f, 15 - 0.65f, false, bgPaint);
        }

        // 第五个环，目前最外层的环
        final float r5 = centerCircleRadius + ring1Wid * 3 + ring2Wid + numberRingWid + ring1Wid / 2 + bgTransparentLineWid * 4;
        c1.bottom = mCenterPoint.y + r5;
        c1.top = mCenterPoint.y - r5;
        c1.left = mCenterPoint.x - r5;
        c1.right = mCenterPoint.x + r5;
        canvas.drawOval(c1, bgPaint);

        // 刻度 - 文字
        textPaint.setTypeface(typeface1);
        final float textR = (r2 + ring1Wid + numberRingWid) * 0.86f;
        textPaint.setColor(bgTextColor);
        textPaint.setTextSize(numberRingWid / 2.5f);
        final int startNum = 0;
        final int stepNum = 20;
        final int startAngleDegree = 240;
        for (int i = 0; i <= 15; i++) {
            String text = String.valueOf(startNum + i * stepNum);
            textPaint.getTextBounds(text, 0, text.length(), textTmpRect);
            textPaint.setTextAlign(Paint.Align.LEFT);
            float curDegree = startAngleDegree + i * 15;
            float x = (float) (textR * Math.sin(Math.toRadians(curDegree)) + mCenterPoint.x);
            float y = (float) (mCenterPoint.y - textR * Math.cos(Math.toRadians(curDegree)));
            x -= textTmpRect.width() / 2f;
            y += textTmpRect.height() / 2f;
            canvas.drawText(String.valueOf(startNum + i * stepNum), x, y, textPaint);
        }

        dataRingOuterR = r5 - ring1Wid / 2f;
        dataRingInnerR = centerCircleRadius + ring1Wid + bgTransparentLineWid;
    }

    private void drawDataText(Canvas canvas) {
        if (typeface1 != null) {
            mPaintValue.setTypeface(typeface1);
        }
        mPaintValue.setShader(null);
        mPaintValue.setStrokeWidth(2);
        mPaintValue.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintValue.setTextSize(mViewHeight * 0.07f);
        mPaintValue.setColor(headerTextColor);
        mPaintValue.getTextBounds(mHeaderTitle, 0, mHeaderTitle.length(), textTmpRect);
        canvas.drawText(mHeaderTitle, mCenterPoint.x - textTmpRect.width() / 2f, mCenterPoint.y - textTmpRect.height(), mPaintValue);

        String centerStr = String.valueOf((int) mRealTimeValue);
        mPaintValue.setTextSize(0.12f * mViewHeight);
        mPaintValue.getTextBounds(centerStr, 0, centerStr.length(), textTmpRect);
        canvas.drawText(centerStr, mCenterPoint.x - textTmpRect.width() / 2f, mCenterPoint.y + textTmpRect.height(), mPaintValue);
    }

    // 绘制圆环
    private void drawDataArc(Canvas canvas) {
        float ringGap = dataRingOuterR - dataRingInnerR;
        float ringCenterR = (dataRingInnerR + dataRingOuterR) / 2f;

        d1.left = mCenterPoint.x - ringCenterR;
        d1.top = mCenterPoint.y - ringCenterR;
        d1.right = mCenterPoint.x + ringCenterR;
        d1.bottom = mCenterPoint.y + ringCenterR;
        float sweep = mRealTimeValue / 480f * 360;
        mPaintValue.setStyle(Paint.Style.STROKE);
        mPaintValue.setColor(mainColorOrange);
        mPaintValue.setStrokeWidth(ringGap);
        mPaintValue.setShader(new RadialGradient(mCenterPoint.x, mCenterPoint.y, dataRingOuterR, dataRingColors, dataRingStops, Shader.TileMode.CLAMP));
        canvas.drawArc(d1, 150, sweep, false, mPaintValue);
    }

    // 更新显示的数据
    public void updateData(final float data, boolean animEnable) {
        mHandler.removeCallbacksAndMessages(null);
        if (animEnable) {
            int stepCount = 15; // 分成那么多步
            int timeStep = animDuration / stepCount; // 时间间隔
            final float preVal = mRealTimeValue;
            final float valueStep = (data - preVal) / stepCount;
            for (int i = 0; i < stepCount; i++) {
                final float curValue = preVal + valueStep * i;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRealTimeValue = curValue;
                        invalidate();
                    }
                }, i * timeStep);
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRealTimeValue = data;
                    invalidate();
                }
            }, animDuration);
        } else {
            mRealTimeValue = data;
            invalidate();
        }
    }

    public void setHeaderTitle(String headerTitle) {
        this.mHeaderTitle = headerTitle;
        invalidate();
    }

    public void setTypeface1(Typeface typeface1) {
        this.typeface1 = typeface1;
    }

    public void destroy() {
        mHandler.removeCallbacksAndMessages(null);
    }

}