package com.rust.aboutview.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rust.aboutview.R;
import com.rust.aboutview.adapter.ProgressReAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 装载ViewPager显示多个Fragment
 * Do not use ButterKnife here
 * Created by Rust on 2018/5/25.
 */
public class DataProgressFragment extends Fragment {

    ProgressContentFragment mContentFragment1;
    ProgressContentFragment mContentFragment2;
    ProgressContentFragment mContentFragment3;
    ProgressContentFragment mContentFragment4;
    ProgressContentFragment mContentFragment5;
    private List<Fragment> mContentFragList = new ArrayList<>();
    FragmentPagerAdapter mPagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentFragment1 = new ProgressContentFragment();
        mContentFragment2 = new ProgressContentFragment();
        mContentFragment3 = new ProgressContentFragment();
        mContentFragment4 = new ProgressContentFragment();
        mContentFragment5 = new ProgressContentFragment();
        List<ProgressReAdapter.ProgressItem> testData = createTestData();
        mContentFragment1.setDataList(0, testData);
        mContentFragment2.setDataList(1, testData);
        mContentFragment3.setDataList(2, testData);
        mContentFragment4.setDataList(3, testData);
        mContentFragment5.setDataList(4, testData);
        mContentFragList.add(mContentFragment1);
        mContentFragList.add(mContentFragment2);
        mContentFragList.add(mContentFragment3);
        mContentFragList.add(mContentFragment4);
        mContentFragList.add(mContentFragment5);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        final String[] tabNames = {"小英雄", "雄英", "地区", "天气", "预报！"};
        for (int i = 0; i < mContentFragList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabNames[i]));
        }
        mPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                return mContentFragList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mContentFragList.get(position);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabNames[position];
            }
        };
        viewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private List<ProgressReAdapter.ProgressItem> createTestData() {
        List<ProgressReAdapter.ProgressItem> testList = new ArrayList<>();
        final int typeCount = 5;
        for (int i = 0; i < typeCount; i++) {
            for (int j = 0; j < typeCount; j++) {
                testList.add(new ProgressReAdapter.ProgressItem("江南将再现高温天！" + i, i + j, (j + 2) * 3, i % typeCount));
                testList.add(new ProgressReAdapter.ProgressItem("新一轮强降水来袭！", i, (i + 1) * 6, i % typeCount));
                testList.add(new ProgressReAdapter.ProgressItem("晴热暴晒继续！周末或有雷阵雨热力不减~" + i, 0, 10, i % typeCount));
                testList.add(new ProgressReAdapter.ProgressItem("晴转阴，阴转大雨！" + i, j, i + j, i % typeCount));
            }
        }
        return testList;
    }
}
