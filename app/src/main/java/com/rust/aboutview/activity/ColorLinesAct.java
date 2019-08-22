package com.rust.aboutview.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.rust.aboutview.R;
import com.rust.aboutview.view.ColorLinesView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created on 2019-8-14
 */
public class ColorLinesAct extends Activity {

    private ColorLinesView mC1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_color_lines);
        mC1 = findViewById(R.id.c1);

        mC1.setHorAxisEndText("End");
        mC1.setMarkList(150, 220, 300);
        mC1.setCompare2(true);
        mC1.setData(new ColorLinesView.LineData(1, Color.parseColor("#ff8f22"), Color.parseColor("#66ff8f22"), genData()));
        mC1.setData(new ColorLinesView.LineData(2, Color.parseColor("#ff5722"), Color.parseColor("#66ff5722"), genData()));
    }

    private List<Float> genData() {
        final int count = 100;
        Random random = new Random();
        List<Float> d1 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            d1.add(random.nextFloat() * 300);
        }
        return d1;
    }

}
