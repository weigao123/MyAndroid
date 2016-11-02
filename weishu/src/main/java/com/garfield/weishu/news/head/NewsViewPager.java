package com.garfield.weishu.news.head;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by gaowei3 on 2016/10/31.
 */

public class NewsViewPager extends ViewPager {

    public NewsViewPager(Context context) {
        super(context);
    }

    public NewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        setOffsetAmount();
    }

    private void setOffsetAmount() {
        setCurrentItem(((NewsPagerAdapter_v1)getAdapter()).getItems().size() * 100, false);
    }
}
