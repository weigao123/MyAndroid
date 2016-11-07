package com.garfield.weishu.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.garfield.baselib.utils.L;
import com.garfield.weishu.R;

/**
 * Created by gaowei3 on 2016/11/4.
 */

/**
 * 不够流畅，因为onTouch调用的密度不够细
 */
public class PullToRefreshView_2 extends LinearLayout implements View.OnTouchListener {

    private ImageView headImage;
    private int containerOffset;
    private boolean mEnabled;
    private boolean hasMeasured;
    private float originPosition;

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
        headImage = new ImageView(getContext());
        headImage.setImageResource(R.drawable.ic_camera_gray);
        addView(headImage, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!hasMeasured) {
            LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) headImage.getLayoutParams();
            headParams.topMargin = containerOffset = - headImage.getMeasuredHeight();
            headImage.setLayoutParams(headParams);
            hasMeasured = true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mEnabled) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mEnabled)
                    return true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mEnabled) {
                    if (originPosition == 0) {
                        originPosition = event.getRawY();
                    }
                    setPullOffset((int)(event.getRawY() - originPosition));
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                checkTheState();
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setPullOffset(int offset) {
        LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) headImage.getLayoutParams();
        headParams.topMargin = containerOffset + offset;
        headImage.setLayoutParams(headParams);
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
        if (!enabled) {
            setPullOffset(0);
        }
    }

    private void checkTheState() {
        LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) headImage.getLayoutParams();
        int marginNow = headParams.topMargin;
        if (marginNow > 0) {

        } else {
            setPullOffset(0);
            reset();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        return false;
    }

    public void reset() {
        mEnabled = false;
        setPullOffset(0);
        originPosition = 0;
    }


}
