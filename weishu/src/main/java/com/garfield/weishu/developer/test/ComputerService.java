package com.garfield.weishu.developer.test;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by gaowei on 2017/6/21.
 */

public class ComputerService extends Service {

    private Binder mBinder = new ICompute.Stub() {

        @Override
        public int add(int a, int b) throws RemoteException {
            return a + b;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
