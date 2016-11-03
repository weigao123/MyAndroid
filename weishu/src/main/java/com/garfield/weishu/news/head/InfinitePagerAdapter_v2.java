package com.garfield.weishu.news.head;

import android.content.Context;
import android.view.ViewGroup;

import com.garfield.weishu.base.viewpager.TPagerAdapter;

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
        int realPosition = position % getItems().size();
        return super.instantiateItem(container, realPosition);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int realPosition = position % getItems().size();
        super.destroyItem(container, realPosition, object);
    }


}
