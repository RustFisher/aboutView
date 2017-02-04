/*
 * MIT License
 *
 * Copyright (c) [2017] [Rust Fisher]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.rustfisher.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;

/**
 * 扇形图统计图
 */
public class PieView extends View {

    private static final String TAG = "CMApp" + PieView.class.getSimpleName();
    private int circleRadius;
    private float ringWid;
    private static final float CENTER_RATIO = 0.70f;// 中心圆的比例
    private int centerColor = Color.WHITE;// 中心圆的颜色
    private ArrayList<PieItem> pieList = new ArrayList<>();

    RectF ringOval = new RectF();

    Paint piePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PieView, defStyleAttr, 0);
        circleRadius = a.getDimensionPixelSize(R.styleable.PieView_pieCircleRadius, (int) dpToPx(65));
        ringWid = a.getDimensionPixelSize(R.styleable.PieView_ringWid, (int) dpToPx(40));
        centerColor = a.getColor(R.styleable.PieView_centerColor, Color.WHITE);
        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int cX = w / 2;
        int cY = h / 2;
        ringOval = new RectF(cX - circleRadius, cY - circleRadius, cX + circleRadius, cY + circleRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (null == pieList) {
            return;
        }

        if (pieList.isEmpty()) {
            return;
        }

        piePaint.setStyle(Paint.Style.FILL);
        piePaint.setStrokeWidth(ringWid);

        int pieCount = pieList.size();
        float valueSum = 0;
        for (PieItem pie : pieList) {
            valueSum += pie.getValue();
        }
        float startAngle = 0;
        for (int i = 0; i < pieCount; i++) {
            PieItem pie = pieList.get(i);
            piePaint.setColor(pie.getColor());
            float sweep = pie.getValue() / valueSum * 360.00f;
            Log.d(TAG, i + " onDraw: start " + startAngle + "; sweep " + sweep + "; " + pie.getValue());
            if (sweep > 0) {
                canvas.drawArc(ringOval, startAngle, sweep, true, piePaint);
            }
            startAngle += sweep;
        }
        piePaint.setColor(centerColor);
        canvas.drawCircle(ringOval.centerX(), ringOval.centerY(), ringWid * CENTER_RATIO, piePaint);
    }

    public ArrayList<PieItem> getPieList() {
        return pieList;
    }

    public void setPieList(ArrayList<PieItem> pieList) {
        this.pieList = pieList;
        invalidate();
    }

    public void addPieItem(PieItem item) {
        pieList.add(item);
        invalidate();
    }

    public static class PieItem {
        private int color;
        private float value;

        public PieItem(int color, float value) {
            this.color = color;
            this.value = value;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }
    }

    /**
     * 获取圆上点的x值
     */
    private float getCirclePointX(float center_x, float r, int radians) {
        return (float) (center_x + r * Math.sin(Math.toRadians(radians)));
    }

    /**
     * 获取圆上点的y值
     */
    private float getCirclePointY(float center_y, float r, int radians) {
        return (float) (center_y + r * Math.cos(Math.toRadians(radians)));
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
