package com.garfield.weishu.base.viewpager;

import android.os.Build;
import android.util.SparseArray;
import android.view.View;

/**
 * The RecycleBin facilitates reuse of views across layouts. The RecycleBin has two levels of
 * storage: ActiveViews and ScrapViews. ActiveViews are those views which were onscreen at the
 * start of a layout. By construction, they are displaying current information. At the end of
 * layout, all views in ActiveViews are demoted to ScrapViews. ScrapViews are old views that
 * could potentially be used by the adapter to avoid allocating views unnecessarily.
 * <p>
 * This class was taken from Android's implementation of {@link android.widget.AbsListView} which
 * is copyrighted 2006 The Android Open Source Project.
 */
public class RecycleBin {

    /**
     * 缓存器，大小为viewTypeCount，每一个都是SparseArray
     */
    private SparseArray<View>[] scrapViews;

    RecycleBin(int viewTypeCount) {
        //noinspection unchecked
        scrapViews = new SparseArray[viewTypeCount];
        for (int i = 0; i < viewTypeCount; i++) {
            scrapViews[i] = new SparseArray<>();
        }
    }

    /**
     * 根据type确定SparseArray<View>
     */
    View getScrapView(int position, int viewType) {
        if (viewType >= 0 && viewType < scrapViews.length) {
            return retrieveFromScrap(scrapViews[viewType], position);
        }
        return null;
    }

    /**
     * 不能每一种type只保存一个Holder的原因是同一个View不能被add给不同的父View
     * 现在是每一个位置都保存一个Holder
     * 适合position不多的地方
     */
    void addScrapView(View scrap, int position, int viewType) {
        scrapViews[viewType].put(position, scrap);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            scrap.setAccessibilityDelegate(null);
        }
    }

    /**
     * position是key，先遍历查找此key，因为是无限循环，每隔几个position就是一样的
     */
    private View retrieveFromScrap(SparseArray<View> scrapViews, int position) {
        int size = scrapViews.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                int fromPosition = scrapViews.keyAt(i);
                View view = scrapViews.get(fromPosition);
                if (fromPosition == position) {
                    /**
                     * 拿出去用了就要释放
                     */
                    scrapViews.remove(fromPosition);
                    return view;
                }
            }
            /**
             * 没有就取出最后一个，一般不会发生，都应该有对应的position
             */
            int index = size - 1;
            View r = scrapViews.valueAt(index);
            scrapViews.remove(scrapViews.keyAt(index));
            return r;
        } else {
            return null;
        }
    }
}
