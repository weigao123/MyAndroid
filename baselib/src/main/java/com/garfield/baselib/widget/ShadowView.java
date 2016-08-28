package com.garfield.baselib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.garfield.baselib.R;

/**
 * Created by gaowei3 on 2016/8/27.
 */
public class ShadowView extends ViewGroup {
    private int mShadowWidth = 15;

    private Drawable mShadowLeft;
    private Drawable mShadowRight;
    private Drawable mShadowTop;
    private Drawable mShadowBottom;

    public ShadowView(Context context) {
        this(context, null);
    }

    public ShadowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mShadowLeft = getResources().getDrawable(R.drawable.shadow_left_gray);
        mShadowRight = getResources().getDrawable(R.drawable.shadow_right_gray);
        mShadowTop = getResources().getDrawable(R.drawable.shadow_top_gray);
        mShadowBottom = getResources().getDrawable(R.drawable.shadow_bottom_gray);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View childView = getChildAt(0);
        childView.measure(widthMeasureSpec, heightMeasureSpec);
        int width = childView.getMeasuredWidth() + mShadowWidth * 2;
        int height = childView.getMeasuredHeight() + mShadowWidth * 2;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View childView = getChildAt(0);
        childView.layout(mShadowWidth, mShadowWidth,
                childView.getMeasuredWidth() + mShadowWidth,
                childView.getMeasuredHeight() + mShadowWidth);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean drawChild = super.drawChild(canvas, child, drawingTime);

        mShadowLeft.setBounds(0, mShadowWidth, mShadowWidth, getHeight()-mShadowWidth);
        mShadowLeft.draw(canvas);
        mShadowRight.setBounds(getWidth()-mShadowWidth, mShadowWidth, getWidth(), getHeight()-mShadowWidth);
        mShadowRight.draw(canvas);
        mShadowTop.setBounds(mShadowWidth, 0, getWidth()-mShadowWidth, mShadowWidth);
        mShadowTop.draw(canvas);
        mShadowBottom.setBounds(mShadowWidth, getHeight()-mShadowWidth, getWidth()-mShadowWidth, getHeight());
        mShadowBottom.draw(canvas);
        return drawChild;
    }

}
