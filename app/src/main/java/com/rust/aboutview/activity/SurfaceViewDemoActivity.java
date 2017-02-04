package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.rust.aboutview.R;
import com.rustfisher.view.MultiLinesChartSView;

/**
 * 使用SurfaceView事例
 */
public class SurfaceViewDemoActivity extends Activity implements View.OnClickListener {

    private MultiLinesChartSView mLinesChart;
    private float mScaleX = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_surface_view_demo);
        mLinesChart = (MultiLinesChartSView) findViewById(R.id.multiLinesChart);
        findViewById(R.id.drawAlphaBtn).setOnClickListener(this);
        findViewById(R.id.drawBetaBtn).setOnClickListener(this);
        findViewById(R.id.drawThetaBtn).setOnClickListener(this);
        findViewById(R.id.drawDeltaBtn).setOnClickListener(this);
        findViewById(R.id.scaleXBtn1).setOnClickListener(this);
        findViewById(R.id.scaleXBtn2).setOnClickListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int dataLen = 4006;
                float[] alpha = new float[dataLen];
                float[] beta = new float[dataLen];
                float[] theta = new float[dataLen];
                float[] delta = new float[dataLen];

                for (int i = 0; i < alpha.length; i++) {
                    alpha[i] = (float) (8.5f * Math.cos(Math.PI * i / 90)) + 16;
                    beta[i] = (float) (10.0f * Math.sin(Math.PI * i / 180)) + 3;
                    theta[i] = (float) (3.5f * Math.cos(Math.PI * i / 60)) + 21;
                    delta[i] = 9 + (float) Math.random() * 10f;
                }

                mLinesChart.input4DataArr(alpha, beta, theta, delta);

            }
        }).start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawAlphaBtn:
                mLinesChart.setShowAlpha(!mLinesChart.isShowAlpha());
                break;
            case R.id.drawBetaBtn:
                mLinesChart.setShowBeta(!mLinesChart.isShowBeta());
                break;
            case R.id.drawThetaBtn:
                mLinesChart.setShowTheta(!mLinesChart.isShowTheta());
                break;
            case R.id.drawDeltaBtn:
                mLinesChart.setShowDelta(!mLinesChart.isShowDelta());
                break;
            case R.id.scaleXBtn1:
                mScaleX *= 2.0f;
                mLinesChart.setScaleRatioX(mScaleX);
                break;
            case R.id.scaleXBtn2:
                mScaleX /= 2.0f;
                mLinesChart.setScaleRatioX(mScaleX);
                break;

        }
    }
}
