package com.garfield.weishu.news.old;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.garfield.baselib.utils.L;
import com.garfield.weishu.base.viewpager.TPagerAdapter;

/**
 * Created by gaowei3 on 2016/10/31.
 */

public class InfiniteViewPager_v2 extends ViewPager {

    public InfiniteViewPager_v2(Context context) {
        super(context);
    }

    public InfiniteViewPager_v2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        addOnPageChangeListener(mOnPageChangeListener);
        setCurrentItem(1, false);
    }


    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                int current = getCurrentItem();
                int lastReal = getAdapter().getCount() - 2;
                if (current == 0) {
                    setCurrentItem(lastReal, false);
                } else if (current == lastReal + 1) {
                    setCurrentItem(1, false);
                }
            }
        }
    };

}
