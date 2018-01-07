package com.garfield.study.aidl;


/**
 * 这个类在本地和远程各一份
 */
public interface ICompute extends android.os.IInterface {

    /**
     * 服务端Binder本地实体
     * Stub继承了ICompute，所以间接实现了IInterface，因此有了行为
     */
    public static abstract class Stub extends android.os.Binder implements ICompute {
        private static final String DESCRIPTOR = "com.garfield.weishu.ICompute";

        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * 将服务端Binder对象转换成客户端需要的AIDL接口对象
         * 运行在客户端，客户端在连接成功后主动去调用，是静态方法
         * 参数是驱动返回的Binder/BinderProxy，多次绑定，这个IBinder是同一个实例
         * 1、本地进程参数是本地Binder实体即Stub，返回还是这个Binder实体Stub
         * 2、跨进程参数是BinderProxy代理对象，返回时再包装一层即返回Stub.Proxy(BinderProxy)
         * 3、都实现了IBinder接口
         * queryLocalInterface在Binder有值，在代理BinderProxy没有值
         */
        public static ICompute asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            // 返回Stub实体或者null
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof ICompute))) {
                return ((ICompute) iin);
            }
            return new Stub.Proxy(obj);
        }

        /**
         * 将AIDL接口对象转换成Binder对象，这种方式最安全
         * 1、如果是本地是就返回Stub.this
         * 2、如果是跨进程的，就通过Proxy的asBinder返回Stub.Proxy.mRemote
         */
        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        /**
         * 运行在服务端，由Binder驱动回调
         */
        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                case TRANSACTION_add: {
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0;
                    _arg0 = data.readInt();
                    int _arg1;
                    _arg1 = data.readInt();
                    /**
                     * 执行服务端本地方法
                     */
                    int _result = this.add(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case TRANSACTION_register: {
                    data.enforceInterface(DESCRIPTOR);
                    com.garfield.study.aidl.ICallback _arg0;
                    _arg0 = com.garfield.study.aidl.ICallback.Stub.asInterface(data.readStrongBinder());
                    this.register(_arg0);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        /**
         * 代理类实现了IInterface，因此同样有了行为
         * mRemote是远程Binder的代理类BinderProxy
         */
        private static class Proxy implements ICompute {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            /**
             * 代理方法
             */
            @Override
            public int add(int a, int b) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(a);
                    _data.writeInt(b);
                    /**
                     * 运行在客户端，调用服务端代理的transact方法
                     */
                    mRemote.transact(Stub.TRANSACTION_add, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @Override
            public void register(com.garfield.study.aidl.ICallback callback) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
                    mRemote.transact(Stub.TRANSACTION_register, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        static final int TRANSACTION_add = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_register = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    }

    public int add(int a, int b) throws android.os.RemoteException;
    public void register(com.garfield.study.aidl.ICallback callback) throws android.os.RemoteException;

}
