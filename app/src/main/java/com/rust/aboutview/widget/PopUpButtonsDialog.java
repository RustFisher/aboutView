package com.rust.aboutview.widget;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.rust.aboutview.R;

public class PopUpButtonsDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "rustApp";

    private Context mContext;
    private View mTitleView;
    private View mItemView1;
    private View mItemView2;
    private View mItemView3;
    private int mWindowHeight = 1920;
    private static final int ITEM_MOVE_TIME_MM = 400; // 运动时间
    private static final int ITEM2_MOVE_TIME_MM = ITEM_MOVE_TIME_MM + 40;
    private static final int ITEM3_MOVE_TIME_MM = ITEM_MOVE_TIME_MM + 80;
    private static final int ITEM4_MOVE_TIME_MM = ITEM_MOVE_TIME_MM + 120;

    public PopUpButtonsDialog(@NonNull Context context) {
        super(context, R.style.DialogFullScreen);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        View root = LayoutInflater.from(mContext).inflate(R.layout.dialog_buttons, null, false);
        mTitleView = root.findViewById(R.id.title_tv);
        mItemView1 = root.findViewById(R.id.btn1);
        mItemView2 = root.findViewById(R.id.btn2);
        mItemView3 = root.findViewById(R.id.btn3);
        mTitleView.setVisibility(View.INVISIBLE);
        mItemView1.setVisibility(View.INVISIBLE);
        mItemView2.setVisibility(View.INVISIBLE);
        mItemView3.setVisibility(View.INVISIBLE);
        setContentView(root);
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        if (null != window) {
            WindowManager windowManager = window.getWindowManager();
            Point sizePoint = new Point();
            windowManager.getDefaultDisplay().getSize(sizePoint);
            mWindowHeight = sizePoint.y;
        }

        Log.d(TAG, "show");
        Log.d(TAG, "窗口高度 " + mWindowHeight);
        ValueAnimator anim = ValueAnimator.ofInt(0, 520);
        anim.setDuration(520);
        anim.setRepeatCount(0);

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (int) valueAnimator.getAnimatedValue();
                int midTargetY = mWindowHeight / 2; // 让中间的子视图居中
                int oneOffset = (int) (mItemView1.getHeight() * 1.5);
                // 最底下那个先跑出来
                if (val >= 0 && val <= ITEM_MOVE_TIME_MM) {
                    updateItemView(val, ITEM_MOVE_TIME_MM, midTargetY - oneOffset, mItemView3);
                } else if (val > ITEM_MOVE_TIME_MM) {
                    updateItemView(ITEM_MOVE_TIME_MM, ITEM_MOVE_TIME_MM, midTargetY - oneOffset, mItemView3);
                }
                if (val >= 40 && val <= ITEM2_MOVE_TIME_MM) {
                    updateItemView(val, ITEM2_MOVE_TIME_MM, midTargetY, mItemView2);
                } else if (val > ITEM2_MOVE_TIME_MM) {
                    updateItemView(ITEM2_MOVE_TIME_MM, ITEM2_MOVE_TIME_MM, midTargetY, mItemView2);
                }
                if (val >= 80 && val <= ITEM3_MOVE_TIME_MM) {
                    updateItemView(val, ITEM3_MOVE_TIME_MM, midTargetY + oneOffset, mItemView1);
                } else if (val > ITEM3_MOVE_TIME_MM) {
                    updateItemView(ITEM3_MOVE_TIME_MM, ITEM3_MOVE_TIME_MM, midTargetY + oneOffset, mItemView1);
                }
                if (val >= 120) {
                    updateItemView(val, ITEM4_MOVE_TIME_MM, midTargetY + 2 * oneOffset, mTitleView);
                }

            }
        });
        anim.start();
    }

    private void updateItemView(int curVal, int endVal, int targetY, View view) {
        view.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) view.getLayoutParams();
        param.bottomMargin = targetY - (endVal - curVal);
        view.setLayoutParams(param);
    }

    @Override
    public void onClick(View view) {

    }
}
