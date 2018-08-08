package com.rust.aboutview.algochart;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.rust.aboutview.R;
import com.rust.aboutview.algochart.algo.AlgoStepSlice;
import com.rust.aboutview.algochart.algo.sort.SelectionSort;
import com.rustfisher.view.DotBarChart;

import java.util.List;

/**
 * 显示算法图表
 */
public class AlgoChartActivity extends Activity {
    private static final String TAG = "rustApp";
    private DotBarChart mDbc1;
    private AlgoPlayer mAlgoPlayer;

    private ImageView mPlayIv;
    private Toolbar mToolbar;
    private int[] mArray1 = ArraySource.genIntArray();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_algo_chart);
        initUI();

        List<AlgoStepSlice> algoStepEntities = SelectionSort.selectSort(mArray1);
        mAlgoPlayer = new AlgoPlayer();
        mAlgoPlayer.setDataList(algoStepEntities);
        mAlgoPlayer.setAlgoChart(mAlgoChart);
        mAlgoPlayer.setListener(aListener);
    }

    private void initUI() {
        mToolbar = findViewById(R.id.toolbar);
        mPlayIv = findViewById(R.id.play_iv);
        mDbc1 = findViewById(R.id.dbc1);
        mPlayIv.setOnClickListener(mOnClickListener);
        findViewById(R.id.next_iv).setOnClickListener(mOnClickListener);
        findViewById(R.id.pre_iv).setOnClickListener(mOnClickListener);
        mDbc1.setData(mArray1, null);

        mToolbar.setNavigationIcon(R.drawable.ic_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlgoPlayer.exit();
                finish();
            }
        });
    }

    private AlgoChart mAlgoChart = new AlgoChart() {
        @Override
        public void showData(AlgoStepSlice entity) {
            mDbc1.setData(entity.dataArray, entity.indexColorMap);
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.play_iv:
                    mAlgoPlayer.togglePlay();
                    break;
                case R.id.pre_iv:
                    mAlgoPlayer.pressPrevious();
                    break;
                case R.id.next_iv:
                    mAlgoPlayer.pressNext();
                    break;
            }
        }
    };

    private AlgoPlayer.AListener aListener = new AlgoPlayer.AListener() {
        @Override
        public void onStateChanged(AlgoPlayer.STATE state) {
            switch (state) {
                case NONE:
                case PAUSE:
                    mPlayIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_media_play));
                    break;
                case PLAYING:
                    mPlayIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause1));
                    break;
            }
        }
    };

}
