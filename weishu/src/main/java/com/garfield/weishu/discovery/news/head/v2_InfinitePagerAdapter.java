package com.garfield.weishu.discovery.news.head;

import android.content.Context;
import android.view.ViewGroup;

import com.garfield.weishu.base.viewpager.TPagerAdapter;
import com.garfield.weishu.discovery.news.head.NewsHeadViewHolder;

import java.util.List;

/**
 * Created by gaowei3 on 2016/10/31.
 */

public class v2_InfinitePagerAdapter extends TPagerAdapter<String> {

    public v2_InfinitePagerAdapter(Context context, List<String> items) {
        super(context, items);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public Class getViewHolderClassAtPosition(int position) {
        return NewsHeadViewHolder.class;
    }

    @Override
    public int getCount() {
        return getItems().size() + 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = getRealPosition(position);
        //L.d("I position: "+position + "  realPosition: "+realPosition);
        return super.instantiateItem(container, realPosition);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //int realPosition = getRealPosition(position);
        //L.d("D position: "+position + "  realPosition: "+realPosition);
        super.destroyItem(container, position, object);
    }

    private int getRealPosition(int position) {
        if (position >= 1 && position <= getItems().size()) {
            return position - 1;
        } else if (position == 0) {
            return getItems().size() - 1;
        } else if (position == getItems().size() + 1) {
            return 0;
        } else {
            return -1;
        }
    }




}
