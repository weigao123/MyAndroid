package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2016/10/19.
 */

public class TakePhotoFragment extends AppBaseFragment implements TAdapterDelegate {

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
                EventDispatcher.getFragmentJumpEvent().onShowCropPhoto(photoPath);
            }
        });
        loadImage();
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
