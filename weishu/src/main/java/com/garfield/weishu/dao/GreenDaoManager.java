package com.garfield.weishu.dao;

import android.content.Context;

import com.garfield.weishu.dao.gen.DaoMaster;
import com.garfield.weishu.dao.gen.UserDao;

/**
 * Created by gaowei on 2017/6/12.
 */

public class GreenDaoManager {

    private static final String DATABASE = "GreenDaoDB";


    private static GreenDaoManager manager = null;
    private UserDao mUserDao;

    public static GreenDaoManager getInstance(Context context) {
        if (manager == null) {
            synchronized (GreenDaoManager.class) {
                if (manager == null) {
                    manager = new GreenDaoManager(context.getApplicationContext());
                }
            }
        }
        return manager;
    }

    private GreenDaoManager(Context context) {
        DaoMaster.OpenHelper openHelper = new DaoMaster.DevOpenHelper(context, DATABASE, null);
        DaoMaster daoMaster = new DaoMaster(openHelper.getWritableDatabase());
        mUserDao = daoMaster.newSession().getUserDao();

    }
}
