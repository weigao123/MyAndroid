package com.garfield.weishu.discovery.developer.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.garfield.baselib.utils.array.ArrayUtils;
import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.R;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2017/1/10.
 */

public class DeveloperThreadFragment extends AppBaseFragment {

    @BindView(R.id.fragment_developer_thread_origin)
    TextView mOriginText;

    @BindView(R.id.fragment_developer_thread_result)
    TextView mResultText;

    @BindView(R.id.fragment_developer_thread_scrollview)
    ScrollView mScrollView;

    private volatile boolean mStop = false;
    private final int[] mArray = ArrayUtils.getArray(50);
    private volatile int mArrayIndex;

    // 初始化实例变量时，就可以使用this了，先执行MyHandler构造器，再执行DeveloperThreadFragment构造器
    private Handler mHandler = new MyHandler(this);

    @Override
    protected String onGetToolbarTitleResource() {
        return "多线程";
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_developer_thread;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mOriginText.setText(Arrays.toString(mArray));
        new Thread(new MyRunnable(0)).start();
        new Thread(new MyRunnable(1)).start();
        new Thread(new MyRunnable(2)).start();
        new Thread(new MyRunnable(3)).start();
        new Thread(new MyRunnable(4)).start();
    }

    private class MyRunnable implements Runnable {
        private int mThreadIndex;

        private MyRunnable(int threadIndex) {
            mThreadIndex = threadIndex;
        }

        @Override
        public void run() {
            synchronized (mArray) {
                /**
                 * 可以都执行到内部，但是同时只能有一个线程在执行
                 * 画图纸，可以发现其实同时只有一个被等待唤醒
                 */
                while (mArrayIndex < mArray.length && !mStop) {
                    L.d("while: "+mThreadIndex);
                    if (mArrayIndex % 5 == mThreadIndex) {
                        Message.obtain(mHandler, mThreadIndex, mArrayIndex, 0).sendToTarget();
                        mArrayIndex++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    /**
                     * 必须得先唤醒再去等待
                     */
                    mArray.notify();    // 被wait的线程可以继续执行
                    try {
                        L.d("wait: "+mThreadIndex);
                        mArray.wait();    // 释放锁，并等待，其他被锁卡住的线程可以执行
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    L.d("wait go on: "+mThreadIndex);
                }
                /**
                 * 必须增加这个，否则退出线程时，其他线程无法被唤醒
                 */
                mArray.notify();
            }
            L.d("thread over: "+mThreadIndex);
        }
    }

    private static class MyHandler extends Handler {

        private WeakReference<DeveloperThreadFragment> mWeakReference;

        private MyHandler(DeveloperThreadFragment fragment) {
            mWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            TextView textView = mWeakReference.get().mResultText;
            textView.setText(textView.getText().toString() + "Thread:" + msg.what + " -> " + msg.arg1 + "\n");
            mWeakReference.get().mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.d("onDestroyView");
        mStop = true;
    }
}
