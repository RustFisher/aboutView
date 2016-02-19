package com.rust.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rust.aboutview.R;

/**
 * Show a floating bar on screen
 */
public class FloatingBarService extends Service {
    //定义浮动窗口布局
    LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;

    Button floatViewButton;

    private static final String TAG = "FloatingBarService";

    @Override
    public void onCreate() {
        super.onCreate();
        createFloatView();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createFloatView() {
        wmParams = new WindowManager.LayoutParams();
        // got the WindowManagerImpl.CompatModeWrapper
        mWindowManager =
                (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        Log.i(TAG, "mWindowManager ---> " + mWindowManager);
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // show on the left and top
        wmParams.gravity = Gravity.START | Gravity.TOP;
        // first position (0,0)
        wmParams.x = 0;
        wmParams.y = 0;

        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_bar_layout, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮
        floatViewButton = (Button) mFloatLayout.findViewById(R.id.float_btn1);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i(TAG, "Width/2--->" + floatViewButton.getMeasuredWidth() / 2);
        Log.i(TAG, "Height/2--->" + floatViewButton.getMeasuredHeight() / 2);
        //设置监听浮动窗口的触摸移动
        floatViewButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                wmParams.x = (int) event.getRawX() - floatViewButton.getMeasuredWidth() / 2;
                Log.i(TAG, "RawX" + event.getRawX());
                Log.i(TAG, "X" + event.getX());
                //减25为状态栏的高度
                wmParams.y = (int) event.getRawY() - floatViewButton.getMeasuredHeight() / 2 - 25;
                Log.i(TAG, "RawY" + event.getRawY());
                Log.i(TAG, "Y" + event.getY());
                //刷新
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                return false;  //此处必须返回false，否则OnClickListener获取不到监听
            }
        });

        floatViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatLayout != null) {
            //移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
        }
    }

}
