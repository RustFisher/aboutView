package com.rust.aboutview.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.rust.aboutview.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义progress bar
 * 各种样式的进度条
 * Created by Rust on 2018/7/19.
 */
public class CustomProgressBarFrag extends Fragment {

    ProgressBar mPb1;
    ProgressBar mPb2;
    ProgressBar mPb3;
    ProgressBar mPb4;
    ProgressBar mPb5;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Timer mTimer = new Timer("CPB");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_custom_progress_bar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPb1 = view.findViewById(R.id.pb1);
        mPb2 = view.findViewById(R.id.pb2);
        mPb3 = view.findViewById(R.id.pb3);
        mPb4 = view.findViewById(R.id.pb4);
        mPb5 = view.findViewById(R.id.pb5);
        setupPb(mPb1, 200);
        setupPb(mPb4, 1000);
        setupPb(mPb5, 1000);
        mTimer.schedule(mTask, 0, 16);
    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }

    private TimerTask mTask = new TimerTask() {
        private int period = 0;

        @Override
        public void run() {
            period++;

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    increasePb(mPb1);
                    if (period % 2 == 0) {
                        increasePb(mPb2);
                    }
                    if (period % 3 == 0) {
                        increasePb(mPb3);
                    }
                    increasePb(mPb4);
                    increasePb(mPb5);
                }
            });
        }
    };

    private void increasePb(ProgressBar pb) {
        if (pb.getProgress() == pb.getMax()) {
            pb.setProgress(0);
        } else {
            pb.setProgress(pb.getProgress() + 1);
        }
    }

    private void setupPb(ProgressBar pb, int max) {
        pb.setMax(max);
    }
}
