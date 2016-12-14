package com.garfield.baselib.utils.system;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by gaowei3 on 2016/12/13.
 */

public class ThreadUtils {

    public static Thread executeInThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }


    public static class Invoker extends Thread {
        private Callback callback;
        public Invoker(Callback callback) {
            this.callback = callback;
        }
        @Override
        public synchronized void start() {
            callback.onBefore();
            super.start();
        }
        @Override
        public void run() {
            final boolean b = callback.doInBackground();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    callback.onAfter(b);
                }
            });
        }
    }

    public interface Callback {
        void onBefore();
        boolean doInBackground();
        void onAfter(boolean b);
    }
}
