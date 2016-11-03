package com.garfield.weishu.news.head;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by gaowei3 on 2016/10/31.
 */

public class InfiniteViewPager extends ViewPager {

    public InfiniteViewPager(Context context) {
        super(context);
    }

    public InfiniteViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        ((InfinitePagerAdapter)adapter).setViewPager(this);
        setCurrentItem(((InfinitePagerAdapter)adapter).getItems().size() * 100, false);
    }

}
