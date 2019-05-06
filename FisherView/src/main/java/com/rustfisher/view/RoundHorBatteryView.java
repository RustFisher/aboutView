package com.rustfisher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


/**
 * 电池电量显示
 * 实际上这也是一个进度条
 * 左右2个半圆
 * Created by Rust on 2018-5-6
 */
public class RoundHorBatteryView extends View {
    private static final String TAG = "RoundHorBatteryView";
    private static final int MODE_DISABLE = 100; // 禁用状态
    private static final int MODE_ABLE = 200;    // 正常工作状态
    private int mode = MODE_DISABLE;
    private float powerValue = 100;
    private float maxValue = 100;

    private int borderDisableColor = Color.parseColor("#747883");
    int borderColor = Color.WHITE;
    private int valueColor = Color.WHITE;

    RectF valLeftCircle = new RectF();
    RectF valRightCircle = new RectF();
    RectF borderRectF = new RectF();
    float borderWidPx = 2;
    final int borderPaddingPx = 6; // 内容与边界的间隙
    final int valuePaddingPx = 6;  // 电量与边界的间隙

    float vWidth = 0;
    float vHeight = 50;

    private Paint batteryPaint;
    private Path valPath; // 电量路径

    public RoundHorBatteryView(Context context) {
        this(context, null);
    }

    public RoundHorBatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        vWidth = getWidth();
        vHeight = getHeight();

        final float valueR = (vHeight - 2 * borderPaddingPx - 2 * valuePaddingPx) / 2;
        valLeftCircle.set(borderPaddingPx + valuePaddingPx, borderPaddingPx + valuePaddingPx,
                2 * valueR + borderPaddingPx + valuePaddingPx, vHeight - borderPaddingPx - valuePaddingPx);

        valRightCircle.set(vWidth - borderPaddingPx - valuePaddingPx - 2 * valueR, borderPaddingPx + valuePaddingPx,
                vWidth - borderPaddingPx - valuePaddingPx, vHeight - borderPaddingPx - valuePaddingPx);

        drawBorder(canvas);
        if (mode == MODE_ABLE) {
            batteryPaint.setColor(valueColor);
            drawValue(canvas);
        }
    }

    /**
     * 画电量
     */
    private void drawValue(Canvas canvas) {
        if (powerValue <= 0) {
            return;
        }

        final float borderR = (vHeight - 2 * borderPaddingPx) / 2;
        final float pbLen = vWidth - 2 * borderPaddingPx - 2 * valuePaddingPx; // 电量总长度
        float valueLen = pbLen * powerValue / maxValue; // 实际电量长度
        if (valueLen > pbLen) {
            valueLen = pbLen; // 限制长度
        }
        final float x0 = borderPaddingPx + valuePaddingPx;
        final float x1 = borderR + borderPaddingPx;
        final float x2 = vWidth - borderPaddingPx - borderR;
//        Log.d(TAG, "drawValue: value len: " + valueLen + ", whole len: " + pbLen + "; x0: " + x0 + ", x1: " + x1 + ", x2: " + x2);

        valueLen += x0; // 现在是view中的横坐标值

        if (valueLen <= x1) {
            drawValueLeftPart(canvas, valueLen, x1);
        } else if (valueLen <= x2) {
            drawValueLeftPart(canvas, x1, x1);
            drawValueMiddlePart(canvas, valueLen, x1);
        } else {
            drawValueLeftPart(canvas, x1, x1);
            drawValueMiddlePart(canvas, x2, x1);
            drawValueRightPart(canvas, valueLen, x2);
        }

    }

    private void drawValueRightPart(Canvas canvas, float valueLen, float x2) {
        float dx = valueLen - x2;
        double theta = Math.acos(dx / (valRightCircle.width() / 2)); // 弧度 0~pi
        double dy = (valRightCircle.width() / 2) * Math.sin(theta);
        float angleDeg = (float) Math.toDegrees(theta); // 角度
        float y2 = (float) (valRightCircle.centerY() + dy);
        valPath.reset();
        valPath.moveTo(valRightCircle.centerX() - batteryPaint.getStrokeWidth() / 2, valRightCircle.top);
        valPath.arcTo(valRightCircle, -90, 90 - angleDeg);
        valPath.lineTo(valueLen, y2);
        valPath.arcTo(valRightCircle, angleDeg, 90 - angleDeg);
        valPath.lineTo(valRightCircle.centerX() - batteryPaint.getStrokeWidth() / 2, valRightCircle.bottom);
        valPath.close();
        canvas.drawPath(valPath, batteryPaint);
    }

    /**
     * 绘制左半圆部分
     */
    private void drawValueLeftPart(Canvas canvas, float valueLen, float x1) {
        float r2 = x1 - valueLen;
        double theta = Math.acos(r2 / (valLeftCircle.width() / 2)); // 弧度 0~pi
        double dy = (valLeftCircle.width() / 2) * Math.sin(theta);
        float angleDeg = (float) Math.toDegrees(theta); // 角度
        float y1 = (float) (valLeftCircle.centerY() - dy);
        float y2 = (float) (valLeftCircle.centerY() + dy);
//        Log.d(TAG, "drawValue: theta:" + theta + ", angleDeg: " + angleDeg);
        valPath.reset();
        valPath.moveTo(valueLen, y1);
        valPath.lineTo(valueLen, y2);
        valPath.arcTo(valLeftCircle, 180 - angleDeg, 2 * angleDeg);
        valPath.close();
        batteryPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(valPath, batteryPaint);
    }

    /**
     * 绘制电量中间的部分
     */
    private void drawValueMiddlePart(Canvas canvas, float valueLen, float x1) {
        valPath.reset();
        valPath.moveTo(x1 - batteryPaint.getStrokeWidth() / 2, valLeftCircle.top); // 稍微往左一点
        valPath.lineTo(valueLen, valLeftCircle.top);
        valPath.lineTo(valueLen, valLeftCircle.bottom);
        valPath.lineTo(x1 - batteryPaint.getStrokeWidth() / 2, valLeftCircle.bottom);
        valPath.close();
        batteryPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(valPath, batteryPaint);
    }

    /**
     * 画外框
     */
    private void drawBorder(Canvas canvas) {
        final float r = (vHeight - 2 * borderPaddingPx) / 2;
        if (mode == MODE_DISABLE) {
            batteryPaint.setColor(borderDisableColor);
        } else if (mode == MODE_ABLE) {
            batteryPaint.setColor(borderColor);
        }
        batteryPaint.setStyle(Paint.Style.STROKE);
        batteryPaint.setStrokeWidth(borderWidPx);
        borderRectF = new RectF(borderPaddingPx, borderPaddingPx, borderPaddingPx + 2 * r, vHeight - borderPaddingPx);
        canvas.drawArc(borderRectF, 90, 180, false, batteryPaint);
        borderRectF = new RectF(vWidth - borderPaddingPx - 2 * r, borderPaddingPx, vWidth - borderPaddingPx, vHeight - borderPaddingPx);
        canvas.drawArc(borderRectF, -90, 180, false, batteryPaint);
        canvas.drawLine(r + borderPaddingPx, borderPaddingPx, vWidth - r - borderPaddingPx, borderPaddingPx, batteryPaint);
        canvas.drawLine(r + borderPaddingPx, vHeight - borderPaddingPx, vWidth - r - borderPaddingPx, vHeight - borderPaddingPx, batteryPaint);
    }

    public void init() {
        if (batteryPaint == null) {
            batteryPaint = new Paint();
            batteryPaint.setColor(Color.WHITE);
            batteryPaint.setAntiAlias(true);
        }
        valPath = new Path();
    }

    public void setPower(float power) {
        powerValue = power;
        if (powerValue < 0) {
            powerValue = 0;
        } else if (powerValue > maxValue) {
            powerValue = maxValue;
        }
        invalidate();
    }

    public void setValueColor(int color) {
        this.valueColor = color;
    }

    public void setValueAndColor(float value, int color) {
        setValueColor(color);
        setPower(value);
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public void setBorderDisableColor(int borderDisableColor) {
        this.borderDisableColor = borderDisableColor;
    }

    public void setMode(int mode) {
        this.mode = mode;
        invalidate();
    }

    public void disableMode() {
        setMode(MODE_DISABLE);
    }

    public void enableMode() {
        setMode(MODE_ABLE);
    }
}