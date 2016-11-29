package com.garfield.weishu.discovery.scan;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.garfield.baselib.utils.drawable.ScreenSizeUtils;
import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.R;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.util.List;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2016/11/29.
 */

public class ScanFragment extends AppBaseFragment implements SurfaceHolder.Callback, Camera.PreviewCallback {

    @BindView(R.id.fragment_scan_surface)
    SurfaceView mSurfaceView;

    private Camera camera;

    {
        setAnimationEnable(false);
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_scan;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mSurfaceView.getHolder().addCallback(this);
        mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try{
            camera = Camera.open();
            camera.setPreviewDisplay(mSurfaceView.getHolder());
            Camera.Parameters params = camera.getParameters();
            List<Camera.Size> supportedPreviewSizes = params.getSupportedPreviewSizes();
            Camera.Size size = null;
            for (int i = supportedPreviewSizes.size() - 1; i >= 0; i--) {
                Camera.Size size_tmp = supportedPreviewSizes.get(i);
                //L.d("scale: " + (float)size_tmp.width / size_tmp.height);
                //L.d("size: width " + size_tmp.width + ", height " + size_tmp.height);
                if (Math.abs((float)size_tmp.width / size_tmp.height - ScreenSizeUtils.getScreenScale()) < 0.01f) {
                    L.d("size: width " + size_tmp.width + ", height " + size_tmp.height);
                    size = size_tmp;
                    break;
                }
            }
            if (size == null) return;
            params.setPreviewSize(size.width, size.height);
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            camera.setParameters(params);
            camera.setDisplayOrientation(90);
            camera.setPreviewCallback(this);
            camera.startPreview();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
//        L.d("onPreviewFrame");

//        Camera.Parameters parameters = camera.getParameters();
//        int imageFormat = parameters.getPreviewFormat();
//        int w = parameters.getPreviewSize().width;
//        int h = parameters.getPreviewSize().height;
//        Rect rect = new Rect(0, 0, w, h);
//        YuvImage yuvImg = new YuvImage(data, imageFormat, w, h, null);
//
//        ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
//        yuvImg.compressToJpeg(rect, 100, outputstream);
//        Bitmap bmp = BitmapFactory.decodeByteArray(outputstream.toByteArray(), 0, outputstream.size());
//
//        Mat testMat = new Mat();
//        Utils.bitmapToMat(bmp, testMat);
//
//        MatOfRect faceDetections = new MatOfRect();
//        faceDetector.detectMultiScale(testMat, faceDetections);
//
//        L.d(String.format("Detected %s faces",
//                faceDetections.toArray().length));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null ;
        }
        //mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
