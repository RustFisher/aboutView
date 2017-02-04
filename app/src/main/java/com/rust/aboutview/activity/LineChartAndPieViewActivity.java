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

package com.rust.aboutview.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.rust.aboutview.R;
import com.rustfisher.view.PieView;
import com.rustfisher.view.SingleLineChart;

import java.util.Random;

/**
 * Line chart
 * Created by Rust on 2017/2/4.
 */
public class LineChartAndPieViewActivity extends Activity {

    private SingleLineChart mLineChart1;
    private PieView mPieView1;
    private PieView mPieView2;
    private DataThread mDataThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_line_chart);
        initUI();
        initUtils();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataThread.interrupt();
    }

    private void initUI() {
        mLineChart1 = (SingleLineChart) findViewById(R.id.lineChart1);
        mPieView1 = (PieView) findViewById(R.id.pieView1);
        mPieView2 = (PieView) findViewById(R.id.pieView2);

        mLineChart1.setDataLineWid(1);

        mPieView1.addPieItem(new PieView.PieItem(Color.RED, 1));
        mPieView1.addPieItem(new PieView.PieItem(Color.BLUE, 4));
        mPieView1.addPieItem(new PieView.PieItem(Color.GREEN, 5));

        mPieView2.addPieItem(new PieView.PieItem(Color.RED, 5));
        mPieView2.addPieItem(new PieView.PieItem(Color.BLUE, 1));
        mPieView2.addPieItem(new PieView.PieItem(Color.GREEN, 4));

    }

    private void initUtils() {
        mDataThread = new DataThread();
        mDataThread.start();
    }

    class DataThread extends Thread {

        @Override
        public void run() {
            super.run();
            final Random random = new Random(System.currentTimeMillis());
            while (!isInterrupted()) {
                try {
                    // SurfaceView can change UI in other thread
                    mLineChart1.inputData(new int[]{random.nextInt(4096) - 2048}, 1);
                    Thread.sleep(16L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
