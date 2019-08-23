package com.rust.aboutview.widget;

import androidx.annotation.NonNull;

public class VKey {

    public static final char BACKSPACE_ASCII = 8;  // 退格
    public static final char SPACE_ASCII = 32;     // 空格

    static final int TYPE_NORMAL = 1; // 普通键
    static final int TYPE_FUNC = 2;   // 功能键

    static final int UI_TEXT_VIEW = 100; // 用TextView实现
    static final int UI_IMAGE_VIEW = 200;// 用ImageView实现

    private int keyType;
    private int uiType = UI_TEXT_VIEW;
    private String keyText;
    private int asciiCode;

    public VKey(String keyText, int keyCode, int type) {
        this.keyText = keyText;
        this.asciiCode = keyCode;
        this.keyType = type;
    }

    public int getAsciiCode() {
        return asciiCode;
    }

    public String getKeyText() {
        return keyText;
    }

    public static VKey normal(String key, int keyCode) {
        return new VKey(key, keyCode, TYPE_NORMAL);
    }

    public static VKey func(String key, int keyCode) {
        return new VKey(key, keyCode, TYPE_FUNC);
    }

    public static VKey backspace() {
        VKey vKey = new VKey("back", BACKSPACE_ASCII, TYPE_FUNC);

        return vKey;
    }

    public boolean isNormal() {
        return keyType == TYPE_NORMAL;
    }

    public boolean isFunc() {
        return keyType == TYPE_FUNC;
    }

    public int getUiType() {
        return uiType;
    }

    public void setUiType(int uiType) {
        this.uiType = uiType;
    }

    public boolean useTextView() {
        return uiType == UI_TEXT_VIEW;
    }

    public boolean useImageView() {
        return uiType == UI_IMAGE_VIEW;
    }

    public boolean isBackSpace() {
        return asciiCode == BACKSPACE_ASCII;
    }

    @NonNull
    @Override
    public String toString() {
        return "keyText{" + keyText + ", " + asciiCode + "}";
    }
}
