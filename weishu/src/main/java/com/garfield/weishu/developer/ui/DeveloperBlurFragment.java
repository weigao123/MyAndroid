package com.garfield.weishu.developer.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.garfield.weishu.R;
import com.garfield.weishu.developer.utils.FastBlur;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import butterknife.BindView;

/**
 * Created by gaowei on 17/5/13.
 */

public class DeveloperBlurFragment extends AppBaseFragment {

    @BindView(R.id.developer_blur)
    ImageView mView;

    @Override
    protected String onGetToolbarTitle() {
        return "FastBlur";
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_developer_blur;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mView.post(new Runnable() {
            @Override
            public void run() {
                BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.picture);
                final Bitmap bitmap = drawable.getBitmap();
                Bitmap result = blur(bitmap, true);
                mView.setImageBitmap(result);
            }
        });
    }

    private Bitmap blur(Bitmap resource, boolean optimize) {
        float scaleFactor = 1;
        float radius = 20;
        if (optimize) {
            scaleFactor = 4;
            radius = 2;
        }
        Bitmap bitmap = Bitmap.createBitmap((int) (resource.getWidth()/scaleFactor),
                (int) (resource.getHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // 把画布缩放相当于把后面要画上的内容缩放，画布想象无限大，最后只裁剪初始化的尺寸
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(resource, 0, 0, paint);
        return FastBlur.doBlur(bitmap, (int)radius, true);
    }
}
