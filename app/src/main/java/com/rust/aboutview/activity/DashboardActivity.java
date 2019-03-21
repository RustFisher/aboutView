package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.rust.aboutview.R;
import com.rustfisher.view.DashboardProgressView;
import com.rustfisher.view.DashboardRoundView;

import java.util.Timer;
import java.util.TimerTask;

public class DashboardActivity extends Activity {

    private DashboardProgressView mDash1;
    private DashboardProgressView mDash2;
    private DashboardRoundView mDr1;
    private DashHandler mDashHandler;

    private Timer mTimer1;
    private int mVal3 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dashboard);
        mDashHandler = new DashHandler(this);

        mDash1 = findViewById(R.id.dashboard_1);
        mDash2 = findViewById(R.id.dashboard_2);
        mDr1 = findViewById(R.id.dash_r1);

        mDash1.setHeaderTitle("仪表盘1");
        mDr1.setHeaderTitle("仪表盘3");

        mTimer1 = new Timer();
        mTimer1.schedule(new TimerTask() {
            @Override
            public void run() {
                mVal3 += Math.random() * 20;
                if (mVal3 > 300) {
                    mVal3 = 0;
                }
                mDr1.updateData(mVal3, true);
            }
        }, 200, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dashThread1.start();
        dashThread2.start();
    }

    @Override
    protected void onDestroy() {
        dashThread1.interrupt();
        dashThread2.interrupt();
        super.onDestroy();
        mTimer1.cancel();
        mDr1.destroy();
    }

    private Thread dashThread1 = new Thread() {
        @Override
        public void run() {
            for (int i = 0; i < 104; i++)
                try {
                    Thread.sleep(500L);
                    mDashHandler.obtainMessage(DashHandler.REFRESH_DASH_1, i, -1).sendToTarget();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
        }
    };

    private Thread dashThread2 = new Thread() {
        @Override
        public void run() {
            for (int i = 104; i > 0; i--)
                try {
                    Thread.sleep(50L);
                    mDashHandler.obtainMessage(DashHandler.REFRESH_DASH_2, i, -1).sendToTarget();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
        }
    };


    static class DashHandler extends Handler {
        public static final int REFRESH_DASH_1 = 100;
        public static final int REFRESH_DASH_2 = 200;

        static DashboardActivity act;

        public DashHandler(DashboardActivity a) {
            act = a;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_DASH_1:
                    act.mDash1.setRealTimeValue(msg.arg1);
                    break;
                case REFRESH_DASH_2:
                    act.mDash2.setRealTimeValue(msg.arg1);
                    break;
            }
        }
    }
}
