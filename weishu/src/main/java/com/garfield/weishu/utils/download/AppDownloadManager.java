package com.garfield.weishu.utils.download;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by raysun on 16/1/28.
 */
public class AppDownloadManager {

    private static AppDownloadManager instance;
    private Context context;
    private DownloadManager manager;
    private Map<Long, String> mSubPathContainer = new HashMap<>();

    public static AppDownloadManager newInstance(Context context) {
        if (instance == null) {
            instance = new AppDownloadManager(context.getApplicationContext());
        }
        return instance;
    }

    private AppDownloadManager(Context context) {
        this.context = context;
        manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public long download(String url, String title) {
        if (TextUtils.isEmpty(url)) return -1;

        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (file.exists() && file.isDirectory()) {
            if (file.listFiles().length > 0) {
                File[] files = file.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        return filename.contains("firmware.C1.bin") || filename.contains("firmware.M1.bin");
                    }
                });
                if (files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].exists()) {
                            files[i].delete();
                        }
                    }
                }
            }
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(title);
        request.setDescription(String.format("%s正在下载，请稍等...", title));
        //request.setDestinationUri(Uri.fromFile(new File(subPath)));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);
        long id = manager.enqueue(request);
        mSubPathContainer.put(id, title);
        return id;
    }

    public long cancel(long id) {
        return manager.remove(id);
    }

    public void release() {
        mSubPathContainer.clear();
    }

    public String getDownloadFilePath(long id) {
        if (mSubPathContainer.containsKey(id)) {
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            return file.getAbsolutePath() + mSubPathContainer.get(id);
        }
        return "";
    }

}
