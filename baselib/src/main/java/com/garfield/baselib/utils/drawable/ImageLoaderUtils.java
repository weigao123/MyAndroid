package com.garfield.baselib.utils.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

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

public class ImageLoaderUtils {

    private static DisplayImageOptions mDisplayImageOptions;
    private static DisplayImageOptions mDisplayImageNoDiskCacheOptions;

    public static void initImageLoader(Context context) {
        int MAX_CACHE_MEMORY_SIZE = (int) (Runtime.getRuntime().maxMemory() / 8);
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, context.getPackageName() + "/cache/image/");

        ImageLoaderConfiguration config = null;
        try {
            config = new ImageLoaderConfiguration.Builder(context)
                    .threadPoolSize(3) // 线程池内加载的数量
                    .threadPriority(Thread.NORM_PRIORITY - 2) // 降低线程的优先级，减小对UI主线程的影响
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new LruMemoryCache(MAX_CACHE_MEMORY_SIZE))
                    .diskCache(new LruDiskCache(cacheDir, new Md5FileNameGenerator(), 0))
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                    .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageLoader.getInstance().init(config);
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

    /**
     * 判断图片地址是否合法，合法地址如下：
     * String uri = "http://site.com/image.png"; // from Web
     * String uri = "file:///mnt/sdcard/image.png"; // from SD card
     * String uri = "content://media/external/audio/albumart/13"; // from content provider
     * String uri = "assets://image.png"; // from assets
     * String uri = "drawable://" + R.drawable.image; // from drawables (only images, non-9patch)
     */
    private static List<String> uriSchemes;
    public static boolean isImageUriValid(String uri) {
        if (TextUtils.isEmpty(uri)) {
            return false;
        }

        if (uriSchemes == null) {
            uriSchemes = new ArrayList<>();
            for (ImageDownloader.Scheme scheme : ImageDownloader.Scheme.values()) {
                uriSchemes.add(scheme.name().toLowerCase());
            }
        }

        for (String scheme : uriSchemes) {
            if (uri.toLowerCase().startsWith(scheme)) {
                return true;
            }
        }

        return false;
    }

}
