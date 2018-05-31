package com.rust.aboutview.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private List<MultiItemAdapter.DataBean> createTestData() {
        List<MultiItemAdapter.DataBean> list = new ArrayList<>();
        list.add(MultiItemAdapter.newOneLineBean("Android - 条纹进度条实现"));
        list.add(MultiItemAdapter.newOneLineBean("Android NDK Makefile相关"));
        list.add(MultiItemAdapter.newOnePicBean(R.drawable.pic_my_little_hero));
        list.add(MultiItemAdapter.newPicOneLineBean("Android 扫描WiFi，连接WiFi", R.drawable.ic_star_gray));
        list.add(MultiItemAdapter.newPicOneLineBean("Android 属性动画使用", R.drawable.ic_tick_in_cube));
        return list;
    }
}
