package com.garfield.study.provider;

import android.content.ContentValues;
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
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CursorWrapper cursor = (CursorWrapper) mActivity.getContentResolver().query(Uri.parse("content://com.test.provider/content"), null, null, null, null);
                if (cursor != null) {

                    cursor.registerContentObserver(new ContentObserver(null) {
                        @Override
                        public void onChange(boolean selfChange) {
                            super.onChange(selfChange);
                            L.d("onChange");
                        }

                        @Override
                        public boolean deliverSelfNotifications() {
                            return true;
                        }
                    });

                    //L.d("ProviderFragment: " + cursor.getWrappedCursor());

                    //cursor.close();
                }


                ContentValues values = new ContentValues();
                mActivity.getContentResolver().insert(Uri.parse("content://com.test.provider/content"), values);

            }
        });


    }

}
