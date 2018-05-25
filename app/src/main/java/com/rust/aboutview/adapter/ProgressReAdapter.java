package com.rust.aboutview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rust.aboutview.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Progress data adapter
 * Created by Rust on 2018/5/25.
 */
public class ProgressReAdapter extends RecyclerView.Adapter<ProgressReAdapter.PVH> {

    private List<ProgressItem> mDataList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public ProgressReAdapter() {

    }

    @NonNull
    @Override
    public PVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRoot = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false);
        return new PVH(itemRoot);
    }

    @Override
    public void onBindViewHolder(@NonNull PVH holder, int position) {
        final ProgressItem progressItem = mDataList.get(position);
        holder.nameTv.setText(progressItem.name);
        holder.headIv.setImageResource(R.drawable.pic_my_little_hero); // For example..
        holder.progressBar.setMax(progressItem.totalProgress);
        holder.progressBar.setProgress(progressItem.currentProgress);
        holder.progressTv.setText(String.format(Locale.CHINA, "%d/%d", progressItem.currentProgress, progressItem.totalProgress));
        holder.itemRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onItemClick(progressItem);
                }
            }
        });
        holder.funcIv.setImageResource(progressItem.showGrid ? R.drawable.ic_4_cube_pink : R.drawable.ic_4_cube_gray);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void replaceData(List<ProgressItem> list) {
        mDataList = new ArrayList<>(list);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * Fake data
     */
    public static class ProgressItem {
        int pId = 0;
        public int pType = 0;
        String picUrl;
        String name;
        int totalProgress;
        int currentProgress;
        boolean showGrid = false;

        public ProgressItem(String name, int cP, int tP, int pType) {
            this.name = name;
            this.currentProgress = cP;
            this.totalProgress = tP;
            this.pType = pType;
        }

        public void setShowGrid(boolean showGrid) {
            this.showGrid = showGrid;
        }
    }

    static class PVH extends RecyclerView.ViewHolder {
        View itemRoot;
        ImageView headIv;
        TextView nameTv;
        TextView progressTv;
        ProgressBar progressBar;
        TextView curTv;
        ImageView starIv;
        ImageView funcIv;
        ImageView tickIv;

        PVH(View itemView) {
            super(itemView);
            itemRoot = itemView;
            headIv = itemView.findViewById(R.id.head_iv);
            nameTv = itemView.findViewById(R.id.name_tv);
            progressTv = itemView.findViewById(R.id.progress_tv);
            progressBar = itemView.findViewById(R.id.progress_pb);
            curTv = itemView.findViewById(R.id.cur_tv);
            tickIv = itemView.findViewById(R.id.tick_iv);
            funcIv = itemView.findViewById(R.id.func_iv);
            starIv = itemView.findViewById(R.id.star_iv);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ProgressItem item);

        void onClickStar(ProgressItem item);

        void onClickFunc(ProgressItem item);

        void onClickTick(ProgressItem item);
    }
}
