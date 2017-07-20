package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_fan);
        mFan1 = (FanView) findViewById(R.id.fanView1);
        mFan2 = (FanView) findViewById(R.id.fanView2);
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

    private Thread mFanThread = new Thread() {
        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                try {
                    Thread.sleep(30);
                    mFanDegree--;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mFan1.setFanDegreeNow(mFanDegree);
                            mFan2.setFanDegreeNow(mFanDegree);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
