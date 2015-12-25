package com.rust.aboutview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
                i.setClass(getApplicationContext(), MyListActivity.class);
                startActivity(i);
            }
        });
    }
}
