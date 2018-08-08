package com.rust.aboutview.algochart.algo;

import android.util.SparseIntArray;

/**
 * 算法步骤
 */
public class AlgoStepSlice {
    public int[] dataArray;
    public SparseIntArray indexColorMap = new SparseIntArray();

    public AlgoStepSlice(int[] data) {
        dataArray = data;
    }

    public void addMarkData(int index, int colorInt) {
        indexColorMap.put(index, colorInt);
    }
}
