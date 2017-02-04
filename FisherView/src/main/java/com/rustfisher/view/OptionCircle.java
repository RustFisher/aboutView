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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class OptionCircle extends ImageView {

    private final Paint paint;
    private final Context context;
    boolean clicked = false;
    boolean addBackground = false;
    int radius = -1;
    int centerOffsetX = 0;
    int centerOffsetY = 0;
    int colorCircle;
    int colorBackground;
    int colorText;
    String textCircle = "";

    public OptionCircle(Context context) {
        this(context, null);
    }

    public OptionCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.STROKE);
        colorCircle = Color.argb(205, 245, 2, 51);
        colorText = colorCircle;
        colorBackground = colorCircle;
    }

    public void setRadius(int r) {
        this.radius = r;
    }

    public void setCenterOffset(int x, int y) {
        this.centerOffsetX = x;
        this.centerOffsetY = y;
    }

    public void setColorCircle(int c) {
        this.colorCircle = c;
    }

    public void setColorText(int c) {
        this.colorText = c;
    }

    public void setColorBackground(int c) {
        this.colorBackground = c;
    }

    public void setText(String s) {
        this.textCircle = s;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public void setAddBackground(boolean add) {
        this.addBackground = add;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = getWidth() / 2;
        int innerCircle = 86;
        if (radius > 0) {
            innerCircle = dip2px(context, radius); //set inner radius
        }

        Drawable drawable = getDrawable();
        if (addBackground) {
        } else {
            // draw circle
            this.paint.setStyle(clicked ? Paint.Style.FILL : Paint.Style.STROKE);
            this.paint.setColor(clicked ? colorBackground : colorCircle);
            this.paint.setStrokeWidth(1.5f);
            canvas.drawCircle(center + centerOffsetX, center + centerOffsetY,
                    innerCircle, this.paint);
        }

        // draw text
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setStrokeWidth(1);
        this.paint.setTextSize(22);
        this.paint.setTypeface(Typeface.MONOSPACE);
        this.paint.setColor(clicked ? Color.WHITE : colorText);
        this.paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        canvas.drawText(textCircle, center + centerOffsetX,
                center + centerOffsetY - (fontMetrics.top + fontMetrics.bottom) / 2, this.paint);
        super.onDraw(canvas);
    }

    /**
     * convert dp to px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
