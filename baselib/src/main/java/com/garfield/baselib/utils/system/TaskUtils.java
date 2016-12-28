package com.garfield.baselib.utils.system;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

/**
 * Created by gaowei3 on 2016/12/13.
 */

public class TaskUtils {

    private static Handler mHandler;
    private static HandlerThread mHandlerThread;

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

    public static HandlerThread executeCycle(final Runnable runnable, final int time) {
        if (mHandlerThread != null && mHandlerThread.isAlive()) {
            mHandlerThread.quit();
        }
        mHandlerThread = new HandlerThread("task_utils");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                mHandler.post(runnable);
                mHandler.sendEmptyMessageDelayed(0, time);
            }
        };
        mHandler.sendEmptyMessage(0);
        return mHandlerThread;
    }
}
