package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.rust.aboutview.R;
import com.rustfisher.fisherandroidchart.DashboardProgressView;

public class DashboardActivity extends Activity {

    private DashboardProgressView mDash1;
    private DashboardProgressView mDash2;
    private DashHandler mDashHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dashboard);
        mDashHandler = new DashHandler(this);

        mDash1 = (DashboardProgressView) findViewById(R.id.dashboard_1);
        mDash2 = (DashboardProgressView) findViewById(R.id.dashboard_2);

        mDash1.setHeaderTitle("dash1");
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
            for (int i = 104; i > -20; i--)
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
