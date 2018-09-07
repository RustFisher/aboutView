package com.rust.aboutview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rust.aboutview.R;
import com.rust.aboutview.fragment.JoystickStyleOneFragment;
import com.rust.aboutview.fragment.JoystickStyleTwoFragment;


public class JoystickActivity extends AppCompatActivity {

    JoystickStyleOneFragment mJoystickStyleOneFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_common_container);
        mJoystickStyleOneFragment = new JoystickStyleOneFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new JoystickStyleTwoFragment())
                .add(R.id.container, mJoystickStyleOneFragment)
                .commit();
    }
}
