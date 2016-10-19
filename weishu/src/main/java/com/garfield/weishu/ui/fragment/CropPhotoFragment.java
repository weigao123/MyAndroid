package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.garfield.weishu.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;

import static com.garfield.weishu.ui.fragment.SelfProfileFragment.INFO_HEAD;


/**
 * Created by gaowei3 on 2016/10/19.
 */

public class CropPhotoFragment extends AppBaseFragment implements View.OnClickListener {

    private String mPhotoPath;

    @BindView(R.id.confirm)
    TextView mConfirm;

    @BindView(R.id.fragment_crop_photo_view)
    PhotoView mPhotoView;

    public static CropPhotoFragment newInstance(String photoPath) {
        Bundle args = new Bundle();
        args.putString(INFO_HEAD, photoPath);
        CropPhotoFragment fragment = new CropPhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_crop_photo;
    }

    @Override
    protected int onGetToolbarTitleResource() {
        return R.string.crop_photo;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mConfirm.setVisibility(View.VISIBLE);
        mConfirm.setOnClickListener(this);
        mPhotoPath = getArguments().getString(INFO_HEAD);

        ImageLoader.getInstance().displayImage("file://" + mPhotoPath, mPhotoView);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(INFO_HEAD, mPhotoPath);
        setFragmentResult(bundle);
        popToFragment(SelfProfileFragment.class, false);
    }
}
