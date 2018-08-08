package com.rust.aboutview.algochart;

import com.rust.aboutview.algochart.algo.AlgoStepSlice;


public interface AlgoChart {

    /**
     * 一般在主线程使用
     *
     * @param data 要显示的数据
     */
    void showData(AlgoStepSlice data);
}
