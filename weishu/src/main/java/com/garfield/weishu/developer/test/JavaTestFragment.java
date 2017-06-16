package com.garfield.weishu.developer.test;

import android.os.Bundle;
import android.view.View;

import com.garfield.weishu.R;
import com.garfield.weishu.developer.java.JavaTest;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

/**
 * Created by gaowei on 2017/6/16.
 */

public class JavaTestFragment extends AppBaseFragment {

    private JavaTest mJavaTest;

    @Override
    protected String onGetToolbarTitle() {
        return "TestJava";
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_java_test;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mJavaTest = new JavaTest();
        mJavaTest.doTest();
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJavaTest.touch();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mJavaTest.destroy();
    }
}
