package com.garfield.weishu.helper.http.volley;

/**
 * Created by gaowei3 on 2016/4/13.
 * 包含两种获取数据的方式
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleyController {

    // 创建一个TAG，方便调试或Log
    private static final String TAG = "VolleyController";

    // 创建一个全局的请求队列
    private RequestQueue mRequestQueue;
    private ImageLoader imageLoader;
    private Context mContext;

    // 创建一个static ApplicationController对象，便于全局访问
    private static VolleyController mInstance;

    private VolleyController(Context context) {
        mContext = context;
    }

    /**
     * 以下为需要我们自己封装的添加请求取消请求等方法
     */

    // 用于返回一个VolleyController单例
    public static VolleyController getInstance(Context context) {
        if (mInstance == null) {
            synchronized(VolleyController.class)
            {
                if (mInstance == null) {
                    mInstance = new VolleyController(context);
                }
            }
        }
        return mInstance;
    }

    // 用于返回全局RequestQueue对象，如果为空则创建它
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null){
            synchronized(VolleyController.class)
            {
                if (mRequestQueue == null){
                    mRequestQueue = Volley.newRequestQueue(mContext);
                }
            }
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader(){
        getRequestQueue();
        //如果imageLoader为空则创建它，第二个参数代表处理图像缓存的类
        if(imageLoader == null){
            imageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache());
        }
        return imageLoader;
    }

    /**
     * 将Request对象添加进RequestQueue，由于Request有*StringRequest,JsonObjectResquest...
     * 等多种类型，所以需要用到*泛型。同时可将*tag作为可选参数以便标示出每一个不同请求
     */

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // 如果tag为空的话，就是用默认TAG
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    // 通过各Request对象的Tag属性取消请求
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    private static class BitmapLruCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

        //单位是KB
        public static int getDefaultLruCacheSize() {
            final int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
            final int cacheSize = maxMemory / 8;
            return cacheSize;
        }

        public BitmapLruCache(){
            this(getDefaultLruCacheSize());
        }

        public BitmapLruCache(int sizeInKiloBytes) {
            super(sizeInKiloBytes);
        }

        @Override
        public int sizeOf(String key, Bitmap value) {
            return value.getRowBytes()*value.getHeight() / 1024;
        }

        @Override
        public Bitmap getBitmap(String url) {
            return get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            put(url, bitmap);
        }
    }
}

