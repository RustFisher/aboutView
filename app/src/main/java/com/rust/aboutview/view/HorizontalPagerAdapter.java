package com.rust.aboutview.view;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rust.aboutview.R;

public class HorizontalPagerAdapter extends PagerAdapter {

    // Needs a final array
    private final FlipperCard[] cards = new FlipperCard[]{
            new FlipperCard(R.mipmap.pic_num_1, "Strategy"),
            new FlipperCard(R.mipmap.pic_num_2, "Design"),
            new FlipperCard(R.mipmap.pic_num_3, "Development")
    };

    private LayoutInflater mLayoutInflater;

    public HorizontalPagerAdapter(LayoutInflater layoutInflater) {
        mLayoutInflater = layoutInflater;
    }

    public HorizontalPagerAdapter(LayoutInflater layoutInflater,FlipperCard[] inputCards) {
        mLayoutInflater = layoutInflater;
        for (int i = 0;i < cards.length;i++) {
            cards[i] = inputCards[i];
        }
    }



    @Override
    public int getCount() {
        return cards.length;
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view;
        view = mLayoutInflater.inflate(R.layout.flipper_card_item, container, false);
        setupItem(view, cards[position]);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }

    public static void setupItem(final View view, final FlipperCard flipperCard) {
        final TextView txt = (TextView) view.findViewById(R.id.txt_item);
        txt.setText(flipperCard.getTitle());

        final ImageView img = (ImageView) view.findViewById(R.id.img_item);
        img.setImageResource(flipperCard.getRes());
    }

    public static class FlipperCard {

        private String mTitle;
        private int mRes;

        public FlipperCard(final int res, final String title) {
            mRes = res;
            mTitle = title;
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(final String title) {
            mTitle = title;
        }

        public int getRes() {
            return mRes;
        }

        public void setRes(final int res) {
            mRes = res;
        }
    }
}
