package com.rust.aboutview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MarkView extends ImageView {

    public static final double PI = Math.PI;
    public static final double PI_DIVER_100 = PI / 100.0;
    private final Paint paint;
    float touchX = 0;
    float touchY = 0;

    public void setTouchX(float touchX) {
        this.touchX = touchX;
    }

    public void setTouchY(float touchY) {
        this.touchY = touchY;
    }

    public MarkView(Context context) {
        this(context, null);
    }

    public MarkView(Context context, AttributeSet attr) {
        super(context, attr);
        this.paint = new Paint();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        paint.setAlpha(180);
        paint.setColor(Color.GRAY);
        canvas.drawLine(touchX - 100, touchY, touchX + 100, touchY, paint);// draw horizon line
        canvas.drawLine(touchX, touchY - 100, touchX, touchY + 100, paint);// draw vertical line

        paint.setColor(Color.argb(255, 255, 0, 0));
        paint.setStrokeWidth(2);
//        for (float x = 0; x < 500; x += PI_DIVER_100) {
//            canvas.drawPoint(x, (float) -(100 * Math.sin(x / 20.0)) + 200, paint);
//        }

        super.onDraw(canvas);
    }


}
