package com.rust.aboutview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.rust.aboutview.R;
import com.rust.aboutview.fragment.DataProgressFragment;
import com.rust.aboutview.fragment.MultiItemListViewFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Show fragment
 * Created by Rust on 2018/5/25.
 */
public class FragContainerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "rustAppFrag";
    private DataProgressFragment mDataProgressFragment;
    private MultiItemListViewFragment mMultiItemListViewFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_frame_container);
        initUI();
    }

    private void initUI() {
        mDataProgressFragment = new DataProgressFragment();
        mMultiItemListViewFragment = new MultiItemListViewFragment();
        ButterKnife.bind(this);
    }

    @OnClick({R.id.show_progress_data_btn, R.id.multi_item_lv_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_progress_data_btn:
                Log.d(TAG, "show DataProgressFragment");
                getSupportFragmentManager().beginTransaction().replace(R.id.container, mDataProgressFragment).commit();
                break;
            case R.id.multi_item_lv_btn:
                Log.d(TAG, "show: mMultiItemListViewFragment");
                getSupportFragmentManager().beginTransaction().replace(R.id.container, mMultiItemListViewFragment).commit();
                break;
        }
    }
}
