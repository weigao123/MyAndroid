package com.garfield.study.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.garfield.baselib.utils.system.L;

/**
 * Created by gaowei on 2017/6/21.
 */

public class ComputerService extends Service {

    private Binder mBinder = new ICompute.Stub() {

        @Override
        public int add(int a, int b) throws RemoteException {
            L.d("a + b");
            return a + b;
        }

        @Override
        public void register(ICallback callback) throws RemoteException {
            L.d("register: " + callback);
            IBinder binder = callback.asBinder();
            L.d("ComputerService callbackBinder:" + binder);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        L.d("ComputerService mBinder:" + mBinder);
        return mBinder;
    }
}
