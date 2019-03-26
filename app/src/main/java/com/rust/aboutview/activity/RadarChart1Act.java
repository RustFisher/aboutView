package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.rust.aboutview.R;
import com.rust.aboutview.view.RadarUxChartView;
import com.rust.aboutview.view.TrapezoidalPb;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 雷达图示例
 * Created on 2019-3-26
 */
public class RadarChart1Act extends Activity {
    private static final String TAG = "RadarChart";

    private RadarUxChartView radar1;
    private TextView tv1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_radar_1);
        radar1 = findViewById(R.id.r1);
        radar1.setListener(new RadarUxChartView.UxListener() {
            @Override
            public void onDateSelected(float mData, float lData, float rData) {
                Log.d(TAG, "onDateSelected: " + mData + ", " + lData + ", " + rData);
            }
        });

        tv1 = findViewById(R.id.s_tv);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radar1.setAbleDrag(!radar1.isAbleDrag());
                updateTv();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTv();
    }

    private void updateTv() {
        tv1.setText(radar1.isAbleDrag() ? "可拖动" : "已锁定");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
