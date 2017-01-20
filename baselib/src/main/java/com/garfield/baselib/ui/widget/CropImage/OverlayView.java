package com.garfield.baselib.ui.widget.CropImage;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by gaowei3 on 2017/1/20.
 */

public class OverlayView extends View {

    protected int mThisWidth, mThisHeight;
    private final RectF mCropViewRect = new RectF();
    private float mTargetAspectRatio;


    public OverlayView(Context context) {
        this(context, null);
    }

    public OverlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mThisWidth = getWidth() - getPaddingRight() - getPaddingLeft();
            mThisHeight = getHeight() - getPaddingBottom() - getPaddingTop();
        }
    }

    void setTargetAspectRatio(float targetAspectRatio) {
        mTargetAspectRatio = targetAspectRatio;
        setupCropBounds();
    }

    void setupCropBounds() {
        int height = (int) (mThisWidth / mTargetAspectRatio);
        if (height > mThisHeight) {
            int width = (int) (mThisHeight * mTargetAspectRatio);
            int halfDiff = (mThisWidth - width) / 2;
            mCropViewRect.set(getPaddingLeft() + halfDiff, getPaddingTop(),
                    getPaddingLeft() + width + halfDiff, getPaddingTop() + mThisHeight);
        } else {
            int halfDiff = (mThisHeight - height) / 2;
            mCropViewRect.set(getPaddingLeft(), getPaddingTop() + halfDiff,
                    getPaddingLeft() + mThisWidth, getPaddingTop() + height + halfDiff);
        }


        //updateGridPoints();
    }
}
