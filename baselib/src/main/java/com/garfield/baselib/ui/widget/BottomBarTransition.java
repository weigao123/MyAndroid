package com.garfield.baselib.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.garfield.baselib.utils.drawable.ColorUtils;
import com.garfield.baselib.utils.system.ScreenUtil;

/**
 * Created by gaowei3 on 2016/11/21.
 */

public class BottomBarTransition extends LinearLayout {

    private int mUnSelectedColor;
    private int mSelectedColor;
    private int mBackgroundColor;

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
        View dividerView = new View(context);
        addView(dividerView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));

        mTabLayout = new LinearLayout(context);
        //会造成有阴影，但当把连着的另外的view也设置成白色，就看不出来阴影了
        // mTabLayout.setBackgroundColor(Color.WHITE);
        mTabLayout.setOrientation(HORIZONTAL);
        addView(mTabLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mTabParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        mTabParams.weight = 1;
    }

    public BottomBarTransition setColor(int colorUnSelected, int colorSelected, int dividerColor, int backgroundColor) {
        mUnSelectedColor = getResources().getColor(colorUnSelected);
        mSelectedColor = getResources().getColor(colorSelected);
        mBackgroundColor = getResources().getColor(backgroundColor);
        getChildAt(0).setBackgroundColor(getResources().getColor(dividerColor));
        mTabLayout.setBackgroundColor(getResources().getColor(backgroundColor));
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
        // 实心图
        private Bitmap mBitmap2;
        // 要画图的区域在整个父Tab的位置
        private Rect mRectIcon;
        // 要画图的区域，以自身为坐标的位置，坐标是0开始，尺寸同上
        private Rect mRectIconSelf;

        // 新建用于画空心渐变图，先整个画纯色，然后再只保留mBitmap1的区域
        private Bitmap mBitmapOutLine;
        // 实心
        private Bitmap mBitmapInner;
        private Canvas mCanvasOutLine;
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
            //drawable1.setColorFilter(mUnSelectedColor, PorterDuff.Mode.SRC_ATOP);   //对Bitmap无效
            mBitmap1 = drawable1.getBitmap().copy(Bitmap.Config.ARGB_8888, true);   //可编辑
            BitmapDrawable drawable2 = (BitmapDrawable) getResources().getDrawable(icon2);
            mBitmap2 = drawable2.getBitmap();

            mTextBound = new Rect();
            mTextSize = ScreenUtil.dp2px(11);
            mTextPaint = new Paint();
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.setAntiAlias(true);
            mTextPaint.setDither(true);
            mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);

            int padding = ScreenUtil.dp2px(5.5f);
            setPadding(padding, padding, padding, padding);

            colorPaint = new Paint();
            colorPaint.setAntiAlias(true);
            colorPaint.setFilterBitmap(true);
            colorPaint.setDither(true);

            bitmapPaint = new Paint();
            bitmapPaint.setAntiAlias(true);
            bitmapPaint.setFilterBitmap(true);
            bitmapPaint.setDither(true);
            // 最后还是背景图的样子，但是要从背景中砍去新图有像素的部分
            bitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

            clearPaint = new Paint();
            clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            initAfterMeasure();
        }

        private void initAfterMeasure() {
            if (!hasMeasured) {
                int iconHeight = getMeasuredHeight() - getPaddingTop() * 3 - mTextBound.height();
                int iconWidth = (int) ((float) mBitmap1.getWidth() / mBitmap1.getHeight() * iconHeight);
                int left = getMeasuredWidth() / 2 - iconWidth / 2;
                int top = getMeasuredHeight() - mTextBound.height() - getPaddingTop() * 2 - iconHeight;
                mRectIcon = new Rect(left, top, left + iconWidth, top + iconHeight);
                mRectIconSelf = new Rect(0, 0, iconWidth, iconHeight);

                mBitmapOutLine = Bitmap.createBitmap(iconWidth, iconHeight, Bitmap.Config.ARGB_8888);
                mCanvasOutLine = new Canvas(mBitmapOutLine);
                mBitmapInner = Bitmap.createBitmap(iconWidth, iconHeight, Bitmap.Config.ARGB_8888);
                mCanvasInner = new Canvas(mBitmapInner);

                // 修改边框的颜色
                Canvas canvas = new Canvas(mBitmap1);
                canvas.drawColor(mUnSelectedColor, PorterDuff.Mode.SRC_IN);

                mTextX = getMeasuredWidth() / 2;
                mTextY = (getMeasuredHeight() + mRectIcon.bottom) / 2 - (mTextBound.top + mTextBound.bottom) / 2 - ScreenUtil.dp2px(2);

                hasMeasured = true;
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawImage(canvas);
            drawText(canvas);
        }

        /**
         * 核心就是把两张原始图像有像素的部分分别渐变成目标蓝色
         * 先绘制空心，再绘制实心，新闻按钮较为特殊
         */
        private void drawImage(Canvas canvas) {
            /**
             * 画原始空心底座，因为渐变空心边有画全透明色的部分，导致边框消失
             */
            if (mShift <= 0.5f) {
                canvas.drawBitmap(mBitmap1, null, mRectIcon, null);
            }
            /**
             * 绘制渐变空心边，从0~255，前后段都要绘制
             */
            //全局变量，被上了colorPaint色，得恢复颜色
            mCanvasOutLine.drawPaint(clearPaint);
            colorPaint.setColor(mSelectedColor);
            // 0~0.5时透明度递增直到255，0.5~1不变
            colorPaint.setAlpha(mShift <= 0.5f ? (int)(255 * 2 * mShift) : 255);
            // 整个矩形全上色
            mCanvasOutLine.drawPaint(colorPaint);
            // 整个矩形纯色区域，只保留mBitmap1的边框的区域
            mCanvasOutLine.drawBitmap(mBitmap1, null, mRectIconSelf, bitmapPaint);
            canvas.drawBitmap(mBitmapOutLine, null, mRectIcon, null);

            /**
             * 绘制渐变内部实心，从0~255，只需要在后半段绘制
             */
            if (mShift > 0.5f) {
                /**
                 * 新闻按钮mBitmap1中间有内容，描边后导致最后是蓝色，为了中间是白色，所以画个白色0~255
                 */
                if (mTabPosition == 2) {
                    colorPaint.setColor(mBackgroundColor);
                    colorPaint.setAlpha((int) (255 * 2 * (mShift - 0.5f)));
                    canvas.drawRect(mRectIcon, colorPaint);
                }

                /**
                 * 新闻按钮mBitmap2中间是空心，所以下面代码导致最后新闻中心也是透明的，需要白色底衬
                 */
                mCanvasInner.drawPaint(clearPaint);
                colorPaint.setColor(mSelectedColor);
                // 0~0.5时透明度0，0.5~1透明度递增到255
                colorPaint.setAlpha((int)(255 * 2 * (mShift - 0.5f)));
                mCanvasInner.drawPaint(colorPaint);
                mCanvasInner.drawBitmap(mBitmap2, null, mRectIconSelf, bitmapPaint);
                canvas.drawBitmap(mBitmapInner, null, mRectIcon, null);
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
