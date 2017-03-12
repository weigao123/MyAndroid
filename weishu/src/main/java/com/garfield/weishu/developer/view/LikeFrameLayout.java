package com.garfield.weishu.developer.view;

/**
 * Created by gaowei3 on 2016/5/20.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.garfield.weishu.R;

import java.util.Random;

public class LikeFrameLayout extends FrameLayout {

    private Interpolator line = new LinearInterpolator();
    private Interpolator acc = new AccelerateInterpolator();
    private Interpolator dce = new DecelerateInterpolator();
    private Interpolator accdec = new AccelerateDecelerateInterpolator();
    private Interpolator[] interpolators = {line, acc, dce, accdec};

    private int mHeight;
    private int mWidth;
    private Drawable[] drawables = new Drawable[3];
    private Random random = new Random();

    private int dHeight;
    private int dWidth;

    public LikeFrameLayout(Context context) {
        super(context);
    }

    public LikeFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LikeFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private void init() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.like_fly);

        BitmapDrawable drawable0 = new BitmapDrawable(getResources(), bitmap);
        BitmapDrawable drawable1 = new BitmapDrawable(getResources(), bitmap);
        BitmapDrawable drawable2 = new BitmapDrawable(getResources(), bitmap);
        drawable0.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        drawable1.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
        drawable2.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

        drawables[0] = drawable0;
        drawables[1] = drawable1;
        drawables[2] = drawable2;

        dHeight = drawable0.getIntrinsicHeight();
        dWidth = drawable0.getIntrinsicWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    public void addHeart() {
        addHeart(mWidth / 2, mHeight - dHeight / 2);
    }

    public void addHeart(int left, int top) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(drawables[random.nextInt(3)]);

        left = left - dWidth / 2;
        top = top - dHeight / 2;
        LayoutParams lp = new LayoutParams(dWidth, dHeight);
        lp.setMargins(left, top, 0, 0);
        imageView.setLayoutParams(lp);
        addView(imageView);

        Animator set = getAnimator(imageView, left, top);
        set.addListener(new AnimEndListener(imageView));
        set.start();
    }

    private Animator getAnimator(View target, int left, int top) {
        AnimatorSet enterAnimator = getEnterAnimator(target);
        ValueAnimator exitAnimator = getBezierValueAnimator(target, left, top);

        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(enterAnimator, exitAnimator);
        finalSet.setInterpolator(interpolators[random.nextInt(4)]);
        return finalSet;
    }

    /**
     * 渐变出现
     */
    private AnimatorSet getEnterAnimator(final View target) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.2f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 0.2f, 1f);
        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(500);
        enter.setInterpolator(new LinearInterpolator());
        enter.playTogether(alpha, scaleX, scaleY);
        return enter;
    }

    /**
     * 渐变飞
     */
    private ValueAnimator getBezierValueAnimator(View target, int left, int top) {
        BezierEvaluator evaluator = new BezierEvaluator(getPointF(2), getPointF(1));
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, new PointF(left, top), new PointF(random.nextInt(getWidth()), 0));
        animator.addUpdateListener(new BezierListener(target));
        animator.setDuration(3000);
        return animator;
    }

    /**
     * 获取中间随机的两个点
     */
    private PointF getPointF(int scale) {
        PointF pointF = new PointF();
        pointF.x = random.nextInt((mWidth - 100));//减去100 是为了控制 x轴活动范围,看效果 随意~~
        //再Y轴上 为了确保第二个点 在第一个点之上,我把Y分成了上下两半 这样动画效果好一些  也可以用其他方法
        pointF.y = random.nextInt((mHeight - 100)) / scale;
        return pointF;
    }

    private class BezierListener implements ValueAnimator.AnimatorUpdateListener {

        private View target;

        BezierListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            PointF pointF = (PointF) animation.getAnimatedValue();
            target.setX(pointF.x);
            target.setY(pointF.y);
            target.setAlpha(1 - animation.getAnimatedFraction());
        }
    }

    private class AnimEndListener extends AnimatorListenerAdapter {
        private View target;

        AnimEndListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            removeViewInLayout(target);
        }
    }

    public class BezierEvaluator implements TypeEvaluator<PointF> {
        private PointF pointF1;
        private PointF pointF2;

        BezierEvaluator(PointF pointF1, PointF pointF2){
            this.pointF1 = pointF1;
            this.pointF2 = pointF2;
        }
        @Override
        public PointF evaluate(float time, PointF startValue,
                               PointF endValue) {
            float timeLeft = 1.0f - time;
            PointF point = new PointF();

            point.x = timeLeft * timeLeft * timeLeft * (startValue.x)
                    + 3 * timeLeft * timeLeft * time * (pointF1.x)
                    + 3 * timeLeft * time * time * (pointF2.x)
                    + time * time * time * (endValue.x);

            point.y = timeLeft * timeLeft * timeLeft * (startValue.y)
                    + 3 * timeLeft * timeLeft * time * (pointF1.y)
                    + 3 * timeLeft * time * time * (pointF2.y)
                    + time * time * time * (endValue.y);
            return point;
        }
    }
}
