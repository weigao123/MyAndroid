package com.garfield.baselib.utils;

import android.content.Context;
import android.graphics.Bitmap;

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
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by gaowei3 on 2016/10/20.
 */

public class ImageLoaderUtils {

    private static DisplayImageOptions mDefaultDisplayImageOptions;

    public static void initImageLoader(Context context) {
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//                .defaultDisplayImageOptions(getDisplayImageOptions())
//                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
//                .memoryCacheSize(2 * 1024 * 1024)
//                .diskCacheSize(30 * 1024 * 1024)
//                .diskCacheFileCount(100)
//                .build();
//        ImageLoader.getInstance().init(config);

//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
////					.threadPoolSize(3)
//                .memoryCacheExtraOptions(280, 280)
////					.discCacheExtraOptions(280, 280, CompressFormat.JPEG, 75, null)
//                .threadPriority(Thread.NORM_PRIORITY)
//                .denyCacheImageMultipleSizesInMemory()
////					.discCacheFileNameGenerator(new Md5FileNameGenerator())
////					.discCache(new LimitedAgeDiscCache(StorageUtils.getCacheDirectory(context),new Md5FileNameGenerator(), discCacheLimitTime))
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .build();
//        ImageLoader.getInstance().init(config);


        int MAX_CACHE_MEMORY_SIZE = (int) (Runtime.getRuntime().maxMemory() / 8);
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, context.getPackageName() + "/cache/image/");


        ImageLoaderConfiguration config = null;
        try {
            config = new ImageLoaderConfiguration
                    .Builder(context)
                    .threadPoolSize(3) // 线程池内加载的数量
                    .threadPriority(Thread.NORM_PRIORITY - 2) // 降低线程的优先级，减小对UI主线程的影响
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new LruMemoryCache(MAX_CACHE_MEMORY_SIZE))
                    .discCache(new LruDiskCache(cacheDir, new Md5FileNameGenerator(), 0))
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                    .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                    .writeDebugLogs()
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        L.d((config == null) + "");
        ImageLoader.getInstance().init(config);

    }

    public static DisplayImageOptions getDisplayImageOptions() {
        if (mDefaultDisplayImageOptions == null) {
            mDefaultDisplayImageOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.unsupport_type)
                    .showImageForEmptyUri(R.drawable.unsupport_type)
                    .showImageOnFail(R.drawable.unsupport_type)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                    .bitmapConfig(Bitmap.Config.RGB_565) // default
                    .displayer(new FadeInBitmapDisplayer(300))
                    .build();
        }
        return mDefaultDisplayImageOptions;
    }
}
