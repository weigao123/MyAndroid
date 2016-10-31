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
     * Views that were on screen at the start of layout. This array is populated at the start of
     * layout, and at the end of layout all view in activeViews are moved to scrapViews.
     * Views in activeViews represent a contiguous range of Views, with position of the first
     * view store in mFirstActivePosition.
     * 其实没打算用他
     */
    private View[] activeViews = new View[0];
    private int[] activeViewTypes = new int[0];

    /**
     * Unsorted views that can be used by the adapter as a convert view.
     * 缓存器，大小为viewTypeCount，每一个都是SparseArray
     */
    private SparseArray<View>[] scrapViews;
    private int viewTypeCount;

    /**
     * 如果只有一个type，就用这个
     */
    private SparseArray<View> currentScrapViews;

    public void setViewTypeCount(int viewTypeCount) {
        if (viewTypeCount < 1) {
            throw new IllegalArgumentException("Can't have a viewTypeCount < 1");
        }
        //noinspection unchecked
        SparseArray<View>[] scrapViews = new SparseArray[viewTypeCount];
        for (int i = 0; i < viewTypeCount; i++) {
            scrapViews[i] = new SparseArray<View>();
        }
        this.viewTypeCount = viewTypeCount;
        currentScrapViews = scrapViews[0];
        this.scrapViews = scrapViews;
    }

    protected boolean shouldRecycleViewType(int viewType) {
        return viewType >= 0;
    }

    /**
     * @return A view from the ScrapViews collection. These are unordered.
     * 根据type确定SparseArray<View>
     */
    View getScrapView(int position, int viewType) {
        if (viewTypeCount == 1) {
            return retrieveFromScrap(currentScrapViews, position);
        } else if (viewType >= 0 && viewType < scrapViews.length) {
            return retrieveFromScrap(scrapViews[viewType], position);
        }
        return null;
    }

    /**
     * Put a view into the ScrapViews list. These views are unordered.
     *
     * @param scrap The view to add
     */
    void addScrapView(View scrap, int position, int viewType) {
        if (viewTypeCount == 1) {
            currentScrapViews.put(position, scrap);
        } else {
            scrapViews[viewType].put(position, scrap);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            scrap.setAccessibilityDelegate(null);
        }
    }

    /**
     * item改变后，把所有activeView移到scrap里缓存，notifyDataSetChanged时调用
     */
    void scrapActiveViews() {
        final View[] activeViews = this.activeViews;
        final int[] activeViewTypes = this.activeViewTypes;
        final boolean multipleScraps = viewTypeCount > 1;

        SparseArray<View> scrapViews = currentScrapViews;
        final int count = activeViews.length;
        for (int i = count - 1; i >= 0; i--) {
            final View victim = activeViews[i];
            if (victim != null) {
                int whichScrap = activeViewTypes[i];

                activeViews[i] = null;
                activeViewTypes[i] = -1;

                // 如果之前的type是-1，就跳过
                if (!shouldRecycleViewType(whichScrap)) {
                    continue;
                }

                // type多于1个
                if (multipleScraps) {
                    scrapViews = this.scrapViews[whichScrap];
                }

                //把所有activeView移到scrap里缓存，0~length-1
                scrapViews.put(i, victim);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    victim.setAccessibilityDelegate(null);
                }
            }
        }

        pruneScrapViews();
    }

    /**
     * 清除activeViews长度以外多余缓存，其实是全清空了
     */
    private void pruneScrapViews() {
        final int maxViews = activeViews.length;
        final int viewTypeCount = this.viewTypeCount;
        final SparseArray<View>[] scrapViews = this.scrapViews;
        for (int i = 0; i < viewTypeCount; ++i) {
            final SparseArray<View> scrapPile = scrapViews[i];
            int size = scrapPile.size();
            // 多余的数量，maxViews=0
            final int extras = size - maxViews;
            size--;
            for (int j = 0; j < extras; j++) {
                // 从尾巴开始
                scrapPile.remove(scrapPile.keyAt(size--));
            }
        }
    }

    /**
     * position是key，先遍历查找此key，因为是无限循环，每隔几个position就是一样的
     * 没有就取出最后一个index的，反正type是一样的
     */
    static View retrieveFromScrap(SparseArray<View> scrapViews, int position) {
        int size = scrapViews.size();
        if (size > 0) {
            // See if we still have a view for this position.
            for (int i = 0; i < size; i++) {
                int fromPosition = scrapViews.keyAt(i);
                View view = scrapViews.get(fromPosition);
                if (fromPosition == position) {
                    scrapViews.remove(fromPosition);
                    return view;
                }
            }
            int index = size - 1;
            View r = scrapViews.valueAt(index);
            scrapViews.remove(scrapViews.keyAt(index));
            return r;
        } else {
            return null;
        }
    }
}
