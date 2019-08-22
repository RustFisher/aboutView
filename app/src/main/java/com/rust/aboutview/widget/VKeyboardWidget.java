package com.rust.aboutview.widget;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rust.aboutview.R;

import java.util.Arrays;
import java.util.List;

/**
 * 2019-8-22
 */
public class VKeyboardWidget {
    private static final String TAG = "rustAppVKeyboardWidget";

    public static final char BACKSPACE_ASCII = 8;  // 退格
    public static final char SPACE_ASCII = 32;     // 空格

    // 键盘默认宽度 一般我们用屏幕宽度来定
    // 用这个宽度来动态计算每个按钮的宽度
    private int keyboardWidthPx = 1080;

    private int normalKeyWidPx = 80;

    private int keyHeightPx = dp2Px(42); // 按键的高度

    private List<Key> line1Keys = Arrays.asList(
            Key.normal("q", 'q'), Key.normal("w", 'w'), Key.normal("e", 'e'),
            Key.normal("r", 'r'), Key.normal("t", 't'), Key.normal("y", 'y'),
            Key.normal("u", 'u'), Key.normal("i", 'i'), Key.normal("o", 'o'), Key.normal("p", 'p')
    );

    private List<Key> line2Keys = Arrays.asList(
            Key.normal("a", 'a'), Key.normal("s", 's'), Key.normal("d", 'd'),
            Key.normal("f", 'f'), Key.normal("g", 'g'), Key.normal("h", 'h'),
            Key.normal("j", 'j'), Key.normal("k", 'k'), Key.normal("l", 'l')
    );

    private List<Key> line3Keys = Arrays.asList(
            Key.normal("z", 'z'), Key.normal("x", 'x'), Key.normal("c", 'c'),
            Key.normal("v", 'v'), Key.normal("b", 'b'), Key.normal("n", 'n'),
            Key.normal("m", 'm'), Key.func("Back", BACKSPACE_ASCII)
    );


    // 按键
    public static class Key {
        static final int TYPE_NORMAL = 1; // 普通键
        static final int TYPE_FUNC = 2;   // 功能键

        static final int UI_TEXT_VIEW = 100; // 用TextView实现
        static final int UI_IMAGE_VIEW = 200;// 用ImageView实现

        private int keyType;
        private int uiType = UI_TEXT_VIEW;
        private String keyText;
        private int asciiCode;

        public Key(String keyText, int keyCode, int type) {
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

        public static Key normal(String key, int keyCode) {
            return new Key(key, keyCode, TYPE_NORMAL);
        }

        public static Key func(String key, int keyCode) {
            return new Key(key, keyCode, TYPE_FUNC);
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

    private LinearLayout rootView;

    public VKeyboardWidget(LinearLayout root, int keyboardWidthPx) {
        this.keyboardWidthPx = keyboardWidthPx;
        this.rootView = root;
        rootView.setOrientation(LinearLayout.VERTICAL);
        initKeyboardUI();
    }

    private void initKeyboardUI() {
        normalKeyWidPx = (keyboardWidthPx - dp2Px(10)) / line1Keys.size() - dp2Px(1) * 2;
        LinearLayout line1 = genLine(line1Keys);
        rootView.addView(line1);
        LinearLayout line2 = genLine(line2Keys);
        rootView.addView(line2);
        LinearLayout line3 = genLine(line3Keys);
        rootView.addView(line3);
    }

    private LinearLayout genLine(List<Key> keys) {
        LinearLayout line = new LinearLayout(rootView.getContext(), null);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setGravity(Gravity.CENTER);
        line.setPadding(dp2Px(5), dp2Px(5), dp2Px(5), 0);
        for (final Key key : keys) {
            if (key.useTextView()) {
                TextView tv = new TextView(rootView.getContext(), null);
                tv.setText(key.getKeyText());
                tv.setTextColor(Color.BLACK);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv.getLayoutParams();
                if (lp == null) {
                    lp = new LinearLayout.LayoutParams(normalKeyWidPx, keyHeightPx);
                }
                lp.leftMargin = dp2Px(1);
                lp.rightMargin = dp2Px(1);
                tv.setLayoutParams(lp);
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundResource(R.drawable.vk_se_tv_1);
                line.addView(tv, lp);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onKeyClick(key);
                        }
                    }
                });
            }
        }
        return line;
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onKeyClick(Key key);
    }

    private static float px2Dp(float px) {
        return (px / Resources.getSystem().getDisplayMetrics().density);
    }

    private static int dp2Px(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
