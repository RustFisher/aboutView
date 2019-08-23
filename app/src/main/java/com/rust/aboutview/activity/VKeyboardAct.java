package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.rust.aboutview.R;
import com.rust.aboutview.widget.vkeyboard.BaseAdapterDark;
import com.rust.aboutview.widget.vkeyboard.VKey;
import com.rust.aboutview.widget.vkeyboard.VKeyboard;
import com.rust.aboutview.widget.vkeyboard.VKeyboardListener;

/**
 * 虚拟键盘页面
 * Created on 2019-8-22
 */
public class VKeyboardAct extends Activity {
    private static final String TAG = "rustAppVKeyboardAct";
    public static int screenWidth = 1080;
    private TextView mTv1;
    private TextView mTv2;
    private VKeyboard vKeyboard1;
    private Editable mEditable1;
    private Editable mEditable2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_v_keyboard);
        Log.d(TAG, "onCreate: 1");

        mTv1 = findViewById(R.id.vk_page_tv1);
        mTv2 = findViewById(R.id.vk_page_tv2);
        mEditable1 = new Editable.Factory().newEditable("");
        mEditable2 = new Editable.Factory().newEditable("");
        vKeyboard1 = findViewById(R.id.vk_1);

        vKeyboard1.setKeyboardListener(new VKeyboardListener() {
            @Override
            public void onKeyClick(VKey key) {
                Log.d(TAG, "onKeyClick: " + key);
                inputKey(key, mEditable1);
                updateTv1();
            }
        });

        VKeyboard vk2 = findViewById(R.id.vk_2);
        vk2.setAdapter(new BaseAdapterDark(screenWidth));
        vk2.setKeyboardListener(new VKeyboardListener() {
            @Override
            public void onKeyClick(VKey key) {
                inputKey(key, mEditable2);
                updateTv2();
            }
        });
        Log.d(TAG, "onCreate: end");
    }

    private void inputKey(VKey key, Editable editable) {
        if (key.isBackSpace()) {
            if (editable.length() > 0) {
                editable.delete(editable.length() - 1, editable.length());
            }
        } else if (key.isOk()) {
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
        } else {
            editable.append(key.getKeyText());
        }

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

    private void updateTv2() {
        mTv2.setText(mEditable2.toString());
    }
}
