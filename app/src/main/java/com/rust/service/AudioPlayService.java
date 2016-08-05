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
    private SoundPool mPianoSp;
    private HashMap<String, Integer> mPianoSpMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mPianoSp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        mPianoSpMap.put(DO, mPianoSp.load(getApplicationContext(), R.raw.do1, 1));
        mPianoSpMap.put(RE, mPianoSp.load(getApplicationContext(), R.raw.re2, 1));
        mPianoSpMap.put(MI, mPianoSp.load(getApplicationContext(), R.raw.mi3, 1));
        mPianoSpMap.put(FA, mPianoSp.load(getApplicationContext(), R.raw.fa4, 1));
        mPianoSpMap.put(SOL, mPianoSp.load(getApplicationContext(), R.raw.sol5, 1));
        mPianoSpMap.put(LA, mPianoSp.load(getApplicationContext(), R.raw.la6, 1));
        mPianoSpMap.put(SI, mPianoSp.load(getApplicationContext(), R.raw.si7, 1));
        mPianoSpMap.put(H_DO, mPianoSp.load(getApplicationContext(), R.raw.h_do1, 1));
        mPianoSpMap.put(H_RE, mPianoSp.load(getApplicationContext(), R.raw.h_re2, 1));
        mPianoSpMap.put(H_MI, mPianoSp.load(getApplicationContext(), R.raw.h_mi3, 1));
        mPianoSpMap.put(H_FA, mPianoSp.load(getApplicationContext(), R.raw.h_fa4, 1));
        mPianoSpMap.put(H_SOL, mPianoSp.load(getApplicationContext(), R.raw.h_sol5, 1));
        mPianoSpMap.put(H_LA, mPianoSp.load(getApplicationContext(), R.raw.h_la6, 1));
        mPianoSpMap.put(H_SI, mPianoSp.load(getApplicationContext(), R.raw.h_si7, 1));

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
                mPianoSp.play(mPianoSpMap.get(DO), 1, 1, 1, 0, 1);
                break;
            case RE:
                mPianoSp.play(mPianoSpMap.get(RE), 1, 1, 1, 0, 1);
                break;
            case MI:
                mPianoSp.play(mPianoSpMap.get(MI), 1, 1, 1, 0, 1);
                break;
            case FA:
                mPianoSp.play(mPianoSpMap.get(FA), 1, 1, 1, 0, 1);
                break;
            case SOL:
                mPianoSp.play(mPianoSpMap.get(SOL), 1, 1, 1, 0, 1);
                break;
            case LA:
                mPianoSp.play(mPianoSpMap.get(LA), 1, 1, 1, 0, 1);
                break;
            case SI:
                mPianoSp.play(mPianoSpMap.get(SI), 1, 1, 1, 0, 1);
                break;
            case H_DO:
                mPianoSp.play(mPianoSpMap.get(H_DO), 1, 1, 1, 0, 1);
                break;
            case H_RE:
                mPianoSp.play(mPianoSpMap.get(H_RE), 1, 1, 1, 0, 1);
                break;
            case H_MI:
                mPianoSp.play(mPianoSpMap.get(H_MI), 1, 1, 1, 0, 1);
                break;
            case H_FA:
                mPianoSp.play(mPianoSpMap.get(H_FA), 1, 1, 1, 0, 1);
                break;
            case H_SOL:
                mPianoSp.play(mPianoSpMap.get(H_SOL), 1, 1, 1, 0, 1);
                break;
            case H_LA:
                mPianoSp.play(mPianoSpMap.get(H_LA), 1, 1, 1, 0, 1);
                break;
            case H_SI:
                mPianoSp.play(mPianoSpMap.get(H_SI), 1, 1, 1, 0, 1);
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
                mPianoSp.release();
                stopSelf();
            }
        }
    };

}
