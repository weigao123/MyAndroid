package com.garfield.weishu.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.garfield.baselib.ui.widget.CropImage.MyCropView;
import com.garfield.weishu.R;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import butterknife.BindView;

import static com.garfield.weishu.setting.SelfProfileFragment.INFO_HEAD;


/**
 * Created by gaowei3 on 2016/10/19.
 */

public class CropPhotoFragment extends AppBaseFragment implements View.OnClickListener {

    private String mPhotoPath;

    @BindView(R.id.confirm)
    TextView mConfirm;

    @BindView(R.id.fragment_crop_photo_view)
    MyCropView mMyCropView;

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
    protected String onGetToolbarTitleResource() {
        return getString(R.string.crop_photo);
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mConfirm.setVisibility(View.VISIBLE);
        mConfirm.setOnClickListener(this);
        mPhotoPath = getArguments().getString(INFO_HEAD);

        //ImageHelper.load(getContext(), "file://" + mPhotoPath, mGestureImageView);
        mMyCropView.setImageUrl("file://" + mPhotoPath);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(INFO_HEAD, mPhotoPath);
        setFragmentResult(bundle);
        popToFragment(SelfProfileFragment.class, false);
    }
}
