package com.rust.aboutview.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rust.aboutview.R;

public class TabFragment1 extends Fragment {

    private View rootView;// cache fragment view
    TextView centerTV;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("rust", "TabFragment1 onCreateView");
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab1, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        // if root view had a parent, remove it.
        if (parent != null) {
            parent.removeView(rootView);
        }
        centerTV = (TextView) rootView.findViewById(R.id.center_tv);
        centerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centerTV.setText(String.format("%s","Tab1 clicked"));
                centerTV.setTextColor(Color.BLACK);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("rust", "TabFragment1 onResume");
    }
}
