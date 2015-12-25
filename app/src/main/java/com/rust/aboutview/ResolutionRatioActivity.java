package com.rust.aboutview;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class ResolutionRatioActivity extends Activity {
    TextView screenInfo;
    TextView screenPPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        screenInfo = (TextView) findViewById(R.id.screen_info);
        screenPPI = (TextView) findViewById(R.id.screen_ppi);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float widthPixels = dm.widthPixels;
        float heightPixels = dm.heightPixels;
        float density = dm.density;
        int screenWidth = (int) (widthPixels * density);
        int screenHeight = (int) (heightPixels * density);
        String info = "屏幕像素:  " + widthPixels + "x" + heightPixels + " 密度 " + density;
        float ppi = (int) Math.sqrt((float)
                (widthPixels * widthPixels + heightPixels * heightPixels)) / 5f;
        String PPI = "PPI  " + ppi;
        screenInfo.setText(info);
        screenPPI.setText(PPI);
    }
}
