package com.garfield.weishu.developer.java;

import com.garfield.baselib.utils.system.L;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by gaowei3 on 2017/3/7.
 */

public class JavaTest {

    private ExecutorService mExecutor;
    private Future<String> mFuture;

    public void doTest() {
        mExecutor = Executors.newFixedThreadPool(10);
        mFuture = mExecutor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                L.d("start");
                Thread.sleep(1000);
                L.d(Thread.currentThread().getState());
                L.d("finish");
                return "ok";
            }
        });
        mExecutor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    L.d(mFuture.get());
                } catch (InterruptedException | CancellationException | ExecutionException e) {
                    L.d(e);
                } finally {
                    L.d("finally");
                }
                L.d("get over");
                return "ok";
            }
        });

        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    L.d(mFuture.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void touch() {
        mFuture.cancel(true);
    }


    public void destroy() {
        mExecutor.shutdown();
    }

}
