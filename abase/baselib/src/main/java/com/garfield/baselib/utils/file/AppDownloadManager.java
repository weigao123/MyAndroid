package com.garfield.baselib.utils.file;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
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
        //指定网络下才能下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //通知栏
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(title);
        request.setDescription(String.format("%s正在下载，请稍等...", title));
        //下载路径
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

//    //注册广播接收者，监听下载状态
//    mContext.registerReceiver(receiver,
//            new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    //下载到本地后执行安装
    protected void installAPK(Context context, File file) {
        if (!file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://" + file.toString());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        //在服务中开启activity必须设置flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
