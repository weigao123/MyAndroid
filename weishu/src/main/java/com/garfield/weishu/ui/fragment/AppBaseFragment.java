package com.garfield.weishu.ui.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.garfield.baselib.fragmentation.anim.DefaultHorizontalAnimator;
import com.garfield.baselib.fragmentation.anim.FragmentAnimator;
import com.garfield.baselib.swipeback.SwipeBackFragment;
import com.garfield.baselib.utils.system.ScreenUtil;
import com.garfield.weishu.BuildConfig;
import com.garfield.weishu.R;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.contact.ContactFragment;
import com.garfield.weishu.developer.test.TestListViewFragment;
import com.garfield.weishu.discovery.DiscoveryFragment;
import com.garfield.weishu.discovery.news.ui.NewsListFragment;
import com.garfield.weishu.discovery.scan.ScanFragment;
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
    protected Toolbar mToolbar;

    @Nullable @BindView(R.id.lazyload_loading)
    View mLoadingView;

    @Nullable @BindView(R.id.lazyload_content)
    View mLazyLoadContentView;

    private PopupWindow mPopupWindow;
    private Unbinder mUnbinder;

    /**
     * 如果直接跳到ViewPager还没被初始化的某一页，setUserVisibleHint会先于onCreate执行，造成crash
     */
    private boolean mIsPrepared;
    private boolean mHasLoaded;
    protected boolean mIsVisibleToUser;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (onGetFragmentLayout() != 0) {
            mRootView = inflater.inflate(onGetFragmentLayout(), container, false);
            if (onEnableSwipe()) {
                mRootView = attachToSwipeBack(mRootView);
                setSwipeBackEnable(true);
            }
            mUnbinder = ButterKnife.bind(this, mRootView);
            onInitViewAndData(mRootView, savedInstanceState);

            // NewsTabFragment没有toolbar
            if (mToolbar != null) {
                //mToolbar.inflateMenu(R.menu.fragment_msg_list);
                //mToolbar.setOnMenuItemClickListener(this);
                mToolbar.setTitleMarginStart(0);
                mToolbar.setContentInsetsAbsolute(0, 0);
                ImageView addView = (ImageView) mToolbar.findViewById(R.id.toolbar_add_view);
                addView.setRotation(45);
                mToolbar.findViewById(R.id.toolbar_add).setOnClickListener(mOnClickListener);
                mToolbar.findViewById(R.id.toolbar_search).setOnClickListener(mOnClickListener);
                mToolbar.findViewById(R.id.toolbar_back).setOnClickListener(mOnClickListener);
                if (onEnableBack()) {
                    mToolbar.findViewById(R.id.toolbar_back_container).setVisibility(View.VISIBLE);
                    TextView title2View = (TextView) mToolbar.findViewById(R.id.toolbar_title2);
                    title2View.setText(onGetToolbarTitle());
                    title2View.setTextColor(ContextCompat.getColor(mActivity, R.color.mainTextColorWhite));
                } else {
                    TextView title1View = (TextView) mToolbar.findViewById(R.id.toolbar_title1);
                    title1View.setText(onGetToolbarTitle());
                    title1View.setTextColor(ContextCompat.getColor(mActivity, R.color.mainTextColorWhite));
                    title1View.setVisibility(View.VISIBLE);
                    title1View.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (BuildConfig.DEBUG && AppBaseFragment.this.getClass() == MainFragment.class) {
                                EventDispatcher.startFragmentEvent(new TestListViewFragment());
                            }
                        }
                    });
                    //mToolbar.setTitle(onGetToolbarTitle());
                    //mToolbar.setTitleTextAppearance(mActivity, R.style.toolbar_text);
                    mToolbar.findViewById(R.id.toolbar_control_view).setVisibility(View.VISIBLE);
                }
            }
        }
        /**
         * lazy load
         */
        mIsPrepared = true;
        if (mLazyLoadContentView != null) {
            mLazyLoadContentView.setAlpha(0f);
            if (mIsVisibleToUser) {
                lazyLoad();
            }
        }
        //L.d("onCreateView: "+getClass().getSimpleName());
        return mRootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //L.d("setUserVisibleHint: "+getClass().getSimpleName() + "  " + isVisibleToUser);
        if (isVisibleToUser) {
            mIsVisibleToUser = true;
            if (mIsPrepared && !mHasLoaded) {
                lazyLoad();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //L.d("onHiddenChanged: "+getClass().getSimpleName() + "  " + hidden);
    }

    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {

    }

    private void lazyLoad() {
        mHasLoaded = true;
        showContent();
        onLazyLoad();
    }

    protected void onLazyLoad() {
    }

    protected void showContent() {
        if (mLazyLoadContentView != null) {
            if (mLoadingView != null) {
                mLoadingView.setVisibility(View.GONE);
            }
            mLazyLoadContentView.animate().alpha(1f).setDuration(500).start();
        }
    }

    protected int onGetFragmentLayout() {
        return 0;
    }

    private boolean onEnableSwipe() {
        return !(this.getClass() == MainFragment.class ||
                this.getClass() == SessionListFragment.class ||
                this.getClass() == ContactFragment.class ||
                this.getClass() == DiscoveryFragment.class ||
                this.getClass() == NewsListFragment.class ||
                this.getClass() == SettingFragment.class);
    }

    private boolean onEnableBack() {
        return !(this.getClass() == MainFragment.class);
    }

    private void initMenu() {
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.pop_window, null);
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 这个有必要，否则在5.0上点不到它，否则5.0上点击外界菜单不被隐藏
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        contentView.findViewById(R.id.menu_item_chat).setOnClickListener(mOnClickListener);
        contentView.findViewById(R.id.menu_item_add_friend).setOnClickListener(mOnClickListener);
        contentView.findViewById(R.id.menu_item_scan).setOnClickListener(mOnClickListener);
        contentView.findViewById(R.id.menu_item_help).setOnClickListener(mOnClickListener);
    }

    protected String onGetToolbarTitle() {
        return getString(R.string.app_name);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.toolbar_add:
                    if (mPopupWindow == null) {
                        initMenu();
                    }
                    int xoff = ScreenUtil.screenWidth - (int)getResources().getDimension(R.dimen.pop_menu_width) - ScreenUtil.dp2px(5);
                    int yoff = -ScreenUtil.dp2px(5);
                    mPopupWindow.showAsDropDown(mToolbar, xoff, yoff);

//                PopupMenu mPopupMenu = new PopupMenu(mActivity, mToolbar, Gravity.END);
//                mPopupMenu.inflateView(R.menu.fragment_msg_list);
//                mPopupMenu.toast();
                    break;
                case R.id.menu_item_chat:
                    mPopupWindow.dismiss();
                    break;
                case R.id.menu_item_add_friend:
                    EventDispatcher.getFragmentJumpEvent().onShowSearchUser();
                    mPopupWindow.dismiss();
                    break;
                case R.id.menu_item_scan:
                    EventDispatcher.startFragmentEvent(new ScanFragment());
                    mPopupWindow.dismiss();
                    break;
                case R.id.menu_item_help:
                    mPopupWindow.dismiss();
                    break;
                case R.id.toolbar_back:
                    popFragment();
                    break;
            }
        }
    };

    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        // 所有的Fragment默认都用这个动画
        // 如果是根元素就不去动画，通过在loadRootFragment时不设置setTransition
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
            // ToDo 是否需要释放？
            //mUnbinder.unbind();
        }
        mIsPrepared = false;
        mHasLoaded = false;
        mIsVisibleToUser = false;
        //L.d("onDestroyView");
    }

    protected View $(int resId) {
        return getView() == null ? null : getView().findViewById(resId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
