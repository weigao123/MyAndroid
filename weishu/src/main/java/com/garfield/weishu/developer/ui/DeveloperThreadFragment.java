package com.garfield.weishu.developer.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.garfield.baselib.utils.array.ArrayUtils;
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

    private static final int THREAD_NUM = 5;

    private volatile boolean mStop = false;
    private final int[] mArray = ArrayUtils.getArray(THREAD_NUM * 5);
    private volatile int mArrayIndex;

    // 初始化实例变量时，就可以使用this了，先执行MyHandler构造器，再执行DeveloperThreadFragment构造器
    private Handler mHandler = new MyHandler(this);

    @Override
    protected String onGetToolbarTitle() {
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
        for (int i = 0; i < THREAD_NUM; i++) {
            new Thread(new MyRunnable(i)).start();
        }
    }

    private class MyRunnable implements Runnable {
        private int mThreadIndex;

        private MyRunnable(int threadIndex) {
            mThreadIndex = threadIndex;
        }

        @Override
        public void run() {
            /**
             * 用锁把这些封装起来，进行线程间的切换
             */
            synchronized (mArray) {
                /**
                 * 由wait可以同时都进入到锁内部，由synchronized同时只能有一个线程在内部活动
                 * 同时只有一个线程是在wait状态，睡一个就去醒一个
                 * 1个wait，1个active，3个等待锁
                 */
                while (mArrayIndex < mArray.length && !mStop) {
                    if (mArrayIndex % THREAD_NUM == mThreadIndex) {
                        Message.obtain(mHandler, mThreadIndex, mArrayIndex, 0).sendToTarget();
                        mArrayIndex++;
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    /**
                     * 先唤醒再去等待
                     */
                    mArray.notify();    // 被wait的线程可以继续执行
                    try {
                        mArray.wait();    // 释放锁，并等待，其他被锁卡住的线程可以执行
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                /**
                 * 线程结束，必须增加这个，唤醒wait的，否则退出时，总有一个线程无法被唤醒，无法中止
                 */
                mArray.notify();
            }
            //L.d("Thread dead: "+mThreadIndex);
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
        mStop = true;
    }
}
