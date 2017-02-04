/*
 * MIT License
 *
 * Copyright (c) [2017] [Rust Fisher]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.rustfisher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 折线图
 * Layout 里面不要给它设背景色
 * 没有平滑效果
 */
public class SingleLineChart extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "rustApp";

    private float yMax = 2048;
    private float yMin = -2048;
    private int maxPoint = 512;
    private float bgLineWid = 2;
    private float dataLineWid = 3;
    private int dataColor1 = Color.parseColor("#f83169");
    private int bgLineColor = Color.parseColor("#161c16");
    private int viewBgColor = Color.parseColor("#4f4f4f");

    private int[] dataBufferDrawing = new int[maxPoint];

    Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private SurfaceHolder holder;
    private DrawThread drawThread;

    public SingleLineChart(Context context) {
        this(context, null);
    }

    public SingleLineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleLineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        holder = getHolder();
        holder.addCallback(this);
        bgPaint.setStrokeWidth(bgLineWid);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setColor(bgLineColor);
        linePaint.setStrokeWidth(dataLineWid);
        linePaint.setStyle(Paint.Style.STROKE);
    }

    public void setDataLineWid(float widDp) {
        dataLineWid = FSUtils.dp2px(widDp, this);
    }

    public void clearData() {
        dataBufferDrawing = new int[maxPoint];
        inputData(new short[]{0}, 1);
    }

    public void setyMin(float yMin) {
        this.yMin = yMin;
    }

    public void setyMax(float yMax) {
        this.yMax = yMax;
    }

    public void setMaxPoint(int maxPoint) {
        this.maxPoint = maxPoint;
        dataBufferDrawing = new int[maxPoint];
    }

    /**
     * 外面给的数据直接传到绘图子线程中
     */
    public void inputData(short[] input, int len) {
        if (null != drawThread) {
            drawThread.inputData(input, len);
        }
    }

    public void inputData(int[] input, int len) {
        if (null != drawThread) {
            drawThread.inputData(input, len);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(holder);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.closeThread();
        Log.d(TAG, "surfaceDestroyed");
    }

    class DrawThread extends Thread {
        private SurfaceHolder holder;
        private boolean mmRunning;
        boolean isPause = false;

        public DrawThread(SurfaceHolder holder) {
            this.holder = holder;
            mmRunning = true;
        }

        public void inputData(int[] input, int len) {
            short[] data = new short[input.length];
            for (int i = 0; i < data.length; i++) {
                data[i] = (short) input[i];
            }
            inputData(data, len);
        }

        public void inputData(short[] input, int len) {
            if (null == input) {
                return;
            }
            if (input.length == 0 || len == 0) {
                return;
            }

            if (len == maxPoint) {
                for (int i = 0; i < maxPoint; i++) {
                    dataBufferDrawing[i] = input[i];
                }
            } else if (len < maxPoint) {
                int preLen = dataBufferDrawing.length - len;
                for (int i = 0; i < preLen; i++) {
                    dataBufferDrawing[i] = dataBufferDrawing[i + len];// 移动旧数据到前面
                }
                for (int i = 0; i < len; i++) {
                    dataBufferDrawing[i + preLen] = input[i];
                }
            } else {
                for (int i = 0; i < maxPoint; i++) {
                    dataBufferDrawing[i] = input[len - maxPoint + i];
                }
            }
            resumeThread();
        }


        @Override
        public void run() {
            while (mmRunning && !isInterrupted()) {
                if (!isPause) {
                    Canvas canvas = null;
                    try {
                        synchronized (holder) {
                            canvas = holder.lockCanvas();
                            canvas.drawColor(viewBgColor);
                            int viewWid = getWidth();
                            int viewHeight = getHeight() - 2;
                            int viewYStart = 2;

                            // Draw bg lines
                            bgPaint.setStrokeWidth(bgLineWid);
                            bgPaint.setStyle(Paint.Style.STROKE);
                            bgPaint.setColor(bgLineColor);
                            canvas.drawLine(0, viewYStart, viewWid, viewYStart, bgPaint);
                            canvas.drawLine(0, (viewHeight - viewYStart) / 5.0f, viewWid, (viewHeight - viewYStart) / 5.0f, bgPaint);
                            canvas.drawLine(0, (viewHeight - viewYStart) / 5.0f * 2, viewWid, (viewHeight - viewYStart) / 5.0f * 2, bgPaint);
                            canvas.drawLine(0, (viewHeight - viewYStart) / 5.0f * 3, viewWid, (viewHeight - viewYStart) / 5.0f * 3, bgPaint);
                            canvas.drawLine(0, (viewHeight - viewYStart) / 5.0f * 4, viewWid, (viewHeight - viewYStart) / 5.0f * 4, bgPaint);
                            canvas.drawLine(0, viewHeight, viewWid, viewHeight, bgPaint);

                            // Draw data
                            linePaint.setColor(dataColor1);
                            float xStep = viewWid / ((float) maxPoint - 1);// 必须转换为float以保证精度
                            for (int i = 0; i < maxPoint - 1; i++) {
                                linePaint.setStrokeWidth(dataLineWid);
                                canvas.drawLine(xStep * i, getYCoor(viewYStart, viewHeight, dataBufferDrawing[i]),
                                        xStep * (i + 1), getYCoor(viewYStart, viewHeight, dataBufferDrawing[i + 1]), linePaint);
                                linePaint.setStrokeWidth(dataLineWid / 2);
                                canvas.drawPoint(xStep * i, getYCoor(viewYStart, viewHeight, dataBufferDrawing[i]), linePaint);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (canvas != null) {
                            holder.unlockCanvasAndPost(canvas);// 结束锁定画图，并提交改变。
                        }
                        pauseThread();
                    }
                } else {
                    onThreadWait();
                }
            }
        }

        /**
         * 暂停线程
         */
        public synchronized void pauseThread() {
            isPause = true;
        }

        /**
         * 线程等待,不提供给外部调用
         */
        private void onThreadWait() {
            try {
                synchronized (this) {
                    this.wait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 线程继续运行
         */
        public synchronized void resumeThread() {
            isPause = false;
            this.notify();
        }

        /**
         * 关闭线程
         */
        public synchronized void closeThread() {
            try {
                mmRunning = false;
                notify();
                interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private float getYCoor(int viewYStart, int viewYLimit, int input) {
            if (input >= yMax) {
                return viewYStart;
            } else if (input <= yMin) {
                return viewYLimit;
            }
            return viewYLimit - (viewYLimit - viewYStart) / (yMax - yMin) * (input - yMin);
        }


    }
}
