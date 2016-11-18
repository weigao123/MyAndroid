package com.garfield.weishu.ui.view;

import android.graphics.Bitmap;

import com.garfield.weishu.nim.cache.UserInfoCache;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.util.List;

import static com.garfield.baselib.utils.drawable.ImageLoaderUtils.isImageUriValid;

/**
 * 图片加载、缓存、管理组件
 */
public class ImageLoaderKit {



    /**
     * 从ImageLoader内存缓存中取出头像位图
     */
    private static Bitmap getMemoryCachedAvatarBitmap(UserInfoProvider.UserInfo userInfo) {
        if(userInfo == null || !isImageUriValid(userInfo.getAvatar())) {
            return null;
        }

        String key = HeadImageView.getAvatarCacheKey(userInfo.getAvatar());

        // DiskCacheUtils.findInCache(uri, ImageLoader.getInstance().getDiskCache() 查询磁盘缓存示例
        List<Bitmap> bitmaps = MemoryCacheUtils.findCachedBitmapsForImageUri(key, ImageLoader.getInstance().getMemoryCache());
        if(bitmaps.size() > 0) {
            return bitmaps.get(0);
        }

        return null;
    }

    /**
     * 异步加载头像位图到ImageLoader内存缓存
     */
    private static void asyncLoadAvatarBitmapToCache(UserInfoProvider.UserInfo userInfo) {
        if(userInfo == null || !isImageUriValid(userInfo.getAvatar())) {
            return;
        }

        String url = HeadImageView.getAvatarCacheKey(userInfo.getAvatar());
        ImageLoader.getInstance().loadImage(url,
                new ImageSize(HeadImageView.DEFAULT_AVATAR_THUMB_SIZE, HeadImageView.DEFAULT_AVATAR_THUMB_SIZE),
                avatarLoadOption, null);

    }

    /**
     * 获取通知栏提醒所需的头像位图，只存内存缓存中取，如果没有则返回空，自动发起异步加载
     */
    public static Bitmap getNotificationBitmapFromCache(UserInfoProvider.UserInfo userInfo) {
        Bitmap cachedBitmap = getMemoryCachedAvatarBitmap(userInfo);
        if(cachedBitmap == null) {
            asyncLoadAvatarBitmapToCache(userInfo);
        } else {
            return BitmapUtil.resizeBitmap(cachedBitmap,
                    HeadImageView.DEFAULT_AVATAR_NOTIFICATION_ICON_SIZE,
                    HeadImageView.DEFAULT_AVATAR_NOTIFICATION_ICON_SIZE);
        }

        return null;
    }

    /**
     * 构建头像缓存
     */
    public static void buildAvatarCache(List<String> accounts) {
        if (accounts == null || accounts.isEmpty()) {
            return;
        }

        UserInfoProvider.UserInfo userInfo;
        for (String account : accounts) {
            userInfo = UserInfoCache.getInstance().getUserInfoByAccount(account);
            asyncLoadAvatarBitmapToCache(userInfo);
        }
    }

    /**
     * 头像ImageLoader加载配置
     */
    private static DisplayImageOptions avatarLoadOption = createImageOptions();

    private static final DisplayImageOptions createImageOptions() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }
}
