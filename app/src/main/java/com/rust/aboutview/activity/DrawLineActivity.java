package com.rust.aboutview.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.rust.aboutview.R;
import com.rust.aboutview.view.MarkView;

public class DrawLineActivity extends Activity {

    TextView markText;
    TextView coordinateText;
    MarkView markView;
    MarkHandler handler = new MarkHandler(this);
    static float x;
    static float y;

    static class MarkHandler extends Handler {
        DrawLineActivity a;

        MarkHandler(DrawLineActivity drawLineActivity) {
            a = drawLineActivity;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1000: {
                    a.markView.setTouchX(x);
                    a.markView.setTouchY(y);
                    a.markView.invalidate();
                }
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_line);
        markView = (MarkView) findViewById(R.id.draw_line);
        coordinateText = (TextView) findViewById(R.id.coordinate);
        markText = (TextView) findViewById(R.id.animation_tv);
        markText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator oa = ObjectAnimator.ofFloat(markText, "alpha", 0f, 1f);
                oa.setDuration(3000);
                oa.start();
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int[] markLocations = {0, 0};
        markView.getLocationOnScreen(markLocations);
        x = event.getX() - markLocations[0];
        y = event.getY() - markLocations[1];
        coordinateText.setText(String.format("x: %s, y:%s", x, y));
        handler.sendEmptyMessage(1000);
        return false;
    }
}
