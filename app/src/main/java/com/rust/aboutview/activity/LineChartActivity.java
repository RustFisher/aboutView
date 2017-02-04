package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;

import com.rust.aboutview.R;
import com.rustfisher.view.SingleLineChart;

import java.util.Random;

/**
 * Line chart
 * Created by Rust on 2017/2/4.
 */
public class LineChartActivity extends Activity {

    private SingleLineChart mLineChart1;
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
        mLineChart1.setDataLineWid(1);
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
