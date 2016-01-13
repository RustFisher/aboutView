package com.rust.aboutview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class OptionCircle extends View {

    private final Paint paint;
    private final Context context;
    int radius = -1;

    public OptionCircle(Context context) {
        this(context, null);
    }

    public OptionCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.STROKE);
    }

    public void setRadius(int r) {
        this.radius = r;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = getWidth() / 2;
        int innerCircle = 69;
        if (radius > 0) {
            innerCircle = dip2px(context, radius); //set inner radius
        }

        // draw circle
        this.paint.setARGB(255, 167, 190, 206);
        this.paint.setStrokeWidth(2);
        canvas.drawCircle(center, center, innerCircle, this.paint);

        super.onDraw(canvas);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
