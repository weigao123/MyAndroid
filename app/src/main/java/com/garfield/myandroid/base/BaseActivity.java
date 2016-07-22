package com.garfield.myandroid.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

/**
 * Created by gaowei3 on 2016/7/18.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "网易新闻");
        menu.add(0, 1, 0, "Liveman");
        return super.onCreateOptionsMenu(menu);
    }
}
