package com.rust.aboutview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Show image
 * Created by Rust on 2018/6/2.
 */
public class ImagePagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Integer> mResIdList;

    public ImagePagerAdapter(Context context, List<Integer> resIdList) {
        mContext = context;
        mResIdList = new ArrayList<>(resIdList);
    }

    @Override
    public int getCount() {
        if (null != mResIdList) {
            return mResIdList.size();
        }
        return 0;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (null != mResIdList) {
            Integer resId = (Integer) ((ImageView) object).getTag();
            if (null != resId) {
                for (Integer i : mResIdList) {
                    if (resId.equals(i)) {
                        return i;
                    }
                }
            }
        }
        return super.getItemPosition(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (mResIdList != null && position < mResIdList.size()) {
            Integer resId = mResIdList.get(position);
            if (resId != null) {
                ImageView itemView = new ImageView(mContext);
                itemView.setScaleType(ImageView.ScaleType.FIT_XY);
                itemView.setPadding(10, 0, 10, 0);
                itemView.setImageResource(resId);

                // 此处假设所有的照片都不同，用resId唯一标识一个itemView；也可用其它Object来标识，只要保证唯一即可
                itemView.setTag(resId);
                container.addView(itemView);
                return itemView;
            }
        }
        return null;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // 注意：此处position是ViewPager中所有要显示的页面的position，与Adapter mDrawableResIdList并不是一一对应的。
        // 因为mDrawableResIdList有可能被修改删除某一个item，在调用notifyDataSetChanged()的时候，ViewPager中的页面
        // 数量并没有改变，只有当ViewPager遍历完自己所有的页面，并将不存在的页面删除后，二者才能对应起来
        int count = container.getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = container.getChildAt(i);
            if (childView == object) {
                container.removeView(childView);
                break;
            }
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


}
