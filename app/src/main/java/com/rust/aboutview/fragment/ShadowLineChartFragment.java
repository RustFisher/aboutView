package com.rust.aboutview.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rust.aboutview.R;
import com.rustfisher.view.ShadowLineChart;

import java.util.Random;

/**
 * 显示折线图
 * Created by Rust on 2018/6/8.
 */
public class ShadowLineChartFragment extends Fragment {
    ShadowLineChart mSc1;
    ShadowLineChart mSc2;
    ShadowLineChart mSc3;
    ShadowLineChart mSc4;
    ShadowLineChart mSc5;

    private TestDataThread mTestDataThread;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTestDataThread = new TestDataThread();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_shadow_line_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSc1 = view.findViewById(R.id.sc1);
        mSc2 = view.findViewById(R.id.sc2);
        mSc3 = view.findViewById(R.id.sc3);
        mSc4 = view.findViewById(R.id.sc4);
        mSc5 = view.findViewById(R.id.sc5);
        mSc3.setDataMax(30);
        mSc3.setDataMin(-30);
        mSc3.setMark1(15);
        mSc3.setMark2(-15);

        shutdownTestThread();
        mTestDataThread = new TestDataThread();
        mTestDataThread.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        shutdownTestThread();
    }

    private void shutdownTestThread() {
        if (null != mTestDataThread) {
            try {
                mTestDataThread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mTestDataThread = null;
        }
    }

    private class TestDataThread extends Thread {
        Random random = new Random(System.currentTimeMillis());
        float[] randomData = new float[2];
        float[] rectWave1 = new float[2];
        float[] sinWave = new float[1];
        float[] cosWave = new float[1];
        int singleData = 0;

        @Override
        public void run() {
            super.run();
            int loopCount = 0;
            int numberFlag1 = 1;
            boolean increase = true;
            while (!isInterrupted()) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                loopCount++;
                if (loopCount >= 200000000) {
                    loopCount = 0;
                }
                if (Math.abs(singleData) > 27) {
                    increase = !increase;
                }
                if (increase) {
                    singleData++;
                } else {
                    singleData--;
                }
                for (int i = 0; i < randomData.length; i++) {
                    randomData[i] = random.nextInt((int) mSc1.getDataMax()) - mSc1.getDataMax() / 2;
                }
                if (loopCount % 15 == 0) {
                    numberFlag1 *= -1;
                }
                for (int i = 0; i < rectWave1.length; i++) {
                    rectWave1[i] = numberFlag1 * 1100;
                }
                sinWave[0] = (float) (Math.sin(4.0f * Math.toRadians(loopCount * 1.0f)) * mSc4.getDataMax() * 0.8f);
                cosWave[0] = (float) (Math.cos(4 * Math.toRadians(loopCount)) * 1024);
                final float[] data1 = randomData.clone(); // 使用副本避免多线程修改数据问题
                final float[] waveData1 = rectWave1.clone();
                final float[] sinWave1 = sinWave.clone();
                final float[] cosWave1 = cosWave.clone();
                final float[] data3 = new float[]{singleData};
                mSc1.post(new Runnable() {
                    @Override
                    public void run() {
                        mSc1.inputData(data1);
                        mSc2.inputData(waveData1);
                        mSc3.inputData(data3);
                        mSc4.inputData(sinWave1);
                        mSc5.inputData(cosWave1);
                    }
                });
            }
        }
    }
}
