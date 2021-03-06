package com.rust.aboutview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 可以展示多条折线
 * Created on 2019-8-14
 */
public class ColorLinesView extends View {
    private static final String TAG = "rustColorLinesView";

    // 代表折线数据
    public static class LineData {
        public final int lineId;
        public int lineColor;
        public List<Float> dataList;
        public int winnerBgColor;

        public LineData(int lineId, int lineColor, int winnerBgColor, List<Float> dataList) {
            this.lineId = lineId;
            this.lineColor = lineColor;
            this.dataList = dataList;
            this.winnerBgColor = winnerBgColor;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj instanceof LineData) {
                LineData input = (LineData) obj;
                return input.lineId == this.lineId;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return lineId;
        }
    }

    private float yMax = 300f;
    private float yMin = 0;
    private String horAxisEndText = "";

    float viewYStart = 2; // 图表线条在view顶部留出的间距
    float axisLineWid = 1f; // 坐标轴线条宽度
    int dataLineWid = 3;
    private boolean compare2 = false; // 当输入2组数据时，是否进行比较

    float axisTextSize;
    private int bgLineColor = Color.parseColor("#bfbfbf");
    private int bgTextColor = Color.parseColor("#595959");
    private int bgFairGameColor = Color.parseColor("#dadada"); // 打平时候的颜色

    private float viewWidth;
    private float viewHeight;
    private float botLeftXOnView; // 图表左下点在view中的x坐标
    private float botLeftYOnView; // 图表左下点在view中的y坐标
    private float originYToBottom;// 图表原点距离view底部的距离

    private List<Integer> markList = new ArrayList<>();
    private List<LineData> lineDataList = new ArrayList<>();

    private Paint bgPaint;
    private Paint linePaint;
    Rect textRect = new Rect();
    Path linePath = new Path();

    public ColorLinesView(Context context) {
        this(context, null);
    }

    public ColorLinesView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorLinesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setHorAxisEndText(String horAxisEndText) {
        this.horAxisEndText = horAxisEndText;
        invalidate();
    }

    public void setMarkList(int... marks) {
        markList.clear();
        for (int i : marks) {
            markList.add(i);
        }
        invalidate();
    }

    public void setCompare2(boolean c) {
        this.compare2 = c;
        invalidate();
    }

    public void setData(LineData data) {
        int i = lineDataList.indexOf(data);
        if (i < 0) {
            lineDataList.add(data);
        } else {
            LineData d = lineDataList.get(i);
            d.dataList = new ArrayList<>(data.dataList);
            d.lineColor = data.lineColor;
        }
        invalidate();
    }

    public void setBgFairGameColor(int bgFairGameColor) {
        this.bgFairGameColor = bgFairGameColor;
    }

    private void init(Context context) {
        botLeftXOnView = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());
        originYToBottom = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());
        viewYStart = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());
        axisLineWid = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
        axisTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8, context.getResources().getDisplayMetrics());

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setStrokeWidth(axisLineWid);
        bgPaint.setColor(bgLineColor);
        bgPaint.setStyle(Paint.Style.STROKE);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeWidth(dataLineWid);
        linePaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = getWidth();
        viewHeight = getHeight();
        botLeftYOnView = viewHeight - originYToBottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        drawBg(canvas);
        for (LineData data : lineDataList) {
            drawDataLine(canvas, data);
        }
    }

    private void drawBg(Canvas canvas) {
        final float yDataRange = yMax - yMin;
        final float yAxisRangeOnView = botLeftYOnView - viewYStart;
        final float yDataStep = yAxisRangeOnView / yDataRange;
        final float textMarginRight = 5;
        final float zeroAxisTextMarginTop = 5;

        drawBgCompareColor(canvas);

        // 画原点
        bgPaint.setTextSize(axisTextSize);
        final String zero = "0";
        bgPaint.getTextBounds(zero, 0, zero.length(), textRect);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(bgTextColor);
        canvas.drawText(zero, botLeftXOnView - textRect.width() - textMarginRight, zeroAxisTextMarginTop + botLeftYOnView + textRect.height(), bgPaint);
        bgPaint.setColor(bgLineColor);
        canvas.drawLine(botLeftXOnView, botLeftYOnView, viewWidth, botLeftYOnView, bgPaint);

        bgPaint.getTextBounds(horAxisEndText, 0, horAxisEndText.length(), textRect);
        bgPaint.setColor(bgTextColor);
        canvas.drawText(horAxisEndText, viewWidth - textRect.width() - textMarginRight, zeroAxisTextMarginTop + botLeftYOnView + textRect.height(), bgPaint);

        for (int i : markList) {
            final String m = String.valueOf(i);
            final float y = getYL(i, yDataStep);
            bgPaint.getTextBounds(m, 0, m.length(), textRect);
            bgPaint.setColor(bgTextColor);
            canvas.drawText(m, botLeftXOnView - textRect.width() - textMarginRight, y + textRect.height() / 2f, bgPaint);
            bgPaint.setColor(bgLineColor);
            canvas.drawLine(botLeftXOnView, y, viewWidth, y, bgPaint);
        }
    }

    // 绘制变色的背景
    private void drawBgCompareColor(Canvas canvas) {
        if (!compare2 || lineDataList.size() != 2) {
            return;
        }
        LineData d1 = lineDataList.get(0);
        LineData d2 = lineDataList.get(1);
        final int size1 = d1.dataList.size();
        final int size2 = d2.dataList.size();
        if (size1 != size2) {
            return;
        }
        final float xStep = (viewWidth - botLeftXOnView) / (size2 - 1);
        final float yDataRange = yMax - yMin;
        final float yAxisRangeOnView = botLeftYOnView - viewYStart;
        final float yDataStep = yAxisRangeOnView / yDataRange;
        final float yMaxDataOnView = getYL(yMax, yDataStep);
        final int compareIndex = 3;
        bgPaint.setStyle(Paint.Style.FILL);

        if (size1 >= compareIndex) {
            canvas.drawRect(botLeftXOnView, yMaxDataOnView, botLeftXOnView + xStep * compareIndex, botLeftYOnView, bgPaint);
        }

        for (int i = compareIndex; i < size1 - compareIndex && i < size2 - compareIndex; i += compareIndex) {
            float a1 = d1.dataList.get(i);
            float a2 = d2.dataList.get(i);
            int color = bgFairGameColor;
            if (a1 > a2) {
                color = d1.winnerBgColor;
            } else if (a1 < a2) {
                color = d2.winnerBgColor;
            }
            bgPaint.setColor(color);
            canvas.drawRect(botLeftXOnView + i * xStep, yMaxDataOnView, botLeftXOnView + xStep * (i + compareIndex), botLeftYOnView, bgPaint);
        }
    }

    private void drawDataLine(Canvas canvas, LineData lineData) {
        List<Float> data = lineData.dataList;
        final int dataCount = data.size();
        final float xStep = (viewWidth - botLeftXOnView) / (dataCount - 1);

        final float yDataRange = yMax - yMin;
        final float yAxisRangeOnView = botLeftYOnView - viewYStart;
        final float yDataStep = yAxisRangeOnView / yDataRange;

        linePath.reset();
        float waveStartX = botLeftXOnView;
        float waveStartY = getYL(data.get(0), yDataStep);
        linePath.moveTo(waveStartX, waveStartY);

        for (int i = 0; i < dataCount - 1; i++) {
            float nextData = data.get(i + 1);
            linePath.lineTo(botLeftXOnView + (i + 1) * xStep, getYL(nextData, yDataStep));
        }
        linePaint.setColor(lineData.lineColor);
        linePaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(linePath, linePaint);
    }

    private float getYL(final float yData, float yDataStep) {
        return botLeftYOnView - (yData - yMin) * yDataStep;
    }

}