package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;

import com.rust.aboutview.R;
import com.rustfisher.view.fan.FanView;


/**
 * Fan demo
 * Created by Rust Fisher on 2017/7/20.
 */
public class FanActivity extends Activity {

    private Handler mHandler = new Handler();

    private FanView mFan1;
    private FanView mFan2;
    private FanView mFan3;
    private FanView mFan4;
    private FanView mFan5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_fan);
        mFan1 = (FanView) findViewById(R.id.fanView1);
        mFan2 = (FanView) findViewById(R.id.fanView2);
        mFan3 = (FanView) findViewById(R.id.fanView3);
        mFan4 = (FanView) findViewById(R.id.fanView4);
        mFan5 = (FanView) findViewById(R.id.fanView5);
        mFanThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFanThread.interrupt();
        mFanThread = null;
        mHandler.removeCallbacksAndMessages(null);
    }

    private int mFanDegree = 0;
    private int mFanDegree1 = 360;

    private Thread mFanThread = new Thread() {
        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                try {
                    Thread.sleep(20);
                    mFanDegree--;
                    mFanDegree1 -= 4;
                    if (mFanDegree < 0) {
                        mFanDegree = 360;
                    }
                    if (mFanDegree1 < 0) {
                        mFanDegree1 = 360;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mFan1.setFanDegreeNow(mFanDegree);
                            mFan2.setFanDegreeNow(mFanDegree);
                            mFan3.setFanDegreeNow(mFanDegree1);
                            mFan4.setFanDegreeNow(mFanDegree1);
                            mFan5.setFanDegreeNow(mFanDegree - 100);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
