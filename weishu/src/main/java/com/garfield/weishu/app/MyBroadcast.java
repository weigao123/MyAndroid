package com.garfield.weishu.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.garfield.baselib.utils.system.L;

/**
 * Created by gaowei on 17/2/12.
 */

public class MyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        L.d("MyBroadcast: "+intent.getAction());
    }
}
