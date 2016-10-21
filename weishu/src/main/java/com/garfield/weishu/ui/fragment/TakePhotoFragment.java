package com.garfield.weishu.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.garfield.baselib.utils.DirectoryUtils;
import com.garfield.baselib.utils.FileUtils;
import com.garfield.baselib.utils.InvokerUtils;
import com.garfield.baselib.utils.PhotoUtil;
import com.garfield.weishu.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.base.adapter.TAdapterDelegate;
import com.garfield.weishu.base.adapter.TViewHolder;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.session.MsgAdapter;
import com.garfield.weishu.setting.PhotoAdapter;
import com.garfield.weishu.setting.PhotoViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.garfield.weishu.ui.fragment.SelfProfileFragment.INFO_HEAD;

/**
 * Created by gaowei3 on 2016/10/19.
 */

public class TakePhotoFragment extends AppBaseFragment implements TAdapterDelegate {

    private static final boolean isUseOwnCrop = true;

    @BindView(R.id.fragment_take_photo_gridview)
    GridView mGridView;

    @BindView(R.id.fragment_take_photo_no_photo)
    TextView mNoPhoto;

    private PhotoAdapter adapter;
    private List<String> items = new ArrayList<>();

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
        adapter = new PhotoAdapter(AppCache.getContext(), items, this);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String photoPath = adapter.getItem(position);
                if (isUseOwnCrop) {
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
                    PhotoUtil.cropImage(TakePhotoFragment.this, sourceUri, targetUri, 500, 500, 1);
                } else {
                    EventDispatcher.getFragmentJumpEvent().onShowCropPhoto(photoPath);
                }
            }
        });
        loadImage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
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
    public Class<? extends TViewHolder> getViewHolderClassAtPosition(int position) {
        return PhotoViewHolder.class;
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
                items.clear();
                List<String> result = PhotoUtil.getGalleryPhotos(AppCache.getContext());
                items.addAll(result);
                return result.isEmpty();
            }

            @Override
            public void onAfter(boolean b) {
                mNoPhoto.setVisibility(b ? View.VISIBLE : View.GONE);
                adapter.notifyDataSetChanged();
            }
        }).start();
    }


}
