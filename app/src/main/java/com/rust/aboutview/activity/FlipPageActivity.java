package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.rust.aboutview.R;
import com.rust.aboutview.view.HorizontalPagerAdapter;

import java.util.Locale;

/**
 * 左右滑动页
 */
public class FlipPageActivity extends Activity {

    public static final String TAG = "RustApp";
    HorizontalPagerAdapter mFlipperAdapter;
    HorizontalInfiniteCycleViewPager mVP;
    TextView mCurTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_flip_page);
        HorizontalPagerAdapter.FlipperCard cards[] = {
                new HorizontalPagerAdapter.FlipperCard(R.mipmap.launcher_text, "A0"),
                new HorizontalPagerAdapter.FlipperCard(R.mipmap.practice_128px, "B1"),
                new HorizontalPagerAdapter.FlipperCard(R.mipmap.pic_num_2, "C2")
        };
        mFlipperAdapter = new HorizontalPagerAdapter(getLayoutInflater(), cards);

        mCurTv = (TextView) findViewById(R.id.flipperCurTv);
        mVP = (HorizontalInfiniteCycleViewPager) findViewById(R.id.hicvp);

        mVP.setAdapter(mFlipperAdapter);
        mVP.setCurrentItem(1);
        Log.d(TAG, "exampleVP.getRealItem():" + mVP.getRealItem());
        mVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateCurField();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        updateCurField();
    }

    private void updateCurField() {
        mCurTv.setText(String.format(Locale.CHINA, "current: %d", mVP.getRealItem()));
    }
}
