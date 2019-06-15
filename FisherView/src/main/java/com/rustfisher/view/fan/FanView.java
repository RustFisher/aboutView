
package com.rustfisher.view.fan;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.rustfisher.view.FSUtils;
import com.rustfisher.view.R;

/**
 * Fan, windmill generator
 * Created by Rust Fisher on 2017/7/20.
 */
public class FanView extends View {
//    private static final String TAG = "rustApp";
    private Paint mPaint = new Paint(); // main paint
    float paintStrokeWid = 2.4f;
    private float propBotWid;     // Pixel - Wid of prop bottom
    private float propTopWid;     // Pixel - Wid of prop top
    float dotHeightRatio = 0.6f;  // Ratio, dot in the view
    private int fanDegree = 60;
    private int propColor;

    Path propPath = new Path();
    Path fanPath = new Path();

    public FanView(Context context) {
        this(context, null);
    }

    public FanView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FanView, defStyleAttr, 0);
        propBotWid = a.getDimensionPixelSize(R.styleable.FanView_propWid, (int) FSUtils.dp2px(3.5f, this));
        propColor = a.getColor(R.styleable.FanView_propColor, Color.BLACK);
        propTopWid = propBotWid * 0.85f;
        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setFanDegreeNow(int fanDegree) {
        this.fanDegree = fanDegree;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setStrokeWidth(paintStrokeWid);
        mPaint.setColor(propColor);
        float cx = getWidth() / 2.0f; // “发动机”位置的x坐标
        float propHeight = getHeight() * dotHeightRatio; // The height of prop
        float fanLen = propHeight / 2; // The length of fan

        float dotY = getHeight() - propHeight;

        // Draw the prop
        propPath.reset();
        propPath.moveTo(cx, getHeight());
        propPath.lineTo(cx - propBotWid / 2, getHeight()); // Left bottom
        propPath.lineTo(cx - propTopWid / 2, getHeight() - propHeight); // Left top
        propPath.lineTo(cx + propTopWid / 2, getHeight() - propHeight); // Right top
        propPath.lineTo(cx + propBotWid / 2, getHeight()); // Right bottom
        propPath.lineTo(cx, getHeight());
        propPath.close();
        canvas.drawPath(propPath, mPaint);

        // Draw the dot
        canvas.drawCircle(cx, dotY, propTopWid * 0.55f, mPaint);

        // Draw the fans
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                canvas.rotate(fanDegree + 120, cx, dotY);
            } else {
                canvas.rotate(120, cx, dotY);
            }
            drawOriginPaddle(canvas, cx, dotY, fanLen);
        }
        // 画桨叶的时候已经把画布旋转了，如果后续还要绘制其他元素，需要把画布旋转回来
        canvas.rotate(-(fanDegree + 3 * 120), cx, dotY);
//        canvas.drawCircle(dotX + fanLen, dotY, 4, mPaint);
    }

    /**
     * 绘制0角度的一个桨叶
     *
     * @param canvas 画布；一般是已经旋转到了目的角度
     * @param dotX   圆心x
     * @param dotY   圆心y
     * @param fanLen 桨叶的长度
     */
    private void drawOriginPaddle(Canvas canvas, float dotX, float dotY, float fanLen) {
        double turnLen = fanLen * 0.25;
        int degreeSpan = (int) Math.toDegrees(Math.atan(propBotWid / 2 / turnLen)); // 中间的节点
        int startDegreeSpan = (degreeSpan * 2);

        float startLeftX = (float) (dotX + propTopWid / 2 * Math.sin(Math.toRadians(startDegreeSpan)));
        float startLeftY = (float) (dotY + propTopWid / 2 * Math.cos(Math.toRadians(startDegreeSpan)));
        float startRightX = (float) (dotX + propTopWid / 2 * Math.sin(Math.toRadians(-startDegreeSpan)));
        float startRightY = (float) (dotY + propTopWid / 2 * Math.cos(Math.toRadians(-startDegreeSpan)));

        float endY = (dotY + fanLen);
        float leftSpanX = (float) (dotX + turnLen * Math.sin(Math.toRadians(degreeSpan)));
        float leftSpanY = (float) (dotY + turnLen * Math.cos(Math.toRadians(degreeSpan)));
        float rightSpanX = (float) (dotX + turnLen * Math.sin(Math.toRadians(-degreeSpan)));
        float rightSpanY = (float) (dotY + turnLen * Math.cos(Math.toRadians(-degreeSpan)));

        fanPath.reset();
        fanPath.moveTo(startLeftX, startLeftY);
        fanPath.lineTo(leftSpanX, leftSpanY);
        fanPath.lineTo(dotX, endY);
        fanPath.lineTo(rightSpanX, rightSpanY);
        fanPath.lineTo(startRightX, startRightY);
        fanPath.lineTo(dotX, dotY);
        fanPath.close();
        canvas.drawPath(fanPath, mPaint);
    }

    /**
     * @param canvas Canvas
     * @param dotX   The generator
     * @param dotY   The generator
     * @param fanLen Length of fan
     * @param degree Angle 0 ~ 360
     * @deprecated see drawOriginPaddle
     */
    @SuppressWarnings("unused")
    private void drawFan(Canvas canvas, float dotX, float dotY, float fanLen, float degree) {
        double radians = Math.toRadians(degree);
        double turnLen = fanLen * 0.25;

        int degreeSpan = (int) Math.toDegrees(Math.atan(propBotWid / 2 / turnLen));

        int startDegreeSpan = (degreeSpan * 2);
//        int endDegreeSpan = 1;

        float startLeftX = (float) (dotX + propTopWid / 2 * Math.sin(Math.toRadians(degree + startDegreeSpan)));
        float startLeftY = (float) (dotY + propTopWid / 2 * Math.cos(Math.toRadians(degree + startDegreeSpan)));
        float startRightX = (float) (dotX + propTopWid / 2 * Math.sin(Math.toRadians(degree - startDegreeSpan)));
        float startRightY = (float) (dotY + propTopWid / 2 * Math.cos(Math.toRadians(degree - startDegreeSpan)));

//        float endLeftX = (float) (dotX + fanLen * Math.sin(Math.toRadians(degree + endDegreeSpan)));
//        float endLeftY = (float) (dotY + fanLen * Math.cos(Math.toRadians(degree + endDegreeSpan)));
//        float endRightX = (float) (dotX + fanLen * Math.sin(Math.toRadians(degree - endDegreeSpan)));
//        float endRightY = (float) (dotY + fanLen * Math.cos(Math.toRadians(degree - endDegreeSpan)));

        float endX = (float) (dotX + fanLen * Math.sin(radians));
        float endY = (float) (dotY + fanLen * Math.cos(radians));
        float leftSpanX = (float) (dotX + turnLen * Math.sin(Math.toRadians(degree + degreeSpan)));
        float leftSpanY = (float) (dotY + turnLen * Math.cos(Math.toRadians(degree + degreeSpan)));
        float rightSpanX = (float) (dotX + turnLen * Math.sin(Math.toRadians(degree - degreeSpan)));
        float rightSpanY = (float) (dotY + turnLen * Math.cos(Math.toRadians(degree - degreeSpan)));

        fanPath.reset();
        fanPath.moveTo(startLeftX, startLeftY);
        fanPath.lineTo(leftSpanX, leftSpanY);
        fanPath.lineTo(endX, endY);
        fanPath.lineTo(rightSpanX, rightSpanY);
        fanPath.lineTo(startRightX, startRightY);
        fanPath.lineTo(dotX, dotY);
        fanPath.close();
        canvas.drawPath(fanPath, mPaint);
    }
}
