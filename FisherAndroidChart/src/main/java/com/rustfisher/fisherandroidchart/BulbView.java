package com.rustfisher.fisherandroidchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

public class BulbView extends ImageView {

    private static final String TAG = BulbView.class.getSimpleName();
    private int lineWid;
    private int botArcR;
    private int botLineLen;
    private int midHeight;
    private int shineLen;
    private float wholeSizeRatio;

    private Paint shinePaint = new Paint();

    Paint bgPaint = new Paint();
    RectF botLeftRect = new RectF();
    RectF botRightRect = new RectF();
    RectF headRect = new RectF();

    public BulbView(Context context) {
        this(context, null);
    }

    public BulbView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BulbView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        lineWid = dpToPx(3);
        botArcR = dpToPx(10);
        botLineLen = dpToPx(14);
        midHeight = dpToPx(28);
        shineLen = dpToPx(15);
        wholeSizeRatio = 1.0f;

        initSize();

    }

    private void initSize() {
        lineWid *= wholeSizeRatio;
        botArcR *= wholeSizeRatio;
        botLineLen *= wholeSizeRatio;
        midHeight *= wholeSizeRatio;
        shineLen *= wholeSizeRatio;

        bgPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(lineWid);
        bgPaint.setColor(Color.WHITE);

        shinePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        shinePaint.setColor(Color.BLUE);
    }

    public void setWholeSizeRatio(float wholeSizeRatio) {
        this.wholeSizeRatio = wholeSizeRatio;
        initSize();
        invalidate();
    }

    public void setBotLineLenDp(int lenDp) {
        this.botLineLen = lenDp;
        initSize();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWid = getWidth();
        int viewHeight = getHeight() - lineWid;

        /**
         * Draw background
         */
        int originX = viewWid / 2;
        int originY = viewHeight;
        int botP1_x = originX - botLineLen / 2;
        int botP1_y = originY;
        int botP2_x = originX + botLineLen / 2;
        int botP2_y = originY;
        int midP1_x = botP2_x + botArcR;
        int midP1_y = botP2_y - botArcR - midHeight;
        int midP2_x = botP2_x + botArcR;
        int midP2_y = botP2_y - botArcR;
        int midP3_x = botP1_x - botArcR;
        int midP3_y = botP1_y - botArcR;
        int midP4_x = botP1_x - botArcR;
        int midP4_y = botP2_y - botArcR - midHeight;

        canvas.drawLine(botP1_x, botP1_y, botP2_x, botP2_y, bgPaint);

        botLeftRect.set(midP3_x, viewHeight - 2 * botArcR, botP1_x + botArcR, botP1_y);
        canvas.drawArc(botLeftRect, 90, 90, false, bgPaint);

        botRightRect.set(botP2_x - botArcR, viewHeight - 2 * botArcR, midP1_x, botP2_y);
        canvas.drawArc(botRightRect, 0, 90, false, bgPaint);

        canvas.drawLine(midP3_x, midP3_y, midP4_x, midP4_y, bgPaint);
        canvas.drawLine(midP2_x, midP2_y, midP1_x, midP1_y, bgPaint);
        canvas.drawLine(midP4_x - lineWid / 2, midP4_y, midP1_x + lineWid / 2, midP1_y, bgPaint);

        // Draw bulb head background
        headRect.set(midP4_x, (float) (midP4_y - (midP1_x - midP4_x) / 2.0), midP1_x, (float) (midP4_y + (midP1_x - midP4_x) / 2.0));
        canvas.drawArc(headRect, 180, 180, false, bgPaint);

        /**
         * Draw shining bulb head
         */
        shinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        headRect.set(midP4_x + lineWid / 2,
                (float) (midP4_y - (midP1_x - midP4_x) / 2.0 + lineWid / 2.0),
                midP1_x - lineWid / 2,
                (float) (midP4_y + (midP1_x - midP4_x) / 2.0 - lineWid));
        canvas.drawArc(headRect, 180, 180, false, shinePaint);

        /**
         * Draw shining lines
         */
        float shineLineWid = (float) (1.4 * lineWid);
        shinePaint.setStrokeWidth(shineLineWid);
        int s2hGap1 = (int) (dpToPx(10) * wholeSizeRatio);
        int s2hGap2 = (int) (dpToPx(8) * wholeSizeRatio);
        int gap3 = (int) (dpToPx(2) * wholeSizeRatio);

        // Far left
        int s1_x = midP4_x - s2hGap1;
        int s1_y = midP4_y;
        float s1Out_x = (float) (s1_x - shineLen * Math.cos(Math.toRadians(30)));
        float s1Out_y = (float) (s1_y - shineLen * Math.sin(Math.toRadians(30)));
        canvas.drawLine(s1_x, s1_y, s1Out_x, s1Out_y, shinePaint);
        canvas.drawCircle(s1_x, s1_y, 0, shinePaint);
        canvas.drawCircle(s1Out_x, s1Out_y, 0, shinePaint);

        // Far right
        int s2_x = midP1_x + s2hGap1;
        int s2_y = midP1_y;
        float s2Out_x = (float) (s2_x + shineLen * Math.cos(Math.toRadians(30)));
        float s2Out_y = (float) (s2_y - shineLen * Math.sin(Math.toRadians(30)));
        canvas.drawLine(s2_x, s2_y, s2Out_x, s2Out_y, shinePaint);
        canvas.drawCircle(s2_x, s2_y, 0, shinePaint);
        canvas.drawCircle(s2Out_x, s2Out_y, 0, shinePaint);

        // Middle
        int s3_x = (midP1_x + midP4_x) / 2;
        int s3_y = midP1_y - (midP1_x - midP4_x) / 2 - s2hGap2;
        float s3Out_y = s3_y - shineLen;
        canvas.drawLine(s3_x, s3_y, s3_x, s3Out_y, shinePaint);
        canvas.drawCircle(s3_x, s3_y, 0, shinePaint);
        canvas.drawCircle(s3_x, s3Out_y, 0, shinePaint);

        // Left shine line
        int s4_x = midP4_x - gap3;
        int s4_y = midP4_y + gap3 / 2 - (midP1_x - midP4_x) / 2;
        float s4Out_x = (float) (s4_x - shineLen * Math.cos(Math.toRadians(60)));
        float s4Out_y = (float) (s4_y - shineLen * Math.sin(Math.toRadians(60)));
        canvas.drawLine(s4_x, s4_y, s4Out_x, s4Out_y, shinePaint);
        canvas.drawCircle(s4_x, s4_y, 0, shinePaint);
        canvas.drawCircle(s4Out_x, s4Out_y, 0, shinePaint);

        // Left shine line
        int s5_x = midP1_x + gap3;
        int s5_y = midP1_y + gap3 / 2 - (midP1_x - midP4_x) / 2;
        float s5Out_x = (float) (s5_x + shineLen * Math.cos(Math.toRadians(60)));
        float s5Out_y = (float) (s5_y - shineLen * Math.sin(Math.toRadians(60)));
        canvas.drawLine(s5_x, s5_y, s5Out_x, s5Out_y, shinePaint);
        canvas.drawCircle(s5_x, s5_y, 0, shinePaint);
        canvas.drawCircle(s5Out_x, s5Out_y, 0, shinePaint);
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
