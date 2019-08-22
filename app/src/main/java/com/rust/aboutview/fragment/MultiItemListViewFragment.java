package com.rust.aboutview.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rust.aboutview.R;
import com.rust.aboutview.multiitemlv.MultiItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Multi item type in ListView
 * Created by Rust on 2018/5/31.
 */
public class MultiItemListViewFragment extends Fragment {
    private static final String TAG = "rustAppMIL";
    private static final String LOG_PRE = "[MultiItemTypeFrag] ";
    private MultiItemAdapter mMultiItemAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, LOG_PRE + "onCreate");
        mMultiItemAdapter = new MultiItemAdapter(getContext());
        mMultiItemAdapter.setDataList(createTestData());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, LOG_PRE + "onCreateView");
        return inflater.inflate(R.layout.frag_multi_item_lv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView lv = view.findViewById(R.id.lv1);
        lv.setAdapter(mMultiItemAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MultiItemAdapter.DataBean item = mMultiItemAdapter.getItem(position);
                Log.d(TAG, "onItemClick: " + item.muType);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private List<MultiItemAdapter.DataBean> createTestData() {
        List<MultiItemAdapter.DataBean> list = new ArrayList<>();
        list.add(MultiItemAdapter.newBannerBean(R.drawable.wallpaper_2018_04_14, R.drawable.wallpaper_2018_04_16, R.drawable.wallpaper_2018_04_19, R.drawable.wallpaper_2018_04_25));
        list.add(MultiItemAdapter.newOneLineBean("Android Activity 基础概念"));
        list.add(MultiItemAdapter.newOneLineBean("Android 横屏时 Activity 跳转显示问题"));
        list.add(MultiItemAdapter.newPicOneLineBean("Android 扫描WiFi，连接WiFi", R.drawable.ic_star_gray));
        list.add(MultiItemAdapter.newPicOneLineBean("Android Activity OOM bug", R.drawable.ic_tick_in_cube));
        list.add(MultiItemAdapter.newPicOneLineBean("Android 属性动画使用", R.drawable.ic_4_cube_gray));
        list.add(MultiItemAdapter.newPicOneLineBean("Android ANR", R.drawable.ic_4_cube_gray));
        list.add(MultiItemAdapter.newPicOneLineBean("Android Fragment 基础概念", R.drawable.ic_4_cube_gray));
        list.add(MultiItemAdapter.newPicOneLineBean("Android 多语言自动适配", R.drawable.ic_tick_in_cube));
        list.add(MultiItemAdapter.newPicOneLineBean("Android AsyncTask", R.drawable.ic_tick_in_cube));
        list.add(MultiItemAdapter.newPicOneLineBean("Android Bluetooth 简析", R.drawable.ic_4_cube_gray));
        list.add(MultiItemAdapter.newPicOneLineBean("Android 广播机制（Broadcast）介绍与使用", R.drawable.ic_star_gray));
        list.add(MultiItemAdapter.newOnePicBean(R.drawable.pic_my_little_hero));
        list.add(MultiItemAdapter.newPicOneLineBean("Android Button selector 自定义背景", R.drawable.ic_star_gray));
        list.add(MultiItemAdapter.newPicOneLineBean("Android DialogFragment 自定义", R.drawable.ic_4_cube_gray));
        list.add(MultiItemAdapter.newPicOneLineBean("Android App 工程中的模块化", R.drawable.ic_4_cube_gray));
        list.add(MultiItemAdapter.newPicOneLineBean("Android handler 可能会造成内存泄露", R.drawable.ic_4_cube_gray));
        list.add(MultiItemAdapter.newOnePicBean(R.drawable.pic_dart_lang));
        list.add(MultiItemAdapter.newOneLineBean("Android 使用OpenCV - 将OpenCV相关库做成模块形式"));
        list.add(MultiItemAdapter.newPicOneLineBean("Android NDK Makefile相关", R.drawable.ic_4_cube_gray));
        list.add(MultiItemAdapter.newPicOneLineBean("Android 图片处理", R.drawable.ic_star_gray));
        return list;
    }
}
