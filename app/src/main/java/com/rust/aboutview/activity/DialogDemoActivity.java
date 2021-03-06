package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;

import com.rust.aboutview.R;
import com.rust.aboutview.widget.PopUpButtonsDialog;


public class DialogDemoActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dialog_demo);
        findViewById(R.id.pop_animate_dialog_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pop_animate_dialog_btn:
                new PopUpButtonsDialog(DialogDemoActivity.this).show();
                break;
        }
    }
}
