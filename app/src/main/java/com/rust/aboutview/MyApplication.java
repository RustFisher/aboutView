package com.rust.aboutview;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static Context context;

    public static Context getMyContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
