package com.rust.msgonbus;

public class AudioPlayEventOnBus {
    public enum AudioType {
        DO, RE, MI, FA, SOL, LA, SI, H_DO, H_RE, H_MI, H_FA, H_SOL, H_LA, H_SI,
        GAI_ZI, WATER
    }

    private AudioType type = AudioType.DO;

    public AudioPlayEventOnBus() {

    }

    public AudioPlayEventOnBus(AudioType type) {
        this.type = type;
    }

    public AudioType getType() {
        return this.type;
    }

    public void setType(AudioType type) {
        this.type = type;
    }
}
