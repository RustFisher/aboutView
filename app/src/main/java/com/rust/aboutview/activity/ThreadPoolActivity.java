package com.rust.aboutview.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rust.aboutview.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThreadPoolActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "RustApp";

    private static final int MSG_TYPE_ONE = 1001;
    private static final int MSG_TYPE_TWO = 1002;

    private static final int CORE_POOL_SIZE = 1;    // 核心线程数
    private static final int MAX_POOL_SIZE = 3;     // 最大线程数
    private static final int BLOCK_SIZE = 3;        // 阻塞队列大小
    private static final long KEEP_ALIVE_TIME = 2;  // 空闲线程超时时间

    private ThreadPoolExecutor mExecutorPool;

    @BindView(R.id.tpTitleTv)
    TextView mTitleTv;
    @BindView(R.id.noteTv)
    TextView mNoteTv;

    @BindView(R.id.tv0)
    TextView mTv0;
    @BindView(R.id.tv1)
    TextView mTv1;
    @BindView(R.id.tv2)
    TextView mTv2;
    @BindView(R.id.tv3)
    TextView mTv3;
    @BindView(R.id.tv4)
    TextView mTv4;
    @BindView(R.id.tv5)
    TextView mTv5;
    @BindView(R.id.tv6)
    TextView mTv6;
    @BindView(R.id.tv7)
    TextView mTv7;
    @BindView(R.id.tv8)
    TextView mTv8;
    @BindView(R.id.tv9)
    TextView mTv9;

    private ArrayList<TextView> mTvList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_thread_pool);
        ButterKnife.bind(this);
        initUI();
        initThreadPool();
    }

    @OnClick({R.id.tpBtn1, R.id.tpBtn2})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tpBtn1:
                addNewTask();
                break;
            case R.id.tpBtn2:
                showPoolInfo("refresh");
                break;
        }
    }

    ThreadPoolExecutor.AbortPolicy mRejectHandle = new ThreadPoolExecutor.AbortPolicy() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
//            super.rejectedExecution(r, e);// 可不抛出异常;自己处理被拒绝的任务
            Toast.makeText(getApplicationContext(), "reject " + r.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    };

    private void initUI() {
        mTvList.add(mTv0);
        mTvList.add(mTv1);
        mTvList.add(mTv2);
        mTvList.add(mTv3);
        mTvList.add(mTv4);
        mTvList.add(mTv5);
        mTvList.add(mTv6);
        mTvList.add(mTv7);
        mTvList.add(mTv8);
        mTvList.add(mTv9);
    }

    private void initThreadPool() {
        mExecutorPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(BLOCK_SIZE),
                Executors.defaultThreadFactory(), mRejectHandle);
        mExecutorPool.allowCoreThreadTimeOut(true);
    }

    public void addNewTask() {
        int num = 0;
        try {
            for (; num < 10; num += 2) {
                mExecutorPool.execute(new WorkFirstRunnable(num));
                mExecutorPool.execute(new WorkSecondRunnable(num + 1));
            }
        } catch (Exception e) {
            Log.e(TAG, "新任务被拒绝 ", e);
        }
        showPoolInfo("added task!");
    }

    // 模拟2种耗时任务
    class WorkFirstRunnable implements Runnable {
        private int number;

        public WorkFirstRunnable(int number) {
            this.number = number;
        }

        @Override
        public synchronized void run() {
            try {
                for (int i = 1; i <= 30; i++) {
                    Thread.sleep(100);
                    mUIHandler.obtainMessage(MSG_TYPE_ONE, number, i).sendToTarget();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class WorkSecondRunnable implements Runnable {
        private int number;

        public WorkSecondRunnable(int number) {
            this.number = number;
        }

        @Override
        public synchronized void run() {
            try {
                for (int i = 1; i <= 30; i++) {
                    Thread.sleep(50);
                    mUIHandler.obtainMessage(MSG_TYPE_TWO, number, i).sendToTarget();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setTextUI(msg.what, msg.arg1, String.valueOf(msg.arg2));
            showPoolInfo("Executing......");
        }
    };

    private void setTextUI(int type, int number, String content) {
        TextView tv = mTvList.get(number);
        if (type == MSG_TYPE_ONE) {
            tv.setTextColor(Color.BLACK);
            tv.setText(String.format(Locale.CHINA, "thread type one %d: %s", number, content));
        } else if (type == MSG_TYPE_TWO) {
            tv.setTextColor(Color.RED);
            tv.setText(String.format(Locale.CHINA, "thread type two %d: %s", number, content));
        }
    }

    private void showPoolInfo(String info) {
        if (mExecutorPool.getActiveCount() == 0) {
            mTitleTv.setText("On hold");
        } else {
            mTitleTv.setText(info);
        }
        mNoteTv.setText(String.format(Locale.CHINA,
                "CorePoolSize: %d \n" +
                        "PoolSize: %d \n" +
                        "MaximumPoolSize: %d \n" +
                        "ActiveCount: %d \n" +
                        "TaskCount: %d \n" +
                        "Block size: %d",
                mExecutorPool.getCorePoolSize(),
                mExecutorPool.getPoolSize(),
                mExecutorPool.getMaximumPoolSize(),
                mExecutorPool.getActiveCount(),
                mExecutorPool.getTaskCount(),
                BLOCK_SIZE));
    }
}

