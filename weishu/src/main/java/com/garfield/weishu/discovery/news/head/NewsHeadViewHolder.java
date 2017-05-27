package com.garfield.weishu.discovery.news.head;

import android.widget.ImageView;

import com.garfield.baselib.utils.http.image.ImageHelper;
import com.garfield.weishu.R;
import com.garfield.weishu.base.viewpager.view.BasePagerViewHolder;
import com.garfield.weishu.discovery.news.bean.netease.NewsBean;

/**
 * Created by gaowei3 on 2016/10/31.
 */

public class NewsHeadViewHolder extends BasePagerViewHolder<NewsBean> {

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
    protected void refresh(NewsBean item) {
        ImageHelper.load(mRootView.getContext(), item.getImgsrc(), mHeadImage);
    }

    @Override
    protected InfinitePagerAdapter getAdapter() {
        return (InfinitePagerAdapter) mAdapter;
    }
}
