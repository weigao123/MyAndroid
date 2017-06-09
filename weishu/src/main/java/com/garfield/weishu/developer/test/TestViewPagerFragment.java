package com.garfield.weishu.developer.test;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.R;
import com.garfield.weishu.base.viewpager.fragment.InfiniteFmAdapter;
import com.garfield.weishu.base.viewpager.fragment.InfiniteFmStateAdapter;
import com.garfield.weishu.base.viewpager.view.BasePagerAdapter;
import com.garfield.weishu.base.viewpager.view.BasePagerViewHolder;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gaowei on 2017/5/26.
 */

public class TestViewPagerFragment extends AppBaseFragment {

    @BindView(R.id.test_viewpager_1)
    ViewPager mViewPager1;

    @BindView(R.id.test_viewpager_2)
    ViewPager mViewPager2;

    @BindView(R.id.test_viewpager_3)
    ViewPager mViewPager3;

    VPageAdapter mAdapter1;
    InfiniteFmAdapter mAdapter2;
    InfiniteStateAdapter mAdapter3;

    private MyOnPageChangeListener mPageChangeListener1;
    private MyOnPageChangeListener mPageChangeListener2;

    private int mRealCount = 2;

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
        mData.add("4");
        mData.add("0");
        mData.add("1");
        mData.add("2");
        mData.add("3");
        mData.add("4");
        mData.add("0");
        mAdapter1 = new VPageAdapter(mActivity, mData);
        mViewPager1.setAdapter(mAdapter1);
        //mViewPager1.setOffscreenPageLimit(2);
        mPageChangeListener1 = new MyOnPageChangeListener(mViewPager1);
        mViewPager1.addOnPageChangeListener(mPageChangeListener1);
        mViewPager1.setCurrentItem(1, false);


        mPageChangeListener2 = new MyOnPageChangeListener(mViewPager2);
        mViewPager2.addOnPageChangeListener(mPageChangeListener2);
        mViewPager2.setOffscreenPageLimit(2);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(OneFragment.newInstance(0));
        fragmentList.add(OneFragment.newInstance(1));
        fragmentList.add(OneFragment.newInstance(2));
        fragmentList.add(OneFragment.newInstance(3));
        fragmentList.add(OneFragment.newInstance(4));
        fragmentList.add(OneFragment.newInstance(5));
        mAdapter2 = new InfiniteFmAdapter(getChildFragmentManager(), fragmentList);
        mViewPager2.setAdapter(mAdapter2);
        mViewPager2.setCurrentItem(100, false);


        mAdapter3 = new InfiniteStateAdapter(getChildFragmentManager());
        mViewPager3.setAdapter(mAdapter3);
        //mViewPager3.setCurrentItem(1000);
    }

    public static class VPageAdapter extends BasePagerAdapter<String> {

        private VPageAdapter(Context context, List<String> items) {
            super(context, items);
        }

        @Override
        public Class getViewHolderClassAtPosition(int position) {
            return PagerViewHolder.class;
        }

        @Override
        public float getPageWidth(int position) {
            return 1f;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
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
    }

    private class InfiniteStateAdapter extends InfiniteFmStateAdapter {

        InfiniteStateAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getRealItem(int position) {
            return OneFragment.newInstance(position);
        }

        @Override
        public int getRealCount() {
            return mRealCount;
        }
    }

    public static class OneFragment extends Fragment {

        private int num;
        public static OneFragment newInstance(int num) {
            OneFragment oneFragment = new OneFragment();
            oneFragment.num = num;
            return oneFragment;
        }
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //L.d("OneFragment onCreate: "+num);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            //L.d("OneFragment onCreateView: "+num);
            View view = inflater.inflate(R.layout.page_string, container, false);
            ((TextView)view.findViewById(R.id.page_string_text)).setText(String.valueOf(num));
            return view;
        }
        @Override
        public void onDestroyView() {
            super.onDestroyView();
            //L.d("num:"+num+"  onDestroyView");
        }
        @Override
        public void onDestroy() {
            super.onDestroy();
            //L.d("num:"+num+"  onDestroy");
        }
    }


    @OnClick(R.id.test_viewpager_btn)
    void click() {
//        mAdapter2 = new InfiniteStateAdapter(getChildFragmentManager());
//        mAdapter2.addFragment(OneFragment.newInstance(3));
//        mAdapter2.addFragment(OneFragment.newInstance(4));
//        mAdapter2.addFragment(OneFragment.newInstance(5));


        List<Fragment> items = new ArrayList<>();
        items.add(OneFragment.newInstance(3));
        items.add(OneFragment.newInstance(4));
        items.add(OneFragment.newInstance(5));
        items.add(OneFragment.newInstance(6));
        items.add(OneFragment.newInstance(7));
        items.add(OneFragment.newInstance(8));
        //mAdapter2.changeFragments(items);
        //mViewPager2.setCurrentItem(100, false);


        //mViewPager2.setAdapter(mAdapter2);




        mRealCount = 5;
        mViewPager3.setAdapter(mAdapter3);
        //mAdapter3.notifyDataSetChanged();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewPager1.removeOnPageChangeListener(mPageChangeListener1);
        mViewPager2.removeOnPageChangeListener(mPageChangeListener2);
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        private ViewPager mViewPager;

        private MyOnPageChangeListener(ViewPager viewPager) {
            mViewPager = viewPager;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //L.d("onPageSelected: #" +position);
//            if (mViewPager == mViewPager1) {
//                //两边各多放2个
//                int current = mViewPager.getCurrentItem();
//                int lastReal = mAdapter1.getCount() - 4;
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
            if (mViewPager == mViewPager1) {
                // 两边各多放1个
                int current = mViewPager1.getCurrentItem();
                int lastReal = mAdapter1.getCount() - 2;
                if (current == 0) {
                    mViewPager1.setCurrentItem(lastReal, false);
                } else if (current == lastReal + 1) {
                    mViewPager1.setCurrentItem(1, false);
                }
            }
            if (mViewPager == mViewPager2) {
                L.d("onPageSelected");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //L.d("onPageScrollStateChanged: #" + state);
        }
    }
}
