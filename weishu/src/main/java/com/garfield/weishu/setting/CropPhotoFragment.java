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
import com.garfield.baselib.utils.file.DirectoryUtils;
import com.garfield.baselib.utils.file.FileUtils;
import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.R;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.io.File;

import butterknife.BindView;

import static com.garfield.baselib.ui.crop.CropParams.PHOTO_INPUT_URI;
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
        return newInstance(photoPath, true, 1, false);
    }

    public static CropPhotoFragment newInstance(String photoPath,
                                                boolean isCrop, float ratio, boolean hold) {
        Bundle args = new Bundle();
        CropParams cropParams = CropParams.of(Uri.fromFile(new File(photoPath)));
        cropParams.withCropRatio(ratio).withIsCrop(isCrop).withIsHold(hold);
        args.putSerializable("cropParams", cropParams);
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
        CropParams cropParams = (CropParams) getArguments().getSerializable("cropParams");
        if (cropParams != null) {
            String photoPath = cropParams.getInputUri().getPath();
            cropParams.withOutputUri(Uri.fromFile(new File(DirectoryUtils.getOwnCacheDirectory("crop"),
                    FileUtils.getFileNameWithoutSuffix(photoPath) + "_crop.jpg")));
            cropParams.withIsCrop(true);
            cropParams.withMaxResultSize(100, 0);
            cropParams.withCropRatio(1);
            cropParams.withIsHold(true);

            mMyCropView.setCropParams(cropParams);

        }

    }

    @Override
    public void onClick(View v) {

        mMyCropView.getGestureCropImageView().cropAndSaveImage(Bitmap.CompressFormat.JPEG, 90,
                new BitmapCropCallback() {

                    @Override
                    public void onBitmapCropped(@NonNull Uri resultUri, int imageWidth, int imageHeight) {
                        L.d(resultUri);
                        L.d(imageWidth);
                        L.d(imageHeight);
                        //Bundle bundle = new Bundle();
                        //bundle.putString(INFO_HEAD, resultUri.getPath());
                        //setFragmentResult(bundle);
                        //popToFragment(SelfProfileFragment.class, false);
                    }

                    @Override
                    public void onCropFailure(@NonNull Throwable t) {
                        L.toast(R.string.error);
                        L.d(t);
                    }
                });
    }
}
