package com.rust.aboutview.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.rust.aboutview.R;

/**
 * 文字全屏
 * Created on 2019-7-10
 */
public class FullTextAct extends Activity {
    private static final String TAG = "rustAppFullText";
    TextView mFullTextTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_full_text);
        mFullTextTv = findViewById(R.id.full_text_tv);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Log.d(TAG, "onCreate: window[" + width + ", " + height + "]");
        mFullTextTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, pxToSp(height, this) * 0.8f);
        mFullTextTv.setSelected(true);
    }

    public float convertPxToDp(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static float pxToSp(int px, Context context) {
        return px / context.getResources().getDisplayMetrics().scaledDensity;
    }

}
