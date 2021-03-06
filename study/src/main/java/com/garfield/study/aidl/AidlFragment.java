package com.garfield.study.aidl;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import com.garfield.baselib.utils.system.L;
import com.garfield.study.app.AppBaseFragment;

/**
 * Created by gaowei on 2017/7/10.
 */

public class AidlFragment extends AppBaseFragment {

    private ICompute mCompute;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mCompute = ICompute.Stub.asInterface(service);
            L.d("AidlFragment connected [IBinder]:" + service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mCompute = null;
        }
    };

    private ICallback mCallback = new ICallback.Stub() {
        @Override
        public void callback(String result) throws RemoteException {
            L.toast("callback: " + result);
        }
    };

    @Override
    protected String onGetToolbarTitle() {
        return "Binder";
    }

    @Override
    protected int onGetFragmentLayout() {
        return 0;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCompute == null) {
                    Intent intent = new Intent("com.garfield.study.aidl");
                    intent.setPackage("com.garfield.study");
                    //Intent intent = new Intent(getContext(), ComputerService.class);
                    getContext().bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
                } else {
                    try {
                        int result = mCompute.add(1, 2);
                        L.toast("result: " + result);
                        mCompute.register(mCallback);
                        mCompute.register(mCallback);


                        Intent intent = new Intent("com.garfield.study.broadcast");
                        Bundle bundle = new Bundle();
                        bundle.putBinder("binder", mCallback.asBinder());
                        intent.putExtras(bundle);
                        getContext().sendBroadcast(intent);

                        intent = new Intent("com.garfield.study.broadcast");
                        bundle = new Bundle();
                        bundle.putBinder("binder", mCallback.asBinder());
                        intent.putExtras(bundle);
                        getContext().sendBroadcast(intent);

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

}
