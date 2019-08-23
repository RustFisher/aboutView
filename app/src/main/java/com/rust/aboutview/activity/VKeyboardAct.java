package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.rust.aboutview.R;
import com.rust.aboutview.widget.VKey;
import com.rust.aboutview.widget.VKeyboard;
import com.rust.aboutview.widget.VKeyboardListener;
import com.rust.aboutview.widget.VKeyboardWidget;

/**
 * 虚拟键盘页面
 * Created on 2019-8-22
 */
public class VKeyboardAct extends Activity {
    private static final String TAG = "rustAppVKeyboardAct";
    private TextView mTv1;
    private VKeyboard vKeyboard1;
    private Editable mEditable1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_v_keyboard);
        Log.d(TAG, "onCreate: 1");

        mTv1 = findViewById(R.id.vk_page_tv1);
        mEditable1 = new Editable.Factory().newEditable("");
        vKeyboard1 = findViewById(R.id.vk_1);

        vKeyboard1.setKeyboardListener(new VKeyboardListener() {
            @Override
            public void onKeyClick(VKey key) {
                Log.d(TAG, "onKeyClick: " + key);
                if (key.isBackSpace()) {
                    if (mEditable1.length() > 0) {
                        mEditable1.delete(mEditable1.length() - 1, mEditable1.length());
                    }
                } else {
                    mEditable1.append(key.getKeyText());
                }
                updateTv1();
            }
        });
        Log.d(TAG, "onCreate: end");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "onWindowFocusChanged: hasFocus: " + hasFocus);
    }

    private void updateTv1() {
        mTv1.setText(mEditable1.toString());
    }
}
