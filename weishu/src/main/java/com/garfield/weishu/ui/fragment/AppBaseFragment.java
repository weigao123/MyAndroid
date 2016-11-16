package com.garfield.weishu.ui.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.garfield.baselib.fragmentation.anim.DefaultHorizontalAnimator;
import com.garfield.baselib.fragmentation.anim.FragmentAnimator;
import com.garfield.baselib.swipeback.SwipeBackFragment;
import com.garfield.baselib.utils.SizeUtils;
import com.garfield.weishu.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.contact.ContactFragment;
import com.garfield.weishu.news.old.NewsTabFragment;
import com.garfield.weishu.news.view.NewsListFragment;
import com.garfield.weishu.session.sessionlist.SessionListFragment;
import com.garfield.weishu.setting.SettingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by gaowei3 on 2016/8/4.
 */
public class AppBaseFragment extends SwipeBackFragment {

    @Nullable @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Nullable @BindView(R.id.toolbar_control_view)
    LinearLayout mToolbarControl;

    private PopupWindow mPopupWindow;
    private Unbinder mUnbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (onGetFragmentLayout() != 0) {
            mRootView = inflater.inflate(onGetFragmentLayout(), container, false);
            if (onEnableSwipe()) {
                mRootView = attachToSwipeBack(mRootView);
                setSwipeBackEnable(true);
            }
            mUnbinder = ButterKnife.bind(this, mRootView);
            // NewsTabFragment没有toolbar
            if (mToolbar != null) {
                mToolbar.setTitle(onGetToolbarTitleResource());
                mToolbar.setTitleTextAppearance(mActivity, R.style.toolbar_text);

                //mToolbar.inflateMenu(R.menu.fragment_msg_list);
                //mToolbar.setOnMenuItemClickListener(this);

                ImageView addView = (ImageView) mToolbar.findViewById(R.id.toolbar_add_view);
                addView.setRotation(45);
                mToolbar.findViewById(R.id.toolbar_add).setOnClickListener(mOnClickListener);
                mToolbar.findViewById(R.id.toolbar_search).setOnClickListener(mOnClickListener);
            }
            onInitViewAndData(mRootView, savedInstanceState);
        }
        return mRootView;
    }

    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {

    }

    protected int onGetFragmentLayout() {
        return 0;
    }

    protected boolean onEnableSwipe() {
        return !(this.getClass() == MainFragment.class ||
                this.getClass() == SessionListFragment.class ||
                this.getClass() == ContactFragment.class ||
                this.getClass() == NewsListFragment.class ||
                this.getClass() == NewsTabFragment.class ||
                this.getClass() == SettingFragment.class);
    }

    private void initMenu() {
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.pop_window, null);
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 这个有必要，否则在5.0上点不到它，否则5.0上点击外界菜单不被隐藏
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        contentView.findViewById(R.id.menu_item_1).setOnClickListener(mOnClickListener);
        contentView.findViewById(R.id.menu_item_2).setOnClickListener(mOnClickListener);
        contentView.findViewById(R.id.menu_item_3).setOnClickListener(mOnClickListener);
        contentView.findViewById(R.id.menu_item_4).setOnClickListener(mOnClickListener);
    }

    protected int onGetToolbarTitleResource() {
        return R.string.app_name;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.toolbar_add:
                    if (mPopupWindow == null) {
                        initMenu();
                    }
                    int xoff = SizeUtils.getScreenWidth(mActivity) - (int)getResources().getDimension(R.dimen.pop_menu_width) - SizeUtils.dp2px(mActivity, 5);
                    int yoff = -SizeUtils.dp2px(mActivity, 5);
                    mPopupWindow.showAsDropDown(mToolbar, xoff, yoff);

//                PopupMenu mPopupMenu = new PopupMenu(mActivity, mToolbar, Gravity.END);
//                mPopupMenu.inflateView(R.menu.fragment_msg_list);
//                mPopupMenu.show();
                    break;
                case R.id.menu_item_1:
                    mPopupWindow.dismiss();
                    break;
                case R.id.menu_item_2:
                    mPopupWindow.dismiss();
                    break;
                case R.id.menu_item_3:
                    mPopupWindow.dismiss();
                    break;
                case R.id.menu_item_4:
                    mPopupWindow.dismiss();
                    break;
            }
        }
    };

    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (!AppCache.isHasAnimation()) {
            return AnimationUtils.loadAnimation(mActivity, com.garfield.baselib.R.anim.no_anim);
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
