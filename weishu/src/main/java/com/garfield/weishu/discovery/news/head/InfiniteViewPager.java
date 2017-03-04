package com.garfield.weishu.discovery.news.head;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by gaowei3 on 2016/10/31.
 */

/**
 * Bug：
 * 在pageCount非常大的时候，setCurrentItem方法如果页码切换跨度大于1时，就会引起明显的卡顿
 * setCurrentItem和getCount成正比耗费时间
 * setAdapter之后，第一次viewPager.setCurrentItem并不会引起ANR
 * http://www.cnblogs.com/everhad/p/5599269.html
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
