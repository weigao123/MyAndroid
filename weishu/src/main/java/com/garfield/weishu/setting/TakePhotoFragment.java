package com.garfield.weishu.setting;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.garfield.baselib.ui.widget.SwitchButton;
import com.garfield.baselib.utils.file.DirectoryUtils;
import com.garfield.baselib.utils.file.FileUtils;
import com.garfield.baselib.utils.drawable.PhotoUtil;
import com.garfield.baselib.utils.system.L;
import com.garfield.baselib.utils.system.ScreenUtils;
import com.garfield.baselib.utils.system.TaskUtils;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.base.listview.TListAdapter;
import com.garfield.weishu.app.SettingsPreferences;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.garfield.weishu.setting.SelfProfileFragment.INFO_HEAD;

/**
 * Created by gaowei3 on 2016/10/19.
 */

public class TakePhotoFragment extends AppBaseFragment {

    @BindView(R.id.fragment_take_photo_no_photo)
    TextView mNoPhoto;

    @BindView(R.id.fragment_take_photo_album)
    TextView mAlbumButton;

    @BindView(R.id.fragment_take_photo_gridview)
    GridView mGridView;

    @BindView(R.id.fragment_take_photo_album_list)
    ListView mAlbumListView;

    @BindView(R.id.fragment_take_photo_album_list_container)
    FrameLayout mAlbumContainer;

    @BindView(R.id.fragment_take_photo_mask)
    FrameLayout mMask;

    @BindView(R.id.fragment_take_photo_use_native_crop_check)
    SwitchButton mUseCropCheck;

    private PhotoListAdapter mPhotoAdapter;
    private List<String> mPhotoItems = new ArrayList<>();

    private AlbumListAdapter mAlbumAdapter;
    private List<PhotoUtil.AlbumInfo> mAlbumItems = new ArrayList<>();

    private HashMap<String, PhotoUtil.AlbumInfo> mAlbumHashMap = new HashMap<>();

    private boolean isAnimatorRunning;
    private int mAlbumPosition;
    // 不带file://
    private File mPhotoOutputPath = new File(DirectoryUtils.getOwnImageCacheDirectory(), "take_photo.jpg");


    protected String onGetToolbarTitleResource() {
        return getString(R.string.photo_album);
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_take_photo;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mUseCropCheck.setSwitchStatus(SettingsPreferences.getCropTool());
        mPhotoAdapter = new PhotoListAdapter(AppCache.getContext(), mPhotoItems);
        mPhotoAdapter.setItemEventListener(new TListAdapter.ItemEventListener<String>() {
            @Override
            public void onItemClick(String item, int position) {
                if (isAnimatorRunning) return;
                if ("Camera".equals(item)) {
                    takePhoto();
                    return;
                }
                if (SettingsPreferences.getCropTool()) {
                    cropPhoto(item);
                } else {
                    EventDispatcher.getFragmentJumpEvent().onShowCropPhoto(item);
                }
            }

            @Override
            public void onItemLongPressed(String item, int position) {

            }
        });
        mGridView.setAdapter(mPhotoAdapter);

        mAlbumAdapter = new AlbumListAdapter(AppCache.getContext(), mAlbumItems);
        mAlbumAdapter.setItemEventListener(new TListAdapter.ItemEventListener<PhotoUtil.AlbumInfo>() {

            @Override
            public void onItemClick(PhotoUtil.AlbumInfo item, int position) {
                if (isAnimatorRunning) return;
                mAlbumPosition = position;
                switchPhotoList(item);
            }

            @Override
            public void onItemLongPressed(PhotoUtil.AlbumInfo item, int position) {

            }
        });
        mAlbumListView.setAdapter(mAlbumAdapter);
        mAlbumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        mAlbumContainer.post(new Runnable() {
            public void run() {
                int containerHeight = mAlbumContainer.getMeasuredHeight();
                // 前台正常，后台时旋转会是0
                if (containerHeight != 0) {
                    ViewGroup.LayoutParams params = mAlbumListView.getLayoutParams();
                    params.height = containerHeight - ScreenUtils.dp2px(80);
                    mAlbumListView.setLayoutParams(params);   //设置高度
                    mAlbumListView.setY(containerHeight);   //下移
                }
            }
        });

        mAlbumListView.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimatorRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimatorRunning = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mAlbumButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mAlbumButton.setTextColor(getResources().getColor(R.color.blue1));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mAlbumButton.setTextColor(getResources().getColor(R.color.white));
                }
                return false;
            }
        });

        mMask.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (isAlbumListShow()) {
                        showHideAlbumList();
                        return true;
                    }
                }
                return false;
            }
        });
        loadImage();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // 解决当前页再进入下一页，转屏后返回显示的问题
        if (!hidden) {
            mAlbumContainer.post(new Runnable() {
                public void run() {
                    int containerHeight = mAlbumContainer.getMeasuredHeight();
                    ViewGroup.LayoutParams params = mAlbumListView.getLayoutParams();
                    params.height = containerHeight - ScreenUtils.dp2px(80);
                    mAlbumListView.setLayoutParams(params);   //设置高度
                    mAlbumListView.setY(containerHeight);   //下移
                }
            });
        }
    }

    private void takePhoto() {
        //mPhotoOutputPath = new File(getContext().getCacheDir(), "take_photo.jpg");
        if (mPhotoOutputPath.exists()) {
            mPhotoOutputPath.delete();
        }
        try {
            mPhotoOutputPath.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri imageUri = Uri.fromFile(mPhotoOutputPath);
        PhotoUtil.takePhoto(TakePhotoFragment.this, imageUri);
    }

    private void cropPhoto(String photoPath) {
        if (SettingsPreferences.getCropTool()) {
            /**
             * File不会增加前缀，Uri会增加file前缀
             */
            Uri sourceUri = PhotoUtil.pathToUri(photoPath);
            File sourceFile = new File(photoPath);
            String a = sourceFile.getName();    //文件名加后缀
            String b = sourceFile.getAbsolutePath();
            String c = sourceFile.getPath();
            String name = FileUtils.removeFileSuffix(a);
            File targetFile = new File(DirectoryUtils.getOwnImageCacheDirectory(), "crop_"+name+".jpg");
            Uri targetUri = Uri.fromFile(targetFile);
            PhotoUtil.cropImage(TakePhotoFragment.this, sourceUri, targetUri, 500, 500);
        } else {
            EventDispatcher.getFragmentJumpEvent().onShowCropPhoto(photoPath);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PhotoUtil.PHOTO_TAKE && resultCode == RESULT_OK) {
            if (data == null) {
                if (mPhotoOutputPath.exists()) {
                    cropPhoto(mPhotoOutputPath.getPath());
                }
            }
        }
        if (requestCode == PhotoUtil.PHOTO_CROP && resultCode == RESULT_OK) {
            Uri uri = data.getData();     //有file前缀，需要把前缀去掉
            if (uri != null) {
                Bundle bundle = new Bundle();
                String a = uri.getScheme();   //前缀
                String b = uri.getSchemeSpecificPart();   //前缀以外的部分
                String c = uri.getLastPathSegment();    //文件名和后缀
                bundle.putString(INFO_HEAD, b);
                setFragmentResult(bundle);
                popToFragment(SelfProfileFragment.class, false);
            } else {
                L.toast(R.string.error);
            }
        }
    }

    private void loadImage() {
        new TaskUtils.Invoker(new TaskUtils.Callback() {
            @Override
            public void onBefore() {
            }

            @Override
            public boolean doInBackground() {
                mPhotoItems.clear();
                mAlbumItems.clear();
                mAlbumHashMap = PhotoUtil.getGalleryPhotos();

                mAlbumItems.addAll(mAlbumHashMap.values());
                sortAlbum(mAlbumItems);

                List<String> allPath = new ArrayList<>();
                for (PhotoUtil.AlbumInfo albumInfo : mAlbumItems) {
                    allPath.addAll(albumInfo.photoPaths);
                }

                if (allPath.size() != 0) {
                    PhotoUtil.AlbumInfo wholeAlbum = new PhotoUtil.AlbumInfo();
                    wholeAlbum.albumName = getResources().getString(R.string.all_albums);
                    wholeAlbum.photoPaths.addAll(allPath);
                    wholeAlbum.albumImage = allPath.get(0);
                    mAlbumItems.add(0, wholeAlbum);
                }

                mPhotoItems.addAll(allPath);
                mPhotoItems.add(0, "Camera");
                return mAlbumHashMap.isEmpty();
            }

            @Override
            public void onAfter(boolean b) {
                mNoPhoto.setVisibility(b ? View.VISIBLE : View.GONE);
                mPhotoAdapter.notifyDataSetChanged();
                mAlbumAdapter.notifyDataSetChanged();
            }
        }).start();
    }

    private void sortAlbum(List<PhotoUtil.AlbumInfo> albumInfos) {
        Collections.sort(albumInfos, new Comparator<PhotoUtil.AlbumInfo>() {
            @Override
            public int compare(final PhotoUtil.AlbumInfo con1, final PhotoUtil.AlbumInfo con2) {
                if (con1.photoPaths.size() == con2.photoPaths.size()) {
                    return 0;
                } else if (con2.photoPaths.size() > con1.photoPaths.size()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    @OnClick(R.id.fragment_take_photo_album)
    void showHideAlbumList() {
        /**
         * 其实还是View Animation最丝滑
         */
        if (isAnimatorRunning) return;
        int containerHeight = mAlbumContainer.getMeasuredHeight();
        int originDiff = ScreenUtils.dp2px(80);
        if (isAlbumListShow()) {
            mAlbumListView.animate().y(containerHeight);
            mMask.setBackgroundColor(getResources().getColor(R.color.pure_trans));
        } else {
            mAlbumListView.animate().y(originDiff);
            mMask.setBackgroundColor(getResources().getColor(R.color.black_trans));
        }
    }

    private boolean isAlbumListShow() {
        int originDiff = ScreenUtils.dp2px(80);
        return Math.abs(mAlbumListView.getY() - originDiff) < 10 || isAnimatorRunning;
    }

    private void switchPhotoList(PhotoUtil.AlbumInfo info) {
        if (isAnimatorRunning) return;
        mPhotoItems.clear();
        mPhotoItems.add("Camera");
        mPhotoItems.addAll(info.photoPaths);
        mPhotoAdapter.notifyDataSetChanged();
        mAlbumAdapter.setAlbumSelect(mAlbumPosition);
        mAlbumAdapter.notifyDataSetChanged();
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                mGridView.setSelection(0);
            }
        });
        showHideAlbumList();
    }

    @Override
    protected boolean onBackPressed() {
        if (isAlbumListShow()) {
            showHideAlbumList();
            return true;
        }
        return super.onBackPressed();
    }

    @OnClick(R.id.fragment_take_photo_use_native_crop)
    void switchCheckedView() {
        mUseCropCheck.setSwitchStatus(!mUseCropCheck.getSwitchStatus());
        SettingsPreferences.setCropTool(mUseCropCheck.getSwitchStatus());
    }
}
