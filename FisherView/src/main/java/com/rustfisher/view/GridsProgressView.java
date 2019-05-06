package com.rustfisher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;


/**
 * 格子进度条
 * 画一堆格子
 * Created by Rust on 2018-5-6
 */
public class GridsProgressView extends View {
    private static final String TAG = "GridsProgressView";
    private static final int MODE_DISABLE = 100; // 禁用状态
    private static final int MODE_ABLE = 200;    // 正常工作状态
    private int mode = MODE_DISABLE;

    private int totalCubesCount = 12; // 一共这么多个格子
    private int cubeCount = 0;       // 格子数值

    private int botCubeColor = Color.parseColor("#9ea1aa");
    private int botCubeDisableColor = Color.parseColor("#555a69");
    private int valueCubeColor = Color.parseColor("#ff8f22");

    final float cubeGapWidRatio = 0.8f; // 方格之间空隙宽度对比方格宽度
    int viewHorPaddingPx = 4; // 水平方向上的间隙
    int viewVerPaddingPx = 4; // 水平方向上的间隙
    float strokeWid = 2;

    float vWidth = 0;
    float vHeight = 50;

    private Paint paint;
    private Path valPath; // 电量路径

    public GridsProgressView(Context context) {
        this(context, null);
    }

    public GridsProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        vWidth = getWidth();
        vHeight = getHeight();

        final float cubeWid = (vWidth - 2 * viewHorPaddingPx) / (totalCubesCount + (totalCubesCount - 1) * cubeGapWidRatio);
        final float gapWid = cubeWid * cubeGapWidRatio;
        if (mode == MODE_ABLE) {
            drawCubes(canvas, cubeWid, gapWid, totalCubesCount, botCubeColor);
            drawCubes(canvas, cubeWid, gapWid, cubeCount, valueCubeColor);
        } else if (mode == MODE_DISABLE) {
            drawCubes(canvas, cubeWid, gapWid, totalCubesCount, botCubeDisableColor);
        }
    }

    /**
     * 绘制方格
     *
     * @param canvas     画布
     * @param cubeWid    单个方格的宽度
     * @param gapWid     方格之间的间隙
     * @param cubesCount 要画多少个方格
     * @param cubeColor  方格的颜色
     */
    private void drawCubes(Canvas canvas, float cubeWid, float gapWid, int cubesCount, int cubeColor) {
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(strokeWid);
        paint.setColor(cubeColor);
        for (int i = 0; i < cubesCount; i++) {
            final float left = viewHorPaddingPx + i * (cubeWid + gapWid);
            final float bottom = vHeight - viewVerPaddingPx;
            valPath.reset();
            valPath.moveTo(left, bottom);
            valPath.lineTo(left, viewVerPaddingPx);
            valPath.lineTo(left + cubeWid, viewVerPaddingPx);
            valPath.lineTo(left + cubeWid, vHeight - viewVerPaddingPx);
            valPath.close();
            canvas.drawPath(valPath, paint);
        }
    }


    public void init() {
        if (paint == null) {
            paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setAntiAlias(true);
        }
        valPath = new Path();
    }

    public void setCubeCount(int count) {
        this.cubeCount = count;
        if (cubeCount > totalCubesCount) {
            cubeCount = totalCubesCount;
        }
        invalidate();
    }

    public int getTotalCubesCount() {
        return totalCubesCount;
    }

    public void enableMode() {
        mode = MODE_ABLE;
        invalidate();
    }

    public void disableMode() {
        mode = MODE_DISABLE;
        invalidate();
    }

}