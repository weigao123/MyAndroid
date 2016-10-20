package com.garfield.study.socket;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gaowei3 on 2016/10/20.
 */

public class SocketManager {
    public static final int CONNECT = 1;
    public static final int DISCONNECT = 2;
    public static final int CONNECT_FAILED = 3;

    private Set<SocketStateObserver> mSocketStateObserver = new HashSet<>();
    private HashMap<Integer, Set<SocketContentObserver>> mObserver = new HashMap<>();
    private HandlerThread mSendThread = new HandlerThread("SendThread");
    private HandlerThread mReceiveThread = new HandlerThread("ReceiveThread");
    {
        mSendThread.start();
        mReceiveThread.start();
    }

    private Handler mSendHandler = new Handler(mSendThread.getLooper()) {
        public void handleMessage(Message msg) {
            client.getTransceiver().send(((Request)msg.obj).toJson());
        }
    };

    private Handler mReceiveHandler = new Handler(mReceiveThread.getLooper()) {
        public void handleMessage(Message msg) {
            Set<SocketContentObserver> observers = new HashSet<>();
            if (msg.what == 7) {
                Collection<Set<SocketContentObserver>> collection = mObserver.values();
                for (Set<SocketContentObserver> observerSet : collection) {
                    observers.addAll(observerSet);
                }
            } else {
                observers.addAll(mObserver.get(msg.what));
            }
            for (SocketContentObserver o : observers) {
                o.receive((Response) msg.obj);
            }
        }
    };

    public interface SocketContentObserver {
        void receive(Response content);
    }

    public interface SocketStateObserver {
        void socketStateChanged(int state);
    }

    public void registerSocketStateObserver(SocketStateObserver o) {
        if (o == null) return;
        mSocketStateObserver.add(o);
    }

    public void unRegisterSocketStateObserver(SocketStateObserver o) {
        if (o == null) return;
        mSocketStateObserver.remove(o);
    }

    public void unRegisterSocketContentObserver(Request request, SocketContentObserver observer) {
        if (request == null) return;
        Set<SocketContentObserver> observers = mObserver.get(request.getMsg_id());
        if (observers != null) {
            observers.remove(observer);
        }
    }

    public void sendRequest(Request request, SocketContentObserver observer) {
        if (request == null) return;
        Set<SocketContentObserver> observers = mObserver.get(request.getMsg_id());
        if (observers == null) {
            observers = new HashSet<>();
            mObserver.put(request.getMsg_id(), observers);
        }
        observers.add(observer);
        Message.obtain(mSendHandler, request.getMsg_id(), request).sendToTarget();
    }

    public void connect() {
        if (!client.isConnected()) {
            client.connect("192.168.42.1", 7878);
        }
    }

    public void disConnect() {
        if (client.isConnected()) {
            client.disconnect();
        }
    }

    private TcpClient client = new TcpClient() {

        @Override
        public void onConnect(SocketTransceiver transceiver) {
            for (SocketStateObserver o : mSocketStateObserver) {
                o.socketStateChanged(CONNECT);
            }
        }

        @Override
        public void onDisconnect(SocketTransceiver transceiver) {
            for (SocketStateObserver o : mSocketStateObserver) {
                o.socketStateChanged(DISCONNECT);
            }
        }

        @Override
        public void onConnectFailed() {
            for (SocketStateObserver o : mSocketStateObserver) {
                o.socketStateChanged(CONNECT_FAILED);
            }
        }

        @Override
        public void onReceive(SocketTransceiver transceiver, final String s) {
            Response response = new Response();
            Message.obtain(mReceiveHandler, response.getMsg_id(), response).sendToTarget();
        }
    };



    public static SocketManager getInstance() {
        if (socketManager == null) {
            socketManager = new SocketManager();
        }
        return socketManager;
    }

    private static SocketManager socketManager;

    private SocketManager() {

    }

}
