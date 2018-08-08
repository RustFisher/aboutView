package com.rust.aboutview.algochart.algo.sort;

import android.graphics.Color;

import com.rust.aboutview.algochart.algo.AlgoStepSlice;
import com.rust.aboutview.algochart.algo.BaseSort;

import java.util.ArrayList;
import java.util.List;

public class SelectionSort extends BaseSort {

    public static List<AlgoStepSlice> selectSort(int a[]) {
        List<AlgoStepSlice> stepEntities = new ArrayList<>();
        int N = a.length;
        stepEntities.add(new AlgoStepSlice(a.clone()));
        for (int i = 0; i < N; i++) {
            int min = i;
            for (int j = i + 1; j < N; j++) {
                if (less(a[j], a[min])) min = j;
            }
            markDot(a, stepEntities, i, min);
            exch(a, i, min);
            markDot(a, stepEntities, i, min);

        }
        return stepEntities;
    }

    private static void markDot(int[] a, List<AlgoStepSlice> stepEntities, int i, int min) {
        AlgoStepSlice algoStepSlice = new AlgoStepSlice(a.clone());
        algoStepSlice.addMarkData(i, Color.RED);
        algoStepSlice.addMarkData(min, Color.YELLOW);
        stepEntities.add(algoStepSlice);
    }

}
