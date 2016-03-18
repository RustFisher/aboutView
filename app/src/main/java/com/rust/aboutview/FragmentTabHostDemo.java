package com.rust.aboutview;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.rust.aboutview.fragment.TabFragment1;
import com.rust.aboutview.fragment.TabFragment2;
import com.rust.aboutview.fragment.TabFragment3;

public class FragmentTabHostDemo extends FragmentActivity {
    public static final String TAB1 = "tab1";
    public static final String TAB2 = "tab2";
    public static final String TAB3 = "tab3";

    public static FragmentTabHost mTabHost;
    LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_tab_host);

        mLayoutInflater = LayoutInflater.from(getApplicationContext());

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        TabHost.TabSpec tabSpec = mTabHost.newTabSpec(TAB1);
        View tabView1 = mLayoutInflater.inflate(R.layout.tab_item, null);
        ImageView tabImage1 = (ImageView) tabView1.findViewById(R.id.back_image);
        tabImage1.setImageResource(R.drawable.floppy_16px);
        TextView tabText1 = (TextView) tabView1.findViewById(R.id.tab_text);
        tabText1.setText("Page1");
        tabSpec.setIndicator(tabView1);
        mTabHost.addTab(tabSpec, TabFragment1.class, null);
//        mTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.train);
        Drawable d = ContextCompat.getDrawable(getApplicationContext(), R.drawable.horn_32px);
        mTabHost.addTab(mTabHost.newTabSpec(TAB2).setIndicator("Page2", d),
                TabFragment2.class, null);

        mTabHost.addTab(mTabHost.newTabSpec(TAB3)
                .setIndicator("Page3"), TabFragment3.class, null);

        mTabHost.setCurrentTab(0);

        mTabHost.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTabHost.getChildAt(0).setBackgroundColor(Color.RED);
            }
        });

    }
}
