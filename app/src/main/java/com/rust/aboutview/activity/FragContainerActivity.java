package com.rust.aboutview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.rust.aboutview.R;
import com.rust.aboutview.fragment.ProgressFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Show fragment
 * Created by Rust on 2018/5/25.
 */
public class FragContainerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "rustAppFrag";
    private ProgressFragment mProgressFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_frame_container);
        initUI();
    }

    private void initUI() {
        mProgressFragment = new ProgressFragment();
        ButterKnife.bind(this);
    }

    @OnClick({R.id.show_progress_data_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_progress_data_btn:
                Log.d(TAG, "show ProgressFragment");
                getSupportFragmentManager().beginTransaction().replace(R.id.container, mProgressFragment).commit();
                break;
        }
    }
}
