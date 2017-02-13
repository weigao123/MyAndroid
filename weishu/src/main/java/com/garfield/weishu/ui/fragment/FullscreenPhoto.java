package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.garfield.baselib.utils.http.image.ImageHelper;
import com.garfield.baselib.utils.system.SystemUtil;
import com.garfield.weishu.R;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;

import static com.garfield.weishu.setting.SelfProfileFragment.INFO_HEAD;

/**
 * Created by gaowei3 on 2016/10/30.
 */

public class FullscreenPhoto extends AppBaseFragment {

    @BindView(R.id.fragment_fullscreen_photo)
    PhotoView mFullscreenPhoto;

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
        //mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SystemUtil.setStatusBarColorK(mActivity, getResources().getColor(R.color.black));

        mPhotoPath = getArguments().getString(INFO_HEAD);
        ImageHelper.load(mRootView.getContext(), mPhotoPath, mFullscreenPhoto);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SystemUtil.setStatusBarColorK(mActivity, getResources().getColor(R.color.colorPrimaryDark));
        //mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
