/*
 * Copyright (C)opy whatever you want
 */
package com.rust.aboutview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MySwipeActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    public static final int REFRESH_COMPLETE = 1300;
    private SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;
    private ArrayAdapter adapter;
    private List<String> mData = new ArrayList<>(
            Arrays.asList("Java", "Javascript", "C++", "Ruby", "Json", "HTML"));

    MyHandler myHandler = new MyHandler(this);

    static class MyHandler extends Handler {
        MySwipeActivity handlerSwipeActivity;

        MyHandler(MySwipeActivity mySwipeActivity) {
            handlerSwipeActivity = mySwipeActivity;
        }

        @Override
        public void handleMessage(Message msg) {
            if (handlerSwipeActivity != null) {
                switch (msg.what) {
                    case REFRESH_COMPLETE:
                        handlerSwipeActivity.mData.addAll(Arrays.asList("C", "ASM", "C#"));
                        handlerSwipeActivity.adapter.notifyDataSetChanged();
                        handlerSwipeActivity.swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_refresh);
        listView = (ListView) findViewById(R.id.swipe_refresh_list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_ly);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.WHITE);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mData);
        listView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        myHandler.sendEmptyMessageAtTime(REFRESH_COMPLETE, 2000);
    }
}
