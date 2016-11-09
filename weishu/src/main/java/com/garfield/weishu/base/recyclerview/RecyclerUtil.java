package com.garfield.weishu.base.recyclerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

/**
 * Created by gaowei3 on 2016/10/29.
 */

public class RecyclerUtil {


    //index是items的index，不包含header
    public static Object getViewHolderByIndex(RecyclerView recyclerView, int index) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForItemId(index);
        return viewHolder;
    }

    public static boolean isAtTop(RecyclerView recyclerView) {
        if (recyclerView == null || recyclerView.getLayoutManager() == null) {
            return false;
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int position = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
        if (position == 0) {
            int top = recyclerView.getChildAt(0).getTop();
            if (top == 0) {
                return true;
            }
        }
        return false;
    }
}
