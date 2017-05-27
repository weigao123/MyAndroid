package com.garfield.weishu.developer.test;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.R;
import com.garfield.weishu.base.viewpager.view.BasePagerAdapter;
import com.garfield.weishu.base.viewpager.view.BasePagerViewHolder;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by gaowei on 2017/5/26.
 */

public class TestViewPagerFragment extends AppBaseFragment {

    @BindView(R.id.test_viewpager)
    ViewPager mViewPager;
    PageAdapter mAdapter;

    private List<String> mData = new ArrayList<>();
    private static int[] mColors = new int[]{Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN, Color.GRAY};

    @Override
    protected String onGetToolbarTitle() {
        return "ViewPager";
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_test_viewpager;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mData.add("5");
        mData.add("0");
        mData.add("1");
        mData.add("2");
        mData.add("3");
        mData.add("4");
        mData.add("0");
        mAdapter = new PageAdapter(mActivity, mData);
        mViewPager.setAdapter(mAdapter);
        //mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        mViewPager.setCurrentItem(1, false);
    }

    private static class PageAdapter extends BasePagerAdapter<String> {

        private PageAdapter(Context context, List<String> items) {
            super(context, items);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public Class getViewHolderClassAtPosition(int position) {
            return PagerViewHolder.class;
        }
    }

    public static class PagerViewHolder extends BasePagerViewHolder<String> {

        private TextView mTextView;

        @Override
        protected int getResId() {
            return R.layout.page_string;
        }

        @Override
        protected void inflateView() {
            mTextView = findView(R.id.page_string_text);
        }

        @Override
        protected void refresh(String item) {
            mTextView.setText(item);
            mTextView.setBackgroundColor(mColors[Integer.valueOf(item)]);
        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            L.d("onPageSelected: #" +position);
//            {
//                //两边各多放2个
//                int current = mViewPager.getCurrentItem();
//                int lastReal = mAdapter.getCount() - 4;
//                if (current == 1) {
//                    mViewPager.setCurrentItem(lastReal - 1, false);
//                } else if (current == 2) {
//                    mViewPager.setCurrentItem(lastReal, false);
//                } else if (current == lastReal + 1) {
//                    mViewPager.setCurrentItem(3, false);
//                } else if (current == lastReal + 2) {
//                    mViewPager.setCurrentItem(4, false);
//                }
//            }
            {
                // 两边各多放1个
                int current = mViewPager.getCurrentItem();
                int lastReal = mAdapter.getCount() - 2;
                if (current == 0) {
                    mViewPager.setCurrentItem(lastReal, false);
                } else if (current == lastReal + 1) {
                    mViewPager.setCurrentItem(1, false);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            L.d("onPageScrollStateChanged: #" + state);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewPager.clearOnPageChangeListeners();
    }
}
