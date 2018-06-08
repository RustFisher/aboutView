package com.rust.aboutview.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.rust.aboutview.R;
import com.rust.aboutview.fragment.ShadowLineChartFragment;

/**
 * 显示Fragment
 * Created by Rust on 2018/6/8.
 */
public class FragHolderActivity extends AppCompatActivity {
    private static final String K_SHOW_TYPE = "key_show_type";
    private static final int SHOW_TYPE_SHADOW_LINE_CHART = 1;

    public static void showShadowLineChart(Activity activity) {
        Intent intent = new Intent(activity.getApplicationContext(), FragHolderActivity.class);
        intent.putExtra(K_SHOW_TYPE, SHOW_TYPE_SHADOW_LINE_CHART);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_frame_layout);
        Intent inIntent = getIntent();
        if (null != inIntent) {
            int sType = inIntent.getIntExtra(K_SHOW_TYPE, SHOW_TYPE_SHADOW_LINE_CHART);
            FragmentManager fragmentManager = getSupportFragmentManager();
            switch (sType) {
                case SHOW_TYPE_SHADOW_LINE_CHART:
                    fragmentManager.beginTransaction().add(R.id.container, new ShadowLineChartFragment()).commit();
                    break;
            }
        }
    }
}
