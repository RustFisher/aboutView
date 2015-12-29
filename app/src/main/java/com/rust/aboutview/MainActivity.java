package com.rust.aboutview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rust.arslan.ArslanActivity;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.goto_image_processing_activity).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent();
                        i.setClass(getApplicationContext(), ImageProcessingActivity.class);
                        startActivity(i);
                    }
                });
        findViewById(R.id.goto_resolution_ratio_activity).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent();
                        i.setClass(getApplicationContext(), ResolutionRatioActivity.class);
                        startActivity(i);
                    }
                });
        findViewById(R.id.goto_list_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getApplicationContext(), MySwipeActivity.class);
                startActivity(i);
            }
        });
        findViewById(R.id.goto_drawer_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getApplicationContext(), DrawerActivity.class);
                startActivity(i);
            }
        });
        findViewById(R.id.goto_arslan_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getApplicationContext(), ArslanActivity.class);
                startActivity(i);
            }
        });
        findViewById(R.id.goto_notification_demo_activity).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent();
                        i.setClass(getApplicationContext(), NotificationActivity.class);
                        startActivity(i);
                    }
                });
    }
}
