package com.garfield.weishu.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.garfield.baselib.utils.L;
import com.garfield.weishu.R;
import com.garfield.weishu.base.recyclerview.RecyclerUtil;

/**
 * Created by gaowei3 on 2016/11/4.
 */

/**
 * 不够流畅，因为onTouch调用的密度不够细
 */
public class PullToRefreshView_2 extends LinearLayout implements View.OnTouchListener {
    private static final int STATE_IDLE = 0;
    private static final int STATE_PULLING = 1;
    private static final int STATE_REFRESHING = 2;

    private ImageView mHeadImage;
    private int containerOffset;
    private boolean hasMeasured;

    private boolean mEnabled = false;
    private int mState = STATE_IDLE;
    private float yDown = 0;

    private RecyclerView mRecyclerView;

    public PullToRefreshView_2(Context context) {
        super(context);
    }

    public PullToRefreshView_2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshView_2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
        //params.gravity = Gravity.CENTER_HORIZONTAL;

        mRecyclerView = (RecyclerView) getChildAt(0);
        mHeadImage = new ImageView(getContext());
        mHeadImage.setImageResource(R.drawable.ic_camera_gray);
        addView(mHeadImage, 0);
        mRecyclerView.setOnTouchListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!hasMeasured) {
            LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) mHeadImage.getLayoutParams();
            headParams.topMargin = containerOffset = - mHeadImage.getMeasuredHeight();
            mHeadImage.setLayoutParams(headParams);
            hasMeasured = true;
        }
    }

    public void setPullOffset(int offset) {
        LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) mHeadImage.getLayoutParams();
        headParams.topMargin = containerOffset + offset;
        mHeadImage.setLayoutParams(headParams);
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
        if (!enabled) {
            setPullOffset(0);
        }
    }

    private void checkTheState() {
        LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) mHeadImage.getLayoutParams();
        int marginNow = headParams.topMargin;
//        if (marginNow > 0) {
//
//        } else {
            setPullOffset(0);
            reset();
//        }
    }

    /**
     * 必须要找个机会拦截消息，否则滑动起来，会拉不下来，原因是两个滚动产生影响
     * 下拉时因为margin的变动，导致手在ListView的相对getY位置变小，会被系统以为向上滚动，isAtTop会变成false，出现各种问题
     * 屏蔽掉ListView的滚动事件：
     * 通过返回true，ListView本身的onTouchEvent就不会调用，也就不会滑动了
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("gaowei", "event: "+event.getAction());

        setIsAbleToPull(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 不回调
                break;
            case MotionEvent.ACTION_MOVE:
                if (mEnabled) {
                    float diff = event.getRawY() - yDown;
                    if (diff >= 0 ) {
                        setPullOffset((int)(event.getRawY() - yDown));
                        return true;
                    } else {
                        reset();
                        return false;
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                checkTheState();
                break;
        }

        return false;
    }

    private void setIsAbleToPull(MotionEvent event) {
        boolean isToTop = RecyclerUtil.isAtTop(mRecyclerView);
        L.d("isToTop: "+isToTop);
        if (isToTop) {
            if (!mEnabled) {
                yDown = event.getRawY();
            }
            mEnabled = true;
        } else {
            mEnabled = false;
        }

    }



    public void reset() {
        mEnabled = false;
        setPullOffset(0);
        yDown = 0;
    }


}
