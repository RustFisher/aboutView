package com.rust.aboutview.widget.vkeyboard;

import java.util.List;

/**
 * Contains keys
 * Created on 2019-8-25
 */
public class VRow {

    private int paddingLeft; // dp
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int marginLeft;
    private int marginTop;
    private int marginRight;
    private int marginBottom;

    private List<VKey> keys;

    public VRow(List<VKey> keys) {
        this.keys = keys;
    }

    public List<VKey> getKeys() {
        return keys;
    }

    public void setKeys(List<VKey> keys) {
        this.keys = keys;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    public int getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    public int getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

    public int getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }
}
