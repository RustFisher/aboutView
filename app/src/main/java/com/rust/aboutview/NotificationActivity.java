package com.rust.aboutview;

import android.app.NotificationManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

class notifyBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager nMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent goHome = new Intent(Intent.ACTION_MAIN);
        goHome.addCategory(Intent.CATEGORY_HOME);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.signal_horn_26px)
                .setContentText("点击返回桌面")
                .setContentTitle("Go home")
                .setTicker("来自广播的提示")
                .setContentIntent(PendingIntent.getActivity(context, 0, goHome, 0));
        Notification notificationBroadcast = builder.build();
        notificationBroadcast.flags |= Notification.FLAG_AUTO_CANCEL;
        nMgr.notify(002, notificationBroadcast);/* id相同；此提示与 Notification 2 只能显示一个 */
    }
}

public class NotificationActivity extends AppCompatActivity {
    public static final String BroadcastNotify = "com.rust.notify.broadcast";

    private EditText editContent;
    Button sendNotification;
    Button notifyButton1;
    Button notifyButton2;
    Button cleanButton;
    Button notifyBroadcast;

    int notificationId = 1;

    private BroadcastReceiver notifyReceiver;
    InputMethodManager inputMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_demo);

        IntentFilter filter = new IntentFilter(BroadcastNotify);
        notifyReceiver = new notifyBroadcast();
        registerReceiver(notifyReceiver, filter);

        /* get the widgets */
        editContent = (EditText) findViewById(R.id.et_content);
        sendNotification = (Button) findViewById(R.id.btn_send_content);
        notifyButton1 = (Button) findViewById(R.id.btn_notify_1);
        notifyButton2 = (Button) findViewById(R.id.btn_notify_2);
        notifyBroadcast = (Button) findViewById(R.id.btn_notify_broadcast);
        cleanButton = (Button) findViewById(R.id.btn_clean_notification);

        /* 构造一个Bitmap，显示在下拉栏中 */
        final Bitmap notifyBitmapTrain = BitmapFactory
                .decodeResource(this.getResources(), R.drawable.train);

        /* 管理器-用来发送notification */
        final NotificationManager nMgr =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notifyButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), NotificationActivity.class);

                NotificationCompat.Builder nBuilder1 =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setTicker("Notify 1 ! ")/* 状态栏显示的提示语 */
                                .setContentText("Go back to RustNotifyDemo")/* 下拉栏中的内容 */
                                .setSmallIcon(R.drawable.notification_small_icon_24)/* 状态栏图片 */
                                .setLargeIcon(notifyBitmapTrain)/* 下拉栏内容显示的图片 */
                                .setContentTitle("notifyButton1 title")/* 下拉栏显示的标题 */
                                .setContentIntent(PendingIntent
                                        .getActivity(getApplicationContext(), 0, intent,
                                                PendingIntent.FLAG_UPDATE_CURRENT));
                                        /* 直接使用PendingIntent.getActivity()；不需要实例 */
                                        /* getActivity() 是 static 方法*/
                Notification n = nBuilder1.build();/* 直接创建Notification */
                n.flags |= Notification.FLAG_AUTO_CANCEL;/* 点击后触发时间，提示自动消失 */
                nMgr.notify(notificationId, n);
            }
        });

        notifyButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder nBuilder2 =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setTicker("Notify 2 ! ")/* 状态栏显示的提示语 */
                                .setContentText("Notification 2 content")/* 下拉栏中的内容 */
                                .setSmallIcon(R.drawable.floppy_16px)/* 状态栏图片 */
                                .setLargeIcon(notifyBitmapTrain)/* 下拉栏内容显示的图片 */
                                .setContentTitle("title2");/* 下拉栏显示的标题 */
                nMgr.notify(notificationId + 1, nBuilder2.build());
                /* 两个id一样的notification不能同时显示，会被新的提示替换掉 */
            }
        });

        sendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editContent.getText().toString();
                if (content.equals("")) {
                    content = "U input nothing";
                }
                NotificationCompat.Builder contentBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setTicker(content)/* 状态栏显示的提示语 */
                                .setContentText("I can auto cancel")/* 下拉栏中的内容 */
                                .setSmallIcon(R.drawable.rain_32px)/* 状态栏图片 */
                                .setLargeIcon(notifyBitmapTrain)/* 下拉栏内容显示的图片 */
                                .setContentTitle("Edit title");/* 下拉栏显示的标题 */
                Notification n = contentBuilder.build();
                nMgr.notify(notificationId + 2, n);
            }
        });

        notifyBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BroadcastNotify);
                sendBroadcast(i);
            }
        });

        cleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nMgr.cancel(notificationId);/* 根据id，撤销notification */
            }
        });
    }

    /**
     * 点击空白处，软键盘自动消失
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMgr.hideSoftInputFromWindow(
                        getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(notifyReceiver);
        super.onDestroy();
    }
}
