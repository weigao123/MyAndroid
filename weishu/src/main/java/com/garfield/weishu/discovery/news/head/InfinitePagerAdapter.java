package com.garfield.weishu.discovery.news.head;

import android.content.Context;
import android.view.ViewGroup;

import com.garfield.weishu.base.viewpager.TPagerAdapter;
import com.garfield.weishu.discovery.news.bean.NewsBean;

import java.util.List;

/**
 * Created by gaowei3 on 2016/10/31.
 */

public class InfinitePagerAdapter extends TPagerAdapter<NewsBean> {

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
        int realPosition = getRealPosition(position);
        return super.instantiateItem(container, realPosition);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int realPosition = getRealPosition(position);
        super.destroyItem(container, realPosition, object);
    }

    public int getRealPosition(int position) {
        return position % getItems().size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
