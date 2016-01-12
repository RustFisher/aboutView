package com.rust.aboutview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.rust.aboutview.R;

public class OvalView extends View{
    private Paint mOvalPaint;
    private int mStrokeWidth = 2;
    private int padding = 3;

    //椭圆参数
    private int mOval_l;
    private int mOval_t;
    private int mOval_r;
    private int mOval_b;

    //构造
    public OvalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRocketView();

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.OvalView);

        mOval_l = a.getDimensionPixelOffset(R.styleable.OvalView_ovalLeft, padding);
        mOval_t = a.getDimensionPixelOffset(R.styleable.OvalView_ovalTop, padding);
        mOval_r = a.getDimensionPixelOffset(R.styleable.OvalView_ovalRight, 100);
        mOval_b = a.getDimensionPixelOffset(R.styleable.OvalView_ovalBottom, 100);

        a.recycle();
    }

    private void initRocketView() {
        mOvalPaint = new Paint();
        mOvalPaint.setAntiAlias(true);
        mOvalPaint.setColor(Color.BLUE);
        mOvalPaint.setStyle(Paint.Style.STROKE);
        mOvalPaint.setStrokeWidth(mStrokeWidth);
        setPadding(padding,padding,padding,padding);
    }

    public void setOvalRect(int l, int t, int r, int b){
        mOval_l = l + padding;
        mOval_t = t + padding;
        mOval_r = r;
        mOval_b = b;
        requestLayout();
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        // 绘制椭圆
        RectF re11 = new RectF(mOval_l, mOval_t, mOval_r, mOval_b);
        canvas.drawOval(re11, mOvalPaint);

//		// 绘制圆形
//		canvas.drawCircle(mCircle_x, mCircle_y, mCircle_r, mPaint);
    }

    /**
     * @see android.view.View#measure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    /**
     * Determines the width of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = mOval_r + getPaddingLeft()
                    + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * Determines the height of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = mOval_b + getPaddingTop()
                    + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }

        return result;
    }
}
