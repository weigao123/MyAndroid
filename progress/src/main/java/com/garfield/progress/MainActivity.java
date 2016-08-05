package com.garfield.progress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.garfield.progress.ViewDragHelper.DragActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_viewdraghelper:
                startActivity(new Intent(this, DragActivity.class));
                break;
        }
    }
}
