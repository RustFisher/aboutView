package com.rust.aboutview.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rust.aboutview.R;
import com.rust.aboutview.adapter.ProgressReAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示具体的数据
 * Do not use ButterKnife here
 * Created by Rust on 2018/5/25.
 */
public class ProgressContentFragment extends Fragment {
    private static final String TAG = "rustAppProC";
    public static final String K_DATA_TYPE = "key_data_type";
    ProgressReAdapter mProgressReAdapter;
    private int mShowType = 0; // 要显示的类别
    private List<ProgressReAdapter.ProgressItem> mDataList;

    public void setDataList(int type, List<ProgressReAdapter.ProgressItem> list) {
        Bundle bundle = getArguments();
        if (null == bundle) {
            bundle = new Bundle();
        }
        bundle.putInt(K_DATA_TYPE, type);
        setArguments(bundle);
        this.mDataList = new ArrayList<>();
        for (ProgressReAdapter.ProgressItem item : list) {
            if (item.pType == type) {
                mDataList.add(item);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (null != bundle) {
            mShowType = bundle.getInt(K_DATA_TYPE, 0);
        } else {
            Log.w(TAG, "onCreate: 无bundle");
        }
        Log.d(TAG, "onCreate: mShowType==" + mShowType);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_progress_content, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated:  mShowType==" + mShowType);
        RecyclerView reView = (RecyclerView) view.findViewById(R.id.re_view);
        mProgressReAdapter = new ProgressReAdapter();
        reView.setLayoutManager(new LinearLayoutManager(getContext()));
        reView.setAdapter(mProgressReAdapter);
        mProgressReAdapter.setOnItemClickListener(new ProgressReAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ProgressReAdapter.ProgressItem item) {

            }

            @Override
            public void onClickStar(ProgressReAdapter.ProgressItem item) {

            }

            @Override
            public void onClickFunc(ProgressReAdapter.ProgressItem item) {

            }

            @Override
            public void onClickTick(ProgressReAdapter.ProgressItem item) {

            }
        });
        mProgressReAdapter.replaceData(mDataList);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: mShowType==" + mShowType);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: mShowType==" + mShowType);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: mShowType==" + mShowType);
    }
}
