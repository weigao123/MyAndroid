package com.garfield.weishu.discovery.news.ui;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.garfield.baselib.utils.http.image.ImageHelper;
import com.garfield.weishu.R;
import com.garfield.weishu.base.recyclerview.BaseRecyclerAdapter;
import com.garfield.weishu.base.recyclerview.BaseRecyclerViewHolder;
import com.garfield.weishu.discovery.news.bean.zhihu.ZhihuDailyItem;

import java.util.List;

/**
 * Created by gaowei3 on 2016/12/5.
 */

public class ZhihuListViewHolder extends BaseRecyclerViewHolder<ZhihuDailyItem> {

    private ImageView zhihuImage;
    private TextView zhihuTitle;

    @Override
    protected int getLayoutResId() {
        return R.layout.item_zhihu;
    }

    @Override
    protected void inflateView() {
        zhihuImage = findView(R.id.item_zhihu_image);
        zhihuTitle = findView(R.id.item_zhihu_title);
    }

    @Override
    protected void setView() {

    }

    @Override
    protected void refresh(ZhihuDailyItem zhihuDailyItem) {
        ImageHelper.load(mRootView.getContext(), zhihuDailyItem.getImages()[0], zhihuImage);

        zhihuTitle.setText(zhihuDailyItem.getTitle());
    }

    @Override
    protected void refresh() {

    }

    @Override
    protected ZhihuRecyclerAdapter getAdapter() {
        return (ZhihuRecyclerAdapter) mAdapter;
    }


    public static class ZhihuRecyclerAdapter extends BaseRecyclerAdapter<ZhihuDailyItem> {
        public ZhihuRecyclerAdapter(Context context, List<ZhihuDailyItem> items) {
            super(context, items);
        }
        @Override
        public Class getViewHolderClassAtPosition(int position) {
            return ZhihuListViewHolder.class;
        }
    }
}
