package com.garfield.baselib.swipeback;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.garfield.baselib.R;
import com.garfield.baselib.fragmentation.SupportFragment;

import java.util.ArrayList;
import java.util.List;

public class SwipeBackLayout2 extends FrameLayout {

    public static final String FRAGMENT_SWIPE_BACKING = "fragment_swipe_backing";

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

    // 有了这个，可能保证 percent>1
    private static final int OVERSCROLL_DISTANCE = 20;

    // 滑动关闭时，Pre显示的最远部分
    private static final int PRE_SCROLL_SHOW = 50;
    private int mPreScrollShow;

    private int mEdgeFlag = EDGE_LEFT;

    // EDGE_LEFT、EDGE_RIGHT，二者之一
    private int mCurrentEdge;

    private Activity mActivity;
    private View mContentView;
    private Fragment mFragment;
    private Fragment mPreFragment;
    private float mScrimOpacity;
    private float mScrollPercent;

    private boolean mEnable = false;

    private ViewDragHelper mDragHelper;
    private Rect mTmpRect = new Rect();
    private List<SwipeListener> mListeners;
    private Drawable mShadowLeft;
    private Drawable mShadowRight;

    public SwipeBackLayout2(Context context) {
        this(context, null);
    }

    public SwipeBackLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mDragHelper = ViewDragHelper.create(this, new ViewDragCallback());
        setShadow(R.drawable.shadow_left_gray, EDGE_LEFT);
        setEdgeOrientation(EDGE_LEFT);

        final float density = getResources().getDisplayMetrics().density;
        mPreScrollShow = (int) (PRE_SCROLL_SHOW * density);
        final float minVel = MIN_FLING_VELOCITY * density;
        mDragHelper.setMinVelocity(minVel);
    }

    public void setEdgeOrientation(int orientation) {
        mEdgeFlag = orientation;
        mDragHelper.setEdgeTrackingEnabled(orientation);
        if (orientation == EDGE_RIGHT || orientation == EDGE_ALL) {
            setShadow(R.drawable.shadow_right_gray, EDGE_RIGHT);
        }
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
        void onDragScrollChanged(float scrollPercent);
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
        if (mPreFragment == null) {
            List<Fragment> fragmentList = mFragment.getFragmentManager().getFragments();
            if (fragmentList != null && fragmentList.size() > 1) {
                int index = fragmentList.indexOf(mFragment);
                for (int i = index - 1; i >= 0; i--) {
                    Fragment preFragment = fragmentList.get(i);
                    if (preFragment != null) {
                        mPreFragment = preFragment;
                        break;
                    }
                }
            }
        }
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

    @Override
    public void computeScroll() {
        mScrimOpacity = 1 - mScrollPercent;
        // 一旦>1就停止滑动，保证只有一次mScrollPercent > 1
        if (mScrollPercent > 1) {
            return;
        }
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void handlePreFragment() {
        if (mPreFragment != null) {
            View view = mPreFragment.getView();
            if (view != null) {
                if (mScrollPercent > 1) {
                    view.scrollTo(0, 0);
                } else if (mScrollPercent == 0.0f) {
                    view.scrollTo(0, 0);
                    if (view.getVisibility() != INVISIBLE) {
                        view.setVisibility(INVISIBLE);
                    }
                } else {
                    if (mCurrentEdge == EDGE_LEFT) {
                        view.scrollTo((int) (mPreScrollShow * (1 - mScrollPercent)), 0);
                    } else if (mCurrentEdge == EDGE_RIGHT) {
                        view.scrollTo((int) (mPreScrollShow * (mScrollPercent - 1)), 0);
                    }
                    if (view.getVisibility() != VISIBLE) {
                        // 实际上mPreFragment.isHidden()还是true
                        view.setVisibility(VISIBLE);
                    }
                }
            }
        }
    }

    private void handleScrollChanged() {
        if (mFragment != null) {
            // computeScroll里的修改保证mScrollPercent>1后，就停止滑动，不再执行此方法
            if (mScrollPercent > 1) {
                setFragmentSwipeBacking(true);
                //FragmentManager manager = mFragment.getFragmentManager();
                // 相当于commit置反，把mFragment置remove，mPreFragment置show
                //manager.popBackStackImmediate();

                ((SupportFragment) mFragment).popFragment();
                setFragmentSwipeBacking(false);
            }
            handlePreFragment();
        }
        if (mActivity != null) {
            if (mScrollPercent > 1) {
                if (!mActivity.isFinishing()) {
                    mActivity.finish();
                }
            }
        }
    }

    /**
     * 设置是否正在pop back
     */
    private void setFragmentSwipeBacking(boolean state) {
        if (mFragment != null) {
            Bundle bundle = mFragment.getArguments();
            if (bundle == null) {
                bundle = new Bundle();
                mFragment.setArguments(bundle);
            }
            bundle.putBoolean(FRAGMENT_SWIPE_BACKING, state);
        }
        if (mPreFragment != null) {
            Bundle bundle = mPreFragment.getArguments();
            if (bundle == null) {
                bundle = new Bundle();
                mPreFragment.setArguments(bundle);
            }
            bundle.putBoolean(FRAGMENT_SWIPE_BACKING, state);
        }
    }

    /**
     * 当子View并不消耗事件时，事件返回到Layout，onTouchEvent执行才能起作用
     * 这样做的好处是，防止随意拦截消息，比如ViewPager就会出问题
     * 缺点，一旦子View消耗了事件，就无效了
     */
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
                    listener.onDragScrollChanged(mScrollPercent);
                }
            }
            // 使阴影贴近Content，进行重绘
            invalidate();
            handleScrollChanged();
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
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!mEnable) return super.onInterceptTouchEvent(event);
        try {
            return mDragHelper.shouldInterceptTouchEvent(event);
        } catch (ArrayIndexOutOfBoundsException e) {
            // FIXME: handle exception
            // issues #9
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnable) return super.onTouchEvent(event);
        mDragHelper.processTouchEvent(event);
        return true;
    }
}
