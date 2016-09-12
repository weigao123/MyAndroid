package com.garfield.weishu.nim.framework;

import android.os.Handler;

import com.garfield.weishu.AppCache;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by huangjun on 2015/3/12.
 */
public class SingleThreadExecutor {

    private static SingleThreadExecutor instance;

    private Handler uiHander;
    private Executor executor;

    private SingleThreadExecutor() {
        uiHander = new Handler(AppCache.getContext().getMainLooper());
        executor = Executors.newSingleThreadExecutor();
    }

    public synchronized static SingleThreadExecutor getInstance() {
        if (instance == null) {
            instance = new SingleThreadExecutor();
        }

        return instance;
    }

    public <T> void execute(NimTask<T> task) {
        if (executor != null) {
            executor.execute(new NimRunnable<>(task));
        }
    }

    public void execute(Runnable runnable) {
        if (executor != null) {
            executor.execute(runnable);
        }
    }

    /**
     * ****************** model *************************
     */

    public interface NimTask<T> {
        T runInBackground();

        void onCompleted(T result);
    }

    private class NimRunnable<T> implements Runnable {

        public NimRunnable(NimTask<T> task) {
            this.task = task;
        }

        private NimTask<T> task;

        @Override
        public void run() {
            final T res = task.runInBackground();
            if (uiHander != null) {
                uiHander.post(new Runnable() {
                    @Override
                    public void run() {
                        task.onCompleted(res);
                    }
                });
            }
        }
    }
}
