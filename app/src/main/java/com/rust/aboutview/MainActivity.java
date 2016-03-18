package com.rust.aboutview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rust.aboutview.bluetooth.BluetoothActivity;
import com.rust.arslan.ArslanActivity;
import com.rust.contactview.PeopleMainActivity;
import com.rust.service.FloatingBarService;

public class MainActivity extends AppCompatActivity {

    ListView demoList;
    SharedPreferences mConfigs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConfigs = getSharedPreferences(AboutViewConfig.APP_CONFIG, MODE_PRIVATE);
        SharedPreferences.Editor editor = mConfigs.edit();
        editor.putBoolean(AboutViewConfig.SHOW_FLOAT_BAR, false);
        editor.apply();
        String[] demoNames = {
                getString(R.string.image_process),
                getString(R.string.device_info),
                getString(R.string.swipe_refresh),
                getString(R.string.drawer_activity),
                getString(R.string.arslan_activity),
                getString(R.string.notification_activity),
                getString(R.string.clip_children_activity),
                getString(R.string.animation_activity),
                getString(R.string.circle_activity),
                getString(R.string.bluetooth_activity),
                getString(R.string.contact_activity),
                getString(R.string.float_bar_activity),
                getString(R.string.drawer_line_activity),
                getString(R.string.fragment_tab_host_activity)};
        demoList = (ListView) findViewById(R.id.demo_list_view);
        ArrayAdapter<String> demoListAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);
        for (String s : demoNames) {
            demoListAdapter.add(s);
        }
        demoList.setAdapter(demoListAdapter);
        demoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                switch (position) {
                    case 0: {
                        i.setClass(getApplicationContext(), ImageProcessingActivity.class);
                        startActivity(i);
                        break;
                    }
                    case 1: {
                        i.setClass(getApplicationContext(), ResolutionRatioActivity.class);
                        startActivity(i);
                        break;
                    }
                    case 2: {
                        i.setClass(getApplicationContext(), MySwipeActivity.class);
                        startActivity(i);
                        break;
                    }
                    case 3: {
                        i.setClass(getApplicationContext(), DrawerActivity.class);
                        startActivity(i);
                        break;
                    }
                    case 4: {
                        i.setClass(getApplicationContext(), ArslanActivity.class);
                        startActivity(i);
                        break;
                    }
                    case 5: {
                        i.setClass(getApplicationContext(), NotificationActivity.class);
                        startActivity(i);
                        break;
                    }
                    case 6: {
                        i.setClass(getApplicationContext(), ClipChildrenActivity.class);
                        startActivity(i);
                        break;
                    }
                    case 7: {
                        i.setClass(getApplicationContext(), FrameAnimationActivity.class);
                        startActivity(i);
                        break;
                    }
                    case 8: {
                        i.setClass(getApplicationContext(), CirclesActivity.class);
                        startActivity(i);
                        break;
                    }
                    case 9: {
                        i.setClass(getApplicationContext(), BluetoothActivity.class);
                        startActivity(i);
                        break;
                    }
                    case 10: {
                        i.setClass(getApplicationContext(), PeopleMainActivity.class);
                        startActivity(i);
                        break;
                    }
                    case 11: {
                        boolean mFloatBarShow =
                                !mConfigs.getBoolean(AboutViewConfig.SHOW_FLOAT_BAR, false);
                        SharedPreferences.Editor editor = mConfigs.edit();
                        editor.putBoolean(AboutViewConfig.SHOW_FLOAT_BAR, mFloatBarShow);
                        editor.apply();
                        if (mConfigs.getBoolean(AboutViewConfig.SHOW_FLOAT_BAR, false)) {
                            Intent floatBarIntent =
                                    new Intent(MainActivity.this, FloatingBarService.class);
                            startService(floatBarIntent);
                        } else {
                            Intent floatBarIntent =
                                    new Intent(MainActivity.this, FloatingBarService.class);
                            stopService(floatBarIntent);
                        }
                        break;
                    }
                    case 12: {
                        i.setClass(getApplicationContext(), DrawLineActivity.class);
                        startActivity(i);
                        break;
                    }
                    case 13: {
                        i.setClass(getApplicationContext(),FragmentTabHostDemo.class);
                        startActivity(i);
                        break;
                    }
                    default:
                        break;
                }
            }
        });

    }

    /**
     * replace findViewById()
     */
    private <T extends View> T fv(int resId) {
        return (T) super.findViewById(resId);
    }
}
