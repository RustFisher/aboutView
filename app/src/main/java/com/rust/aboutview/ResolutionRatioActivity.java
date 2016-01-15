package com.rust.aboutview;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ResolutionRatioActivity extends Activity {

    TextView screenInfo;
    TextView screenPPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        screenInfo = (TextView) findViewById(R.id.screen_info);
        screenPPI = (TextView) findViewById(R.id.screen_ppi);
        // 设成1表示显示触摸位置，需要权限
        android.provider.Settings.System.putInt(getContentResolver(), "show_touches", 1);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> activities = getPackageManager().queryIntentActivities(intent, 0);
        for (int i = 0; i < activities.size(); i++) {
            Log.d("rust", activities.get(i).toString());
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float widthPixels = dm.widthPixels;
        float heightPixels = dm.heightPixels;
        float density = dm.density;
        String info = "屏幕像素:  " + widthPixels + "x" + heightPixels + " 密度 " + density;
        float screenWidth = widthPixels / (160 * density);
        float screenHeight = heightPixels / (160 * density);
        float screenSize = (float) Math.sqrt(
                screenHeight * screenHeight + screenWidth * screenWidth);

        String size = "wid = " + screenWidth + "'" + "  height = " + screenHeight + "'" +
                "\nsize = " + screenSize + "'";
        screenInfo.setText(info);
        screenPPI.setText(size);
    }

    @Override
    protected void onStop() {
        super.onStop();
        android.provider.Settings.System.putInt(getContentResolver(), "show_touches", 0);
    }
}
