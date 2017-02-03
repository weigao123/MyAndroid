package com.garfield.baselib.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.garfield.baselib.R;
import com.garfield.baselib.utils.drawable.ColorUtils;
import com.garfield.baselib.utils.system.ScreenUtils;

/**
 * Created by gaowei3 on 2016/11/21.
 */

public class BottomBarTransition extends LinearLayout {

    private int mUnSelectedColor;
    private int mSelectedColor;

    private LinearLayout mTabLayout;
    private LayoutParams mTabParams;

    private int mCurrentPosition = 0;
    private OnTabSelectedListener mListener;

    public BottomBarTransition(Context context) {
        this(context, null);
    }

    public BottomBarTransition(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarTransition(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        View lineView = new View(context);
        lineView.setBackgroundColor(getResources().getColor(R.color.gray));
        addView(lineView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));

        mTabLayout = new LinearLayout(context);
        //会造成有阴影，当把连着的另外的view也设置成白色，就看不出来阴影了
        mTabLayout.setBackgroundColor(Color.WHITE);
        mTabLayout.setOrientation(HORIZONTAL);
        addView(mTabLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mTabParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        mTabParams.weight = 1;
    }

    public BottomBarTransition setColor(int colorUnSelected, int colorSelected) {
        mUnSelectedColor = getResources().getColor(colorUnSelected);
        mSelectedColor = getResources().getColor(colorSelected);
        return this;
    }

    public BottomBarTransition addItem(int icon1, int icon2, int title) {
        final Tab tab = new Tab(getContext(), icon1, icon2, title);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener == null) return;

                int toPos = tab.getTabPosition();
                if (mCurrentPosition == toPos) {
                    mListener.onTabReselected(toPos);
                } else {
                    mListener.onTabSelected(toPos, mCurrentPosition);
                    setTabSelected(toPos);
                }
            }
        });
        tab.setTabPosition(mTabLayout.getChildCount());
        tab.setLayoutParams(mTabParams);
        mTabLayout.addView(tab);
        return this;
    }

    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        mListener = onTabSelectedListener;
    }

    public void setTabSelected(int position) {
        if (position == mCurrentPosition) return;
        ((Tab)mTabLayout.getChildAt(mCurrentPosition)).setShift(0);
        ((Tab)mTabLayout.getChildAt(position)).setShift(1);
        mCurrentPosition = position;
    }

    public void setTabShifting(int position, float shift) {
        if (shift == 0 || shift == 1) return;
        ((Tab)mTabLayout.getChildAt(position)).setShift(1 - shift);
        ((Tab)mTabLayout.getChildAt(position + 1)).setShift(shift);
    }

    public interface OnTabSelectedListener {
        void onTabSelected(int position, int prePosition);
        void onTabReselected(int position);
    }

    private class Tab extends View {
        // 只有边的图
        private Bitmap mBitmap1;
        // 被填充的图
        private Bitmap mBitmap2;
        // 要画图的区域在整个Tab的位置
        private Rect mIconRect;
        // 要画图的区域，坐标是0
        private Rect mIconSize;

        private Bitmap mBitmapOutSide;
        private Bitmap mBitmapInner;
        private Canvas mCanvasOutSide;
        private Canvas mCanvasInner;

        // 专门用来画颜色的paint
        private Paint colorPaint;
        // 专门用于DST_IN的paint
        private Paint bitmapPaint;
        // 专门用于clear的paint
        private Paint clearPaint;

        private Rect mTextBound;
        private Paint mTextPaint;
        private int mTextSize;
        private String mText;
        private int mTextX;
        private int mTextY;

        // 在按钮列表中的位置
        private int mTabPosition;

        // 0.0 ~ 1.0f
        private float mShift;

        private boolean hasMeasured;

        public Tab(Context context, int icon1, int icon2, int title) {
            super(context);

            mText = getResources().getString(title);
            BitmapDrawable drawable1 = (BitmapDrawable) getResources().getDrawable(icon1);
            mBitmap1 = drawable1.getBitmap();
            BitmapDrawable drawable2 = (BitmapDrawable) getResources().getDrawable(icon2);
            mBitmap2 = drawable2.getBitmap();

            mTextBound = new Rect();
            mTextSize = ScreenUtils.dp2px(11);
            mTextPaint = new Paint();
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.setAntiAlias(true);
            mTextPaint.setDither(true);
            mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);

            int padding = ScreenUtils.dp2px(5.5f);
            setPadding(padding, padding, padding, padding);

            colorPaint = new Paint();
            colorPaint.setAntiAlias(true);
            colorPaint.setFilterBitmap(true);
            colorPaint.setDither(true);

            bitmapPaint = new Paint();
            bitmapPaint.setAntiAlias(true);
            bitmapPaint.setFilterBitmap(true);
            bitmapPaint.setDither(true);
            // 从背景中砍去新图的区域，只保留背景中，新图有内容的区域
            bitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

            clearPaint = new Paint();
            clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            initAfterMeasure();
            drawImage(canvas);
            drawText(canvas);
        }

        private void initAfterMeasure() {
            if (!hasMeasured) {
                int iconHeight = getMeasuredHeight() - getPaddingTop() * 3 - mTextBound.height();
                int iconWidth = (int) ((float) mBitmap1.getWidth() / mBitmap1.getHeight() * iconHeight);
                int left = getMeasuredWidth() / 2 - iconWidth / 2;
                int top = getMeasuredHeight() - mTextBound.height() - getPaddingTop() * 2 - iconHeight;
                mIconRect = new Rect(left, top, left + iconWidth, top + iconHeight);
                mIconSize = new Rect(0, 0, iconWidth, iconHeight);

                mBitmapOutSide = Bitmap.createBitmap(iconWidth, iconHeight, Bitmap.Config.ARGB_8888);
                mCanvasOutSide = new Canvas(mBitmapOutSide);
                mBitmapInner = Bitmap.createBitmap(iconWidth, iconHeight, Bitmap.Config.ARGB_8888);
                mCanvasInner = new Canvas(mBitmapInner);

                mTextX = getMeasuredWidth() / 2;
                mTextY = (getMeasuredHeight() + mIconRect.bottom) / 2 - (mTextBound.top + mTextBound.bottom) / 2 - ScreenUtils.dp2px(2);

                hasMeasured = true;
            }
        }

        private void drawImage(Canvas canvas) {
            /**
             * 画底座，否则一开始都是空白
             */
            if (mShift <= 0.5f) {
                canvas.drawBitmap(mBitmap1, null, mIconRect, null);
            }
            /**
             * 描边，需要一直描
             */
            mCanvasOutSide.drawPaint(clearPaint);
            // 0~255
            colorPaint.setColor(mSelectedColor);
            // 0~0.5时透明度递增直到255，0.5~1不变
            colorPaint.setAlpha(mShift <= 0.5f ? (int)(255 * 2 * mShift) : 255);
            mCanvasOutSide.drawPaint(colorPaint);
            // 整个颜色区域，只保留mBitmap1的边框
            mCanvasOutSide.drawBitmap(mBitmap1, null, mIconSize, bitmapPaint);
            canvas.drawBitmap(mBitmapOutSide, null, mIconRect, null);

            /**
             * 填充内部，只需要在后半段填充即可
             */
            if (mShift > 0.5f) {
                /**
                 * 新闻按钮，mBitmap1中间有内容，描边后导致最后是蓝色，为了中间是白色，所以画个白色
                 */
                if (mTabPosition == 2) {
                    colorPaint.setColor(Color.WHITE);
                    // 0~255
                    colorPaint.setAlpha((int) (255 * 2 * (mShift - 0.5f)));
                    canvas.drawRect(mIconRect, colorPaint);
                }

                /**
                 * 新闻按钮，mBitmap2中间是空心，所以下面代码最后是透明的
                 */
                mCanvasInner.drawPaint(clearPaint);
                colorPaint.setColor(mSelectedColor);
                // 0~0.5时透明度0，0.5~1透明度递增到255
                colorPaint.setAlpha((int)(255 * 2 * (mShift - 0.5f)));
                mCanvasInner.drawPaint(colorPaint);
                mCanvasInner.drawBitmap(mBitmap2, null, mIconSize, bitmapPaint);
                canvas.drawBitmap(mBitmapInner, null, mIconRect, null);
            }
        }

        private void drawText(Canvas canvas) {
//            mTextPaint.setColor(mUnSelectedColor);
//            canvas.drawText(mText, mTextX, mTextY, mTextPaint);
//            mTextPaint.setColor(mSelectedColor);
//            mTextPaint.setAlpha((int)(255 * mShift));

            int color = ColorUtils.evaluate(mShift, mUnSelectedColor, mSelectedColor);
            mTextPaint.setColor(color);
            canvas.drawText(mText, mTextX, mTextY, mTextPaint);
        }

        public void setTabPosition(int position) {
            mTabPosition = position;
            if (position == 0) {
                setShift(1);
            }
        }

        public int getTabPosition() {
            return mTabPosition;
        }

        /**
         * 百分比
         */
        public void setShift(float shift) {
            if (Math.abs(shift - mShift) < 0.01f) return;
            mShift = shift;
            invalidate();
        }
    }
}
