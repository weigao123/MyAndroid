package com.garfield.baselib.utils.drawable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;

import com.garfield.baselib.Cache;
import com.garfield.baselib.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PhotoUtil {

    public static class AlbumInfo {
        public String albumName;
        public String albumImage;
        public List<String> photoPaths;
        public AlbumInfo() {
            photoPaths = new ArrayList<>();
        }
    }
    public static HashMap<String, AlbumInfo> getGalleryPhotos() {
        HashMap<String, AlbumInfo> galleryList = new HashMap<>();
        final Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = null;
        try {
            cursor = Cache.getContext().getContentResolver().query(images, null, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int _id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    String albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                    if (!galleryList.containsKey(albumName)) {
                        AlbumInfo albumInfo = new AlbumInfo();
                        albumInfo.albumName = albumName;
                        albumInfo.albumImage = path;
                        albumInfo.photoPaths.add(path);
                        galleryList.put(albumName, albumInfo);
                    } else {
                        AlbumInfo albumInfo = galleryList.get(albumName);
                        albumInfo.photoPaths.add(path);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return galleryList;
    }

    @SuppressWarnings("deprecation")
    public static ArrayList<String> getGalleryPhotos(Activity act) {
        ArrayList<String> galleryList = new ArrayList<String>();
        try {
            final String[] columns = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID};
            final String orderBy = MediaStore.Images.Media._ID;
            Cursor imagecursor = act.managedQuery(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                    null, null, orderBy);
            if (imagecursor != null && imagecursor.getCount() > 0) {
                while (imagecursor.moveToNext()) {
                    String item = new String();
                    int dataColumnIndex = imagecursor
                            .getColumnIndex(MediaStore.Images.Media.DATA);
                    item = imagecursor.getString(dataColumnIndex);
                    galleryList.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.reverse(galleryList);
        return galleryList;
    }

    public static Bitmap convertViewToBitmap(View view) {
        Bitmap bitmap = null;
        try {
            int width = view.getWidth();
            int height = view.getHeight();
            if (width != 0 && height != 0) {
                bitmap = Bitmap.createBitmap(width, height,
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                view.layout(0, 0, width, height);
                view.setBackgroundColor(Color.WHITE);
                view.draw(canvas);
            }
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;

    }

    public static boolean saveImageToGallery(Context context, Bitmap bmp,
                                             boolean isPng) {
        if (bmp == null) {
            return false;
        }
        File appDir = new File(Environment.getExternalStorageDirectory(),
                context.getString(R.string.app_name));
        if (!appDir.exists()) {
            if (!appDir.mkdir()) {
                return false;
            }
        }
        String fileName;
        if (isPng) {
            fileName = System.currentTimeMillis() + ".png";
        } else {
            fileName = System.currentTimeMillis() + ".jpg";
        }
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            if (isPng) {
                bmp.compress(CompressFormat.PNG, 100, fos);
            } else {
                bmp.compress(CompressFormat.JPEG, 100, fos);
            }
            bmp.recycle();
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(appDir)));
        return true;
    }


    /**
     * 如果是本地文件，前缀不要带file
     */
    public static Uri pathToUri(String path) {
        File file = new File(path);
        return Uri.fromFile(file);
    }

    public static final int PHOTO_TAKE = 1;
    public static final int PHOTO_CROP = 2;
    public static void cropImage(Fragment context, Uri uri, Uri outUri, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        context.startActivityForResult(intent, PHOTO_CROP);
    }

    public static void takePhoto(Fragment context, Uri outUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        context.startActivityForResult(intent, PHOTO_TAKE);
    }

}
