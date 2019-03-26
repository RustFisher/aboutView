package com.rust.aboutview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 梯形进度条
 * Created on 2019-3-25
 */
public class TrapezoidalPb extends View {

    private int mViewHeight;
    private int mViewWidth;

    private float maxProgress = 100f;
    private float curProgress = 0f;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();
    private int mainColor = Color.parseColor("#f65212");
    float topXGapInTrap = 30;// 相对于内容梯形的值
    final int degree1 = 35;
    Rect cRect = new Rect(); // 代表可绘制的区域

    public TrapezoidalPb(Context context) {
        super(context);
    }

    public TrapezoidalPb(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TrapezoidalPb(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewHeight = h;
        mViewWidth = w;
        final int padding = 3;
        cRect.top = padding;
        cRect.left = padding;
        cRect.bottom = mViewHeight - padding;
        cRect.right = mViewWidth - padding;
        topXGapInTrap = (float) (cRect.height() * Math.tan(Math.toRadians(degree1)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawProgress(canvas);
    }

    public void setProgress(int p) {
        curProgress = p;
        invalidate();
    }

    private void drawProgress(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        path.reset();
        path.moveTo(cRect.left, cRect.bottom); // 左下角
        if (curProgress == 0) {
            return;
        }
        float xLenInTrap = cRect.width() * (curProgress / maxProgress); // 梯形上的长度 相对值
        if (xLenInTrap < topXGapInTrap) {
            float y1 = cRect.top + cRect.height() - (float) (xLenInTrap / Math.tan(Math.toRadians(degree1))); // 绝对坐标值
            path.lineTo(cRect.left + xLenInTrap, y1);
            path.lineTo(cRect.left + xLenInTrap, cRect.bottom);
            path.close();
        } else if (xLenInTrap < cRect.width() - topXGapInTrap) {
            path.lineTo(cRect.left + topXGapInTrap, cRect.top); // 左上顶点
            path.lineTo(cRect.left + xLenInTrap, cRect.top);
            path.lineTo(cRect.left + xLenInTrap, cRect.bottom);
            path.close();
        } else {
            path.lineTo(cRect.left + topXGapInTrap, cRect.top); // 左上顶点
            path.lineTo(cRect.right - topXGapInTrap, cRect.top);// 右上顶点
            float y2 = cRect.top + cRect.height() - (float) ((cRect.width() - xLenInTrap) / Math.tan(Math.toRadians(degree1)));
            path.lineTo(xLenInTrap, y2);
            path.lineTo(xLenInTrap, cRect.bottom);
            path.close();
        }
        canvas.drawPath(path, paint);
    }

    private void drawBg(Canvas canvas) {
        paint.setColor(mainColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        path.reset();
        path.moveTo(cRect.left, cRect.bottom); // 左下角
        path.lineTo(cRect.left + topXGapInTrap, cRect.top); // 左上顶点
        path.lineTo(cRect.right - topXGapInTrap, cRect.top);// 右上顶点
        path.lineTo(cRect.right, cRect.bottom);// 右下角
        path.close();
        canvas.drawPath(path, paint);
    }
}
