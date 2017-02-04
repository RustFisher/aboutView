package com.rust.aboutview.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rust.aboutview.R;

import java.util.ArrayList;

public class PageListAdapter extends RecyclerView.Adapter<PageListAdapter.ViewHolder> {

    private ArrayList<DeviceItemViewEntity> mDataList;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;// Set this listener to item view

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(View view) {
            super(view);
        }
    }

    public static class DeviceItemViewEntity {
        public String itemID;
        public String typeName;

        public DeviceItemViewEntity(String ID, String name) {
            this.itemID = ID;
            this.typeName = name;
        }
    }

    // Provide a suitable constructor (depends on the kind of data set)
    public PageListAdapter(ArrayList<DeviceItemViewEntity> deviceItemViewEntities) {
        mDataList = deviceItemViewEntities;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.page_item_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.mTextView = (TextView) v.findViewById(R.id.item_title_field);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextView.setText(mDataList.get(position).typeName);
        if (mOnItemClickListener != null) {
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            });
            holder.mTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(v, position);
                    return true;
                }
            });
        }
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
