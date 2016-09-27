package com.rust.aboutview.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.rust.aboutview.R;
import com.rustfisher.fisherandroidchart.BulbView;

public class BulbViewActivity extends Activity {

    private BulbView bulbView1;
    private BulbView bulbView2;
    BulbView bulbView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_bulb_view);

        bulbView1 = (BulbView) findViewById(R.id.bulb_view_1);
        bulbView2 = (BulbView) findViewById(R.id.bulb_view_2);
        bulbView3 = (BulbView) findViewById(R.id.bulb_view_3);

        bulbView2.setWholeSizeRatio(1.6f);
        bulbView3.setWholeSizeRatio(2.3f);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bulbView1.setBotLineLenDp(20 + finalI);
                            bulbView1.setShineColor(Color.rgb(20 + finalI * 6, 0, 0));
                            bulbView1.setBulbOutlineColor(Color.rgb(0, 0, 20 + finalI * 6));
                        }
                    });
                }
            }
        }).start();
    }
}
