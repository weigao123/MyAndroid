package com.garfield.weishu.ui.fragment;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.garfield.baselib.utils.DirectoryUtils;
import com.garfield.baselib.utils.FileUtils;
import com.garfield.baselib.utils.InvokerUtils;
import com.garfield.baselib.utils.PhotoUtil;
import com.garfield.baselib.utils.SizeUtils;
import com.garfield.weishu.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.base.listview.TListAdapterDelegate;
import com.garfield.weishu.base.listview.TListViewHolder;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.setting.AlbumListAdapter;
import com.garfield.weishu.setting.AlbumListViewHolder;
import com.garfield.weishu.setting.PhotoListAdapter;
import com.garfield.weishu.setting.PhotoListViewHolder;

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
import static com.garfield.weishu.ui.fragment.SelfProfileFragment.INFO_HEAD;

/**
 * Created by gaowei3 on 2016/10/19.
 */

public class TakePhotoFragment extends AppBaseFragment implements TListAdapterDelegate {

    private static final boolean isUseNativeCropFunction = true;

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

    private PhotoListAdapter mPhotoAdapter;
    private List<String> mPhotoItems = new ArrayList<>();

    private AlbumListAdapter mAlbumAdapter;
    private List<PhotoUtil.AlbumInfo> mAlbumItems = new ArrayList<>();

    private HashMap<String, PhotoUtil.AlbumInfo> mAlbumHashMap = new HashMap<>();

    private boolean isAnimatorRunning;
    private int mAlbumPosition;
    // 不带file://
    private File mPhotoOutputPath = new File(DirectoryUtils.getOwnImageCacheDirectory(AppCache.getContext()), "take_photo.jpg");

    protected int onGetToolbarTitleResource() {
        return R.string.photo_album;
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_take_photo;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mPhotoAdapter = new PhotoListAdapter(AppCache.getContext(), mPhotoItems, this);
        mGridView.setAdapter(mPhotoAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isAnimatorRunning) return;
                if (position == 0) {
                    takePhoto();
                    return;
                }
                String photoPath = mPhotoAdapter.getItem(position);
                if (isUseNativeCropFunction) {
                    cropPhoto(photoPath);
                } else {
                    EventDispatcher.getFragmentJumpEvent().onShowCropPhoto(photoPath);
                }
            }
        });

        mAlbumAdapter = new AlbumListAdapter(AppCache.getContext(), mAlbumItems, new TListAdapterDelegate() {
            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public Class<? extends TListViewHolder> getViewHolderClassAtPosition(int position) {
                return AlbumListViewHolder.class;
            }

            @Override
            public boolean enabled(int position) {
                return false;
            }
        });
        mAlbumListView.setAdapter(mAlbumAdapter);
        mAlbumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isAnimatorRunning) return;
                mAlbumPosition = position;
                switchPhotoList(mAlbumItems.get(position));
            }
        });

        mAlbumContainer.post(new Runnable() {
            public void run() {
                int containerHeight = mAlbumContainer.getMeasuredHeight();
                ViewGroup.LayoutParams params = mAlbumListView.getLayoutParams();
                params.height = containerHeight - SizeUtils.dp2px(getContext(), 80);
                mAlbumListView.setLayoutParams(params);   //设置高度
                mAlbumListView.setY(containerHeight);   //下移
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

    private void takePhoto() {
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
        /**
         * File不会增加前缀，Uri会增加file前缀
         */
        Uri sourceUri = PhotoUtil.pathToUri(photoPath);
        File sourceFile = new File(photoPath);
        String a = sourceFile.getName();    //文件名加后缀
        String b = sourceFile.getAbsolutePath();
        String c = sourceFile.getPath();
        String name = FileUtils.removeFileSuffix(a);
        File targetFile = new File(DirectoryUtils.getOwnImageCacheDirectory(AppCache.getContext()), "crop_"+name+".jpg");
        Uri targetUri = Uri.fromFile(targetFile);
        PhotoUtil.cropImage(TakePhotoFragment.this, sourceUri, targetUri, 500, 500);
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
            Bundle bundle = new Bundle();
            String a = uri.getScheme();   //前缀
            String b = uri.getSchemeSpecificPart();   //前缀以外的部分
            String c = uri.getLastPathSegment();    //文件名和后缀
            bundle.putString(INFO_HEAD, b);
            setFragmentResult(bundle);
            popToFragment(SelfProfileFragment.class, false);
        }
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public Class<? extends TListViewHolder> getViewHolderClassAtPosition(int position) {
        return PhotoListViewHolder.class;
    }

    @Override
    public boolean enabled(int position) {
        return false;
    }

    private void loadImage() {
        new InvokerUtils(new InvokerUtils.Callback() {
            @Override
            public void onBefore() {
            }

            @Override
            public boolean onRun() {
                mPhotoItems.clear();
                mAlbumItems.clear();
                mAlbumHashMap = PhotoUtil.getGalleryPhotos(AppCache.getContext());

                mAlbumItems.addAll(mAlbumHashMap.values());
                sortAlbum(mAlbumItems);

                List<String> allPath = new ArrayList<>();
                for (PhotoUtil.AlbumInfo albumInfo : mAlbumItems) {
                    allPath.addAll(albumInfo.photoPaths);
                }

                PhotoUtil.AlbumInfo wholeAlbum = new PhotoUtil.AlbumInfo();
                wholeAlbum.albumName = getResources().getString(R.string.all_albums);
                wholeAlbum.photoPaths.addAll(allPath);
                wholeAlbum.albumImage = allPath.get(0);
                mAlbumItems.add(0, wholeAlbum);

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
        int originDiff = SizeUtils.dp2px(getContext(), 80);
        if (isAlbumListShow()) {
            mAlbumListView.animate().y(containerHeight);
            mMask.setBackgroundColor(getResources().getColor(R.color.pure_trans));
        } else {
            mAlbumListView.animate().y(originDiff);
            mMask.setBackgroundColor(getResources().getColor(R.color.black_trans));
        }
    }

    private boolean isAlbumListShow() {
        int originDiff = SizeUtils.dp2px(getContext(), 80);
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
}
