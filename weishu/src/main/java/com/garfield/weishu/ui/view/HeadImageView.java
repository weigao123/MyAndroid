package com.garfield.weishu.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;

import com.garfield.baselib.ui.widget.CircleImageView;
import com.garfield.weishu.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.nim.cache.UserInfoCache;
import com.netease.nimlib.sdk.nos.model.NosThumbParam;
import com.netease.nimlib.sdk.nos.util.NosThumbImageUtil;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.imageaware.NonViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by huangjun on 2015/11/13.
 */
public class HeadImageView extends CircleImageView {

    public static final int DEFAULT_AVATAR_THUMB_SIZE = (int) AppCache.getContext().getResources().getDimension(R.dimen.avatar_max_size);
    public static final int DEFAULT_AVATAR_NOTIFICATION_ICON_SIZE = (int) AppCache.getContext().getResources().getDimension(R.dimen.avatar_notification_size);

    private DisplayImageOptions options = createImageOptions();
    private static int defaultIcon = R.drawable.unsupport_type;

    private static final DisplayImageOptions createImageOptions() {

        return new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultIcon)
                .showImageOnFail(defaultIcon)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public HeadImageView(Context context) {
        super(context);
    }

    public HeadImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 加载用户头像（默认大小的缩略图）
     *
     * @param account
     */
    public void loadBuddyAvatar(String account) {
        loadBuddyAvatar(account, DEFAULT_AVATAR_THUMB_SIZE);
    }

    /**
     * 加载用户头像（原图）
     *
     * @param account
     */
    public void loadBuddyOriginalAvatar(String account) {
        loadBuddyAvatar(account, 0);
    }

    /**
     * 加载用户头像（指定缩略大小）
     *
     * @param account
     * @param thumbSize 缩略图的宽、高
     */
    private void loadBuddyAvatar(final String account, final int thumbSize) {
        // 先显示默认头像
        setImageResource(defaultIcon);

        // 判断是否需要ImageLoader加载
        final UserInfoProvider.UserInfo userInfo = UserInfoCache.getInstance().getUserInfoByAccount(account);
        boolean needLoad = userInfo != null && ImageLoaderKit.isImageUriValid(userInfo.getAvatar());

        doLoadImage(needLoad, account, userInfo != null ? userInfo.getAvatar() : null, thumbSize);
    }



    /**
     * ImageLoader异步加载
     */
    private void doLoadImage(final boolean needLoad, final String tag, final String url, final int thumbSize) {
        if (needLoad) {
            setTag(tag); // 解决ViewHolder复用问题
            /**
             * 若使用网易云信云存储，这里可以设置下载图片的压缩尺寸，生成下载URL
             * 如果图片来源是非网易云信云存储，请不要使用NosThumbImageUtil
             */
            final String thumbUrl = makeAvatarThumbNosUrl(url, thumbSize);

            // 异步从cache or NOS加载图片
            ImageLoader.getInstance().displayImage(thumbUrl, new NonViewAware(new ImageSize(thumbSize, thumbSize),
                    ViewScaleType.CROP), options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (getTag() != null && getTag().equals(tag)) {
                        setImageBitmap(loadedImage);
                    }
                }
            });
        } else {
            setTag(null);
        }
    }

    /**
     * 解决ViewHolder复用问题
     */
    public void resetImageView() {
        setImageBitmap(null);
    }

    /**
     * 生成头像缩略图NOS URL地址（用作ImageLoader缓存的key）
     */
    private static String makeAvatarThumbNosUrl(final String url, final int thumbSize) {
        return thumbSize > 0 ? NosThumbImageUtil.makeImageThumbUrl(url, NosThumbParam.ThumbType.Crop, thumbSize, thumbSize) : url;
    }

    public static String getAvatarCacheKey(final String url) {
        return makeAvatarThumbNosUrl(url, DEFAULT_AVATAR_THUMB_SIZE);
    }
}
