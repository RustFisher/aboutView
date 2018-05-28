package com.rust.aboutview.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rust.aboutview.R;

import java.util.ArrayList;

public class PageListAdapter extends RecyclerView.Adapter<PageListAdapter.ViewHolder> {
    public enum ItemType {
        VIEW, WIDGET, FUNCTION
    }

    private ArrayList<DeviceItemViewEntity> mDataList;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;// Set this listener to item view

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View itemRoot;
        ImageView headIv;
        TextView nameTv;

        ViewHolder(View view) {
            super(view);
            itemRoot = view;
        }
    }

    public static class DeviceItemViewEntity {
        public String itemID;
        public String typeName;
        public ItemType itemType = ItemType.VIEW;

        public DeviceItemViewEntity(String ID, String name) {
            this.itemID = ID;
            this.typeName = name;
        }

        public DeviceItemViewEntity(String ID, String name, ItemType type) {
            this.itemID = ID;
            this.typeName = name;
            this.itemType = type;
        }
    }

    public PageListAdapter(ArrayList<DeviceItemViewEntity> deviceItemViewEntities) {
        mDataList = deviceItemViewEntities;
    }

    @Override
    public PageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.page_item_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.nameTv = v.findViewById(R.id.item_title_field);
        vh.headIv = v.findViewById(R.id.head_iv);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DeviceItemViewEntity entity = mDataList.get(position);
        holder.nameTv.setText(entity.typeName);
        switch (entity.itemType) {
            case VIEW:
                holder.headIv.setImageResource(R.drawable.ic_view);
                break;
            case WIDGET:
                holder.headIv.setImageResource(R.drawable.ic_handler);
                break;
            case FUNCTION:
                holder.headIv.setImageResource(R.drawable.ic_apps_white_24dp);
                break;
        }
        if (mOnItemClickListener != null) {
            holder.itemRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            });
            holder.itemRoot.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(v, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
