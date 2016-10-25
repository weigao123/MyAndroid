package com.garfield.weishu.session.listview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;


import com.garfield.weishu.R;

import java.util.ArrayList;
import java.util.List;

public class AutoRefreshListView extends ListView {
    public enum State {
        REFRESHING,
        RESET,
    }

    public enum Mode {
        FRONT,
        END,
        BOTH,
    }

    public interface OnRefreshListener {
        public void onRefreshFromStart();

        public void onRefreshFromEnd();
    }

    private OnRefreshListener refreshListener;
    private List<OnScrollListener> scrollListeners = new ArrayList<OnScrollListener>();

    /**
     * 刷新的状态 REFRESHING, RESET
     */
    private State state = State.RESET;
    /**
     * 使能的模式
     */
    private Mode mode = Mode.FRONT;
    /**
     * 当前的模式
     */
    private Mode currentMode = Mode.FRONT;
    /**
     * 前边是否有可以获取的数据
     */
    private boolean refreshableStart = true;
    private boolean refreshableEnd = true;

    private ViewGroup refreshHeader;
    private ViewGroup refreshFooter;

    private int offsetY;

    public AutoRefreshListView(Context context) {
        super(context);
        init(context);
    }

    public AutoRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * 注册刷新的
     */
    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        // replaced by addOnScrollListener
        throw new UnsupportedOperationException("Use addOnScrollListener instead!");
    }

    /**
     * 正常listview滑动状态的监听
     */
    public void addOnScrollListener(OnScrollListener l) {
        scrollListeners.add(l);
    }

    public void removeOnScrollListener(OnScrollListener l) {
        scrollListeners.remove(l);
    }

    private void init(Context context) {
        addRefreshView(context);

        super.setOnScrollListener(new OnScrollListener() {
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                for (OnScrollListener listener : scrollListeners) {
                    listener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
            }

            public void onScrollStateChanged(AbsListView view, int scrollState) {
                for (OnScrollListener listener : scrollListeners) {
                    listener.onScrollStateChanged(view, scrollState);
                }
            }
        });

        initRefreshListener();

        state = State.RESET;
    }

    private void addRefreshView(Context context) {
        refreshHeader = (ViewGroup) View.inflate(context, R.layout.listview_refresh, null);
        addHeaderView(refreshHeader, null, false);
        refreshFooter = (ViewGroup) View.inflate(context, R.layout.listview_refresh, null);
        addFooterView(refreshFooter, null, false);
    }

    private void initRefreshListener() {
        OnScrollListener listener = new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && state == State.RESET) {
                    // 马上就要露出head了
                    boolean reachTop = (getFirstVisiblePosition() < getHeaderViewsCount() && getCount() > getHeaderViewsCount());
                    if (reachTop) {
                        onRefresh(true);
                    } else {
                        boolean reachBottom = getLastVisiblePosition() >= getCount() - 1;
                        if (reachBottom) {
                            onRefresh(false);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        };

        addOnScrollListener(listener);
    }

    /**
     * 根据滑动位置自动调用，true就是到最上面了，false就是到最下面了
     */
    private void onRefresh(boolean start) {
        if (refreshListener != null) {
            View firstVisibleChild = getChildAt(getHeaderViewsCount());
            if (firstVisibleChild != null) {
                offsetY = firstVisibleChild.getTop();
            }

            if (start && refreshableStart && mode != Mode.END) {
                currentMode = Mode.FRONT;
                state = State.REFRESHING;
                refreshListener.onRefreshFromStart();
            } else if (refreshableEnd && mode != Mode.FRONT) {
                currentMode = Mode.END;
                state = State.REFRESHING;
                refreshListener.onRefreshFromEnd();
            }
            updateRefreshView();
        }
    }

    private void updateRefreshView() {
        switch (state) {
            case REFRESHING:
                getRefreshView().getChildAt(0).setVisibility(View.VISIBLE);
                break;
            case RESET:
                if (currentMode == Mode.FRONT) {
                    refreshHeader.getChildAt(0).setVisibility(refreshableStart ? View.INVISIBLE : View.GONE);
                } else {
                    refreshFooter.getChildAt(0).setVisibility(View.GONE);
                }
                break;
        }
    }

    private ViewGroup getRefreshView() {
        switch (currentMode) {
            case END:
                return refreshFooter;
            case FRONT:
            default:
                return refreshHeader;
        }
    }

    public void onRefreshStart(Mode mode) {
        state = State.REFRESHING;
        currentMode = mode;
    }

    /**
     * count是新加载的数量，requestCount是想要加载的数量
     * 加载完成，并且要滚动list到之前的开头
     */
    public void onRefreshComplete(int count, int requestCount, boolean needOffset) {
        state = State.RESET;
        resetRefreshView(count, requestCount);
        if (!needOffset) {
            return;
        }

        if (currentMode == Mode.FRONT) {
            setSelectionFromTop(count + getHeaderViewsCount(), refreshableStart ? offsetY : 0);
        }
    }

    public void onRefreshComplete() {
        state = State.RESET;
        updateRefreshView();
    }

    private void resetRefreshView(int count, int requestCount) {
        if (currentMode == Mode.FRONT) {
            /**
             * 如果是第1次加载，如果count<requestCount, 表示没有数据了。
             * 如果是第2次以后的加载，为了列表稳定，只有count>0, 就保留header的高度
             */
            if (getCount() == count + getHeaderViewsCount() + getFooterViewsCount()) {
                refreshableStart = (count == requestCount);
            } else {
                refreshableStart = (count > 0);
            }
        } else {
            refreshableEnd = (count > 0);
        }
        updateRefreshView();
    }

    /**
     * handle over scroll when no more data
     */
    private boolean isBeingDragged = false;
    private int startY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (Build.VERSION.SDK_INT < 11) {
            try {
                return onTouchEventInternal(event);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return onTouchEventInternal(event);
        }
    }

    private boolean onTouchEventInternal(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchBegin(event);
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(event);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                onTouchEnd();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void onTouchBegin(MotionEvent event) {
        int firstItemIndex = getFirstVisiblePosition();
        if (!refreshableStart && firstItemIndex <= getHeaderViewsCount() && !isBeingDragged) {
            isBeingDragged = true;
            startY = (int) event.getY();
        }
    }

    private void onTouchMove(MotionEvent event) {
        /** check state again */
        onTouchBegin(event);
        if (!isBeingDragged) {
            return;
        }

        /** scroll to dragged position */
        int offsetY = (int) (event.getY() - startY);
        offsetY = Math.max(offsetY, 0) / 2;
        refreshHeader.setPadding(0, offsetY, 0, 0);
    }

    private void onTouchEnd() {
        if (isBeingDragged) {
            refreshHeader.setPadding(0, 0, 0, 0);
        }

        isBeingDragged = false;
    }
}
