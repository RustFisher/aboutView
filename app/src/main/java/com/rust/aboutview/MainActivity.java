package com.rust.aboutview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rust.aboutview.activity.FragmentCommunicationActivity;
import com.rust.arslan.ArslanActivity;
import com.rust.contactview.PeopleMainActivity;
import com.rust.service.FloatingBarService;
import com.rust.widget.PageItemDecoration;
import com.rust.widget.PageListAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String IMAGE_PROCESSING = "image_processing";
    private static final String DEVICE_INFO = "device_info";
    private static final String PULL_TO_REFRESH = "pull_to_refresh";
    private static final String DRAWER_LAYOUT = "drawer_layout";
    private static final String ARSLAN_NET_WORK = "Arslan";
    private static final String NOTIFICATION_DEMO = "notification_demo";
    private static final String CLIP_DEMO = "clip_demo";
    private static final String CIRCLE_VIEW = "circle_view";
    private static final String CONTACT_PAGE = "contact_page";
    private static final String FLOAT_WINDOW = "floating_window";
    private static final String FOLLOW_CURSOR = "follow_cursor";
    private static final String FRAGMENT_TAB_HOST = "fragment_tab_host";
    private static final String FRAGMENT_COMMUNITY = "fragment_community";

    private RecyclerView mPagesView;

    SharedPreferences mConfigs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConfigs = getSharedPreferences(AboutViewConfig.APP_CONFIG, MODE_PRIVATE);
        SharedPreferences.Editor editor = mConfigs.edit();
        editor.putBoolean(AboutViewConfig.SHOW_FLOAT_BAR, false);
        editor.apply();

        mPagesView = (RecyclerView) findViewById(R.id.pages_view);

        initPageList();

    }

    private void initPageList() {
        final ArrayList<PageListAdapter.DeviceItemViewEntity> pageItemViewEntities = new ArrayList<>();
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(IMAGE_PROCESSING, getString(R.string.image_process)));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(DEVICE_INFO, getString(R.string.device_info)));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(PULL_TO_REFRESH, getString(R.string.swipe_refresh)));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(DRAWER_LAYOUT, getString(R.string.drawer_activity)));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(ARSLAN_NET_WORK, getString(R.string.arslan_activity)));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(NOTIFICATION_DEMO, getString(R.string.notification_activity)));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(CLIP_DEMO, getString(R.string.clip_children_activity)));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(CIRCLE_VIEW, getString(R.string.circle_activity)));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(CONTACT_PAGE, getString(R.string.contact_activity)));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(FLOAT_WINDOW, getString(R.string.float_bar_activity)));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(FOLLOW_CURSOR, getString(R.string.drawer_line_activity)));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(FRAGMENT_TAB_HOST, getString(R.string.fragment_tab_host_activity)));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(FRAGMENT_COMMUNITY, "fragment与activity通信"));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mPagesView.setLayoutManager(gridLayoutManager);

        PageListAdapter pageListAdapter = new PageListAdapter(pageItemViewEntities);
        pageListAdapter.setOnItemClickListener(new PageListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id = pageItemViewEntities.get(position).itemID;
                Intent i = new Intent();
                switch (id) {
                    case IMAGE_PROCESSING:
                        i.setClass(getApplicationContext(), ImageProcessingActivity.class);
                        startActivity(i);
                        break;
                    case DEVICE_INFO:
                        i.setClass(getApplicationContext(), ResolutionRatioActivity.class);
                        startActivity(i);
                        break;
                    case PULL_TO_REFRESH:
                        i.setClass(getApplicationContext(), MySwipeActivity.class);
                        startActivity(i);
                        break;
                    case DRAWER_LAYOUT:
                        i.setClass(getApplicationContext(), DrawerActivity.class);
                        startActivity(i);
                        break;
                    case ARSLAN_NET_WORK:
                        i.setClass(getApplicationContext(), ArslanActivity.class);
                        startActivity(i);
                        break;
                    case NOTIFICATION_DEMO:
                        i.setClass(getApplicationContext(), NotificationActivity.class);
                        startActivity(i);
                        break;
                    case CLIP_DEMO:
                        i.setClass(getApplicationContext(), ClipChildrenActivity.class);
                        startActivity(i);
                        break;
                    case CIRCLE_VIEW:
                        i.setClass(getApplicationContext(), CirclesActivity.class);
                        startActivity(i);
                        break;
                    case CONTACT_PAGE:
                        i.setClass(getApplicationContext(), PeopleMainActivity.class);
                        startActivity(i);
                        break;
                    case FLOAT_WINDOW:
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
                    case FOLLOW_CURSOR:
                        i.setClass(getApplicationContext(), DrawLineActivity.class);
                        startActivity(i);
                        break;
                    case FRAGMENT_TAB_HOST:
                        i.setClass(getApplicationContext(), FragmentTabHostDemo.class);
                        startActivity(i);
                        break;
                    case FRAGMENT_COMMUNITY:
                        i.setClass(getApplicationContext(), FragmentCommunicationActivity.class);
                        startActivity(i);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        mPagesView.addItemDecoration(new PageItemDecoration(getApplicationContext()));
        mPagesView.setAdapter(pageListAdapter);
    }

    /**
     * replace findViewById()
     */
    private <T extends View> T fv(int resId) {
        return (T) super.findViewById(resId);
    }
}
