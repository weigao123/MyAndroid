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
        setOffsetAmount(adapter);
    }

    private void setOffsetAmount(PagerAdapter adapter) {
        if (adapter instanceof InfinitePagerAdapter_v1) {
            setCurrentItem(((InfinitePagerAdapter_v1)getAdapter()).getItems().size() * 100, false);
        } else if (adapter instanceof InfinitePagerAdapter_v2) {
            setCurrentItem(1, false);
        }
    }
}
