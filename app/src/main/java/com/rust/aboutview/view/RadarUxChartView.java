package com.rust.aboutview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Locale;

/**
 * 雷达图
 * 正方形
 * 带有交互功能
 * Created on 2019-3-26
 */
public class RadarUxChartView extends View {
    private static final String TAG = "RadarUxChart";
    private static final float COS30 = (float) Math.cos(Math.toRadians(30));
    private static final float SIN30 = (float) Math.sin(Math.toRadians(30));

    private int mViewHeight;
    private int mViewWidth;
    private Point centerPoint = new Point(); // 中心点
    private Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint dataPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    final DashPathEffect circleEffect = new DashPathEffect(new float[]{5, 2}, 0);
    final Path dashLinePath = new Path();
    final Path dataPath = new Path();
    RectF circle = new RectF();
    private int mainColor = Color.parseColor("#ff8f22");
    private int dataFillColor = Color.parseColor("#8cff8f22");
    private boolean ableDrag = false; // 能否运行拖动顶点

    private float r1 = 10; // 数据最小值所在的位置
    private float r2 = 20;
    private float r3 = 30; // 数据最大值所在的位置

    private float dataLimitMax = 1.0f; // 数据最大值 设置的数据不能大于这个值
    private float dataLimitMin = 0.5f; // 数据最小值 设置的数据不能小于这个值

    private float actionDownX; // 手指按下的View内x坐标
    private float actionDownY;

    private Dot dm = new Dot("中间");
    private Dot dl = new Dot("左边");
    private Dot dr = new Dot("右边");

    private Dot firstTouchDot = null; // 选中的点

    public RadarUxChartView(Context context) {
        super(context);
    }

    public RadarUxChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RadarUxChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewHeight = h;
        mViewWidth = w;
        centerPoint.x = w / 2;
        centerPoint.y = h / 2;

        r3 = (mViewWidth - 30) * 0.5f;
        r2 = r3 * 0.75f;
        r1 = 2 * r2 - r3;

        calMiddleDot();
        calLeftDot();
        calRightDot();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawData(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!ableDrag) return false;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                actionDownX = event.getX();
                actionDownY = event.getY();
                findControlDot(actionDownX, actionDownY);
                break;
            case MotionEvent.ACTION_MOVE:
                float mX = event.getX();
                float mY = event.getY();
//                Log.d(TAG, "onTouchEvent: MOVE " + mX + ", " + mY);
                if (firstTouchDot == null) {
                    return false;
                }
                if (firstTouchDot == dm) {
                    setDataM((centerPoint.y - mY) / r3);
                    invalidate();
                } else if (firstTouchDot == dl) {
                    setDataL((centerPoint.x - mX) * COS30 / r3);
                    invalidate();
                } else if (firstTouchDot == dr) {
                    setDataR((mX - centerPoint.x) * COS30 / r3);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (firstTouchDot != null) {
                    if (listener != null) {
                        listener.onDateSelected(dm.data, dl.data, dr.data);
                    }
                    firstTouchDot = null;
                }
                float upX = event.getX();
                float upY = event.getY();
                if (actionDownY == upY && actionDownX == upX) {
                    performClick();
                }
                break;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    // 找到点中的那个点
    private void findControlDot(float x, float y) {
        if (dm.touchMe(x, y)) {
            firstTouchDot = dm;
        } else if (dl.touchMe(x, y)) {
            firstTouchDot = dl;
        } else if (dr.touchMe(x, y)) {
            firstTouchDot = dr;
        } else {
            firstTouchDot = null;
        }
//        Log.d(TAG, "findControlDot: " + firstTouchDot);
    }

    private void drawData(Canvas canvas) {
        dataPaint.setColor(mainColor);
        dataPaint.setStyle(Paint.Style.STROKE);
        dataPaint.setStrokeWidth(7);
        dataPath.reset();
        dataPath.moveTo(dm.x, dm.y);
        dataPath.lineTo(dl.x, dl.y);
        dataPath.lineTo(dr.x, dr.y);
        dataPath.close();
        canvas.drawPath(dataPath, dataPaint);
        dataPaint.setStyle(Paint.Style.FILL);
        dataPaint.setColor(dataFillColor);
        canvas.drawPath(dataPath, dataPaint);
        if (ableDrag) {
            drawDot(canvas, dl);
            drawDot(canvas, dm);
            drawDot(canvas, dr);
        }
    }

    private void drawDot(Canvas canvas, Dot dot) {
        dataPaint.setColor(mainColor);
        dataPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(dot.x, dot.y, dataPaint.getStrokeWidth() * 1.5f, dataPaint);
        dataPaint.setColor(Color.WHITE);
        dataPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(dot.x, dot.y, dataPaint.getStrokeWidth() * 1.5f, dataPaint);
    }

    private void drawBg(Canvas canvas) {
        bgPaint.setColor(mainColor);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(4);
        bgPaint.setPathEffect(circleEffect);
        drawBgCircle(r1, canvas);
        drawBgCircle(r2, canvas);
        drawBgCircle(r3, canvas);
        dashLinePath.reset();
        dashLinePath.moveTo(centerPoint.x, centerPoint.y);
        dashLinePath.lineTo(centerPoint.x, centerPoint.y - r3);
        canvas.drawPath(dashLinePath, bgPaint);
        float dx = r3 * COS30;
        float dy = r3 * SIN30;
        dashLinePath.reset();
        dashLinePath.moveTo(centerPoint.x, centerPoint.y);
        dashLinePath.lineTo(centerPoint.x - dx, centerPoint.y + dy);
        canvas.drawPath(dashLinePath, bgPaint);
        dashLinePath.reset();
        dashLinePath.moveTo(centerPoint.x, centerPoint.y);
        dashLinePath.lineTo(centerPoint.x + dx, centerPoint.y + dy);
        canvas.drawPath(dashLinePath, bgPaint);
    }

    private void drawBgCircle(float r, Canvas canvas) {
        circle = new RectF(centerPoint.x - r, centerPoint.y - r, centerPoint.y + r, centerPoint.x + r);
        canvas.drawOval(circle, bgPaint);
    }

    public float getDataM() {
        return dm.data;
    }

    // 设置中间那个数值
    public void setDataM(float d) {
        dm.data = Math.max(d, dataLimitMin);
        dm.data = Math.min(dm.data, dataLimitMax);
        calMiddleDot();
        invalidate();
    }

    public float getDataL() {
        return dl.data;
    }

    // 设置左边那个数值
    public void setDataL(float d) {
        dl.data = Math.max(d, dataLimitMin);
        dl.data = Math.min(dl.data, dataLimitMax);
        calLeftDot();
        invalidate();
    }

    public float getDataR() {
        return dr.data;
    }

    // 设置右边那个数值
    public void setDataR(float d) {
        dr.data = Math.max(d, dataLimitMin);
        dr.data = Math.min(dr.data, dataLimitMax);
        calRightDot();
        invalidate();
    }

    public void setAbleDrag(boolean ableDrag) {
        this.ableDrag = ableDrag;
        invalidate();
    }

    public boolean isAbleDrag() {
        return ableDrag;
    }

    private void calMiddleDot() {
        dm.x = centerPoint.x;
        dm.y = centerPoint.y - dm.data * r3;
    }

    private void calLeftDot() {
        dl.x = centerPoint.x - (dl.data * r3 * COS30);
        dl.y = centerPoint.y + (dl.data * r3 * SIN30);
    }

    private void calRightDot() {
        dr.x = centerPoint.x + (dr.data * r3 * COS30);
        dr.y = centerPoint.y + (dr.data * r3 * SIN30);
    }

    // 代表数据顶点
    public static class Dot {
        String name;
        float x;
        float y;
        float r = 25; // 触摸的圆点半径
        float data = 0.75f;

        Dot(String name) {
            this.name = name;
        }

        void setData(float d) {
            data = d;
        }

        public void setX(float x) {
            this.x = x;
        }

        public void setY(float y) {
            this.y = y;
        }

        boolean touchMe(float inputX, float inputY) {
            return Math.abs(x - inputX) <= r && Math.abs(y - inputY) <= r;
        }

        @Override
        public String toString() {
            return String.format(Locale.CHINA, "dot: {name: %s, x: %.2f, y: %.2f, data: %.2f}",
                    name, x, y, data);
        }
    }

    private UxListener listener;

    public void setListener(UxListener listener) {
        this.listener = listener;
    }

    public interface UxListener {
        void onDateSelected(float mData, float lData, float rData); // 拖动选定数值
    }

}
