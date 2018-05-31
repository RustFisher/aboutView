package com.rust.aboutview.multiitemlv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rust.aboutview.R;
import com.rust.aboutview.adapter.holder.BannerVH;

import java.util.ArrayList;
import java.util.List;

/**
 * Multi item type
 * Created by Rust on 2018/5/31.
 */
public class MultiItemAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private List<DataBean> mDataList = new ArrayList<>();

    public MultiItemAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setDataList(List<DataBean> list) {
        mDataList = new ArrayList<>(list);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public DataBean getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataBean dataBean = mDataList.get(position);
        switch (dataBean.muType) {
            case ONE_LINE:
                OneLineVH oneLineVH;
                if (null == convertView) {
                    convertView = mLayoutInflater.inflate(R.layout.item_one_line, null);
                    oneLineVH = new OneLineVH(convertView);
                    convertView.setTag(oneLineVH);
                } else {
                    oneLineVH = (OneLineVH) convertView.getTag();
                }
                oneLineVH.tv1.setText(dataBean.str1);
                break;
            case PIC_AND_ONE_LINE:
                PicOneLineVH picOneLineVH;
                if (null == convertView) {
                    convertView = mLayoutInflater.inflate(R.layout.item_pic_one_line, null);
                    picOneLineVH = new PicOneLineVH(convertView);
                    convertView.setTag(picOneLineVH);
                } else {
                    picOneLineVH = (PicOneLineVH) convertView.getTag();
                }
                picOneLineVH.iv1.setImageResource(dataBean.picRes1);
                picOneLineVH.tv1.setText(dataBean.str1);
                break;
            case ONE_PIC:
                OnePicVH onePicVH;
                if (null == convertView) {
                    convertView = mLayoutInflater.inflate(R.layout.item_one_pic, null);
                    onePicVH = new OnePicVH(convertView);
                    convertView.setTag(onePicVH);
                } else {
                    onePicVH = (OnePicVH) convertView.getTag();
                }
                onePicVH.iv1.setImageResource(dataBean.picRes1);
                break;
            case BANNER:
                BannerVH bannerVH;
                if (null == convertView) {
                    convertView = mLayoutInflater.inflate(R.layout.item_banner, null);
                    bannerVH = new BannerVH();
                    convertView.setTag(bannerVH);
                } else {
                    bannerVH = (BannerVH) convertView.getTag();
                }

                break;
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).muType.code;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    public static class DataBean {
        public MuType muType;
        public String str1;
        public String str2;
        public int picRes1;
        public int picRes2;

        public DataBean() {

        }
    }

    class OneLineVH {
        TextView tv1;

        public OneLineVH(View item) {
            tv1 = item.findViewById(R.id.tv1);
        }
    }

    class OnePicVH {
        ImageView iv1;

        public OnePicVH(View item) {
            iv1 = item.findViewById(R.id.iv1);
        }
    }

    class PicOneLineVH {
        TextView tv1;
        ImageView iv1;

        public PicOneLineVH(View item) {
            tv1 = item.findViewById(R.id.tv1);
            iv1 = item.findViewById(R.id.iv1);
        }
    }

    public static DataBean newOneLineBean(String text) {
        DataBean bean = new DataBean();
        bean.muType = MuType.ONE_LINE;
        bean.str1 = text;
        return bean;
    }

    public static DataBean newOnePicBean(int resId) {
        DataBean bean = new DataBean();
        bean.muType = MuType.ONE_PIC;
        bean.picRes1 = resId;
        return bean;
    }

    public static DataBean newPicOneLineBean(String text, int resId) {
        DataBean bean = new DataBean();
        bean.muType = MuType.PIC_AND_ONE_LINE;
        bean.str1 = text;
        bean.picRes1 = resId;
        return bean;
    }

}