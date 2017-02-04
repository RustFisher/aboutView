package com.rust.aboutview.activity;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.rust.aboutview.R;

public class FrameAnimationActivity extends Activity {
    ImageView syncNoneEffect;
    AnimationDrawable noneAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_anima);
        syncNoneEffect = (ImageView) findViewById(R.id.none_effect_animation);
        noneAnimation = (AnimationDrawable) syncNoneEffect.getDrawable();
        findViewById(R.id.btn_none_effect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        syncNoneEffect.post(new Runnable() {
            @Override
            public void run() {
                noneAnimation.start();
            }
        });
    }
}
