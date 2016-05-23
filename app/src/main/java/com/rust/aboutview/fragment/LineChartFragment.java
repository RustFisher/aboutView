package com.rust.aboutview.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rust.aboutview.R;

import java.util.Locale;

public class LineChartFragment extends Fragment {

    private TextView titleField;
    private TextView contentField;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);
        titleField = (TextView) rootView.findViewById(R.id.title_field);
        contentField = (TextView) rootView.findViewById(R.id.tv_field);

        return rootView;
    }

    public void addEntries(short[] data) {
        contentField.setText(String.format(Locale.CHINESE, "%d", data[0]));
        onResume();
    }

    public void addText(String s) {
        if (!TextUtils.isEmpty(s)) {
            contentField.setText(s);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
