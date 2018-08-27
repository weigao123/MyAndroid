package com.garfield.baselib.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garfield.baselib.R;

import java.util.ArrayList;
import java.util.List;

public class BottomBar extends LinearLayout {
    private static final int TRANSLATE_DURATION_MILLIS = 200;

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private boolean mVisible = true;
    private boolean mHasShadow = false;
    private int mUnSelectedColor;
    private int mSelectedColor;

    private LinearLayout mTabLayout;
    private LayoutParams mTabParams;
    private int mCurrentPosition = 0;
    private OnTabSelectedListener mListener;

    private List<Tab> mTabList = new ArrayList<>();

    public BottomBar(Context context) {
        this(context, null);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);

//        if (attrs != null) {
//            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomBar);
//            mHasShadow = ta.getBoolean(R.styleable.BottomBar_hasShadow, false);
//            ta.recycle();
//        }
//
//        if (mHasShadow) {
//            ImageView shadowView = new ImageView(context);
//            shadowView.setBackgroundResource(R.drawable.actionbar_shadow_up);
//            addView(shadowView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        }

        View lineView = new View(context);
        lineView.setBackgroundColor(getResources().getColor(R.color.gray));
        addView(lineView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));

        mTabLayout = new LinearLayout(context);
        //会造成有阴影，当把连着的另外的view也设置成白色，就看不出来阴影了
        mTabLayout.setBackgroundColor(Color.WHITE);
        mTabLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(mTabLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mTabParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        mTabParams.weight = 1;
    }

    public BottomBar setColor(int colorUnSelected, int colorSelected) {
        mUnSelectedColor = colorUnSelected;
        mSelectedColor = colorSelected;
        return this;
    }

    public BottomBar addItem(int resource, String content) {
        final Tab tab = new Tab(getContext(), resource, content, mUnSelectedColor, mSelectedColor);
        mTabList.add(tab);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener == null) return;

                int pos = tab.getTabPosition();
                if (mCurrentPosition == pos) {
                    mListener.onTabReselected(pos);
                } else {
                    tab.setSelected(true);
                    mListener.onTabUnselected(mCurrentPosition);
                    mTabLayout.getChildAt(mCurrentPosition).setSelected(false);
                    mCurrentPosition = pos;
                    mListener.onTabSelected(pos, mCurrentPosition);
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
        mTabList.get(mCurrentPosition).setSelected(false);
        mTabList.get(position).setSelected(true);
        mCurrentPosition = position;
    }

    public void performClickItem(final int position) {
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                mTabLayout.getChildAt(position).performClick();
            }
        });
    }

    public interface OnTabSelectedListener {
        void onTabSelected(int position, int prePosition);
        void onTabUnselected(int position);
        void onTabReselected(int position);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, mCurrentPosition);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        if (mCurrentPosition != ss.position) {
            mTabLayout.getChildAt(mCurrentPosition).setSelected(false);
            mTabLayout.getChildAt(ss.position).setSelected(true);
        }
        mCurrentPosition = ss.position;
    }

    static class SavedState extends BaseSavedState {
        private int position;

        public SavedState(Parcel source) {
            super(source);
            position = source.readInt();
        }

        public SavedState(Parcelable superState, int position) {
            super(superState);
            this.position = position;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(position);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }


    public void hide() {
        hide(true);
    }

    public void show() {
        show(true);
    }

    public void hide(boolean anim) {
        toggle(false, anim, false);
    }

    public void show(boolean anim) {
        toggle(true, anim, false);
    }

    public boolean isVisible() {
        return mVisible;
    }

    private void toggle(final boolean visible, final boolean animate, boolean force) {
        if (mVisible != visible || force) {
            mVisible = visible;
            int height = getHeight();
            if (height == 0 && !force) {
                ViewTreeObserver vto = getViewTreeObserver();
                if (vto.isAlive()) {
                    // view树完成测量并且分配空间而绘制过程还没有开始的时候播放动画。
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = getViewTreeObserver();
                            if (currentVto.isAlive()) {
                                currentVto.removeOnPreDrawListener(this);
                            }
                            toggle(visible, animate, true);
                            return true;
                        }
                    });
                    return;
                }
            }
            int translationY = visible ? 0 : height;
            if (animate) {
                animate().setInterpolator(mInterpolator)
                        .setDuration(TRANSLATE_DURATION_MILLIS)
                        .translationY(translationY);
            } else {
                ViewCompat.setTranslationY(this, translationY);
            }
        }
    }







    private class Tab extends FrameLayout {
        private ImageView mIcon;
        private TextView mTvTitle;
        private Context mContext;
        private int mTabPosition = -1;

        private int mUnSelectedColor;
        private int mSelectedColor;


        public Tab(Context context, int icon, CharSequence title, int unSelectedColor, int selectedColor) {
            super(context);
            init(context, icon, title, unSelectedColor, selectedColor);
        }

        private void init(Context context, int icon, CharSequence title, int unSelectedColor, int selectedColor) {
            mContext = context;
            mUnSelectedColor = unSelectedColor;
            mSelectedColor = selectedColor;

            TypedArray typedArray = context.obtainStyledAttributes(new int[]{R.attr.selectableItemBackgroundBorderless});
            //点击产生波纹效果
            //Drawable drawable = typedArray.getDrawable(0);
            //setBackgroundDrawable(drawable);
            //或：
            int backgroundResource = typedArray.getResourceId(0, 0);
            setBackgroundResource(backgroundResource);
            typedArray.recycle();

            LinearLayout lLContainer = new LinearLayout(context);
            lLContainer.setOrientation(LinearLayout.VERTICAL);
            lLContainer.setGravity(Gravity.CENTER);
            LayoutParams paramsContainer = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            paramsContainer.gravity = Gravity.CENTER;
            lLContainer.setLayoutParams(paramsContainer);

            mIcon = new ImageView(context);
            int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            mIcon.setImageResource(icon);
            mIcon.setLayoutParams(params);
            mIcon.setColorFilter(ContextCompat.getColor(context, unSelectedColor));
            lLContainer.addView(mIcon);

            mTvTitle = new TextView(context);
            mTvTitle.setText(title);
            LinearLayout.LayoutParams paramsTv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            paramsTv.topMargin =  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
            mTvTitle.setTextSize(10);
            mTvTitle.setTextColor(ContextCompat.getColor(context, unSelectedColor));
            mTvTitle.setLayoutParams(paramsTv);
            lLContainer.addView(mTvTitle);

            addView(lLContainer);
        }

        @Override
        public void setSelected(boolean selected) {
            super.setSelected(selected);
            if (selected) {
                mIcon.setColorFilter(ContextCompat.getColor(mContext, mSelectedColor));
                mTvTitle.setTextColor(ContextCompat.getColor(mContext, mSelectedColor));
            } else {
                mIcon.setColorFilter(ContextCompat.getColor(mContext, mUnSelectedColor));
                mTvTitle.setTextColor(ContextCompat.getColor(mContext, mUnSelectedColor));
            }
        }

        public void setTabPosition(int position) {
            mTabPosition = position;
            if (position == 0) {
                setSelected(true);
            }
        }

        public int getTabPosition() {
            return mTabPosition;
        }
    }
}
