package com.garfield.weishu.news.view;

import android.widget.ImageView;
import android.widget.TextView;

import com.garfield.baselib.utils.ImageLoaderUtils;
import com.garfield.weishu.R;
import com.garfield.weishu.base.recyclerview.TRecyclerAdapter;
import com.garfield.weishu.base.recyclerview.TRecyclerViewHolder;
import com.garfield.weishu.news.bean.NewsBean;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by gaowei3 on 2016/11/4.
 */

public class NewsViewHolder extends TRecyclerViewHolder<NewsBean> {

    private ImageView newsImage;
    private TextView newsTitle;
    private TextView newsSource;
    private TextView newsReply;

    @Override
    protected int getLayoutResId() {
        return R.layout.item_news;
    }

    @Override
    protected void inflateView() {
        newsImage = findView(R.id.item_news_image);
        newsTitle = findView(R.id.item_news_title);
        newsSource = findView(R.id.item_news_source);
        newsReply = findView(R.id.item_news_reply);
    }

    @Override
    protected void setView() {

    }

    @Override
    protected void refresh(NewsBean newsBean) {
        ImageLoader.getInstance().displayImage(newsBean.getImgsrc(), newsImage, ImageLoaderUtils.getDisplayImageNoDiskCacheOptions());
        newsTitle.setText(newsBean.getTitle());
        newsSource.setText(newsBean.getSource());
        newsReply.setText(getAdapter().getContext().getResources().getString(R.string.num_reply, newsBean.getReplyCount()));
    }

    static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i< c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i]> 65280 && c[i]< 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    @Override
    protected void refresh() {

    }

    @Override
    protected NewsRecyclerAdapter getAdapter() {
        return (NewsRecyclerAdapter) mAdapter;
    }
}
