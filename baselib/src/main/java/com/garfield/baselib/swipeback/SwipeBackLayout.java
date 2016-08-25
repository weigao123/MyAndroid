package com.garfield.baselib.swipeback;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.garfield.baselib.R;

import java.util.ArrayList;
import java.util.List;

public class SwipeBackLayout extends FrameLayout {

    //可以让View进行fling快滑（惯性滑动）的速度，大于该速度就会滑动
    private static final int MIN_FLING_VELOCITY = 400; // dips per second

    private static final int DEFAULT_SCRIM_COLOR = 0x99000000;

    private static final int FULL_ALPHA = 255;

    public static final int EDGE_LEFT = ViewDragHelper.EDGE_LEFT;
    public static final int EDGE_RIGHT = ViewDragHelper.EDGE_RIGHT;
    public static final int EDGE_ALL = EDGE_LEFT | EDGE_RIGHT;

    public static final int STATE_IDLE = ViewDragHelper.STATE_IDLE;
    public static final int STATE_DRAGGING = ViewDragHelper.STATE_DRAGGING;
    public static final int STATE_SETTLING = ViewDragHelper.STATE_SETTLING;

    //滑动超过这个比例，就关闭
    private static final float DEFAULT_SCROLL_THRESHOLD = 0.3f;

    private static final int OVERSCROLL_DISTANCE = 10;

    private int mEdgeFlag;

    private Activity mActivity;
    private View mContentView;
    private Fragment mFragment;
    private Fragment mPreFragment;

    private boolean mEnable = true;
    private boolean mOnlyEdgeDrag = true;

    private ViewDragHelper mDragHelper;


    private List<SwipeListener> mListeners;

    private Drawable mShadowLeft;
    private Drawable mShadowRight;

    private float mScrimOpacity;
    private float mScrollPercent;

    private int mCurrentEdge;

    private Rect mTmpRect = new Rect();

    public SwipeBackLayout(Context context) {
        this(context, null);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mDragHelper = ViewDragHelper.create(this, new ViewDragCallback());
        setShadow(R.drawable.shadow_left, EDGE_LEFT);
        setEdgeOrientation(EDGE_LEFT);

        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;
        mDragHelper.setMinVelocity(minVel);
    }

    public void setEdgeOrientation(int orientation) {
        mEdgeFlag = orientation;
        mDragHelper.setEdgeTrackingEnabled(orientation);
        if (orientation == EDGE_RIGHT || orientation == EDGE_ALL) {
            setShadow(R.drawable.shadow_right, EDGE_RIGHT);
        }
    }

    public void setOnlyEdgeDrag(boolean onlyEdgeDrag) {
        mOnlyEdgeDrag = onlyEdgeDrag;
    }

    public void setEnable(boolean enable) {
        mEnable = enable;
    }

    public void addSwipeListener(SwipeListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        mListeners.add(listener);
    }

    public void removeSwipeListener(SwipeListener listener) {
        if (mListeners == null) {
            return;
        }
        mListeners.remove(listener);
    }

    public interface SwipeListener {
        void onScrollStateChange(int state);
        //EDGE_LEFT, EDGE_RIGHT
        void onEdgeTouch(int edgeFlag);
        void onDragPositionChanged(float scrollPercent);
    }

    public void setShadow(int resId, int edgeFlag) {
        setShadow(getResources().getDrawable(resId), edgeFlag);
    }

    public void setShadow(Drawable shadow, int edgeFlag) {
        if ((edgeFlag & EDGE_LEFT) != 0) {
            mShadowLeft = shadow;
        } else if ((edgeFlag & EDGE_RIGHT) != 0) {
            mShadowRight = shadow;
        }
        invalidate();
    }

    public void attachToActivity(Activity activity) {
        mActivity = activity;
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.windowBackground
        });
        int background = a.getResourceId(0, 0);
        a.recycle();

        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        addView(decorChild);
        mContentView = decorChild;
        decor.addView(this);
    }

    //view是Fragment的第二层View，第一层是mSwipeBackLayout
    public void attachToFragment(Fragment fragment, View view) {
        addView(view);
        mFragment = fragment;
        mContentView = view;
    }

    /**
     * 每次invalidate后，调用，分别去绘制阴影和透明度
     * canvas就是SwipeBackLayout自己
     * 每次执行时，canvas就会被刷新，上次绘制的阴影等都被清空
     */
    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        //判断当前child是否是mContentView，确实只有一个child
        final boolean drawContent = child == mContentView;
        boolean drawChild = super.drawChild(canvas, child, drawingTime);
        if (mScrimOpacity > 0 && drawContent && mDragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE) {
            drawShadow(canvas, child);
            drawScrim(canvas, child);
        }
        return drawChild;
    }

    private void drawScrim(Canvas canvas, View child) {
        final int baseAlpha = (DEFAULT_SCRIM_COLOR & 0xff000000) >>> 24;
        final int alpha = (int) (baseAlpha * mScrimOpacity);
        final int color = alpha << 24;

        if ((mCurrentEdge & EDGE_LEFT) != 0) {
            //canvas就是SwipeBackLayout，以自己的左上角为基点
            canvas.clipRect(0, 0, child.getLeft(), getHeight());
        } else if ((mCurrentEdge & EDGE_RIGHT) != 0) {
            canvas.clipRect(child.getRight(), 0, getRight(), getHeight());
        }
        canvas.drawColor(color);
    }

    private void drawShadow(Canvas canvas, View child) {
        final Rect childRect = mTmpRect;
        //获取位置坐标
        child.getHitRect(childRect);

        if ((mEdgeFlag & EDGE_LEFT) != 0) {
            //设置一个包围区域，这个坐标是canvas的区域，当draw时绘制到的区域
            mShadowLeft.setBounds(childRect.left - mShadowLeft.getIntrinsicWidth(), childRect.top, childRect.left, childRect.bottom);
            mShadowLeft.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
            mShadowLeft.draw(canvas);
        }

        if ((mEdgeFlag & EDGE_RIGHT) != 0) {
            mShadowRight.setBounds(childRect.right, childRect.top, childRect.right + mShadowRight.getIntrinsicWidth(), childRect.bottom);
            mShadowRight.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
            mShadowRight.draw(canvas);
        }
    }

    /**
     * 先获取同级的Fragment，然后设置成可见
     */
    private void makePreVisible() {
        if (mFragment != null) {
            if (mPreFragment == null) {
                List<Fragment> fragmentList = mFragment.getFragmentManager().getFragments();
                if (fragmentList != null && fragmentList.size() > 1) {
                    int index = fragmentList.indexOf(mFragment);
                    for (int i = index - 1; i >= 0; i--) {
                        Fragment fragment = fragmentList.get(i);
                        if (fragment != null && fragment.getView() != null) {
                            fragment.getView().setVisibility(VISIBLE);
                            mPreFragment = fragment;
                            break;
                        }
                    }
                }
            } else {
                View preView = mPreFragment.getView();
                if (preView != null && preView.getVisibility() != VISIBLE) {
                    preView.setVisibility(VISIBLE);
                }
            }
        }
    }

    @Override
    public void computeScroll() {
        mScrimOpacity = 1 - mScrollPercent;
        // 保证只有一次mScrollPercent > 1
        if (mScrollPercent > 1) {
            return;
        }
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void handleCloseSelf() {
        // computeScroll里的修改保证mScrollPercent只有一次>1
        if (mScrollPercent > 1) {
            if (mFragment != null) {
                if (!mFragment.isDetached()) {
                    FragmentManager manager = mFragment.getFragmentManager();
                    manager.popBackStack();
                }
            }
            if (mActivity != null) {
                if (!mActivity.isFinishing()) {
                    mActivity.finish();
                }
            }
        }
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View view, int pointerId) {
            boolean isEdgeTouched = mDragHelper.isEdgeTouched(mEdgeFlag, pointerId);
            if (isEdgeTouched) {
                if (mDragHelper.isEdgeTouched(EDGE_LEFT, pointerId)) {
                    mCurrentEdge = EDGE_LEFT;
                } else if (mDragHelper.isEdgeTouched(EDGE_RIGHT, pointerId)) {
                    mCurrentEdge = EDGE_RIGHT;
                }
                if (mListeners != null) {
                    for (SwipeListener listener : mListeners) {
                        listener.onEdgeTouch(mCurrentEdge);
                    }
                }
                makePreVisible();
            }

            return isEdgeTouched;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mEdgeFlag & (EDGE_LEFT | EDGE_RIGHT);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if ((mCurrentEdge & EDGE_LEFT) != 0) {
                mScrollPercent = Math.abs((float) left / mContentView.getWidth());
            } else if ((mCurrentEdge & EDGE_RIGHT) != 0) {
                mScrollPercent = Math.abs((float) left / mContentView.getWidth());
            }
            if (mListeners != null && mDragHelper.getViewDragState() == STATE_DRAGGING && mScrollPercent <= 1 && mScrollPercent > 0) {
                for (SwipeListener listener : mListeners) {
                    listener.onDragPositionChanged(mScrollPercent);
                }
            }
            // 使阴影贴近Content，进行重绘
            invalidate();
            handleCloseSelf();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            final int childWidth = releasedChild.getWidth();
            int left = 0, top = 0;
            if ((mCurrentEdge & EDGE_LEFT) != 0) {
                left = xvel > 0 || xvel == 0 && mScrollPercent > DEFAULT_SCROLL_THRESHOLD ? childWidth + OVERSCROLL_DISTANCE : 0;
            } else if ((mCurrentEdge & EDGE_RIGHT) != 0) {
                left = xvel < 0 || xvel == 0 && mScrollPercent > DEFAULT_SCROLL_THRESHOLD ? -(childWidth + OVERSCROLL_DISTANCE) : 0;
            }
            mDragHelper.settleCapturedViewAt(left, top);
            invalidate();
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int ret = 0;
            if ((mCurrentEdge & EDGE_LEFT) != 0) {
                ret = Math.min(child.getWidth(), Math.max(left, 0));
            } else if ((mCurrentEdge & EDGE_RIGHT) != 0) {
                ret = Math.min(0, Math.max(left, -child.getWidth()));
            }
            return ret;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if (mListeners != null) {
                for (SwipeListener listener : mListeners) {
                    listener.onScrollStateChange(state);
                }
            }
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mEnable) return super.onInterceptTouchEvent(ev);
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnable) return super.onTouchEvent(event);
        mDragHelper.processTouchEvent(event);
        return true;
    }
}
