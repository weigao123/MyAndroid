package com.garfield.weishu.developer.test;


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
         * 运行在客户端，客户端在连接成功后主动去调用，是静态方法
         * 参数是驱动返回的远程Binder，如果是本地进程就返回Binder实体，跨进程就返回的是BinderProxy，在native实现
         * queryLocalInterface在Binder有值，在代理BinderProxy没有值
         */
        public static ICompute asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof ICompute))) {
                return ((ICompute) iin);
            }
            return new Stub.Proxy(obj);
        }

        /**
         * 目的是通过接口形式的引用返回自己的实例
         * IMessenger mTarget;
         * return mTarget.asBinder();
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
            }
            return super.onTransact(code, data, reply, flags);
        }

        /**
         * 代理类实现了IInterface，因此同样有了行为
         * 内部组合了远程Binder的代理类BinderProxy
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
        }

        static final int TRANSACTION_add = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    }

    public int add(int a, int b) throws android.os.RemoteException;
}
