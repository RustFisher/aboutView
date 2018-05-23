package com.rust.aboutview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rust.aboutview.R;
import com.rust.aboutview.widget.RoundCornerProgressDialog;
import com.rustfisher.view.RoundCornerImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 圆角图片示例
 * Created by Rust on 2018/5/23.
 */
public class RoundCornerActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.r_iv_1)
    RoundCornerImageView mRIv1;
    @BindView(R.id.r_iv_2)
    RoundCornerImageView mRIv2;
    @BindView(R.id.r_iv_3)
    RoundCornerImageView mRIv3;
    @BindView(R.id.r_iv_4)
    RoundCornerImageView mRIv4;

    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private RoundCornerProgressDialog mRoundCornerProgressDialog;
    private ProgressThread mProgressThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_round_corner);
        initUI();
    }

    private void initUI() {
        ButterKnife.bind(this);
        mRIv1.setRadiusDp(12);
        mRIv2.setRadiusDp(23);
        mRIv3.setRadiusPx(40);
        mRIv4.setRadiusPx(200);
    }

    @OnClick(R.id.pop_dialog_btn)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_dialog_btn:
                popRoundProgressDialog();
                break;
        }
    }

    private void popRoundProgressDialog() {
        if (null == mRoundCornerProgressDialog) {
            mRoundCornerProgressDialog = new RoundCornerProgressDialog();
        }
        mRoundCornerProgressDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTranslucentOrigin);
        mRoundCornerProgressDialog.show(getSupportFragmentManager(), RoundCornerProgressDialog.F_TAG);
        if (null != mProgressThread) {
            mProgressThread.interrupt();
            try {
                mProgressThread.join(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mProgressThread = null;
        }
        mProgressThread = new ProgressThread();
        mProgressThread.start();
    }

    private class ProgressThread extends Thread {

        private int progress = 0;

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                progress++;
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                if (progress > 100) {
                    progress = 0;
                }
                final int p = progress;
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mRoundCornerProgressDialog.updatePercent(p);
                    }
                });
            }
        }
    }

}
