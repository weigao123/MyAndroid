package com.garfiled.weixin2.base;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.garfield.baselib.fragmentation.SupportFragment;
import com.garfield.baselib.utils.SizeUtils;
import com.garfiled.weixin2.R;


/**
 * Created by gaowei3 on 2016/8/4.
 */
public class AppBaseFragment extends SupportFragment implements Toolbar.OnMenuItemClickListener {

    private Toolbar mToolbar;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 这里不能使用mActivity，因为这个Activity下有好多个toolbar
        mToolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        // NewsTabFragment没有toolbar
        if (mToolbar != null) {
            mToolbar.setTitle("微信");
            mToolbar.setTitleTextAppearance(mActivity, R.style.toolbar_text);
            mToolbar.inflateMenu(R.menu.fragment_msg_list);
            mToolbar.setOnMenuItemClickListener(this);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_list:
                View contentView = LayoutInflater.from(mActivity).inflate(R.layout.pop_window, null);
                final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                int xoff = SizeUtils.getScreenWidth(mActivity) - (int)getResources().getDimension(R.dimen.pop_menu_width) - SizeUtils.dp2px(mActivity, 10);
                // 这个有必要，否则在5.0上点不到它
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAsDropDown(mToolbar, xoff, 0);
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return false;
                    }
                });
                //final PopupMenu popupMenu = new PopupMenu(mActivity, mToolbar, Gravity.END);
                //popupMenu.inflate(R.menu.fragment_msg_list);
                //popupMenu.show();
        }
        return false;
    }
}
