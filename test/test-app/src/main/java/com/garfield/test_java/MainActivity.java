package com.garfield.test_java;

import android.Manifest;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.garfield.test_app.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);


        //requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
    }
}
