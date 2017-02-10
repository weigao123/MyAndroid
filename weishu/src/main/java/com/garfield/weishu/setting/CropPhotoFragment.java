package com.garfield.weishu.setting;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.garfield.baselib.ui.crop.CropParams;
import com.garfield.baselib.ui.crop.callback.BitmapCropCallback;
import com.garfield.baselib.ui.crop.view.MyCropView;
import com.garfield.baselib.ui.crop.view.TransformImageView;
import com.garfield.baselib.utils.file.DirectoryUtils;
import com.garfield.baselib.utils.file.FileUtils;
import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.R;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.io.File;

import butterknife.BindView;

import static com.garfield.weishu.setting.SelfProfileFragment.INFO_HEAD;


/**
 * Created by gaowei3 on 2016/10/19.
 */

public class CropPhotoFragment extends AppBaseFragment implements View.OnClickListener {

    {
        setAnimationEnable(false);
    }

    @BindView(R.id.confirm)
    TextView mConfirm;

    @BindView(R.id.fragment_crop_photo_view)
    MyCropView mMyCropView;

    public static CropPhotoFragment newInstance(String photoPath) {
        return newInstance(photoPath, true, 1, true);
    }

    public static CropPhotoFragment newInstance(String photoPath,
                                                boolean cropEnable, float cropRatio, boolean cropHoldEnable) {
        Bundle args = new Bundle();
        CropParams cropParams = CropParams.of(Uri.fromFile(new File(photoPath)));
        cropParams.withCropRatio(cropRatio).withCropEnable(cropEnable).withHoldEnable(cropHoldEnable);
        args.putParcelable("cropParams", cropParams);
        CropPhotoFragment fragment = new CropPhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_crop_photo;
    }

    @Override
    protected String onGetToolbarTitleResource() {
        return getString(R.string.crop_photo);
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mConfirm.setVisibility(View.VISIBLE);
        mConfirm.setOnClickListener(this);
        final CropParams cropParams = (CropParams) getArguments().get("cropParams");
        if (cropParams != null) {
            String photoPath = cropParams.getInputUri().getPath();
            cropParams.withOutputUri(Uri.fromFile(new File(DirectoryUtils.getOwnCacheDirectory("crop"), FileUtils.getFileNameWithoutSuffix(photoPath) + "_crop.jpg")));
            //cropParams.withCropEnable(true);
            cropParams.withScaleEnable(true);
            cropParams.withRotateEnable(true);
            cropParams.withMaxResultSize(300, 0);
            //cropParams.withCropRatio(1);
            //cropParams.withHoldEnable(true);
            mMyCropView.setCropParams(cropParams);
            mMyCropView.setTransformListener(new TransformImageView.TransformImageListener() {
                @Override
                public void onLoadComplete() {

                }

                @Override
                public void onLoadFailure(@NonNull Exception e) {

                }

                @Override
                public void onRotate(float currentAngle) {
                    L.d("rotate: "+currentAngle);
                }

                @Override
                public void onScale(float currentScale) {
                    L.d("scale: "+currentScale);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {

        mMyCropView.getGestureCropImageView().cropAndSaveImage(Bitmap.CompressFormat.JPEG, 90, new BitmapCropCallback() {

            @Override
            public void onBitmapCropped(@NonNull Uri resultUri, int imageWidth, int imageHeight) {
//                L.d(resultUri);
//                L.d(imageWidth);
//                L.d(imageHeight);
                Bundle bundle = new Bundle();
                bundle.putString(INFO_HEAD, resultUri.getPath());
                setFragmentResult(bundle);
                popToFragment(SelfProfileFragment.class, false);
            }

            @Override
            public void onCropFailure(@NonNull Throwable t) {
                L.toast(R.string.error);
                L.d(t);
            }
        });
    }
}
