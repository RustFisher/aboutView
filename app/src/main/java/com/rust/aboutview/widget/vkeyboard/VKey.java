package com.rust.aboutview.widget.vkeyboard;

import android.graphics.Color;

import androidx.annotation.NonNull;

/**
 * Key
 * Created on 2019-8-22
 */
public class VKey {

    /// key code
    public static final char BACKSPACE_ASCII = 8;  // 退格 ascii
    public static final char SPACE_ASCII = 32;     // 空格 ascii
    public static final int OK_CODE = 10001;       // ok键 特殊按键

    /// key type
    private static final int TYPE_NORMAL = 1;        // 普通键
    private static final int TYPE_NORMAL_EMPTY = -1; // 普通键大小 用来占位
    private static final int TYPE_FUNC = 2;          // 功能键

    /// key view class
    private static final int UI_TEXT_VIEW = 100; // 用TextView实现
    private static final int UI_IMAGE_VIEW = 200;// 用ImageView实现

    private int keyType = TYPE_NORMAL;
    private int uiType = UI_TEXT_VIEW;

    private float keyViewWidthDp = 20;
    private float keyViewHeightDp = 42;
    private int backgroundColor = Color.WHITE;
    private int backgroundResId;
    private boolean useBgRes = false;
    private int marginLeft = 3;  // dp
    private int marginRight = 3;

    /// Use TextView
    private String keyText;
    private int keyCode; // 有的是ascii码，有的是自定义码
    private int textColor = Color.BLACK;
    private int textSizeSp = 12;
    private boolean textBold = false;

    /// Use ImageView
    private int imageResId;

    public VKey() {
    }

    public VKey(String keyText, int keyCode, int type) {
        this.keyText = keyText;
        this.keyCode = keyCode;
        this.keyType = type;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public String getKeyText() {
        return keyText;
    }

    public boolean isNormal() {
        return keyType == TYPE_NORMAL;
    }

    public boolean isFunc() {
        return keyType == TYPE_FUNC;
    }

    public boolean isOk() {
        return keyCode == OK_CODE;
    }

    public int getUiType() {
        return uiType;
    }

    public void setUiType(int uiType) {
        this.uiType = uiType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }

    public void setKeyText(String keyText) {
        this.keyText = keyText;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTextSizeSp(int textSizeSp) {
        this.textSizeSp = textSizeSp;
    }

    public void setTextBold(boolean textBold) {
        this.textBold = textBold;
    }

    public int getKeyType() {
        return keyType;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getTextSizeSp() {
        return textSizeSp;
    }

    public boolean isTextBold() {
        return textBold;
    }

    public boolean useTextView() {
        return uiType == UI_TEXT_VIEW;
    }

    public boolean useImageView() {
        return uiType == UI_IMAGE_VIEW;
    }

    public boolean isBackSpace() {
        return keyCode == BACKSPACE_ASCII;
    }

    public boolean isEmptyNormalType() {
        return keyType == TYPE_NORMAL_EMPTY;
    }

    public int getBackgroundResId() {
        return backgroundResId;
    }

    public void setBackgroundResId(int backgroundResId) {
        this.backgroundResId = backgroundResId;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public float getKeyViewWidthDp() {
        return keyViewWidthDp;
    }

    public void setKeyViewWidthDp(float keyViewWidthDp) {
        this.keyViewWidthDp = keyViewWidthDp;
    }

    public float getKeyViewHeightDp() {
        return keyViewHeightDp;
    }

    public void setKeyViewHeightDp(float keyViewHeightDp) {
        this.keyViewHeightDp = keyViewHeightDp;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    public int getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

    public boolean isUseBgRes() {
        return useBgRes;
    }

    public void setUseBgRes(boolean useBgRes) {
        this.useBgRes = useBgRes;
    }

    @NonNull
    @Override
    public String toString() {
        return "keyText{" + keyText + ", " + keyCode + "}";
    }

    public static VKey normal(String key, int keyCode) {
        return new VKey(key, keyCode, TYPE_NORMAL);
    }

    public static VKey func(String key, int keyCode) {
        return new VKey(key, keyCode, TYPE_FUNC);
    }

    public static VKey backspace() {
        return new VKey("back", BACKSPACE_ASCII, TYPE_FUNC);
    }

    public static VKey ok() {
        VKey key = new VKey("OK", OK_CODE, TYPE_FUNC);
        key.setTextBold(true);
        return key;
    }

    public static VKey emptyNormal() {
        return new VKey("", 0, TYPE_NORMAL_EMPTY);
    }

    // 创建一个使用ImageView的按键
    public static VKey iv(int bgRes, int imageResId) {
        VKey key = new VKey();
        key.setKeyType(TYPE_NORMAL);
        key.setUiType(UI_IMAGE_VIEW);
        key.setImageResId(imageResId);
        key.setBackgroundResId(bgRes);
        return key;
    }

}
