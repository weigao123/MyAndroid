package com.garfield.study.touch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.garfield.baselib.utils.L;
import com.garfield.study.R;

/**
 * Created by gaowei3 on 2016/8/16.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touch_activity_main);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        L.d("MainActivity onTouchEvent");
        return super.onTouchEvent(event);
    }
}
