package com.rust.aboutview.adapter.holder;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.View;

import com.rust.aboutview.R;
import com.rust.aboutview.adapter.ImagePagerAdapter;
import com.rust.aboutview.multiitemlv.MultiItemAdapter;

/**
 * Banner view holder
 * 轮播
 * Created by Rust on 2018/5/31.
 */
public class BannerVH {
    private static final String TAG = "rustAppBannerVH";
    private Context mContext;
    private ViewPager mViewPager;
    private ImagePagerAdapter mImagePagerAdapter;

    public BannerVH(Context context, View convertedView) {
        mContext = context;
        if (null != convertedView) {
            mViewPager = convertedView.findViewById(R.id.vp);
        }
    }

    public void setDataBean(MultiItemAdapter.DataBean dataBean) {
        if (null == mImagePagerAdapter) {
            mImagePagerAdapter = new ImagePagerAdapter(mContext, dataBean.picResIdList);
            mViewPager.setAdapter(mImagePagerAdapter);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                    Log.d(TAG, "onPageScrolled");
                }

                @Override
                public void onPageSelected(int position) {
                    Log.d(TAG, "onPageSelected -> " + position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    Log.d(TAG, "onPageScrollStateChanged: " + state);
                }
            });

        }
    }


}
