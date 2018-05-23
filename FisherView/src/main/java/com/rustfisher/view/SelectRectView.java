package com.rustfisher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 手指从左上滑到右下  画出矩形
 * Created by Rust on 2018/5/8.
 */
public class SelectRectView extends View {
    private static final String TAG = "SelectRectView";

    private float aDownX;    // action down 按下时的x值
    private float aDownY;
    private float draggingX; // 拖动过程中的x值
    private float draggingY;
    private boolean userDragging = false;
    private int rectColor = Color.BLUE;
    private Paint paint = new Paint();
    private OnSelectedListener onSelectedListener;
    private boolean working = false;

    public void setRectColor(int rectColor) {
        this.rectColor = rectColor;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public SelectRectView(Context context) {
        super(context);
    }

    public SelectRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        this.onSelectedListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (userDragging) {
            paint.setColor(rectColor);
            paint.setStrokeWidth(8);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(aDownX, aDownY, draggingX, draggingY, paint);
        }
    }

    /**
     * @param event 拦截并处理触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!working) {
            userDragging = false;
            return false; // 如果没有激活  不处理触摸事件
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                aDownX = event.getX();
                aDownY = event.getY();
                userDragging = false;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                draggingX = event.getX();
                draggingY = event.getY();
                userDragging = true;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                draggingX = event.getX();
                draggingY = event.getY();
                userDragging = false;
                invalidate();
                notifySelected();
                break;
        }
        return true; // 拦截掉触摸事件
    }

    private void notifySelected() {
        if (null != onSelectedListener) {
            onSelectedListener.onSelectedRect((draggingX > aDownX && draggingY > aDownY), aDownX, aDownY, draggingX, draggingY, getWidth(), getHeight());
        }
    }

    public interface OnSelectedListener {
        void onSelectedRect(boolean validSelected, float startX, float startY, float endX, float endY, int viewWid, int viewHeight);
    }
}
