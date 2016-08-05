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


import com.rust.AboutConstants;
import com.rust.aboutview.R;
import com.rust.msgonbus.AudioPlayEventOnBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

public class AudioPlayService extends Service {

    private static final String TAG = AudioPlayService.class.getSimpleName();

    private static final String DO = "do";
    private static final String RE = "re";
    private static final String MI = "mi";
    private static final String FA = "fa";
    private static final String SOL = "sol";
    private static final String LA = "la";
    private static final String SI = "si";
    private static final String H_DO = "h_do";
    private static final String H_RE = "h_re";
    private static final String H_MI = "h_mi";
    private static final String H_FA = "h_fa";
    private static final String H_SOL = "h_sol";
    private static final String H_LA = "h_la";
    private static final String H_SI = "h_si";
    private SoundPool mSoundPool;
    private HashMap<String, Integer> mSpMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        mSpMap.put(DO, mSoundPool.load(getApplicationContext(), R.raw.do1, 1));
        mSpMap.put(RE, mSoundPool.load(getApplicationContext(), R.raw.re2, 1));
        mSpMap.put(MI, mSoundPool.load(getApplicationContext(), R.raw.mi3, 1));
        mSpMap.put(FA, mSoundPool.load(getApplicationContext(), R.raw.fa4, 1));
        mSpMap.put(SOL, mSoundPool.load(getApplicationContext(), R.raw.sol5, 1));
        mSpMap.put(LA, mSoundPool.load(getApplicationContext(), R.raw.la6, 1));
        mSpMap.put(SI, mSoundPool.load(getApplicationContext(), R.raw.si7, 1));
        mSpMap.put(H_DO, mSoundPool.load(getApplicationContext(), R.raw.h_do1, 1));
        mSpMap.put(H_RE, mSoundPool.load(getApplicationContext(), R.raw.h_re2, 1));
        mSpMap.put(H_MI, mSoundPool.load(getApplicationContext(), R.raw.h_mi3, 1));
        mSpMap.put(H_FA, mSoundPool.load(getApplicationContext(), R.raw.h_fa4, 1));
        mSpMap.put(H_SOL, mSoundPool.load(getApplicationContext(), R.raw.h_sol5, 1));
        mSpMap.put(H_LA, mSoundPool.load(getApplicationContext(), R.raw.h_la6, 1));
        mSpMap.put(H_SI, mSoundPool.load(getApplicationContext(), R.raw.h_si7, 1));

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
                mSoundPool.play(mSpMap.get(DO), 1, 1, 1, 0, 1);
                break;
            case RE:
                mSoundPool.play(mSpMap.get(RE), 1, 1, 1, 0, 1);
                break;
            case MI:
                mSoundPool.play(mSpMap.get(MI), 1, 1, 1, 0, 1);
                break;
            case FA:
                mSoundPool.play(mSpMap.get(FA), 1, 1, 1, 0, 1);
                break;
            case SOL:
                mSoundPool.play(mSpMap.get(SOL), 1, 1, 1, 0, 1);
                break;
            case LA:
                mSoundPool.play(mSpMap.get(LA), 1, 1, 1, 0, 1);
                break;
            case SI:
                mSoundPool.play(mSpMap.get(SI), 1, 1, 1, 0, 1);
                break;
            case H_DO:
                mSoundPool.play(mSpMap.get(H_DO), 1, 1, 1, 0, 1);
                break;
            case H_RE:
                mSoundPool.play(mSpMap.get(H_RE), 1, 1, 1, 0, 1);
                break;
            case H_MI:
                mSoundPool.play(mSpMap.get(H_MI), 1, 1, 1, 0, 1);
                break;
            case H_FA:
                mSoundPool.play(mSpMap.get(H_FA), 1, 1, 1, 0, 1);
                break;
            case H_SOL:
                mSoundPool.play(mSpMap.get(H_SOL), 1, 1, 1, 0, 1);
                break;
            case H_LA:
                mSoundPool.play(mSpMap.get(H_LA), 1, 1, 1, 0, 1);
                break;
            case H_SI:
                mSoundPool.play(mSpMap.get(H_SI), 1, 1, 1, 0, 1);
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
