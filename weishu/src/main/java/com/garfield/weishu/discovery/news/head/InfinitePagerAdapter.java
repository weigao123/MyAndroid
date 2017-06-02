package com.garfield.weishu.discovery.news.head;

import android.content.Context;
import android.view.ViewGroup;

import com.garfield.weishu.base.viewpager.view.BasePagerAdapter;
import com.garfield.weishu.discovery.news.bean.netease.NewsBean;

import java.util.List;

/**
 * Created by gaowei3 on 2016/10/31.
 */

public class InfinitePagerAdapter extends BasePagerAdapter<NewsBean> {

    public InfinitePagerAdapter(Context context, List<NewsBean> items) {
        super(context, items);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        //会ANR
        //getViewPager().setCurrentItem(0, false);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public Class getViewHolderClassAtPosition(int position) {
        return NewsHeadViewHolder.class;
    }

    @Override
    public int getCount() {
        if (getItems().isEmpty()) return 0;
        return Integer.MAX_VALUE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 重定位到mItems里真实的数据
        int realPosition = getRealPosition(position);
        return super.instantiateItem(container, realPosition);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int realPosition = getRealPosition(position);
        super.destroyItem(container, realPosition, object);
    }

    int getRealPosition(int position) {
        return position % getItems().size();
    }

    /**
     * 如果为 POSITION_UNCHANGED，则什么都不做；
     * 如果为 POSITION_NONE，则调用 PagerAdapter.destroyItem() 来去掉该对象，
     * 并设置为需要刷新 (needPopulate = true) 以便触发 PagerAdapter.instantiateItem() 来生成新的对象
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
