package com.garfield.weishu.news.old;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.garfield.baselib.utils.L;
import com.garfield.weishu.base.viewpager.TPagerAdapter;
import com.garfield.weishu.news.head.NewsViewHolder;

import java.util.List;

/**
 * Created by gaowei3 on 2016/10/31.
 */

public class InfinitePagerAdapter_v2 extends TPagerAdapter<String> {

    public InfinitePagerAdapter_v2(Context context, List<String> items) {
        super(context, items);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public Class getViewHolderClassAtPosition(int position) {
        return NewsViewHolder.class;
    }

    @Override
    public int getCount() {
        return getItems().size() + 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = getRealPosition(position);
        //L.d("I position: "+position + "  realPosition: "+realPosition);
        return super.instantiateItem(container, realPosition);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //int realPosition = getRealPosition(position);
        //L.d("D position: "+position + "  realPosition: "+realPosition);
        super.destroyItem(container, position, object);
    }

    private int getRealPosition(int position) {
        if (position >= 1 && position <= getItems().size()) {
            return position - 1;
        } else if (position == 0) {
            return getItems().size() - 1;
        } else if (position == getItems().size() + 1) {
            return 0;
        } else {
            return -1;
        }
    }




}
