package com.rust.aboutview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.rust.aboutview.view.OptionCircle;


public class CirclesActivity extends Activity {

    private static final int CENTER_CIRCLE = 101;

    OptionCircle centerCircle;

    CircleHandler handler = new CircleHandler(this);

    /**
     * Handler : update UI
     */
    static class CircleHandler extends Handler {
        CirclesActivity activity;

        CircleHandler(CirclesActivity a) {
            activity = a;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CENTER_CIRCLE: {

                }

            }
        }
    }

    class UpdateCircles implements Runnable {

        @Override
        public void run() {
            while (true) {
                Message message = new Message();
                message.what = CENTER_CIRCLE;
                handler.sendEmptyMessage(message.what);
                try {
                    Thread.sleep(10); // pause
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_choose);
        centerCircle = (OptionCircle) findViewById(R.id.center_circle);
        centerCircle.invalidate();
        centerCircle.setRadius(58);

    }


}
