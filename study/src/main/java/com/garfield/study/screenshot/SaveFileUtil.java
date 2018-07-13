package com.garfield.study.screenshot;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by liqiang1 on 2017/8/31.
 */

public class SaveFileUtil {

    private static final String TAG = SaveFileUtil.class.getSimpleName();

    public static final String FILE_HEAD = "bbt-"; //保存文件名头
    private static MediaScannerConnection mMediaConnection;

    /**
     * 保存图片到图库
     *
     * @param path
     * @param context
     * @param bmp
     */
    public static boolean saveImageToGallery(String path, Context context, Bitmap bmp) {
        try {
            File appDir = new File(path);
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            long dateTaken = System.currentTimeMillis();
            String fileName = FILE_HEAD + System.currentTimeMillis() + ".jpg";
            Log.d(TAG, fileName);
            String finalFileName = insertImage(context.getContentResolver(), dateTaken, path, fileName, bmp);
            notifyGallery(context, path, finalFileName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 插入图库
     *
     * @param resolver
     * @param dateTaken
     * @param directory
     * @param fileName
     * @param source
     * @return
     */
    private static String insertImage(ContentResolver resolver, long dateTaken, String directory, String fileName, Bitmap source) {
        OutputStream outputStream = null;
        String finalFileName = "";
        try {
            File file = createFile(directory, fileName);
            finalFileName = file.getName();
            outputStream = new FileOutputStream(file);
            source.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            ContentValues values = new ContentValues(7);
            values.put(MediaStore.Images.Media.TITLE, finalFileName);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, finalFileName);
            values.put(MediaStore.Images.Media.DATE_TAKEN, dateTaken);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            source.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return finalFileName;
    }

    /**
     * 创建文件
     *
     * @param directory
     * @param filename
     * @return
     */
    private static File createFile(String directory, String filename) {
        try {
            File file = new File(directory, filename);
            if (file.createNewFile()) {
                return file;
            } else {
                String newFileName = FILE_HEAD + System.currentTimeMillis() + 1;
                createFile(directory, newFileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通知图库更新
     *
     * @param context
     * @param path
     * @param fileName
     */
    private static void notifyGallery(Context context, final String path, final String fileName) {
        //两种方式通知图库
        try {
            Log.d(TAG, path + fileName);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path + fileName))));

            mMediaConnection = new MediaScannerConnection(context, new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override
                public void onMediaScannerConnected() {
                    Log.d(TAG, "onMediaScannerConnected:" + path);
                    mMediaConnection.scanFile(path + fileName, "image/jpeg");
                }

                @Override
                public void onScanCompleted(String path, Uri uri) {
                    Log.d(TAG, "onScanCompleted:" + path);
                    mMediaConnection.disconnect();
                    mMediaConnection = null;
                }
            });
            mMediaConnection.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
