package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.rust.aboutview.R;
import com.rust.aboutview.widget.VKeyboardWidget;

/**
 * 虚拟键盘页面
 * Created on 2019-8-22
 */
public class VKeyboardAct extends Activity {
    private static final String TAG = "rustAppVKeyboardAct";
    private VKeyboardWidget mKeyboardWidget;
    private TextView mTv1;

    private Editable mEditable1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_v_keyboard);

        mTv1 = findViewById(R.id.vk_page_tv1);
        mEditable1 = new Editable.Factory().newEditable("");

        LinearLayout linearLayout2 = findViewById(R.id.vk_2);
        mKeyboardWidget = new VKeyboardWidget(linearLayout2, 1080);

        mKeyboardWidget.setOnItemClickListener(new VKeyboardWidget.OnItemClickListener() {
            @Override
            public void onKeyClick(VKeyboardWidget.Key key) {
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
    }

    private void updateTv1() {
        mTv1.setText(mEditable1.toString());
    }
}
