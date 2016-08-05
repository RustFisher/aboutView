package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.rust.aboutview.R;
import com.rust.msgonbus.AudioPlayEventOnBus;

import org.greenrobot.eventbus.EventBus;

public class AudioPlayerActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_audio_player);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
        findViewById(R.id.btn7).setOnClickListener(this);
        findViewById(R.id.btn8).setOnClickListener(this);
        findViewById(R.id.btn9).setOnClickListener(this);
        findViewById(R.id.btn10).setOnClickListener(this);
        findViewById(R.id.btn11).setOnClickListener(this);
        findViewById(R.id.btn12).setOnClickListener(this);
        findViewById(R.id.btn13).setOnClickListener(this);
        findViewById(R.id.btn14).setOnClickListener(this);
        findViewById(R.id.gai_zi).setOnClickListener(this);
        findViewById(R.id.water).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn1:
                EventBus.getDefault().post(new AudioPlayEventOnBus(AudioPlayEventOnBus.AudioType.DO));
                break;
            case R.id.btn2:
                EventBus.getDefault().post(new AudioPlayEventOnBus(AudioPlayEventOnBus.AudioType.RE));
                break;
            case R.id.btn3:
                EventBus.getDefault().post(new AudioPlayEventOnBus(AudioPlayEventOnBus.AudioType.MI));
                break;
            case R.id.btn4:
                EventBus.getDefault().post(new AudioPlayEventOnBus(AudioPlayEventOnBus.AudioType.FA));
                break;
            case R.id.btn5:
                EventBus.getDefault().post(new AudioPlayEventOnBus(AudioPlayEventOnBus.AudioType.SOL));
                break;
            case R.id.btn6:
                EventBus.getDefault().post(new AudioPlayEventOnBus(AudioPlayEventOnBus.AudioType.LA));
                break;
            case R.id.btn7:
                EventBus.getDefault().post(new AudioPlayEventOnBus(AudioPlayEventOnBus.AudioType.SI));
                break;
            case R.id.btn8:
                EventBus.getDefault().post(new AudioPlayEventOnBus(AudioPlayEventOnBus.AudioType.H_DO));
                break;
            case R.id.btn9:
                EventBus.getDefault().post(new AudioPlayEventOnBus(AudioPlayEventOnBus.AudioType.H_RE));
                break;
            case R.id.btn10:
                EventBus.getDefault().post(new AudioPlayEventOnBus(AudioPlayEventOnBus.AudioType.H_MI));
                break;
            case R.id.btn11:
                EventBus.getDefault().post(new AudioPlayEventOnBus(AudioPlayEventOnBus.AudioType.H_FA));
                break;
            case R.id.btn12:
                EventBus.getDefault().post(new AudioPlayEventOnBus(AudioPlayEventOnBus.AudioType.H_SOL));
                break;
            case R.id.btn13:
                EventBus.getDefault().post(new AudioPlayEventOnBus(AudioPlayEventOnBus.AudioType.H_LA));
                break;
            case R.id.btn14:
                EventBus.getDefault().post(new AudioPlayEventOnBus(AudioPlayEventOnBus.AudioType.H_SI));
                break;
            case R.id.gai_zi:
                EventBus.getDefault().post(new AudioPlayEventOnBus(AudioPlayEventOnBus.AudioType.GAI_ZI));
                break;
            case R.id.water:
                EventBus.getDefault().post(new AudioPlayEventOnBus(AudioPlayEventOnBus.AudioType.WATER));
                break;
        }
    }
}
