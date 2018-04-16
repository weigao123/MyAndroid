package com.garfield.study.cursor;

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

/**
 * Created by gaowei on 2017/7/10.
 */

public class CursorFragment extends AppBaseFragment {

    private ContentResolver contentResolver;

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
        testCursor();
        //testLoader();

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
    }

    private void insertData() {
        ContentValues values = new ContentValues();
        contentResolver.insert(TestProvider.BASE_URI, values);
    }

    private void testCursor() {
        contentResolver = mActivity.getContentResolver();
        CursorWrapper cursor = (CursorWrapper) contentResolver.query(
                TestProvider.BASE_URI,
                null,
                null,
                null,
                null);
        if (cursor != null) {
            //cursor.setNotificationUri(getContext().getContentResolver(), Uri.parse("content://com.test.provider/content"));
            cursor.registerContentObserver(new ContentObserver(null) {
                @Override
                public void onChange(boolean selfChange) {
                    super.onChange(selfChange);
                    L.d("cursor onChange");
                }

                @Override
                public boolean deliverSelfNotifications() {
                    return true;
                }
            });
        }

    }

    private void testLoader() {
        getLoaderManager().initLoader(1, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Uri CONTACT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                return new CursorLoader(mActivity, CONTACT_URI, null,
                        null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        });
    }

}
