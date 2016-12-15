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

public class BottomBar2 extends LinearLayout {
    public final static int DIRECTION_LEFT = 0;
    public final static int DIRECTION_RIGHT = 1;

    private int mUnSelectedColor;
    private int mSelectedColor;

    private LinearLayout mTabLayout;
    private LayoutParams mTabParams;

    private int mCurrentPosition = 0;
    private OnTabSelectedListener mListener;

    public BottomBar2(Context context) {
        super(context);
        init(context);
    }

    public BottomBar2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomBar2(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public BottomBar2 setColor(int colorUnSelected, int colorSelected) {
        mUnSelectedColor = getResources().getColor(colorUnSelected);
        mSelectedColor = getResources().getColor(colorSelected);
        return this;
    }

    public BottomBar2 addItem(int icon1, int icon2, int title) {
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
        // 要画图的区域
        private Rect mIconRect;

        private Bitmap mBitmapOutLine;
        private Bitmap mBitmapInner;
        private Canvas mCanvasOutLine;
        private Canvas mCanvasInner;

        private Paint colorPaint;
        private Paint bitmapPaint;
        private Paint clearPaint;

        private Rect mTextBound;
        private Paint mTextPaint;
        private int mTextSize;
        private String mText;
        private int mX;
        private int mY;

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

                mBitmapOutLine = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                mCanvasOutLine = new Canvas(mBitmapOutLine);
                mBitmapInner = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                mCanvasInner = new Canvas(mBitmapInner);

                mX = getMeasuredWidth() / 2;
                mY = (getMeasuredHeight() + mIconRect.bottom) / 2 - (mTextBound.top + mTextBound.bottom) / 2 - ScreenUtils.dp2px(2);

                hasMeasured = true;
            }
        }

        private void drawImage(Canvas canvas) {
            /**
             * 画底座
             */
            if (mShift <= 0.5f) {
                canvas.drawBitmap(mBitmap1, null, mIconRect, null);
            }
            /**
             * 描边
             */
            mCanvasOutLine.drawPaint(clearPaint);
            // 0~255
            colorPaint.setColor(mSelectedColor);
            // 0~0.5时透明度递增直到255，0.5~1不变
            colorPaint.setAlpha(mShift <= 0.5f ? (int)(255 * 2 * mShift) : 255);
            mCanvasOutLine.drawRect(mIconRect, colorPaint);
            // 整个颜色区域，只保留mBitmap1的边框
            mCanvasOutLine.drawBitmap(mBitmap1, null, mIconRect, bitmapPaint);
            canvas.drawBitmap(mBitmapOutLine, 0, 0, null);

            if (mShift > 0.5f) {
                /**
                 * 因为新闻按钮，中间最后是白色，所以画个白色
                 */
                colorPaint.setColor(getResources().getColor(R.color.white));
                // 0~255
                colorPaint.setAlpha((int)(255 * 2 * (mShift - 0.5f)));
                canvas.drawRect(mIconRect, colorPaint);

                /**
                 * 填充内部
                 */
                mCanvasInner.drawPaint(clearPaint);
                colorPaint.setColor(mSelectedColor);
                // 0~255
                colorPaint.setAlpha((int)(255 * 2 * (mShift - 0.5f)));
                mCanvasInner.drawRect(mIconRect, colorPaint);
                mCanvasInner.drawBitmap(mBitmap2, null, mIconRect, bitmapPaint);
                canvas.drawBitmap(mBitmapInner, 0, 0, null);
            }
        }

        private void drawText(Canvas canvas) {
//            mTextPaint.setColor(mUnSelectedColor);
//            canvas.drawText(mText, mX, mY, mTextPaint);
//            mTextPaint.setColor(mSelectedColor);
//            mTextPaint.setAlpha((int)(255 * mShift));

            int color = ColorUtils.evaluate(mShift, mUnSelectedColor, mSelectedColor);
            mTextPaint.setColor(color);
            canvas.drawText(mText, mX, mY, mTextPaint);
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
