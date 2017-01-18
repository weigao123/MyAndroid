package com.garfield.baselib.utils.http.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.garfield.baselib.Cache;
import com.garfield.baselib.R;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaowei3 on 2016/10/20.
 */

public class ImageLoaderHelper {

    private static DisplayImageOptions mDisplayImageOptions;
    private static DisplayImageOptions mDisplayImageNoDiskCacheOptions;

    public static void init() {
        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        File cacheDir = StorageUtils.getOwnCacheDirectory(Cache.getContext(), Cache.getContext().getPackageName() + "/cache/image/ImageLoader");

        try {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(Cache.getContext())
                    .threadPoolSize(3) // 线程池内加载的数量
                    .threadPriority(Thread.NORM_PRIORITY - 2) // 降低线程的优先级，减小对UI主线程的影响
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new LruMemoryCache(memoryCacheSize))
                    .diskCache(new LruDiskCache(cacheDir, new Md5FileNameGenerator(), 0))
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())   //默认配置
                    .imageDownloader(new BaseImageDownloader(Cache.getContext(), 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                    .build();
            ImageLoader.getInstance().init(config);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void clear() {
        ImageLoader.getInstance().clearMemoryCache();
    }

    public static DisplayImageOptions getDisplayImageOptions(int image) {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(image)
                .showImageForEmptyUri(image)
                .showImageOnFail(image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.RGB_565)
                //.displayer(new FadeInBitmapDisplayer(500))
                .build();
    }

    public static DisplayImageOptions getDisplayImageOptions() {
        if (mDisplayImageOptions == null) {
            mDisplayImageOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.image_default)
                    .showImageForEmptyUri(R.drawable.image_default)
                    .showImageOnFail(R.drawable.image_default)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    //.displayer(new FadeInBitmapDisplayer(500))
                    .build();
        }
        return mDisplayImageOptions;
    }

    public static DisplayImageOptions getDisplayImageNoDiskCacheOptions() {
        if (mDisplayImageNoDiskCacheOptions == null) {
            mDisplayImageNoDiskCacheOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.image_default)
                    .showImageForEmptyUri(R.drawable.image_default)
                    .showImageOnFail(R.drawable.image_default)
                    .cacheInMemory(true)
                    .cacheOnDisk(false)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    //.displayer(new FadeInBitmapDisplayer(500))
                    .build();
        }
        return mDisplayImageNoDiskCacheOptions;
    }



}
