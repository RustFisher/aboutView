package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.rust.aboutview.R;
import com.rust.aboutview.widget.PopUpButtonsDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogDemoActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dialog_demo);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.pop_animate_dialog_btn)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pop_animate_dialog_btn:
                new PopUpButtonsDialog(DialogDemoActivity.this).show();
                break;
        }
    }
}
