package com.garfield.weishu.discovery.developer.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
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

    private final int[] mArray = ArrayUtils.getArray(10);
    private int mIndex;
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
        new Thread(mRunnable1).start();
        new Thread(mRunnable2).start();
    }

    private Runnable mRunnable1 = new Runnable() {
        @Override
        public void run() {
            synchronized (mArray) {
                while (mIndex < 10) {
                    mHandler.sendEmptyMessage(mIndex);
                    mIndex++;
                    mArray.notify();
                    try {
                        Thread.sleep(500);
                        mArray.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private Runnable mRunnable2 = new Runnable() {
        @Override
        public void run() {
            synchronized (mArray) {
                while (mIndex < 10) {
                    mHandler.sendEmptyMessage(mIndex);
                    mIndex++;
                    mArray.notify();
                    try {
                        Thread.sleep(500);
                        mArray.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };


    private static class MyHandler extends Handler {

        private WeakReference<DeveloperThreadFragment> mWeakReference;

        private MyHandler(DeveloperThreadFragment fragment) {
            mWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            mWeakReference.get().mResultText.setText("" + msg.what);
        }
    }


}
