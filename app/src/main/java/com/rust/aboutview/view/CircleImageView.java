package com.rust.aboutview.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.rust.aboutview.R;

/**
 * A little circle who can add a background color.
 */
public class CircleImageView extends ImageView {

    private int mRadius;
    private boolean mSelected = false;
    int mColor = Color.BLACK;
    Paint mPaint = new Paint();

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyleAttr, 0);
        mRadius = a.getDimensionPixelSize(R.styleable.CircleImageView_CIVCircleRadius, dpToPx(12, getResources()));
        a.recycle();
    }

    public void setColor(int color) {
        this.mColor = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1.5f);
        if (mSelected) {
            mPaint.setColor(Color.WHITE);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mPaint);
        }
        mPaint.setColor(mColor);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius - 10, mPaint);
    }

    @Override
    public void setSelected(boolean selected) {
        mSelected = selected;
        super.setSelected(selected);
        invalidate();
    }

    public static int dpToPx(int dp, Resources resources) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

}

