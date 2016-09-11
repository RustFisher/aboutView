package com.rust.aboutview.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rust.aboutview.R;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class RxAndroidActivity extends Activity {

    private static final String TAG = RxAndroidActivity.class.getSimpleName();

    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;
    private TextView mTimerTv;

    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_rx_android);
        Toolbar toolbar = (Toolbar) findViewById(R.id.act_rx_toolbar);
        toolbar.setTitle("RxAndroid demo");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

        mTv1 = (TextView) findViewById(R.id.act_rx_tv1);
        mTv2 = (TextView) findViewById(R.id.act_rx_tv2);
        mTv3 = (TextView) findViewById(R.id.act_rx_tv3);
        mTimerTv = (TextView) findViewById(R.id.act_rx_timer_tv);

        findViewById(R.id.act_rx_btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCount++;
                Log.d(TAG, "onClick: " + mCount);
                /**
                 * 实例1 将事件源与订阅者关联起来
                 */
                @SuppressWarnings("unchecked")
                Observable<String> observable = Observable.create(mObservableAction)
                        .subscribeOn(AndroidSchedulers.mainThread());
                observable.subscribe(mSubscriber1);// 先通知一个，再通知另一个
                observable.subscribe(mActionTv2);  // 这个可以一直执行下去
            }
        });

        findViewById(R.id.act_rx_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 事件产生，分发给订阅者
                Observable<String> oba1 = Observable.just("事件分发源 " + mCount);
                oba1.observeOn(AndroidSchedulers.mainThread());
                oba1.subscribe(mActionTv3);
                oba1.subscribe(mActionShowToast);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int s = 0;
                while (s <= 100) {
                    Observable<String> timerOb = Observable.just(String.valueOf(s) + "s");
                    timerOb.observeOn(AndroidSchedulers.mainThread());
                    timerOb.subscribe(mActionTimer);
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                    s++;
                }
            }
        }).start();
    }

    /**
     * 接收消息的订阅者
     */
    Subscriber<String> mSubscriber1 = new Subscriber<String>() {
        @Override
        public void onCompleted() {
            Log.d(TAG, "onCompleted: got sth");
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, "onError");
        }

        @Override
        public void onNext(String str) {
            Log.d(TAG, "onNext:" + str);
            mTv1.setText(str);
        }
    };

    /**
     * 被观察者
     */
    Observable.OnSubscribe mObservableAction = new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            subscriber.onNext("mObservableAction: " + mCount);
            /**
             * Notifies the Observer that the {@link Observable}
             * has finished sending push-based notifications
             */
            subscriber.onCompleted();// 执行了此方法后，将不再接收处理消息
        }
    };

    /**
     * 作为观察者 - 接收到事件后执行操作
     * 不知为何要起 Action1 这个名字
     */
    private Action1<String> mActionTv2 = new Action1<String>() {
        @Override
        public void call(String s) {
            mTv2.setText(s);
        }
    };

    private Action1<String> mActionTv3 = new Action1<String>() {
        @Override
        public void call(String s) {
            mTv3.setText(s);
        }
    };

    private Action1<String> mActionShowToast = new Action1<String>() {
        @Override
        public void call(String s) {
            Toast.makeText(RxAndroidActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    };

    private Action1<String> mActionTimer = new Action1<String>() {
        @Override
        public void call(String s) {
            final String second = s;
            /**
             * 跑在UI线程里更新
             */
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTimerTv.setText(second);
                }
            });
        }
    };

}
