package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.rust.aboutview.R;
import com.rust.aboutview.view.TrapezoidalPb;
import com.rustfisher.view.RoundHorBatteryView;

import java.util.Timer;
import java.util.TimerTask;

public class Pb1Act extends Activity {

    private TrapezoidalPb pb1;
    private RoundHorBatteryView bat1;

    private Timer timer = new Timer();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pb1);
        pb1 = findViewById(R.id.pb1);
        bat1 = findViewById(R.id.bat_1);
        bat1.setPower(0);

        timer.schedule(new TimerTask() {
            int progress = 0;

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progress < 20) {
                            progress += 5;
                        } else if (progress < 80) {
                            progress += 9;
                        } else {
                            progress++;
                        }
                        if (progress > 100) {
                            progress = 0;
                        }
                        pb1.setProgress(progress);
                        bat1.setPower(progress);
                    }
                });
            }
        }, 1, 200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
