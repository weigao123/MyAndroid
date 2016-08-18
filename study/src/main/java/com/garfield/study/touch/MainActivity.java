package com.garfield.study.touch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import com.garfield.baselib.utils.L;
import com.garfield.study.R;
import com.garfield.study.touch.view.OneView;

/**
 * Created by gaowei3 on 2016/8/16.
 */
public class MainActivity extends AppCompatActivity {

    private OneView mOneView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touch_activity_main);
        mOneView = (OneView) findViewById(R.id.touch_one_view);

        mOneView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                L.d("MainActivity onDrag");
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //L.d("MainActivity onTouchEvent: "+event.getActionMasked());
        return super.onTouchEvent(event);
    }


}
