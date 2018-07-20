package com.rust.aboutview.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.rust.aboutview.R;
import com.rust.aboutview.fragment.CustomProgressBarFrag;
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
    private static final String K_PAGE_TYPE = "key_page_type";
    private static final int PAGE_CUSTOM_PB = 1;
    private DataProgressFragment mDataProgressFragment;
    private MultiItemListViewFragment mMultiItemListViewFragment;
    private CustomProgressBarFrag mCustomProgressBarFrag;

    public static void goCustomPbPage(Activity activity) {
        Intent intent = new Intent(activity, FragContainerActivity.class);
        intent.putExtra(K_PAGE_TYPE, PAGE_CUSTOM_PB);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_frame_container);
        initUI();
        Intent ii = getIntent();
        if (null != ii) {
            int pageType = ii.getIntExtra(K_PAGE_TYPE, 0);
            switch (pageType) {
                case PAGE_CUSTOM_PB:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, mCustomProgressBarFrag).commit();
                    break;
                default:
                    break;
            }
        }
    }

    private void initUI() {
        mDataProgressFragment = new DataProgressFragment();
        mMultiItemListViewFragment = new MultiItemListViewFragment();
        mCustomProgressBarFrag = new CustomProgressBarFrag();
        ButterKnife.bind(this);
    }

    @OnClick({R.id.show_progress_data_btn, R.id.multi_item_lv_btn, R.id.custom_pb_btn})
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
            case R.id.custom_pb_btn:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, mCustomProgressBarFrag).commit();
                break;
        }
    }
}
