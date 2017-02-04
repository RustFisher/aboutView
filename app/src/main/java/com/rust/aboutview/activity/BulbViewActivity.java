package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;

import com.rust.aboutview.R;
import com.rustfisher.view.BulbView;

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
        bulbView3.setWholeSizeRatio(1.3f);

    }
}
