package com.garfield.weishu.base.recyclerview;

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
}
