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
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;


public final class PolygonsTextView extends TextView {

    private int bgColor = Color.WHITE;
    private int strokeWidth = 1;
    Paint bgPaint = new Paint();
    Path bgPath = new Path();

    public PolygonsTextView(Context context) {
        this(context, null);
    }

    public PolygonsTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PolygonsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PolygonsTextView, defStyleAttr, 0);
        bgColor = a.getColor(R.styleable.PolygonsTextView_PolygonsTv_bgColor, Color.WHITE);
        strokeWidth = a.getDimensionPixelSize(R.styleable.PolygonsTextView_PolygonsTv_strokeWid, dpToPx(1));
        a.recycle();

        bgPaint.setColor(bgColor);
        bgPaint.setStrokeWidth(strokeWidth);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int wid = getWidth();
        int height = getHeight();

        int bp0_x = strokeWidth / 2;// TextView已经设置了宽高范围,必须往里缩一点
        int bp0_y = strokeWidth / 2;// 顺时针定义这几个点
        int bp1_x = (int) (wid - height * 0.5);
        int bp1_y = bp0_y;
        int bp2_x = wid - bp0_x;
        int bp2_y = (int) (height * 0.7) - bp0_y;
        int bp3_x = bp2_x;
        int bp3_y = height - bp0_y;
        int bp4_x = (int) (height * 0.25);
        int bp4_y = bp3_y;
        int bp5_x = bp0_x;
        int bp5_y = (int) (height * 0.75);

        bgPath.moveTo(bp0_x, bp0_y);
        bgPath.lineTo(bp1_x, bp1_y);
        bgPath.lineTo(bp2_x, bp2_y);
        bgPath.lineTo(bp3_x, bp3_y);
        bgPath.lineTo(bp4_x, bp4_y);
        bgPath.lineTo(bp5_x, bp5_y);
        bgPath.close();
        canvas.drawPath(bgPath, bgPaint);
        bgPath.reset();

    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
