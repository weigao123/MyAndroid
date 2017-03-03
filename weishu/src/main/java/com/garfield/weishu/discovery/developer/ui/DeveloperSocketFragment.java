package com.garfield.weishu.discovery.developer.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by gaowei3 on 2017/3/2.
 */

public class DeveloperSocketFragment extends AppBaseFragment {

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);


        // 监听本地2016端口
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(2016);
            serverSocket.setSoTimeout(60 * 1000);
            Socket socket = serverSocket.accept();
            // 连接成功
            InetAddress inetAddress = socket.getInetAddress();
            String ip = inetAddress.getHostAddress();
            // 读
            InputStream inputStream = socket.getInputStream();
            byte buffer[] = new byte[1024];
            int temp;
            while ((temp = inputStream.read(buffer)) != -1) {
                Log.i("tcp socket", "result: "+ new String(buffer, 0, temp));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void onn(){}

    private static class MyHandler extends Handler {
        DeveloperSocketFragment mFragment;
        MyHandler(DeveloperSocketFragment fragment) {
            mFragment = fragment;

            mFragment.onn();
        }

    }
}
