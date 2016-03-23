package com.rust.aboutview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rust.aboutview.R;

public class TabFragment1 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("rust", "TabFragment1 onCreateView");
        return inflater.inflate(R.layout.fragment_tab1, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("rust", "TabFragment1 onResume");

    }
}
