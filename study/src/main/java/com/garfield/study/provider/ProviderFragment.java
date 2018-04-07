package com.garfield.study.provider;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.CursorWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.garfield.baselib.utils.system.L;
import com.garfield.study.app.AppBaseFragment;

/**
 * Created by gaowei on 2017/7/10.
 */

public class ProviderFragment extends AppBaseFragment {

    private ContentResolver mContentResolver;

    @Override
    protected String onGetToolbarTitle() {
        return "Provider";
    }

    @Override
    protected int onGetFragmentLayout() {
        return 0;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        mContentResolver = mActivity.getContentResolver();

        final ContentObserver contentObserver = new ContentObserver(null) {
            @Override
            public boolean deliverSelfNotifications() {
                return true;
            }

            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                L.d("onChange");
            }
        };
        mContentResolver.registerContentObserver(Uri.parse("content://com.test.provider1/c"), false, contentObserver);


        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CursorWrapper cursor = (CursorWrapper) mActivity.getContentResolver().query(Uri.parse("content://com.test.provider/content"), null, null, null, null);
                L.d(cursor);
//                if (cursor != null) {
//
//                    cursor.setNotificationUri(getContext().getContentResolver(), Uri.parse("content://com.test.provider/content"));
//                    cursor.registerContentObserver(new ContentObserver(null) {
//                        @Override
//                        public void onChange(boolean selfChange) {
//                            super.onChange(selfChange);
//                            //L.d("onChange");
//                        }
//
//                        @Override
//                        public boolean deliverSelfNotifications() {
//                            return true;
//                        }
//                    });
//                }
//                ContentValues values = new ContentValues();
//                mContentResolver.insert(Uri.parse("content://com.test.provider/content"), values);




                    mContentResolver.notifyChange(Uri.parse("content://com.test.provider1/c"), contentObserver);
            }
        });


    }

}
