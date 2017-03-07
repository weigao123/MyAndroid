package com.garfield.weishu.developer.music.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by gaowei3 on 2017/2/16.
 */

public class MusicStateService extends Service {

    public class MyBinder extends Binder {
        public MusicStateService getService() {
            return MusicStateService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }






}
