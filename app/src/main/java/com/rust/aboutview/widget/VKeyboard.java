package com.rust.aboutview.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rust.aboutview.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Keyboard
 * Created on 2019-8-23
 */
public class VKeyboard extends FrameLayout {
    private String TAG = "rustAppVKeyboard";

    private int viewId = View.NO_ID;

    // 键盘默认宽度 一般我们用屏幕宽度来定
    // 用这个宽度来动态计算每个按钮的宽度
    private int keyboardWidthPx = 1080;

    private int normalKeyWidPx = 80;

    private int keyHeightPx = dp2Px(42); // 按键的高度
    private List<TextView> cacheTvs;
    private List<ViewGroup> rows;

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

    private VKeyboardListener keyboardListener;

    public VKeyboard(@NonNull Context context) {
        this(context, null);
    }

    public VKeyboard(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VKeyboard(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int[] attrsArray = new int[]{
                android.R.attr.id, // 0
                android.R.attr.background, // 1
                android.R.attr.layout_width, // 2
                android.R.attr.layout_height // 3
        };
        final TypedArray a = context.obtainStyledAttributes(attrs, attrsArray);
        viewId = a.getResourceId(0, View.NO_ID);
        TAG += ("[" + viewId + "]");
        int layoutWidth = a.getLayoutDimension(2, ViewGroup.LayoutParams.MATCH_PARENT);
        if (layoutWidth > 0) {
            keyboardWidthPx = layoutWidth;
        }
        a.recycle();
        initKeyboardUI(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, String.format(Locale.CHINA, "onSizeChanged w:%d, h:%d, oldw:%d, oldh:%d", w, h, oldw, oldh));
        keyboardWidthPx = w;
        calNormalKeyWid();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure end. get wid and height: " + getWidth() + ", " + getHeight());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout");
        super.onLayout(changed, left, top, right, bottom);
//        adjustItemViewSize();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw");
        super.onDraw(canvas);
    }

    private void initKeyboardUI(Context context) {
        cacheTvs = new ArrayList<>();
        rows = new ArrayList<>();
        ViewGroup.LayoutParams lp1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        calNormalKeyWid();
        LinearLayout rootRow1 = new LinearLayout(context, null);
        rootRow1.setOrientation(LinearLayout.VERTICAL);
        rootRow1.addView(genLine(line1Keys, context), lp1);
        rootRow1.addView(genLine(line2Keys, context), lp1);
        rootRow1.addView(genLine(line3Keys, context), lp1);
        addView(rootRow1, lp1);
    }

    private void calNormalKeyWid() {
        normalKeyWidPx = (keyboardWidthPx - dp2Px(10)) / line1Keys.size() - dp2Px(1) * 2;
        Log.d(TAG, "calNormalKeyWid: " + normalKeyWidPx);
    }

    private void adjustItemViewSize() {
//        Log.d(TAG, "adjustItemViewSize: rows count: " + rows.size());
        for (ViewGroup line : rows) {
            ViewGroup.LayoutParams lineLp = line.getLayoutParams();
            if (lineLp == null) {
                lineLp = new ViewGroup.LayoutParams(keyboardWidthPx, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            lineLp.width = keyboardWidthPx;
            line.setLayoutParams(lineLp);
//            Log.d(TAG, "adjustItemViewSize: line child count: " + line.getChildCount());
            for (int i = 0; i < line.getChildCount(); i++) {
//                Log.d(TAG, "adjustItemViewSize: i: " + i);
                View child = line.getChildAt(i);
                ViewGroup.LayoutParams lp = child.getLayoutParams();
                if (lp == null) {
                    lp = new ViewGroup.LayoutParams(normalKeyWidPx, keyHeightPx);
                }
                lp.width = normalKeyWidPx;
                lp.height = keyHeightPx;
                child.setLayoutParams(lp);
//                Log.d(TAG, "adjustItemViewSize: " + normalKeyWidPx + ", " + keyHeightPx);
            }
        }
    }

    private LinearLayout genLine(List<VKey> keys, Context context) {
        LinearLayout line = new LinearLayout(context, null);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setGravity(Gravity.CENTER);
        line.setPadding(dp2Px(5), dp2Px(5), dp2Px(5), 0);
        for (final VKey VKey : keys) {
            if (VKey.useTextView()) {
                TextView tv = new TextView(context, null);
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
                        if (keyboardListener != null) {
                            keyboardListener.onKeyClick(VKey);
                        }
                    }
                });
                cacheTvs.add(tv);
            }
        }
        Log.d(TAG, "genLine: current tv count: " + cacheTvs.size());
        rows.add(line);
        return line;
    }

    public void setKeyboardListener(VKeyboardListener keyboardListener) {
        this.keyboardListener = keyboardListener;
    }

    private static float px2Dp(float px) {
        return (px / Resources.getSystem().getDisplayMetrics().density);
    }

    private static int dp2Px(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
