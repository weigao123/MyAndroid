package com.garfield.weishu.discovery.developer.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
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

    private volatile boolean mStop = false;
    private final int[] mArray = ArrayUtils.getArray(18);
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
    }

    private class MyRunnable implements Runnable {
        private int mThreadIndex;

        private MyRunnable(int threadIndex) {
            mThreadIndex = threadIndex;
        }

        @Override
        public void run() {
            synchronized (mArray) {
                while (mArrayIndex < mArray.length && !mStop) {
                    if (mArrayIndex % 3 == mThreadIndex) {
                        Message.obtain(mHandler, mThreadIndex, mArrayIndex, 0).sendToTarget();
                        mArrayIndex++;
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mArray.notify();
                    try {
                        mArray.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
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
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mStop = true;
    }
}
