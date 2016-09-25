package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;

import com.rust.aboutview.R;
import com.rustfisher.fisherandroidchart.DashboardProgressView;

public class DashboardActivity extends Activity {

    private DashboardProgressView mDash1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dashboard);

        mDash1 = (DashboardProgressView) findViewById(R.id.dashboard_1);

        mDash1.setHeaderTitle("dash1");

    }
}
