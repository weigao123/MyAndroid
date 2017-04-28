package com.garfield.weishu.discovery.news.head;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garfield.weishu.R;
import com.garfield.weishu.base.viewpager.TPagerAdapter;
import com.garfield.weishu.discovery.news.bean.netease.NewsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gaowei3 on 2016/10/31.
 */

public class NewsHeadView extends FrameLayout {

    @BindView(R.id.view_news_head_viewpager)
    InfiniteViewPager mInfiniteViewPager;

    @BindView(R.id.view_news_head_title)
    TextView mHeadTitle;

    @BindView(R.id.view_news_head_bottom)
    LinearLayout mTitleContainer;

    @BindView(R.id.view_news_head_point)
    LinearLayout mPointContainer;

    private InfinitePagerAdapter mAdapter;

    private List<NewsBean> mItems = new ArrayList<>();

    public NewsHeadView(Context context) {
        super(context);
        init(context);
    }

    public NewsHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NewsHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_news_head_infinite, this);
        ButterKnife.bind(this, this);
        refreshItems(mItems);
    }

    public List<NewsBean> getItems() {
        return mItems;
    }

    /**
     * 必须要重建Adapter，否则卡顿
     */
    public void refreshItems(List<NewsBean> items) {
        setVisibility(items.isEmpty() ? GONE : VISIBLE);
        mItems.clear();
        mItems.addAll(items);
        TPagerAdapter.ItemEventListener itemEventListener = null;
        if (mAdapter != null) {
            itemEventListener = mAdapter.getItemEventListener();
        }
        mAdapter = new InfinitePagerAdapter(getContext(), mItems);
        mAdapter.setItemEventListener(itemEventListener);
        mInfiniteViewPager.setAdapter(mAdapter);
        mInfiniteViewPager.clearOnPageChangeListeners();
        mInfiniteViewPager.addOnPageChangeListener(mOnPageChangeListener);
        showPagerPoint();
    }

    public void setItemEventListener(TPagerAdapter.ItemEventListener itemEventListener) {
        mAdapter.setItemEventListener(itemEventListener);
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            switchPoint(mAdapter.getRealPosition(position));
            mHeadTitle.setText(mItems.get(mAdapter.getRealPosition(position)).getTitle());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void switchPoint(int position) {
        if (mPointContainer.getChildCount() == 0) return;
        for (int i = 0; i < mPointContainer.getChildCount(); i ++) {
            if (i == position) {
                mPointContainer.getChildAt(position).setSelected(true);
                continue;
            }
            mPointContainer.getChildAt(i).setSelected(false);
        }
    }

    private void showPagerPoint() {
        mPointContainer.removeAllViews();
        LinearLayout.LayoutParams pointParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pointParam.setMargins(8, 0, 8, 0);
        for (int i = 0; i < mItems.size(); i ++) {
            ImageView point = new ImageView(getContext());
            point.setImageResource(R.drawable.page_point);
            mPointContainer.addView(point, pointParam);
        }
        switchPoint(0);
    }
}
