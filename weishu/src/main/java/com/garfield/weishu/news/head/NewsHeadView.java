package com.garfield.weishu.news.head;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
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
    NewsViewPager mNewsViewPager;

    @BindView(R.id.view_news_head_title)
    TextView mHeadTitle;

    @BindView(R.id.view_news_head_number)
    TextView mHeadNumber;

    private Unbinder unbinder;
    private List<String> mItems = new ArrayList<>();

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
        unbinder = ButterKnife.bind(this, this);

        mItems.add("a");
        mItems.add("a");
        mItems.add("a");
        mItems.add("a");
        NewsPagerAdapter adapter = new NewsPagerAdapter(getContext(), mItems);
        mNewsViewPager.setAdapter(adapter);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
