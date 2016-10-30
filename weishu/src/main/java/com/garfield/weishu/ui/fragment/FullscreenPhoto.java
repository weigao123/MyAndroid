package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.garfield.weishu.R;
import com.garfield.weishu.setting.CropPhotoFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;

import static com.garfield.weishu.setting.SelfProfileFragment.INFO_HEAD;

/**
 * Created by gaowei3 on 2016/10/30.
 */

public class FullscreenPhoto extends AppBaseFragment {

    @BindView(R.id.fragment_fullscreen_photo)
    ImageView mFullscreenPhoto;

    private String mPhotoPath;

    public static FullscreenPhoto newInstance(String photoPath) {
        Bundle args = new Bundle();
        args.putString(INFO_HEAD, photoPath);
        FullscreenPhoto fragment = new FullscreenPhoto();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_fullscreen_photo;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mPhotoPath = getArguments().getString(INFO_HEAD);
        ImageLoader.getInstance().displayImage(mPhotoPath, mFullscreenPhoto);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
