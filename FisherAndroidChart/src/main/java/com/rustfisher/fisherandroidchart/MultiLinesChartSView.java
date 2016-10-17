package com.rustfisher.fisherandroidchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * 多条折线图
 */
public class MultiLinesChartSView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private static final String TAG = "rustApp";

    private static final float Y_MAX = 32.0f;
    private static final float SCALE_RATIO_X_MIN = 1.0F;
    private static final float SCALE_RATIO_X_MAX = 10.0F;
    private static final float ZOOM_X_DIFFICULTY = 500.00F; // 缩放难度，越大缩放越慢
    private static final float MOVE_X_MIN_DIS = 2.0f;       // 手指每次拖动的最小距离
    private static final float SLIDER_MIN_WID = 100.0f;     // 滑块最小宽度
    private static final float MOVE_DATA_SLOW_RATIO = 3.0F; // 滑动图表时不让图表走太快

    /**
     * 用户的动作
     */
    enum MOTION_TYPE {
        DRAG, ZOOM
    }

    private MOTION_TYPE motionType = MOTION_TYPE.DRAG;

    private int bgLineWid = 2;
    private int dataLineWid = 3;
    private float sliderHeight = dpToPx(20);// 滑块高度
    private float scaleRatioX = SCALE_RATIO_X_MIN;// X轴方向缩放

    private float[] alphaArr = {};// 存放数据的地方
    private float[] betaArr = {};
    private float[] thetaArr = {};
    private float[] deltaArr = {};

    private int alphaColor = Color.parseColor("#31abf8");// 绘制折线的颜色
    private int betaColor = Color.parseColor("#f1d913");
    private int thetaColor = Color.parseColor("#8961ff");
    private int deltaColor = Color.parseColor("#ad45b8");
    private int bgLineColor = Color.parseColor("#d6dcd6");
    private int bgScrollColor = Color.parseColor("#e0e9ef");
    private int sliderColor = Color.parseColor("#abc3b3");
    private int bgSurfaceViewColor = Color.WHITE;

    private boolean showAlpha = true;
    private boolean showBeta = true;
    private boolean showTheta = true;
    private boolean showDelta = true;

    Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Path dataPath = new Path();

    private SurfaceHolder holder;
    private DrawThread drawThread;

    private int gridCount = 10;// 放大到最大后格子的个数
    private int viewWid;
    private int viewHeight;
    private int drawDataWid;// 数据卷轴总宽度
    private float drawDataStartX;
    private float drawDataLeftDis;
    private float drawDataRightDis;
    private float sliderWid;// 滑块宽度
    private float sliderStartX;// 滑块左下标

    /**
     * 缩放相关属性
     */
    int activePointerId1;
    int activePointerId2;
    float xDis;
    float preScaleRatioX;

    private float pointerFirstDownX;
    private float pointerFirstDownY;
    private boolean dragSlider = false;


    public MultiLinesChartSView(Context context) {
        this(context, null);
    }

    public MultiLinesChartSView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiLinesChartSView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        holder = getHolder();
        holder.addCallback(this);
        bgPaint.setStrokeWidth(bgLineWid);
        bgPaint.setColor(bgLineColor);
        linePaint.setStrokeWidth(dataLineWid);
        linePaint.setStyle(Paint.Style.STROKE);
        setOnTouchListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = h;
        viewWid = w;
        drawDataWid = Math.max(viewWid, (int) (viewWid * scaleRatioX));
        drawDataStartX = 0;
        drawDataLeftDis = 0;
        drawDataRightDis = 0;
        sliderWid = viewWid;
        sliderStartX = 0;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
        drawThread = new DrawThread(holder);// 创建一个绘图线程
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.closeThread();
        Log.d(TAG, "surfaceDestroyed");
    }

    public void inputDataAndColor(float[] alpha, int alphaColor,
                                  float[] beta, int betaColor,
                                  float[] theta, int thetaColor,
                                  float[] delta, int deltaColor) {
        this.alphaColor = alphaColor;
        this.betaColor = betaColor;
        this.thetaColor = thetaColor;
        this.deltaColor = deltaColor;
        input4DataArr(alpha, beta, theta, delta);
    }

    public void input4DataArr(float[] alpha, float[] beta, float[] theta, float[] delta) {
        if (null == alpha || null == beta || null == theta || null == delta) {
            return;
        }
        this.alphaArr = alpha;
        this.betaArr = beta;
        this.thetaArr = theta;
        this.deltaArr = delta;
        resumeDrawThread();
    }

    public void setScaleRatioX(float ratioX) {
        this.scaleRatioX = ratioX;
        if (this.scaleRatioX < SCALE_RATIO_X_MIN) {
            this.scaleRatioX = SCALE_RATIO_X_MIN;
        } else if (this.scaleRatioX > SCALE_RATIO_X_MAX) {
            this.scaleRatioX = SCALE_RATIO_X_MAX;
        }
        drawDataWid = Math.max(viewWid, (int) (viewWid * scaleRatioX));
        if (drawDataLeftDis == drawDataRightDis) {
            drawDataLeftDis = (drawDataWid - viewWid) / 2;
            drawDataRightDis = drawDataLeftDis;
        } else {
            float dLen = drawDataWid - viewWid;// 总共变化的长度要按比例分配给左边和右边
            float sumLen = drawDataLeftDis + drawDataRightDis;
            drawDataLeftDis = dLen * (drawDataLeftDis / sumLen);
            drawDataRightDis = dLen * (drawDataRightDis / sumLen);
        }
        drawDataStartX = 0 - drawDataLeftDis;
        resetSlider();
        resumeDrawThread();
    }

    // 计算滑块宽度和起始坐标
    private void resetSlider() {
        sliderWid = viewWid * viewWid / drawDataWid;
        if (sliderWid < SLIDER_MIN_WID) {
            sliderWid = SLIDER_MIN_WID;
        }
        sliderStartX = drawDataLeftDis / drawDataWid * viewWid;
    }

    public float getScaleRatioX() {
        return this.scaleRatioX;
    }

    public boolean isShowDelta() {
        return showDelta;
    }

    public boolean isShowTheta() {
        return showTheta;
    }

    public boolean isShowBeta() {
        return showBeta;
    }

    public boolean isShowAlpha() {
        return showAlpha;
    }

    public void setShowAlpha(boolean show) {
        this.showAlpha = show;
        resumeDrawThread();
    }

    public void setShowBeta(boolean showBeta) {
        this.showBeta = showBeta;
        resumeDrawThread();
    }

    public void setShowTheta(boolean showTheta) {
        this.showTheta = showTheta;
        resumeDrawThread();
    }

    public void setShowDelta(boolean showDelta) {
        this.showDelta = showDelta;
        resumeDrawThread();
    }

    private void resumeDrawThread() {
        if (null != drawThread) {
            drawThread.resumeThread();
        }
    }

    class DrawThread extends Thread {

        private SurfaceHolder mmHolder;
        private boolean mmRunning;
        private boolean mmIsPause;

        public DrawThread(SurfaceHolder holder) {
            this.mmHolder = holder;
            mmRunning = true;
        }

        @Override
        public void run() {
            while (mmRunning && !isInterrupted()) {
                if (!mmIsPause) {
                    Canvas canvas = null;
                    try {
                        synchronized (mmHolder) {
                            canvas = holder.lockCanvas();        // 锁定画布，一般在锁定后就可以通过其返回的画布对象Canvas，在其上面画图等操作了。
                            canvas.drawColor(bgSurfaceViewColor);// 设置画布背景颜色

//                            Log.d(TAG, "viewWid: " + viewWid + ";  drawDataWid " + drawDataWid + ";  drawStartX " + drawStartX);

                            float viewYStart = sliderHeight;

                            // Draw bg lines
                            drawBgLines(canvas, viewWid, viewHeight, viewYStart, drawDataWid, drawDataStartX);

                            // Draw scroll bar and bg
                            bgPaint.setColor(bgScrollColor);
                            canvas.drawRect(0, 0, viewWid, sliderHeight, bgPaint);
                            bgPaint.setColor(sliderColor);
                            canvas.drawRect(sliderStartX, 0, sliderStartX + sliderWid, sliderHeight, bgPaint);

                            // Draw data
                            linePaint.setStrokeWidth(dataLineWid);
                            linePaint.setStyle(Paint.Style.STROKE);
                            if (showDelta) {
                                linePaint.setColor(deltaColor);
                                drawDataLinePath(canvas, drawDataWid, drawDataStartX, viewHeight, viewYStart, deltaArr);
                            }
                            if (showTheta) {
                                linePaint.setColor(thetaColor);
                                drawDataLinePath(canvas, drawDataWid, drawDataStartX, viewHeight, viewYStart, thetaArr);
                            }
                            if (showBeta) {
                                linePaint.setColor(betaColor);
                                drawDataLinePath(canvas, drawDataWid, drawDataStartX, viewHeight, viewYStart, betaArr);
                            }
                            if (showAlpha) {
                                linePaint.setColor(alphaColor);
                                drawDataLinePath(canvas, drawDataWid, drawDataStartX, viewHeight, viewYStart, alphaArr);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (canvas != null) {
                            mmHolder.unlockCanvasAndPost(canvas);// 结束锁定画图，并提交改变。
                        }
                        pauseThread();
                    }
                } else {
                    onThreadWait();
                }
            }
        }


        /**
         * 绘制网格背景
         *
         * @param viewWid     画布真实宽度
         * @param viewHeight  画布高度
         * @param viewYStart  Y轴起始坐标
         * @param drawDataWid 实际需要的宽度
         * @param drawStartX  画数据的起始X坐标
         */
        private void drawBgLines(Canvas canvas, int viewWid, int viewHeight, float viewYStart, int drawDataWid, float drawStartX) {
            bgPaint.setStrokeWidth(bgLineWid);
            bgPaint.setColor(bgLineColor);
            canvas.drawLine(0, viewYStart, viewWid, viewYStart, bgPaint);
            canvas.drawLine(0, viewHeight, viewWid, viewHeight, bgPaint);
            for (float i = 1; i <= 5.0; i++) {
                canvas.drawLine(0, (viewHeight - viewYStart) / 5.0f * i + viewYStart, viewWid,
                        (viewHeight - viewYStart) / 5.0f * i + viewYStart, bgPaint);
            }
            if (scaleRatioX == SCALE_RATIO_X_MAX) {
                float gridStep = viewWid / (float) gridCount;// 背景格子宽度
                for (int i = 0; i < gridCount; i++) {
                    canvas.drawLine(gridStep * i, viewYStart, gridStep * i, viewHeight, bgPaint);
                }
            }
        }

        public synchronized void pauseThread() {
            mmIsPause = true;
        }

        /**
         * 线程等待,不提供给外部调用
         */
        private void onThreadWait() {
            try {
                synchronized (this) {
//                    Log.d(TAG, "DrawThread waiting");
                    this.wait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public synchronized void resumeThread() {
            mmIsPause = false;
            this.notify();
        }

        public synchronized void closeThread() {
            try {
                mmRunning = false;
                notify();
                interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 绘制 path 数据
        private void drawDataLinePath(Canvas canvas, int wholeWid, float drawStart, int viewHeight, float viewYStart, float[] dataUp) {
            int dataLen = dataUp.length;
            if (dataLen > 0) {
                float xStep = wholeWid / (float) (dataLen - 1);// dx 横轴单位长度
                int drawStartIndex = 0;
                int drawEndIndex = dataLen - 1;
                if (drawStart < 0 && Math.abs(drawStart) > xStep) {
                    drawStartIndex = (int) (Math.abs(drawStart) / xStep);
                }
                if (drawStart < 0) {
                    int end = (int) ((Math.abs(drawStart) + viewWid) / xStep) + 1;
                    drawEndIndex = Math.min(end, drawEndIndex);
                }
                dataPath.reset();
                dataPath.moveTo(drawStart, getYCoor(viewYStart, viewHeight, dataUp[drawStartIndex]));
                for (int i = drawStartIndex; i <= drawEndIndex; i++) {
                    dataPath.lineTo(xStep * i + drawStart, getYCoor(viewYStart, viewHeight, dataUp[i]));
                }
                canvas.drawPath(dataPath, linePaint);
            }
        }

        private float getYCoor(float viewYStart, int viewYLimit, float input) {
            if (input >= Y_MAX) {
                return viewYStart;
            } else if (input == 0) {
                return viewYLimit;
            }
            return viewYLimit - (viewYLimit - viewYStart) / Y_MAX * input;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:// 1 finger
                pointerFirstDownX = event.getX();
                pointerFirstDownY = event.getY();
                dragSlider = pointerFirstDownY <= sliderHeight;
                motionType = MOTION_TYPE.DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:// 两根手指
                int pointerCount = event.getPointerCount();
                if (pointerCount == 2) {
                    motionType = MOTION_TYPE.ZOOM;
                    // Get the pointer ID
                    activePointerId1 = event.getPointerId(0);
                    activePointerId2 = event.getPointerId(1);

                    // Use the pointer ID to find the index of the active pointer
                    // and fetch its position
                    int pointerIndex1 = event.findPointerIndex(activePointerId1);
                    int pointerIndex2 = event.findPointerIndex(activePointerId2);
                    // Get the pointer's current position
                    float x1 = event.getX(pointerIndex1);
                    float y1 = event.getY(pointerIndex1);
                    float x2 = event.getX(pointerIndex2);
                    float y2 = event.getY(pointerIndex2);
                    xDis = Math.abs(x1 - x2);// 两个手指之间的距离
                    preScaleRatioX = getScaleRatioX();// 存储两个手指点击时的X轴缩放比例
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int movePointerCount = event.getPointerCount();
                if (movePointerCount == 2 && isZooming()) {
                    // Get the pointer ID
                    activePointerId1 = event.getPointerId(0);
                    activePointerId2 = event.getPointerId(1);

                    int pointerIndex1 = event.findPointerIndex(activePointerId1);
                    int pointerIndex2 = event.findPointerIndex(activePointerId2);

                    float x1 = event.getX(pointerIndex1);
                    float y1 = event.getY(pointerIndex1);
                    float x2 = event.getX(pointerIndex2);
                    float y2 = event.getY(pointerIndex2);
                    float dxDis = Math.abs(x1 - x2) - xDis;// 两个手指之间的距离的变化
                    setScaleRatioX(preScaleRatioX + dxDis / ZOOM_X_DIFFICULTY);
                } else if (movePointerCount == 1 && isDragging()) {
                    float moveX = event.getX() - pointerFirstDownX;
                    pointerFirstDownX = event.getX();// 重置起始点击位置
                    if (dragSlider) {
                        moveSlider(-moveX);
                    } else {
                        moveDataScroll(moveX);
                    }
                }
                break;
        }

        return true;
    }

    private void moveSlider(float dx) {
        if (Math.abs(dx) < MOVE_X_MIN_DIS) {
            return;
        }
        float targetDataStartX = drawDataStartX + dx * scaleRatioX;
        updateDataScroll(targetDataStartX);
    }

    /**
     * 移动数据卷轴
     */
    private void moveDataScroll(float dx) {
        if (Math.abs(dx) < MOVE_X_MIN_DIS) {
            return;
        }
        updateDataScroll(drawDataStartX + dx * (scaleRatioX / MOVE_DATA_SLOW_RATIO));
    }

    private void updateDataScroll(float targetDataStartX) {
        if (targetDataStartX > 0) {
            targetDataStartX = 0;
        } else if (targetDataStartX <= (viewWid - drawDataWid)) {
            targetDataStartX = viewWid - drawDataWid;
        }
        drawDataStartX = targetDataStartX;
        drawDataLeftDis = Math.abs(targetDataStartX);
        drawDataRightDis = drawDataWid - viewWid - drawDataLeftDis;

        resetSlider();
        resumeDrawThread();
    }

    private boolean isZooming() {
        return MOTION_TYPE.ZOOM.equals(motionType);
    }

    private boolean isDragging() {
        return MOTION_TYPE.DRAG.equals(motionType);
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
