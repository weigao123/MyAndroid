package com.garfield.study.aidl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.garfield.baselib.utils.system.L;

/**
 * Created by gaowei on 2018/2/23.
 */

public class BinderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            IBinder binder = bundle.getBinder("binder");
            L.d("BinderReceiver [IBinder]:" + binder);
        }
    }
}
