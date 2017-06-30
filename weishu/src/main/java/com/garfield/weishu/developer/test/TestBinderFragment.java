package com.garfield.weishu.developer.test;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.R;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

/**
 * Created by gaowei on 2017/6/21.
 */

public class TestBinderFragment extends AppBaseFragment {

    private ICompute mCompute;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            L.toast("connected");
            mCompute = ICompute.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mCompute = null;
        }
    };

    @Override
    protected String onGetToolbarTitle() {
        return "Binder";
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_test_binder;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCompute == null) {
                    Intent intent = new Intent("com.garfield.weishu.aidl");
                    intent.setPackage("com.garfield.weishu");
                    //Intent intent = new Intent(getContext(), ComputerService.class);
                    getContext().bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
                } else {
                    try {
                        int result = mCompute.add(1, 2);
                        L.toast("result: " + result);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }
}
