package com.rust.aboutview;

import android.app.Activity;
import android.os.Bundle;

import com.rust.aboutview.view.OvalView;

public class CirclesActivity extends Activity {
    OvalView ovalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_choose);
        ovalView = (OvalView) findViewById(R.id.rocket_view);
        ovalView.setOvalRect(0, 0, 500, 100);

    }
}
