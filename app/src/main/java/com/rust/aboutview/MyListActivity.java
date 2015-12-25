package com.rust.aboutview;

import android.app.ListActivity;
import android.os.Bundle;

public class MyListActivity extends ListActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
    }

}
