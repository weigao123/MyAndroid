package com.garfield.baselib.utils.http.image;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.garfield.baselib.R;
import com.garfield.baselib.utils.file.DirectoryUtils;
import com.garfield.baselib.utils.system.L;

import java.io.File;

/**
 * Created by gaowei3 on 2017/1/17.
 */

public class GlideHelper {

    public static void load(Activity activity, String url, ImageView imageView) {
        Glide.with(activity)
             .load(url)
             .diskCacheStrategy(DiskCacheStrategy.RESULT)
             .placeholder(R.drawable.image_default)
             .error(R.drawable.image_default)
             //.animate(R.anim.item_alpha_in)     //加载动画
             .into(imageView);
    }

    public static void load(Fragment fragment, String url, ImageView imageView) {
        Glide.with(fragment)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
                //.animate(R.anim.item_alpha_in)     //加载动画
                .into(imageView);
    }

    public static void load(Context context, String url, ImageView imageView) {
        L.d(((ContextWrapper)context).getBaseContext());
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
                //.animate(R.anim.item_alpha_in)     //加载动画
                .into(imageView);
    }

    public static class MyGlideModule implements GlideModule {

        @Override
        public void applyOptions(Context context, GlideBuilder builder) {
            /**
             * 内存缓存
             */
            MemorySizeCalculator calculator = new MemorySizeCalculator(context);
            int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
            int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
            builder.setMemoryCache(new LruResourceCache(defaultMemoryCacheSize));
            builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize));

            /**
             * 硬盘缓存
             */
            File cacheDir = DirectoryUtils.getOwnCacheDirectory("/cache/image");
            int diskCacheSize = 1024 * 1024 * 30;
            builder.setDiskCache(new DiskLruCacheFactory(cacheDir.getPath(), "Glide", diskCacheSize));
            //存放在内部存储 data/data/package/cache/
            //builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "Glide", diskCacheSize));
            //存放在外置存储
            //builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "Glide", diskCacheSize));

            /**
             * 图片解码
             */
            builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        }

        @Override
        public void registerComponents(Context context, Glide glide) {

        }
    }

}
