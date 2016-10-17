package com.rustfisher.fisherandroidchart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

public class BulbView extends ImageView {

    private static final String TAG = BulbView.class.getSimpleName();
    private float lineWid;
    private float botArcR;
    private float botLineLen;
    private float midHeight;
    private float shineLen;
    private float wholeSizeRatio;
    private int shineColor;
    private int bulbOutlineColor;
    private int out2shineGap;
    private boolean showShiningLines = false;

    private Paint shinePaint = new Paint();

    Paint bgPaint = new Paint();
    RectF botLeftRect = new RectF();
    RectF botRightRect = new RectF();
    RectF headRect = new RectF();
    RectF headShineRectF = new RectF();
    RectF baseBotRectF = new RectF();
    SweepGradient outerSg;
    int[] sgColors = {Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED, Color.parseColor("#f202ff"), Color.BLUE};

    public BulbView(Context context) {
        this(context, null);
    }

    public BulbView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BulbView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BulbView, defStyleAttr, 0);
        lineWid = a.getDimensionPixelSize(R.styleable.BulbView_lineWid, (int) dpToPx(1.5f));
        botArcR = a.getDimensionPixelSize(R.styleable.BulbView_botArcRadius, (int) dpToPx(7));
        botLineLen = a.getDimensionPixelSize(R.styleable.BulbView_botLineLen, (int) dpToPx(10));
        midHeight = a.getDimensionPixelSize(R.styleable.BulbView_midHeight, (int) dpToPx(20));
        shineLen = a.getDimensionPixelSize(R.styleable.BulbView_shineLen, (int) (botLineLen * 0.85));
        wholeSizeRatio = a.getFloat(R.styleable.BulbView_wholeSizeRatio, 1.0f);
        bulbOutlineColor = a.getColor(R.styleable.BulbView_bulbOutlineColor, Color.WHITE);
        shineColor = a.getColor(R.styleable.BulbView_shineColor, Color.BLUE);
        showShiningLines = a.getBoolean(R.styleable.BulbView_showShiningLines, false);
        out2shineGap = a.getDimensionPixelSize(R.styleable.BulbView_out2ShineGap, (int) dpToPx(20));
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

    public int getOut2shineGap() {
        return out2shineGap;
    }

    public void setOut2shineGap(int out2shineGap) {
        this.out2shineGap = out2shineGap;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        outerSg = new SweepGradient(w / 2, h / 2, sgColors, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bgPaint.setShader(null);
        bgPaint.setStrokeWidth(lineWid);
        int viewWid = getWidth();
        float viewHeight = getHeight() - lineWid;
        int outerR = (viewWid > viewHeight ? (int) viewHeight : viewWid) / 2;
        int shineCircleR = (outerR - out2shineGap);

        float center_x = viewWid / 2;
        float center_y = viewHeight / 2;

        /**
         * Draw background
         */
        float bulbOriginX = center_x;
        float bulbOriginY = viewHeight * 0.7f;
        float bulbBotP1_x = (float) (bulbOriginX - botLineLen / 2.0);// Left
        float bulbBotP1_y = bulbOriginY;
        float bulbBotP2_x = (float) (bulbOriginX + botLineLen / 2.0);// Right
        float bulbBotP2_y = bulbOriginY;
        float bulbMidP1_x = (float) (bulbOriginX + botLineLen / 2.0 + botArcR);
        float bulbMidP1_y = bulbBotP2_y - botArcR - midHeight;
        float bulbMidP2_x = bulbBotP2_x + botArcR;
        float bulbMidP2_y = bulbBotP2_y - botArcR;
        float bulbMidP3_x = (float) (bulbOriginX - botLineLen / 2.0 - botArcR);
        float bulbMidP3_y = bulbOriginY - botArcR;
        float bulbMidP4_x = (float) (bulbOriginX - botLineLen / 2.0 - botArcR);
        float bulbMidP4_y = bulbOriginY - botArcR - midHeight;

        canvas.drawLine(bulbBotP1_x, bulbBotP1_y, bulbBotP2_x + 2, bulbBotP2_y, bgPaint); // Add offset

        botLeftRect.set(bulbMidP3_x, bulbOriginY - 2 * botArcR, bulbBotP1_x + botArcR, bulbBotP1_y);
        canvas.drawArc(botLeftRect, 89, 91, false, bgPaint);

        botRightRect.set(bulbBotP2_x - botArcR, bulbOriginY - 2 * botArcR, bulbMidP1_x, bulbBotP2_y);
        canvas.drawArc(botRightRect, -1, 91, false, bgPaint);

        canvas.drawLine(bulbMidP3_x, bulbMidP3_y, bulbMidP4_x, bulbMidP4_y, bgPaint);
        canvas.drawLine(bulbMidP2_x, bulbMidP2_y, bulbMidP1_x, bulbMidP1_y, bgPaint);
        canvas.drawLine(bulbMidP4_x - lineWid / 2, bulbMidP4_y, bulbMidP1_x + lineWid / 2, bulbMidP1_y, bgPaint);

        // Draw bulb head background
        headRect.set(bulbMidP4_x, (float) (bulbMidP4_y - (bulbMidP1_x - bulbMidP4_x) / 2.0), bulbMidP1_x, (float) (bulbMidP4_y + (bulbMidP1_x - bulbMidP4_x) / 2.0));
        canvas.drawArc(headRect, 180, 180, false, bgPaint);

        // Draw bulb bottom
        float botGap = 4;
        float baseStart = bulbBotP1_x - 4;
        float baseEnd = bulbBotP2_x + 6;
        float baseBotLen = 10;
        canvas.drawLine(baseStart, bulbBotP1_y + lineWid + botGap, baseEnd, bulbBotP2_y + lineWid + botGap, bgPaint);
        canvas.drawLine(baseStart, bulbBotP1_y + (lineWid + botGap) * 2, baseEnd, bulbBotP2_y + (lineWid + botGap) * 2, bgPaint);
        canvas.drawLine(baseStart, bulbBotP1_y + (lineWid + botGap) * 3, baseEnd, bulbBotP2_y + (lineWid + botGap) * 3, bgPaint);
        canvas.drawLine(baseStart + baseBotLen, bulbBotP1_y + (lineWid + botGap) * 4, baseEnd - baseBotLen, bulbBotP2_y + (lineWid + botGap) * 4, bgPaint);
        bgPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(baseStart, bulbBotP1_y + lineWid + botGap, lineWid / 2, bgPaint);
        canvas.drawCircle(baseStart, bulbBotP1_y + (lineWid + botGap) * 2, lineWid / 2, bgPaint);
        canvas.drawCircle(baseStart, bulbBotP1_y + (lineWid + botGap) * 3, lineWid / 2, bgPaint);
        canvas.drawCircle(baseEnd, bulbBotP2_y + lineWid + botGap, lineWid / 2, bgPaint);
        canvas.drawCircle(baseEnd, bulbBotP2_y + (lineWid + botGap) * 2, lineWid / 2, bgPaint);
        canvas.drawCircle(baseEnd, bulbBotP2_y + (lineWid + botGap) * 3, lineWid / 2, bgPaint);

        canvas.drawCircle(baseStart + baseBotLen, bulbBotP1_y + (lineWid + botGap) * 4, lineWid / 2, bgPaint);
        canvas.drawCircle(baseEnd - baseBotLen, bulbBotP1_y + (lineWid + botGap) * 4, lineWid / 2, bgPaint);

        baseBotRectF.set(baseStart + baseBotLen, bulbBotP1_y + (lineWid + botGap) * 4, baseEnd - baseBotLen, bulbBotP1_y + (lineWid + botGap) * 4 + lineWid);
        canvas.drawOval(baseBotRectF, bgPaint);

        // Draw outer circle
        float outerWidth = 2.0f;
        bgPaint.setStrokeWidth(outerWidth);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setShader(outerSg);
        canvas.drawCircle(center_x, center_y, outerR - dpToPx(4), bgPaint);
        bgPaint.setShader(null);

        /**
         * Draw shining bulb head
         * Draw an outline and then draw inner
         */
        shinePaint.setStyle(Paint.Style.STROKE);
        float shineLineWid = (float) (1.4 * lineWid);
        shinePaint.setStrokeWidth(shineLineWid);
        headShineRectF.set((bulbMidP4_x + lineWid),
                (float) (bulbMidP4_y - (bulbMidP1_x - bulbMidP4_x) / 2.0 + lineWid),
                (bulbMidP1_x - lineWid),
                (float) (bulbMidP4_y + (bulbMidP1_x - bulbMidP4_x) / 2.0 - lineWid * 2));
        canvas.drawArc(headShineRectF, 180, 180, false, shinePaint);
        shinePaint.setStyle(Paint.Style.FILL_AND_STROKE);// Draw inner color
        headShineRectF.set((bulbMidP4_x + lineWid),
                (float) (bulbMidP4_y - (bulbMidP1_x - bulbMidP4_x) / 2.0 + lineWid),
                (bulbMidP1_x - lineWid),
                (float) (bulbMidP4_y + (bulbMidP1_x - bulbMidP4_x) / 2.0 - lineWid * 2 + 2)); // Bottom add offset
        canvas.drawArc(headShineRectF, 180, 180, false, shinePaint);

        /**
         * Draw shining circle
         */
        shinePaint.setStyle(Paint.Style.STROKE);
        shinePaint.setStrokeWidth(outerWidth);
        canvas.drawCircle(center_x, center_y, shineCircleR, shinePaint);

        // Draw lines
        float shineLineStart = shineCircleR + dpToPx(2);
        float shineLineEnd = shineCircleR + out2shineGap * 0.6f + dpToPx(2);

        for (int ra = 0; ra <= 360; ra += 8) {
            canvas.drawLine(getCirclePointX(center_x, shineLineStart, ra), getCirclePointY(center_y, shineLineStart, ra),
                    getCirclePointX(center_x, shineLineEnd, ra), getCirclePointY(center_y, shineLineEnd, ra), shinePaint);
        }

        drawShiningLines(canvas, bulbMidP1_x, bulbMidP1_y, bulbMidP4_x, bulbMidP4_y);
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
