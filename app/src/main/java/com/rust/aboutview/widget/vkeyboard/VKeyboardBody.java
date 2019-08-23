package com.rust.aboutview.widget.vkeyboard;

/**
 * 键盘的背景
 * Created on 2019-8-24
 */
public class VKeyboardBody {

    private int bgColor;
    private int bgResId;
    private boolean bgUseRes = false;

    public VKeyboardBody(int bgColor) {
        this.bgColor = bgColor;
    }

    public VKeyboardBody(int bgResId, boolean bgUseRes) {
        this.bgResId = bgResId;
        this.bgUseRes = bgUseRes;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getBgResId() {
        return bgResId;
    }

    public void setBgResId(int bgResId) {
        this.bgResId = bgResId;
    }

    public boolean isBgUseRes() {
        return bgUseRes;
    }

    public void setBgUseRes(boolean bgUseRes) {
        this.bgUseRes = bgUseRes;
    }
}
