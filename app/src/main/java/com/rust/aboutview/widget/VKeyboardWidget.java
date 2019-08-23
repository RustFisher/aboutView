package com.rust.aboutview.widget;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rust.aboutview.R;

import java.util.Arrays;
import java.util.List;

/**
 * 2019-8-22
 */
public class VKeyboardWidget {
    private static final String TAG = "rustAppVKeyboardWidget";

    // 键盘默认宽度 一般我们用屏幕宽度来定
    // 用这个宽度来动态计算每个按钮的宽度
    private int keyboardWidthPx = 1080;

    private int normalKeyWidPx = 80;

    private int keyHeightPx = dp2Px(42); // 按键的高度

    private List<VKey> line1Keys = Arrays.asList(
            VKey.normal("q", 'q'), VKey.normal("w", 'w'), VKey.normal("e", 'e'),
            VKey.normal("r", 'r'), VKey.normal("t", 't'), VKey.normal("y", 'y'),
            VKey.normal("u", 'u'), VKey.normal("i", 'i'), VKey.normal("o", 'o'), VKey.normal("p", 'p')
    );

    private List<VKey> line2Keys = Arrays.asList(
            VKey.normal("a", 'a'), VKey.normal("s", 's'), VKey.normal("d", 'd'),
            VKey.normal("f", 'f'), VKey.normal("g", 'g'), VKey.normal("h", 'h'),
            VKey.normal("j", 'j'), VKey.normal("k", 'k'), VKey.normal("l", 'l')
    );

    private List<VKey> line3Keys = Arrays.asList(
            VKey.normal("z", 'z'), VKey.normal("x", 'x'), VKey.normal("c", 'c'),
            VKey.normal("v", 'v'), VKey.normal("b", 'b'), VKey.normal("n", 'n'),
            VKey.normal("m", 'm'), VKey.backspace()
    );


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

    private LinearLayout genLine(List<VKey> keys) {
        LinearLayout line = new LinearLayout(rootView.getContext(), null);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setGravity(Gravity.CENTER);
        line.setPadding(dp2Px(5), dp2Px(5), dp2Px(5), 0);
        for (final VKey VKey : keys) {
            if (VKey.useTextView()) {
                TextView tv = new TextView(rootView.getContext(), null);
                tv.setText(VKey.getKeyText());
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
                            onItemClickListener.onKeyClick(VKey);
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
        void onKeyClick(VKey VKey);
    }

    private static float px2Dp(float px) {
        return (px / Resources.getSystem().getDisplayMetrics().density);
    }

    private static int dp2Px(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
