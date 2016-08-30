package com.garfield.weishu.base;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.garfield.baselib.fragmentation.SupportFragment;
import com.garfield.baselib.utils.SizeUtils;
import com.garfield.weishu.R;


/**
 * Created by gaowei3 on 2016/8/4.
 */
public class AppBaseFragment extends SupportFragment implements View.OnClickListener {

    private Toolbar mToolbar;
    private PopupWindow mPopupWindow;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 这里不能使用mActivity，因为这个Activity下有好多个toolbar
        mToolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        // NewsTabFragment没有toolbar
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.app_name);
            mToolbar.setTitleTextAppearance(mActivity, R.style.toolbar_text);
            //mToolbar.inflateMenu(R.menu.fragment_msg_list);
            //mToolbar.setOnMenuItemClickListener(this);

            ImageView addView = (ImageView) getView().findViewById(R.id.toolbar_add_view);
            addView.setRotation(45);
            //addView.setColorFilter(ContextCompat.getColor(mActivity, R.color.white));
            getView().findViewById(R.id.toolbar_add).setOnClickListener(this);
            getView().findViewById(R.id.toolbar_search).setOnClickListener(this);
        }
    }

    private void initMenu() {
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.pop_window, null);
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 这个有必要，否则在5.0上点不到它，否则5.0上点击外界菜单不被隐藏
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        contentView.findViewById(R.id.menu_item_1).setOnClickListener(this);
        contentView.findViewById(R.id.menu_item_2).setOnClickListener(this);
        contentView.findViewById(R.id.menu_item_3).setOnClickListener(this);
        contentView.findViewById(R.id.menu_item_4).setOnClickListener(this);
    }

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
//                mPopupMenu.inflate(R.menu.fragment_msg_list);
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

}
