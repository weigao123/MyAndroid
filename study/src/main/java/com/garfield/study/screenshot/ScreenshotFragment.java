package com.garfield.study.screenshot;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;

import com.garfield.baselib.utils.system.L;
import com.garfield.study.app.AppBaseFragment;
import com.garfield.study.cursor.TestProvider;

/**
 * Created by gaowei on 2017/7/10.
 */

public class ScreenshotFragment extends AppBaseFragment {

    @Override
    protected String onGetToolbarTitle() {
        return "Screenshot";
    }

    @Override
    protected int onGetFragmentLayout() {
        return 0;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ScreenshotDetector.getInstance(mActivity).onGotoForeground(mActivity);
    }

    @Override
    public void onStop() {
        super.onStop();
        ScreenshotDetector.getInstance(mActivity).onGotoBackground();
    }
}
