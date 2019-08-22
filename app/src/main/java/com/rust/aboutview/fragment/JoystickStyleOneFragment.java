package com.rust.aboutview.fragment;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rust.aboutview.R;
import com.rustfisher.uijoystick.controller.DefaultController;
import com.rustfisher.uijoystick.listener.JoystickTouchViewListener;
import com.rustfisher.uijoystick.model.PadStyle;

import java.util.Locale;

public class JoystickStyleOneFragment extends Fragment {

    private static final String TAG = "rustApp";
    DefaultController mDefaultController;
    TextView mModeTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_joystick_style_1, container, false);
        initJoystick(root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mModeTv = view.findViewById(R.id.mode_tv);
        mModeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mDefaultController.getPadStyle()) {
                    case FLOATING:
                        mDefaultController.setPadStyle(PadStyle.FIXED);
                        break;
                    case FIXED:
                        mDefaultController.setPadStyle(PadStyle.FLOATING);
                        break;
                }
                updateModeTv();
            }
        });
        updateModeTv();
    }

    private void updateModeTv() {
        mModeTv.setText(String.format(Locale.CHINA, "当前模式 - %s", mDefaultController.getPadStyle().toString()));
    }

    private void initJoystick(View root) {
        mDefaultController =
                new DefaultController(getContext(),
                        (RelativeLayout) root.findViewById(R.id.joystick_container));
        mDefaultController.createViews();
        mDefaultController.showViews(false);
        mDefaultController.setLeftTouchViewListener(new JoystickTouchViewListener() {
            @Override
            public void onTouch(float horizontalPercent, float verticalPercent) {
                Log.d(TAG, "onTouch left: " + horizontalPercent + ", " + verticalPercent);
            }

            @Override
            public void onReset() {
                Log.d(TAG, "onReset: left");
            }

            @Override
            public void onActionDown() {
                Log.d(TAG, "onActionDown: left");
            }

            @Override
            public void onActionUp() {
                Log.d(TAG, "onActionUp: left");
            }
        });
        mDefaultController.setRightTouchViewListener(new JoystickTouchViewListener() {
            @Override
            public void onTouch(float horizontalPercent, float verticalPercent) {
                Log.d(TAG, "onTouch right: " + horizontalPercent + ", " + verticalPercent);
            }

            @Override
            public void onReset() {
                Log.d(TAG, "onReset: right");
            }

            @Override
            public void onActionDown() {
                Log.d(TAG, "onActionDown: right");
            }

            @Override
            public void onActionUp() {
                Log.d(TAG, "onActionUp: right");
            }
        });
    }
}
