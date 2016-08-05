package com.rust.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import com.rust.AboutConstants;
import com.rust.aboutview.R;
import com.rust.msgonbus.AudioPlayEventOnBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

public class AudioPlayService extends Service {

    private static final String TAG = AudioPlayService.class.getSimpleName();

    private static final String DING = "ding";
    private static final String DING_DING_DING = "ding_ding_ding";
    private SoundPool mSoundPool;
    private HashMap<String, Integer> mSpMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        mSpMap.put(DING, mSoundPool.load(getApplicationContext(), R.raw.do1, 1));
        mSpMap.put(DING_DING_DING, mSoundPool.load(getApplicationContext(), R.raw.re2, 1));

        registerReceiver(mAudioBroadcastReceiver, new IntentFilter(AboutConstants.STOP_ALL_SERVICES));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mAudioBroadcastReceiver);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AudioPlayEventOnBus event) {
        switch (event.getType()) {
            case DO:
                mSoundPool.play(mSpMap.get(DING), 1, 1, 1, 0, 1);
                break;
            case RE:
                mSoundPool.play(mSpMap.get(DING_DING_DING), 1, 1, 1, 0, 1);
                break;
            default:
                break;
        }
    }

    private final BroadcastReceiver mAudioBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (AboutConstants.STOP_ALL_SERVICES.equals(action)) {
                mSoundPool.release();
                stopSelf();
            }
        }
    };

}
