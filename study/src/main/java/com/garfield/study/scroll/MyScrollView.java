package com.garfield.study.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class MyScrollView extends ViewGroup {

    private Scroller mScroller;
    private int mLastY;
    private int mStart;

    public MyScrollView(Context context) {
        super(context);
        initView(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyScrollView(Context context, AttributeSet attrs,
                        int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mScroller = new Scroller(context);
    }

    @Override
    protected void onLayout(boolean changed,
                            int l, int t, int r, int b) {
        int childCount = getChildCount();
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                child.layout(0, i * height,
                        width, (i + 1) * height);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,
                             int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; ++i) {
            View childView = getChildAt(i);
            measureChild(childView,
                    widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mScroller.isFinished()) {
            return true;
        }
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                mStart = getScrollY();
                break;
            case MotionEvent.ACTION_MOVE:
                //手指微小的移动量，手指向上y变小，画面上移应该使用正数，所以mLastY - y
                int dy = mLastY - y;
                if (getScrollY() < 0) {
                    dy = 0;
                }
                if (getScrollY() > getHeight()*3) {
                    dy = 0;
                }
                scrollBy(0, dy);
                //作为上一帧的位置
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                mScroller.abortAnimation();
                //此时View内容的偏移量
                int mEnd = getScrollY();
                int dScrollY = mEnd - mStart;
                //变大肯定是向上
                if (dScrollY > 0) {
                    //返回，向下，负数
                    if (dScrollY < getHeight()/2) {
                        mScroller.startScroll(
                                0, getScrollY(),
                                0, -dScrollY);
                        //继续向上，正数
                    } else {
                        mScroller.startScroll(
                                0, getScrollY(),
                                0, getHeight() - dScrollY);
                    }
                    //变小肯定是向下
                } else {
                    //返回，向上，正数
                    if (-dScrollY < getHeight()/2) {
                        mScroller.startScroll(
                                0, getScrollY(),
                                0, -dScrollY);
                        //继续向下，负数
                    } else {
                        mScroller.startScroll(
                                0, getScrollY(),
                                0, -(getHeight() + dScrollY));
                    }
                }
                break;
        }
        postInvalidate();
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }
}
