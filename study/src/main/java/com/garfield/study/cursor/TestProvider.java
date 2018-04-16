package com.garfield.study.cursor;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garfield.baselib.utils.system.L;

public class TestProvider extends ContentProvider {

    public static final String BASE_URI_STRING = "content://com.test.provider/content";
    public static final Uri BASE_URI = Uri.parse(BASE_URI_STRING);

    private DBOpenHelper mDBOpenHelper;
    private Context mContext;

    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDBOpenHelper = new DBOpenHelper(mContext, "contact.db", null, 1);
        L.d("TestProvider: onCreate");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("book", projection, selection, selectionArgs, null, null, sortOrder);
        L.d("TestProvider: query: " + cursor);

        cursor.setNotificationUri(mContext.getContentResolver(), BASE_URI);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        //SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        //db.insert("book", null, values);
        L.d("TestProvider: insert");

        mContext.getContentResolver().notifyChange(Uri.parse("content://com.test.provider/content"), null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
