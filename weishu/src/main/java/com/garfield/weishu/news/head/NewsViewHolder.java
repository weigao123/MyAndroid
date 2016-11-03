package com.garfield.weishu.news.head;

import android.widget.ImageView;

import com.garfield.weishu.R;
import com.garfield.weishu.base.viewpager.TPagerViewHolder;

/**
 * Created by gaowei3 on 2016/10/31.
 */

public class NewsViewHolder extends TPagerViewHolder<String> {

    private ImageView mHeadImage;

    @Override
    protected int getResId() {
        return R.layout.item_news_head;
    }

    @Override
    protected void inflateView() {
        mHeadImage = findView(R.id.item_news_head_image);
    }

    @Override
    protected void setView() {

    }

    @Override
    protected void refresh(String item) {
        mHeadImage.setImageResource(R.drawable.cheese_1);
        //ImageLoader.getInstance().displayImage(item, mHeadImage, ImageLoaderUtils.getDisplayImageNoDiskCacheOptions());
    }

    @Override
    protected InfinitePagerAdapter getAdapter() {
        return (InfinitePagerAdapter) mAdapter;
    }
}
