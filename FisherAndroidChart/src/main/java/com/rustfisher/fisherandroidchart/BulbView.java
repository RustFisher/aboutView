package com.rustfisher.fisherandroidchart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

public class BulbView extends ImageView {

    //    private static final String TAG = BulbView.class.getSimpleName();
    private float lineWid;
    private float botArcR;
    private float botLineLen;
    private float midHeight;
    private float shineLen;
    private float wholeSizeRatio;
    private int shineColor;
    private int bulbOutlineColor;
    private boolean showShiningLines = false;

    private Paint shinePaint = new Paint();

    Paint bgPaint = new Paint();
    RectF botLeftRect = new RectF();
    RectF botRightRect = new RectF();
    RectF headRect = new RectF();
    RectF headShineRectF = new RectF();

    public BulbView(Context context) {
        this(context, null);
    }

    public BulbView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BulbView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BulbView, defStyleAttr, 0);
        lineWid = a.getDimensionPixelSize(R.styleable.BulbView_lineWid, (int) dpToPx(2));
        botArcR = a.getDimensionPixelSize(R.styleable.BulbView_botArcRadius, (int) dpToPx(7));
        botLineLen = a.getDimensionPixelSize(R.styleable.BulbView_botLineLen, (int) dpToPx(14));
        midHeight = a.getDimensionPixelSize(R.styleable.BulbView_midHeight, (int) dpToPx(28));
        shineLen = a.getDimensionPixelSize(R.styleable.BulbView_shineLen, (int) (botLineLen * 0.85));
        wholeSizeRatio = a.getFloat(R.styleable.BulbView_wholeSizeRatio, 1.0f);
        bulbOutlineColor = a.getColor(R.styleable.BulbView_bulbOutlineColor, Color.WHITE);
        shineColor = a.getColor(R.styleable.BulbView_shineColor, Color.BLUE);
        showShiningLines = a.getBoolean(R.styleable.BulbView_showShiningLines, false);
        a.recycle();

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
        bgPaint.setColor(bulbOutlineColor);

        shinePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        shinePaint.setColor(shineColor);
    }

    public void setWholeSizeRatio(float wholeSizeRatio) {
        this.wholeSizeRatio = wholeSizeRatio;
        initSize();
        invalidate();
    }

    public void setBotLineLenDp(float lenDp) {
        this.botLineLen = dpToPx(lenDp);
        initSize();
        invalidate();
    }

    public void setShineColor(int shineColor) {
        this.shineColor = shineColor;
        shinePaint.setColor(shineColor);
        invalidate();
    }

    public void setBulbOutlineColor(int bulbOutlineColor) {
        this.bulbOutlineColor = bulbOutlineColor;
        bgPaint.setColor(bulbOutlineColor);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWid = getWidth();
        float viewHeight = getHeight() - lineWid;

        /**
         * Draw background
         */
        float originX = viewWid / 2;
        float originY = viewHeight;
        float botP1_x = (float) (originX - botLineLen / 2.0);// Left
        float botP1_y = originY;
        float botP2_x = (float) (originX + botLineLen / 2.0);// Right
        float botP2_y = originY;
        float midP1_x = (float) (originX + botLineLen / 2.0 + botArcR);
        float midP1_y = botP2_y - botArcR - midHeight;
        float midP2_x = botP2_x + botArcR;
        float midP2_y = botP2_y - botArcR;
        float midP3_x = (float) (originX - botLineLen / 2.0 - botArcR);
        float midP3_y = originY - botArcR;
        float midP4_x = (float) (originX - botLineLen / 2.0 - botArcR);
        float midP4_y = originY - botArcR - midHeight;

        canvas.drawLine(botP1_x, botP1_y, botP2_x + 2, botP2_y, bgPaint); // Add offset

        botLeftRect.set(midP3_x, viewHeight - 2 * botArcR, botP1_x + botArcR, botP1_y);
        canvas.drawArc(botLeftRect, 89, 91, false, bgPaint);

        botRightRect.set(botP2_x - botArcR, viewHeight - 2 * botArcR, midP1_x, botP2_y);
        canvas.drawArc(botRightRect, -1, 91, false, bgPaint);

        canvas.drawLine(midP3_x, midP3_y, midP4_x, midP4_y, bgPaint);
        canvas.drawLine(midP2_x, midP2_y, midP1_x, midP1_y, bgPaint);
        canvas.drawLine(midP4_x - lineWid / 2, midP4_y, midP1_x + lineWid / 2, midP1_y, bgPaint);

        // Draw bulb head background
        headRect.set(midP4_x, (float) (midP4_y - (midP1_x - midP4_x) / 2.0), midP1_x, (float) (midP4_y + (midP1_x - midP4_x) / 2.0));
        canvas.drawArc(headRect, 180, 180, false, bgPaint);

        /**
         * Draw shining bulb head
         * Draw an outline and then draw inner
         */
        shinePaint.setStyle(Paint.Style.STROKE);
        float shineLineWid = (float) (1.4 * lineWid);
        shinePaint.setStrokeWidth(shineLineWid);
        headShineRectF.set((midP4_x + lineWid),
                (float) (midP4_y - (midP1_x - midP4_x) / 2.0 + lineWid),
                (midP1_x - lineWid),
                (float) (midP4_y + (midP1_x - midP4_x) / 2.0 - lineWid * 2));
        canvas.drawArc(headShineRectF, 180, 180, false, shinePaint);
        shinePaint.setStyle(Paint.Style.FILL_AND_STROKE);// Draw inner color
        headShineRectF.set((midP4_x + lineWid),
                (float) (midP4_y - (midP1_x - midP4_x) / 2.0 + lineWid),
                (midP1_x - lineWid),
                (float) (midP4_y + (midP1_x - midP4_x) / 2.0 - lineWid * 2 + 2)); // Bottom add offset
        canvas.drawArc(headShineRectF, 180, 180, false, shinePaint);

        drawShiningLines(canvas, midP1_x, midP1_y, midP4_x, midP4_y);
    }

    /**
     * Draw shining lines
     */
    private void drawShiningLines(Canvas canvas, float midP1_x, float midP1_y, float midP4_x, float midP4_y) {
        if (!showShiningLines) {
            return;
        }
        int s2hGap1 = (int) (dpToPx(10) * wholeSizeRatio);
        int s2hGap2 = (int) (dpToPx(8) * wholeSizeRatio);
        int gap3 = (int) (dpToPx(2) * wholeSizeRatio);

        // Far left
        float s1_x = midP4_x - s2hGap1;
        float s1_y = midP4_y;
        float s1Out_x = (float) (s1_x - shineLen * Math.cos(Math.toRadians(30)));
        float s1Out_y = (float) (s1_y - shineLen * Math.sin(Math.toRadians(30)));
        canvas.drawLine(s1_x, s1_y, s1Out_x, s1Out_y, shinePaint);
        canvas.drawCircle(s1_x, s1_y, 0, shinePaint);
        canvas.drawCircle(s1Out_x, s1Out_y, 0, shinePaint);

        // Far right
        float s2_x = midP1_x + s2hGap1;
        float s2_y = midP1_y;
        float s2Out_x = (float) (s2_x + shineLen * Math.cos(Math.toRadians(30)));
        float s2Out_y = (float) (s2_y - shineLen * Math.sin(Math.toRadians(30)));
        canvas.drawLine(s2_x, s2_y, s2Out_x, s2Out_y, shinePaint);
        canvas.drawCircle(s2_x, s2_y, 0, shinePaint);
        canvas.drawCircle(s2Out_x, s2Out_y, 0, shinePaint);

        // Middle
        float s3_x = (midP1_x + midP4_x) / 2;
        float s3_y = midP1_y - (midP1_x - midP4_x) / 2 - s2hGap2;
        float s3Out_y = s3_y - shineLen;
        canvas.drawLine(s3_x, s3_y, s3_x, s3Out_y, shinePaint);
        canvas.drawCircle(s3_x, s3_y, 0, shinePaint);
        canvas.drawCircle(s3_x, s3Out_y, 0, shinePaint);

        // Left shine line
        float s4_x = midP4_x - gap3;
        float s4_y = midP4_y + gap3 / 2 - (midP1_x - midP4_x) / 2;
        float s4Out_x = (float) (s4_x - shineLen * Math.cos(Math.toRadians(60)));
        float s4Out_y = (float) (s4_y - shineLen * Math.sin(Math.toRadians(60)));
        canvas.drawLine(s4_x, s4_y, s4Out_x, s4Out_y, shinePaint);
        canvas.drawCircle(s4_x, s4_y, 0, shinePaint);
        canvas.drawCircle(s4Out_x, s4Out_y, 0, shinePaint);

        // Left shine line
        float s5_x = midP1_x + gap3;
        float s5_y = midP1_y + gap3 / 2 - (midP1_x - midP4_x) / 2;
        float s5Out_x = (float) (s5_x + shineLen * Math.cos(Math.toRadians(60)));
        float s5Out_y = (float) (s5_y - shineLen * Math.sin(Math.toRadians(60)));
        canvas.drawLine(s5_x, s5_y, s5Out_x, s5Out_y, shinePaint);
        canvas.drawCircle(s5_x, s5_y, 0, shinePaint);
        canvas.drawCircle(s5Out_x, s5Out_y, 0, shinePaint);
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
