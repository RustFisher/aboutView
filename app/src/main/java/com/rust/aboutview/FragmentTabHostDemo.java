package com.rust.aboutview;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.rust.aboutview.fragment.TabFragment1;
import com.rust.aboutview.fragment.TabFragment2;
import com.rust.aboutview.fragment.TabFragment3;

import java.util.HashMap;

public class FragmentTabHostDemo extends FragmentActivity {

    public static final int COLOR_GRAY_01 = 0xFFADADAD;
    public static final int COLOR_GREEN_01 = 0xFF73BF00;

    public static final String TAB1 = "tab1";
    public static final String TAB2 = "tab2";
    public static final String TAB3 = "tab3";
    public static final String TABS[] = {TAB1, TAB2, TAB3};

    public static HashMap<String, Integer> mTabMap;
    public static FragmentTabHost mTabHost;
    LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_tab_host);
        mTabMap = new HashMap<>();
        mTabMap.put(TAB1, 0);
        mTabMap.put(TAB2, 1);
        mTabMap.put(TAB3, 2);
        mLayoutInflater = LayoutInflater.from(getApplicationContext());

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.getTabWidget().setMinimumHeight(120);// set the tab height
        mTabHost.getTabWidget().setDividerDrawable(null);

        TabHost.TabSpec tabSpec = mTabHost.newTabSpec(TABS[0]);
        View tabView1 = mLayoutInflater.inflate(R.layout.tab_item, null);
        final ImageView tabImage1 = (ImageView) tabView1.findViewById(R.id.tab_image);
        final TextView tabText1 = (TextView) tabView1.findViewById(R.id.tab_text);
        tabImage1.setImageResource(R.drawable.a4a);
        tabText1.setText(getString(R.string.tab_label_1));
        tabText1.setTextColor(COLOR_GREEN_01);

        tabSpec.setIndicator(tabView1);
        mTabHost.addTab(tabSpec, TabFragment1.class, null);

        View tabView2 = mLayoutInflater.inflate(R.layout.tab_item, null);
        final ImageView tabImage2 = (ImageView) tabView2.findViewById(R.id.tab_image);
        tabImage2.setImageResource(R.drawable.a49);
        final TextView tabText2 = (TextView) tabView2.findViewById(R.id.tab_text);
        tabText2.setText(getString(R.string.tab_label_2));

        mTabHost.addTab(mTabHost.newTabSpec(TABS[1]).setIndicator(tabView2),
                TabFragment2.class, null);

        View tabView3 = mLayoutInflater.inflate(R.layout.tab_item, null);
        final ImageView tabImage3 = (ImageView) tabView3.findViewById(R.id.tab_image);
        tabImage3.setImageResource(R.drawable.a49);
        final TextView tabText3 = (TextView) tabView3.findViewById(R.id.tab_text);
        tabText3.setText(getString(R.string.tab_label_3));

        mTabHost.addTab(mTabHost.newTabSpec(TABS[2])
                .setIndicator(tabView3), TabFragment3.class, null);

        mTabHost.setCurrentTab(0);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int child = mTabMap.get(tabId);
                tabImage1.setImageResource(R.drawable.a49);
                tabImage2.setImageResource(R.drawable.a49);
                tabImage3.setImageResource(R.drawable.a49);
                tabText1.setTextColor(COLOR_GRAY_01);
                tabText2.setTextColor(COLOR_GRAY_01);
                tabText3.setTextColor(COLOR_GRAY_01);
                switch (child) {
                    case 0:
                        tabImage1.setImageResource(R.drawable.a4a);
                        tabText1.setTextColor(COLOR_GREEN_01);
                        break;
                    case 1:
                        tabImage2.setImageResource(R.drawable.a4a);
                        tabText2.setTextColor(COLOR_GREEN_01);
                        break;
                    case 2:
                        tabImage3.setImageResource(R.drawable.a4a);
                        tabText3.setTextColor(COLOR_GREEN_01);
                        break;
                }
            }
        });

    }
}
