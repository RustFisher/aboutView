package com.rust.aboutview;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.rust.aboutview.fragment.TabFragment1;
import com.rust.aboutview.fragment.TabFragment2;
import com.rust.aboutview.fragment.TabFragment3;

import java.util.HashMap;

public class FragmentTabHostDemo extends FragmentActivity {
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

        TabHost.TabSpec tabSpec = mTabHost.newTabSpec(TABS[0]);
        View tabView1 = mLayoutInflater.inflate(R.layout.tab_item, null);
        ImageView tabImage1 = (ImageView) tabView1.findViewById(R.id.back_image);
        ImageView tabBottom1 = (ImageView) tabView1.findViewById(R.id.bottom_line);
        tabImage1.setImageResource(R.drawable.floppy_16px);
        tabBottom1.setBackgroundColor(Color.BLUE);
        TextView tabText1 = (TextView) tabView1.findViewById(R.id.tab_text);
        tabText1.setText("Page1");
        tabText1.setTextColor(Color.argb(255, 255, 255, 255));
        tabSpec.setIndicator(tabView1);
        mTabHost.addTab(tabSpec, TabFragment1.class, null);
//        mTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.train);
        Drawable d = ContextCompat.getDrawable(getApplicationContext(), R.drawable.horn_32px);
        mTabHost.addTab(mTabHost.newTabSpec(TABS[1]).setIndicator("Page2", d),
                TabFragment2.class, null);

        mTabHost.addTab(mTabHost.newTabSpec(TABS[2])
                .setIndicator("Page3"), TabFragment3.class, null);

        mTabHost.setCurrentTab(0);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int child = mTabMap.get(tabId);
                mTabHost.getTabWidget().getChildAt(child)
                        .findViewById(R.id.bottom_line).setVisibility(View.VISIBLE);
            }
        });

//        final View tab1 = mTabHost.getTabWidget().getChildAt(0);
//        tab1.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        tab1.setBackgroundColor(Color.argb(155, 200, 200, 200));
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        tab1.setBackgroundColor(Color.argb(255, 200, 33, 33));
//                        break;
//                }
//                return false;
//            }
//        });

    }
}
