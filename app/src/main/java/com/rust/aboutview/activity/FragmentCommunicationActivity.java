package com.rust.aboutview.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.rust.aboutview.R;
import com.rust.aboutview.fragment.LineChartFragment;

public class FragmentCommunicationActivity extends Activity {

    private LineChartFragment mLine = new LineChartFragment();
    private EditText et1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_with_fragment);

        et1 = (EditText) findViewById(R.id.et_1);

        FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
        fragmentManager.add(R.id.line_chart_container1, mLine);
        fragmentManager.commit();

        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLine.addText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * By using bundle
     */
    private void addEntry(short[] data) {
        Bundle bundle = new Bundle();
        bundle.putShortArray("rawData", data);
        LineChartFragment lineChartFragment = new LineChartFragment();
        lineChartFragment.setArguments(bundle);
        FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
        fragmentManager.replace(R.id.line_chart_container1, lineChartFragment);
        fragmentManager.commit();
    }
}
