package com.garfield.baselib.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by gaowei3 on 2016/7/31.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (onGetActivityLayout() != 0) {
            setContentView(onGetActivityLayout());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected int onGetActivityLayout() {
        return 0;
    }
}
