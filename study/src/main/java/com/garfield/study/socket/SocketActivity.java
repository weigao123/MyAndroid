package com.garfield.study.socket;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.garfield.baselib.utils.L;
import com.garfield.study.R;
import com.garfield.study.socket.SocketManager;

/**
 * Created by gaowei3 on 2016/10/20.
 */

public class SocketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        new Runnable() {
            @Override
            public void run() {
                while (true) {

                    L.d("run: "+Thread.currentThread().getName());
                }
            }
        }.run();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.socket_connect:
                SocketManager.getInstance().connect();
                break;
            case R.id.socket_session:
                //SocketManager.getInstance().sendRequest("{\"msg_id\" : 257,\"token\" : 0}", null);
                break;
        }
    }

}
