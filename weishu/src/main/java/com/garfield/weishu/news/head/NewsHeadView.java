package com.garfield.weishu.news.head;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by gaowei3 on 2016/10/31.
 */

public class NewsHeadView extends FrameLayout {

    @BindView(R.id.view_news_head_viewpager)
    InfiniteViewPager mInfiniteViewPager;

    @BindView(R.id.view_news_head_title)
    TextView mHeadTitle;

    @BindView(R.id.view_news_head_point)
    LinearLayout mTitleContainer;

    private LinearLayout mPointContainer;
    private InfinitePagerAdapter mAdapter;

    private Unbinder unbinder;
    private List<String> mItems = new ArrayList<>();

    public NewsHeadView(Context context) {
        super(context);
    }

    public NewsHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init(getContext());
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_news_head_infinite, this);
        unbinder = ButterKnife.bind(this, this);

        mItems.add("a");
        mItems.add("a");
        mItems.add("a");
        mItems.add("a");
        mAdapter = new InfinitePagerAdapter(getContext(), mItems);
        mInfiniteViewPager.setAdapter(mAdapter);
        mInfiniteViewPager.addOnPageChangeListener(mOnPageChangeListener);
        showPagerPoint();
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            switchPoint(mAdapter.getRealPosition(position));

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (unbinder != null) {
            unbinder.unbind();
        }
        mInfiniteViewPager.removeOnPageChangeListener(mOnPageChangeListener);
    }

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
        mPointContainer = new LinearLayout(getContext());
        LinearLayout.LayoutParams pointParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pointParam.setMargins(8, 8, 8, 8);
        pointParam.gravity = Gravity.CENTER_VERTICAL;
        for (int i = 0; i < mItems.size(); i ++) {
            ImageView point = new ImageView(getContext());
            point.setLayoutParams(pointParam);
            point.setImageResource(R.drawable.page_point);
            mPointContainer.addView(point);
        }
        LinearLayout.LayoutParams containerParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        containerParam.gravity = Gravity.CENTER_VERTICAL;
        mTitleContainer.addView(mPointContainer, containerParam);
        switchPoint(0);
    }
}
