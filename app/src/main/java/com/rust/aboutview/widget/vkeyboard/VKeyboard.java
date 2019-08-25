package com.rust.aboutview.widget.vkeyboard;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Keyboard
 * Created on 2019-8-23
 */
public class VKeyboard extends FrameLayout {
    private String TAG = "rustAppVKeyboard";

    private List<TextView> cacheTvs;
    private Context ctx;
    private LinearLayout board1; // 26个字母

    private VKeyboardListener keyboardListener;

    public VKeyboard(@NonNull Context context) {
        this(context, null);
    }

    public VKeyboard(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VKeyboard(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ctx = context;
        Log.d(TAG, "VKeyboard create");
        int[] attrsArray = new int[]{
                android.R.attr.id, // 0
                android.R.attr.background, // 1
                android.R.attr.layout_width, // 2
                android.R.attr.layout_height // 3
        };
        final TypedArray a = context.obtainStyledAttributes(attrs, attrsArray);
        int viewId = a.getResourceId(0, View.NO_ID);
        TAG += ("[" + viewId + "]");
        a.recycle();
        initKeyboardUI(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, String.format(Locale.CHINA, "onSizeChanged w:%d, h:%d, oldw:%d, oldh:%d", w, h, oldw, oldh));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Log.d(TAG, "onMeasure end. get wid and height: " + getWidth() + ", " + getHeight());
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
        Log.d(TAG, "initKeyboardUI");
        cacheTvs = new ArrayList<>();
        ViewGroup.LayoutParams lp1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        board1 = new LinearLayout(context, null);
        board1.setOrientation(LinearLayout.VERTICAL);
        addView(board1, lp1);
    }

    private LinearLayout genLine(VRow vRow, Context context) {
        List<VKey> keys = vRow.getKeys();
        LinearLayout line = new LinearLayout(context, null);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setGravity(Gravity.CENTER);
        line.setPadding(dp2Px(vRow.getPaddingLeft()), dp2Px(vRow.getPaddingTop()), dp2Px(vRow.getPaddingRight()), dp2Px(vRow.getPaddingBottom()));
        for (final VKey vKey : keys) {
            if (vKey.useTextView()) {
                TextView tv = new TextView(context, null);
                tv.setText(vKey.getKeyText());
                if (vKey.isTextBold()) {
                    tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
                }
                tv.setTextColor(vKey.getTextColor());
                tv.setTextSize(vKey.getTextSizeSp());
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv.getLayoutParams();
                if (lp == null) {
                    lp = new LinearLayout.LayoutParams(dp2Px(vKey.getKeyViewWidthDp()), dp2Px(vKey.getKeyViewHeightDp()));
                }
                lp.leftMargin = dp2Px(vKey.getMarginLeft());
                lp.rightMargin = dp2Px(vKey.getMarginRight());
                tv.setLayoutParams(lp);
                tv.setGravity(Gravity.CENTER);
                if (vKey.isUseBgRes()) {
                    tv.setBackgroundResource(vKey.getBackgroundResId());
                } else {
                    tv.setBackgroundColor(vKey.getBackgroundColor());
                }
                line.addView(tv, lp);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (keyboardListener != null) {
                            keyboardListener.onKeyClick(vKey);
                        }
                    }
                });
                if (vKey.isEmptyNormalType()) {
                    tv.setVisibility(INVISIBLE);
                }
                cacheTvs.add(tv);
            }
        }
        Log.d(TAG, "genLine: current tv count: " + cacheTvs.size());
        return line;
    }

    public void setKeyboardListener(VKeyboardListener keyboardListener) {
        this.keyboardListener = keyboardListener;
    }

    private static int dp2Px(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    Adapter adapter;

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        if (this.adapter != null) {
            loadAdapter();
        } else {
            Log.w(TAG, "setAdapter: input adapter is null");
        }
    }

    private void loadAdapter() {
        List<VRow> rows = adapter.getRows();
        VKeyboardBody keyboardBody = adapter.getKeyboardBody();
        if (keyboardBody.isBgUseRes()) {
            board1.setBackgroundResource(keyboardBody.getBgResId());
        } else {
            board1.setBackgroundColor(keyboardBody.getBgColor());
        }
        for (VRow row : rows) {
            LinearLayout line = genLine(row, ctx);
            board1.addView(line);
        }
    }

    public static abstract class Adapter {

        public abstract List<VRow> getRows();

        public abstract VKeyboardBody getKeyboardBody();

        public float px2Dp(float px) {
            return (px / Resources.getSystem().getDisplayMetrics().density);
        }

        public int dp2Px(float dp) {
            return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
        }

    }
}
