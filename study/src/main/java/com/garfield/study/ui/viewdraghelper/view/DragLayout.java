package com.garfield.study.ui.viewdraghelper.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.garfield.baselib.utils.system.L;
import com.garfield.study.R;
import com.nineoldandroids.view.ViewHelper;

public class DragLayout extends FrameLayout implements View.OnClickListener {

    private boolean isShowShadow = true;

    private GestureDetectorCompat mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private ViewDragHelper mDragHelper;
    private DragListener mDragListener;
    private int range;
    private int width;
    private int height;
    private int mainLeft;
    private Context context;
    private ImageView iv_shadow;
    private RelativeLayout vg_left;
    private MyRelativeLayout vg_main;
    private Status status = Status.Close;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        MyGestureDetector myGestureDetector = new MyGestureDetector();
        mGestureDetector = new GestureDetectorCompat(context, myGestureDetector);
        mScaleGestureDetector = new ScaleGestureDetector(context, new MyScaleGestureDetector());
        mGestureDetector.setOnDoubleTapListener(myGestureDetector);
        mDragHelper = ViewDragHelper.create(this, dragHelperCallback);
    }

    class MyGestureDetector implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
        @Override
        public boolean onDown(MotionEvent e) {
            //L.d("onDown");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            //L.d("onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            //L.d("onSingleTapUp");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
            //L.d("onScroll");
            return Math.abs(dy) <= Math.abs(dx);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            //L.d("onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //L.d("onFling");
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //L.d("onSingleTapConfirmed");
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //L.d("onDoubleTap");
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            //L.d("onDoubleTapEvent");
            return false;
        }
    }

    private class MyScaleGestureDetector implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            L.d("onScale: "+detector.getScaleFactor());

            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            L.d("onScaleBegin");
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            L.d("onScaleEnd");
        }
    }

    private ViewDragHelper.Callback dragHelperCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return width;
        }

        /**
         *  拖动vg_left时，left和dx值一样
         *
         *  dx是这次微分手移动的距离dx，移动的快dx就大，dx累计起来，就是手移动的总距离
         *  left是上次微分后的位置再加上这次微分手移动的距离dx，left是以父左上角为坐标基点
         *
         *  left是由原来的位置加上dx计算而来的，而每次原来的位置都是0，所以left=dx
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //L.d("clampViewPositionHorizontal dx: "+dx);
            if (mainLeft + dx < 0) {
                return 0;
            } else if (mainLeft + dx > range) {
                return range;
            } else {
                return left;
            }
        }

        /**
         *  核心是为了计算mainLeft的值
         *
         *  拖动vg_left时，left竟然和dx值一样，都是微分位移，原因是每次都被重置布局
         *
         *  left如果在拖动状态取决于clampViewPositionHorizontal
         *  dx是和上次调用时的位置偏移
         *
         *  从静止开始拖动，一开始left=0，拖动dx，则在这里left=dx，dx=dx，然后被vg_left.layout(0, 0, width, height)布局到0
         *  又拖动dx，这时还是left=0，拖动dx，则这里left=dx，dx=dx，然后被vg_left.layout(0, 0, width, height)布局到0
         *  所以left一直等于dx
         *  如果把vg_left.layout重新布局去掉，left就是实际的位置了
         *  dispatchDragEvent对View的缩放，估计是并没有改变View的layout坐标？
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                int dx, int dy) {
            //L.d("onViewPositionChanged left: "+left);
            //L.d("onViewPositionChanged dx: "+dx);

            if (changedView == vg_main) {
                mainLeft = left;
            } else {
                mainLeft = mainLeft + dx;
            }
            if (mainLeft < 0) {
                mainLeft = 0;
            } else if (mainLeft > range) {
                mainLeft = range;
            }

            if (isShowShadow) {
                iv_shadow.layout(mainLeft, 0, mainLeft + width, height);
            }
            // 必须要有，拖拽vg_main时，ViewDragHelper直接作用于vg_main，所以vg_main会自动移动
            // 拖拽vg_left时，vg_main不会跟着移动，需要手动layout
            if (changedView == vg_left) {
                vg_left.layout(0, 0, width, height);
                vg_main.layout(mainLeft, 0, mainLeft + width, height);
            }

            if (changedView == vg_main && left == 0) {
                vg_left.layout(0, 0, width, height);
            }

            dispatchDragEvent(mainLeft);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (xvel > 0) {
                open();
            } else if (xvel < 0) {
                close();
            } else if (releasedChild == vg_main && mainLeft > range * 0.3) {
                open();
            } else if (releasedChild == vg_left && mainLeft > range * 0.7) {
                open();
            } else {
                close();
            }
        }
    };

    public interface DragListener {
        void onOpen();

        void onClose();

        void onDrag(float percent);
    }

    public void setDragListener(DragListener dragListener) {
        this.mDragListener = dragListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isShowShadow) {
            iv_shadow = new ImageView(context);
            iv_shadow.setImageResource(R.drawable.shadow);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            addView(iv_shadow, 1, lp);
        }
        vg_left = (RelativeLayout) getChildAt(0);
        vg_main = (MyRelativeLayout) getChildAt(isShowShadow ? 2 : 1);
        vg_main.setDragLayout(this);
        vg_left.setClickable(true);
        vg_main.setClickable(true);
        vg_main.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    public ViewGroup getVg_main() {
        return vg_main;
    }

    public ViewGroup getVg_left() {
        return vg_left;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = vg_left.getMeasuredWidth();
        height = vg_left.getMeasuredHeight();
        range = (int) (width * 0.6f);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        vg_left.layout(0, 0, width, height);
        vg_main.layout(mainLeft, 0, mainLeft + width, height);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mScaleGestureDetector.onTouchEvent(ev);
        mGestureDetector.onTouchEvent(ev);
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        try {
            mDragHelper.processTouchEvent(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void dispatchDragEvent(int mainLeft) {
        if (mDragListener == null) {
            return;
        }
        float percent = mainLeft / (float) range;
        animateView(percent);
        mDragListener.onDrag(percent);
        Status lastStatus = status;
        if (lastStatus != getStatus() && status == Status.Close) {
            mDragListener.onClose();
        } else if (lastStatus != getStatus() && status == Status.Open) {
            mDragListener.onOpen();
        }
    }

    private void animateView(float percent) {
        float f1 = 1 - percent * 0.3f;
        //主页一边移动，一边缩放
        ViewHelper.setScaleX(vg_main, f1);
        ViewHelper.setScaleY(vg_main, f1);
        //侧页
        ViewHelper.setTranslationX(vg_left, -vg_left.getWidth() / 2.3f + vg_left.getWidth() / 2.3f * percent);
        ViewHelper.setScaleX(vg_left, 0.5f + 0.5f * percent);
        ViewHelper.setScaleY(vg_left, 0.5f + 0.5f * percent);
        ViewHelper.setAlpha(vg_left, percent);
        if (isShowShadow) {
            ViewHelper.setScaleX(iv_shadow, f1 * 1.4f * (1 - percent * 0.12f));
            ViewHelper.setScaleY(iv_shadow, f1 * 1.85f * (1 - percent * 0.12f));
        }
        getBackground().setColorFilter(evaluate(percent, Color.BLACK, Color.TRANSPARENT), Mode.SRC_OVER);
    }

    private Integer evaluate(float fraction, Integer startInt, Integer endInt) {
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;
        return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
                | (int) ((startR + (int) (fraction * (endR - startR))) << 16)
                | (int) ((startG + (int) (fraction * (endG - startG))) << 8)
                | (int) ((startB + (int) (fraction * (endB - startB))));
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public enum Status {
        Drag, Open, Close
    }

    public Status getStatus() {
        if (mainLeft == 0) {
            status = Status.Close;
        } else if (mainLeft == range) {
            status = Status.Open;
        } else {
            status = Status.Drag;
        }
        return status;
    }

    public void open() {
        open(true);
    }

    public void open(boolean animate) {
        if (animate) {
            if (mDragHelper.smoothSlideViewTo(vg_main, range, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            vg_main.layout(range, 0, range * 2, height);
            dispatchDragEvent(range);
        }
    }

    public void close() {
        close(true);
    }

    public void close(boolean animate) {
        if (animate) {
            if (mDragHelper.smoothSlideViewTo(vg_main, 0, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            vg_main.layout(0, 0, width, height);
            dispatchDragEvent(0);
        }
    }

}
