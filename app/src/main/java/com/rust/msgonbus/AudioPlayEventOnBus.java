package com.rust.msgonbus;

public class AudioPlayEventOnBus {
    public enum AudioType {
        DO, RE
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
