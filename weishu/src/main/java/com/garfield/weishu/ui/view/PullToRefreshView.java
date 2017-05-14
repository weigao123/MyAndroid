package com.garfield.weishu.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garfield.baselib.utils.system.ScreenUtil;
import com.garfield.weishu.R;
import com.garfield.weishu.base.recyclerview.RecyclerUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.animation.Animation.INFINITE;

/**
 * Created by gaowei3 on 2016/11/4.
 */

/**
 * 不够流畅，因为onTouch调用的密度不够细
 */
public class PullToRefreshView extends LinearLayout implements View.OnTouchListener {
    // 正在下拉，幅度还不够刷新的位置
    public static final int STATUS_PULLING_NOT_YET = 0;
    public static final int STATUS_RELEASE_TO_REFRESH = 1;
    public static final int STATUS_REFRESHING = 2;
    // 刷新完成或未刷新状态
    public static final int STATUS_REFRESH_FINISHED = 3;

    /**
     * 功能1：刷新状态时，判断松手后，Head停留在显示还是隐藏
     * 功能2：刷新状态时，如果当前是Head显示，再次去触摸时，diff就要以containerOffset为基准，否则以yDown为基准
     */
    private boolean isRefreshingHeadShowing;
    private int currentStatus = STATUS_REFRESH_FINISHED;
    private int lastStatus = currentStatus;

    private int containerOffset;
    private boolean mHasMeasured;
    private boolean mEnabled = false;
    private float yDown = 0;

    public static final long ONE_MINUTE = 60 * 1000;
    public static final long ONE_HOUR = 60 * ONE_MINUTE;
    public static final long ONE_DAY = 24 * ONE_HOUR;
    public static final long ONE_MONTH = 30 * ONE_DAY;
    public static final long ONE_YEAR = 12 * ONE_MONTH;
    private SharedPreferences preferences;
    private static final String UPDATED_AT = "updated_at";
    private long lastUpdateTime;

    private RecyclerView mRecyclerView;
    private LinearLayout mPullRefreshHead;

    @BindView(R.id.pull_to_refresh_arrow)
    ImageView mArrow;
    @BindView(R.id.pull_to_refresh_progress)
    ImageView mProgress;

    @BindView(R.id.pull_to_refresh_description)
    TextView description;
    @BindView(R.id.pull_to_refresh_updated_at)
    TextView updateAt;

    private ValueAnimator mOffsetAnimator;
    private List<Integer> mOffsetAnimatorTo = new ArrayList<>();
    private boolean mIsTouching;

    private OnPullRefreshListener mListener;

    public PullToRefreshView(Context context) {
        super(context);
        init(context);
    }

    public PullToRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullToRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPullRefreshHead = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.pull_to_refresh, this, false);
        addView(mPullRefreshHead, 0);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        ButterKnife.bind(this, this);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        //refreshUpdatedAtValue();

        //mArrow.setColorFilter(getResources().getColor(R.color.colorPrimary));
        //mProgress.setColorFilter(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        /**
         * 这两个不能放到构造函数里，否则错误
         */
        mRecyclerView = (RecyclerView) getChildAt(1);
        mRecyclerView.setOnTouchListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!mHasMeasured) {
            containerOffset = - mPullRefreshHead.getMeasuredHeight();
            // 有可能会在layout之前就执行了refreshStart
            setPullOffset(currentStatus != STATUS_REFRESHING ? 0 : -containerOffset);
            updateHeaderView();
            mHasMeasured = true;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsTouching = true;
                yDown = ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                mIsTouching = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 必须要找个机会拦截消息，否则滑动起来，会拉不下来，原因是两个滚动产生影响
     * 下拉时因为margin的变动，导致手在ListView的相对getY位置变小，会被系统以为向上滚动，isAtTop会变成false，出现各种问题
     * 屏蔽掉ListView的滚动事件：
     * 通过返回true，ListView本身的onTouchEvent就不会调用，也就不会滑动了
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        setIsAbleToPull(event);
        /**
         * 只要在顶部，就打开功能，一旦上移，就会reset关闭，下移当挂住Top时，yDown更新当前位置
         */
        if (mEnabled) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 没有拦截，是透明的，不会回调
                    break;
                case MotionEvent.ACTION_MOVE:
                    // item不会消耗move，所以正常返回
                    float diff = event.getRawY() - yDown;
                    if (currentStatus != STATUS_REFRESHING) {
                        if (diff >= 0) {
                            int smoothDiff = (int) (event.getRawY() - yDown) / 2;
                            if (smoothDiff >= -containerOffset) {
                                currentStatus = STATUS_RELEASE_TO_REFRESH;
                            } else if (smoothDiff > 0 && smoothDiff < -containerOffset) {
                                currentStatus = STATUS_PULLING_NOT_YET;
                            }
                            setPullOffset(smoothDiff);
                            updateHeaderView();
                            return true;
                        } else {
                            // 关闭
                            refreshEnd(false);
                            return false;
                        }
                    } else {
                        /**
                         * 正在刷新时去触摸，yDown是新的触摸点
                         */
                        if (isRefreshingHeadShowing) {
                            if (diff >= 0) {
                                // 平滑拉
                                int firstShowHeadDiff = - containerOffset + (int)diff / 2;
                                setPullOffset(firstShowHeadDiff);
                                return true;
                            } else {
                                // 不平滑收回，无法平滑
                                int firstShowHeadDiff = - containerOffset + (int)diff;
                                if (firstShowHeadDiff >= 0) {
                                    setPullOffset(firstShowHeadDiff);
                                    return true;
                                } else {
                                    setPullOffset(0);
                                    isRefreshingHeadShowing = false;
                                    return false;
                                }
                            }
                        } else {
                            if (diff >= 0) {
                                int smoothDiff = (int) (event.getRawY() - yDown) / 2;
                                setPullOffset(smoothDiff);
                                return true;
                            } else {
                                setPullOffset(0);
                                return false;
                            }
                        }
                    }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    /**
                     * 功能打开的状态下，只有 STATUS_PULLING_NOT_YET、STATUS_RELEASE_TO_REFRESH
                     */
                    if (currentStatus == STATUS_PULLING_NOT_YET) {
                        refreshEnd(false);
                    } else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
                        isRefreshingHeadShowing = true;
                        currentStatus = STATUS_REFRESHING;
                        setPullOffset(-containerOffset);
                        updateHeaderView();
                        isRefreshingHeadShowing = true;
                        doTask();
                    } else if (currentStatus == STATUS_REFRESHING) {
                        setPullOffset(0);
                        isRefreshingHeadShowing = false;
                    }
                    break;
            }
        }
        return false;
    }

    /**
     * offset露出来的那部分高度
     */
    private void setPullOffset(int offset) {
        LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) mPullRefreshHead.getLayoutParams();
        if (mIsTouching) {
            headParams.topMargin = containerOffset + offset;
            mPullRefreshHead.setLayoutParams(headParams);
        } else {
            // 松手后，先要回到正在刷新的位置，这时马上Task结束，而上次的动画还未结束
            if (mOffsetAnimator != null && mOffsetAnimator.isRunning()) {
                mOffsetAnimatorTo.add(offset);
                return;
            }
            mOffsetAnimatorTo.add(offset);
            startOffsetAnimator(offset);
        }
    }

    private ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
        public void onAnimationUpdate(ValueAnimator animation) {
            Integer value = (Integer) animation.getAnimatedValue();   //取出新值
            LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) mPullRefreshHead.getLayoutParams();
            headParams.topMargin = value;
            mPullRefreshHead.setLayoutParams(headParams);
        }
    };

    private AnimatorListenerAdapter animatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            mOffsetAnimatorTo.remove(0);
            if (mOffsetAnimatorTo.size() == 0) {
                updateHeaderView();
                return;
            }
            Integer offset = mOffsetAnimatorTo.get(0);
            startOffsetAnimator(offset);
        }
    };

    private void startOffsetAnimator(int offset) {
        LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) mPullRefreshHead.getLayoutParams();
        mOffsetAnimator = ValueAnimator.ofInt(headParams.topMargin, containerOffset + offset);
        mOffsetAnimator.addUpdateListener(updateListener);
        mOffsetAnimator.addListener(animatorListener);
        int diff = Math.abs(containerOffset + offset - headParams.topMargin);
        int time = (diff < -containerOffset + 10) ? ScreenUtil.px2dp(diff) * 4 : 300;
        mOffsetAnimator.setDuration(time);
        mOffsetAnimator.start();
    }

    private void updateHeaderView() {
        if (lastStatus != currentStatus) {
            lastStatus = currentStatus;
            if (currentStatus == STATUS_PULLING_NOT_YET) {
                description.setText(getResources().getString(R.string.pull_to_refresh));
                mArrow.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.INVISIBLE);
                mProgress.clearAnimation();
                rotateArrow();
            } else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
                description.setText(getResources().getString(R.string.release_to_refresh));
                mArrow.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.INVISIBLE);
                mProgress.clearAnimation();
                rotateArrow();
            } else if (currentStatus == STATUS_REFRESHING) {
                description.setText(getResources().getString(R.string.refreshing));
                mProgress.setVisibility(View.VISIBLE);
                mArrow.setVisibility(View.INVISIBLE);
                mArrow.clearAnimation();
                rotateProgress();
            } else if (currentStatus == STATUS_REFRESH_FINISHED) {
                mArrow.clearAnimation();
                mProgress.clearAnimation();
            }
            //refreshUpdatedAtValue();
        }
    }

    /**
     * 只在状态改变时才调用
     */
    private void rotateArrow() {
        float pivotX = mArrow.getWidth() / 2f;
        float pivotY = mArrow.getHeight() / 2f;
        float fromDegrees = 0f;
        float toDegrees = 0f;
        if (currentStatus == STATUS_PULLING_NOT_YET) {
            fromDegrees = 180f;
            toDegrees = 360f;
        } else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
            fromDegrees = 0f;
            toDegrees = 180f;
        }
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
        animation.setDuration(200);
        animation.setFillAfter(true);
        mArrow.startAnimation(animation);
    }

    private void rotateProgress() {
        float pivotX = mProgress.getWidth() / 2f;
        float pivotY = mProgress.getHeight() / 2f;
        float fromDegrees = 0f;
        float toDegrees = 359f;
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
        animation.setRepeatCount(INFINITE);
        animation.setDuration(800);
        animation.setFillAfter(true);
        animation.setInterpolator(new LinearInterpolator());
        mProgress.startAnimation(animation);
    }

    private void refreshUpdatedAtValue() {
        lastUpdateTime = preferences.getLong(UPDATED_AT, -1);
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - lastUpdateTime;
        long timeIntoFormat;
        String updateAtValue;
        if (lastUpdateTime == -1) {
            updateAtValue = getResources().getString(R.string.not_updated_yet);
        } else if (timePassed < 0) {
            updateAtValue = getResources().getString(R.string.time_error);
        } else if (timePassed < ONE_MINUTE) {
            updateAtValue = getResources().getString(R.string.updated_just_now);
        } else if (timePassed < ONE_HOUR) {
            timeIntoFormat = timePassed / ONE_MINUTE;
            String value = timeIntoFormat + "分钟";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else if (timePassed < ONE_DAY) {
            timeIntoFormat = timePassed / ONE_HOUR;
            String value = timeIntoFormat + "小时";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else if (timePassed < ONE_MONTH) {
            timeIntoFormat = timePassed / ONE_DAY;
            String value = timeIntoFormat + "天";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else if (timePassed < ONE_YEAR) {
            timeIntoFormat = timePassed / ONE_MONTH;
            String value = timeIntoFormat + "个月";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else {
            timeIntoFormat = timePassed / ONE_YEAR;
            String value = timeIntoFormat + "年";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        }
        updateAt.setText(updateAtValue);
        // 文字大小可能从小的，变成大的
        invalidate();
    }

    private void setIsAbleToPull(MotionEvent event) {
        boolean isToTop = RecyclerUtil.isAtTop(mRecyclerView);
        if (isToTop) {
            if (!mEnabled) {
                yDown = event.getRawY();
            }
            mEnabled = true;
        } else {
            mEnabled = false;
        }
    }

    private void refreshEnd(boolean isFinished) {
        if (currentStatus == STATUS_REFRESH_FINISHED) return;
        currentStatus = STATUS_REFRESH_FINISHED;
        mEnabled = false;
        yDown = 0;
        setPullOffset(0);
        //不能现在更新，防止progress动画归位，在offset动画结束后更新
        //updateHeaderView();
        isRefreshingHeadShowing = false;
        if (isFinished) {
            preferences.edit().putLong(UPDATED_AT, System.currentTimeMillis()).apply();
        }
    }

    private void refreshStart() {
        if (currentStatus == STATUS_REFRESHING) return;
        currentStatus = STATUS_REFRESHING;
        setPullOffset(-containerOffset);
        if (mHasMeasured) {
            // 如果View还没有被测量等，会造成动画原点偏离，等layout结束后再更新
            updateHeaderView();
        }
        isRefreshingHeadShowing = true;
    }

    private void doTask() {
        if (mListener != null) {
            mListener.onPullRefresh();
        }
        //new RefreshingTask().execute();
    }

    public void setRefreshState(boolean isRefreshing) {
        if (isRefreshing) {
            refreshStart();
        } else {
            refreshEnd(false);
        }
    }

    private class RefreshingTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(23000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            refreshEnd(true);
        }
    }

    public void setOnRefreshListener(OnPullRefreshListener listener) {
        mListener = listener;
    }

    public interface OnPullRefreshListener {
        void onPullRefresh();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mOffsetAnimator != null && mOffsetAnimator.isRunning()) {
            mOffsetAnimator.cancel();
        }
        mArrow.clearAnimation();
        mProgress.clearAnimation();
    }
}
