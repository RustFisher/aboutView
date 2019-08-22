package com.rust.aboutview.view;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rust.aboutview.R;

import java.util.ArrayList;

/**
 * Adapter for showing color list
 */
public class ColorBoardListAdapter extends RecyclerView.Adapter<ColorBoardListAdapter.ViewHolder> {

    private ArrayList<ColorItemViewEntity> mDataList;
    private int mSelectedPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;// Set this listener to item view

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    public void setSelectedPosition(int position) {
        this.mSelectedPosition = position;
        notifyDataSetChanged();
        notifyItemChanged(position);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView mImageView;

        public ViewHolder(View view) {
            super(view);
        }
    }

    public static class ColorItemViewEntity {
        public int color;
        public int id;

        public ColorItemViewEntity(int ID, int color) {
            this.id = ID;
            this.color = color;
        }
    }

    // Provide a suitable constructor (depends on the kind of data set)
    public ColorBoardListAdapter(ArrayList<ColorItemViewEntity> deviceItemViewEntities) {
        mDataList = deviceItemViewEntities;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ColorBoardListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_item_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.mImageView = (CircleImageView) v.findViewById(R.id.color_item_image_view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mImageView.setColor(mDataList.get(position).color);
        holder.mImageView.setSelected(mSelectedPosition == position);
        if (mOnItemClickListener != null) {
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            });
            holder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
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
