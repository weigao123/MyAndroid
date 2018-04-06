package com.garfield.study.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_BOOT = "create table if not exists book ("
            + "id integer primary key autoincrement,"     //primary key主键，autoincrement自增长
            + "author text,"          //文本类型
            + "price real,"            //浮点型
            + "pages integer)";     //整型

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
