package com.rust.aboutview.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.rust.aboutview.R;
import com.rustfisher.view.SelectRectView;

import java.util.Locale;

/**
 * 框选
 * Created by Rust on 2018/5/23.
 */
public class SelectRectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_drag);
        final TextView textView = findViewById(R.id.p_tv);
        final SelectRectView selectRectView = findViewById(R.id.se_view);
        selectRectView.setWorking(true);
        selectRectView.setOnSelectedListener(new SelectRectView.OnSelectedListener() {
            @Override
            public void onSelectedRect(boolean validSelected, float startX, float startY, float endX, float endY, int viewWid, int viewHeight) {
                textView.setText(String.format(Locale.CHINA,
                        "working:%b\nview size [%4d, %4d]\nstart [%4.2f, %4.2f]\nend  [%4.2f, %4.2f]",
                        validSelected, viewWid, viewHeight, startX, startY, endX, endY));
            }
        });
    }

}
