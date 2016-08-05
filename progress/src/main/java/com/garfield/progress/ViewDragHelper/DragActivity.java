package com.garfield.progress.ViewDragHelper;

import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.garfield.progress.R;

public class DragActivity extends AppCompatActivity {

    private ViewDragHelper mViewDragHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);

        mViewDragHelper.create(this.g, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return false;
            }
        });
    }
}
