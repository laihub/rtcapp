package com.closeli.demo.clbidirectionrenderdemo;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.closeli.library.camera.render.CLCameraSurfaceViewRender;
import com.closeli.library.camera.render.CLResizeSurfaceView;
import com.closeli.library.camera.tools.CLCameraManager;
import com.closeli.library.camera.tools.CLLoger;
import com.closeli.library.camera.tools.PixelBuffer;

import java.nio.Buffer;

/**
 * Created by piaovalentin on 2017/3/31.
 */

public class CLBIVideoDemoActivity extends Activity {

    private final String TAG = getClass().getSimpleName();

    protected CLResizeSurfaceView mGLMainView;
    protected CLCameraSurfaceViewRender mCameraRender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bivideodemo);

        setUpCamera();

        int glVer = CLCameraSurfaceViewRender.supperMaxGLVersion(this);// select GLES 2.0
        setUpMainView(glVer);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mGLMainView.onResume();
        final Camera.Size previewSize = CLCameraManager.sharedCameraManager().getCameraPreviewSize();
        mGLMainView.queueEvent(new Runnable() {
            @Override
            public void run() {
                mCameraRender.setCameraPreviewSize(previewSize.width, previewSize.height);
            }
        });

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        float x = CLCameraManager.sharedCameraManager().getVideoRatio(previewSize.width, previewSize.height) * size.y;
        if(x >= size.x) {
            mGLMainView.resize((int) x, size.y);
        }else{
            float y = size.x/CLCameraManager.sharedCameraManager().getVideoRatio(previewSize.width, previewSize.height);
            mGLMainView.resize(size.x, (int)y);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        CLCameraManager.sharedCameraManager().closeCamera();
        mGLMainView.queueEvent(new Runnable() {
            @Override
            public void run() {
                mCameraRender.notifyPausing();
            }
        });
        mGLMainView.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

//        CLCameraManager.sharedCameraManager().closeCamera();
    }

    private void setUpCamera() {
        try {
            CLCameraManager.init(this);
            CLCameraManager.sharedCameraManager().setSwapAble(true);
            CLCameraManager.sharedCameraManager().config(null);

            CLCameraManager.sharedCameraManager().setCameraManagerCallback(new CLCameraManager.CLCameraManagerCallback() {
                @Override
                public int previewFacing() {
                    return Camera.CameraInfo.CAMERA_FACING_FRONT;
                }

                @Override
                public boolean needRsetConfigBeforePreview() {
                    return false;
                }

                @Override
                public void onCameraFrameAvailable(SurfaceTexture surfaceTexture) {
                    mGLMainView.requestRender();
                }

                @Override
                public void onCameraPreviewStartWithSize(final Camera.Size previewSize) {

                    mGLMainView.queueEvent(new Runnable() {
                        public void run() {
                            if (null != mCameraRender && null != previewSize) {
                                mCameraRender.setCameraPreviewSize(previewSize.width, previewSize.height);
                            }
                        }
                    });
                    CLLoger.trace(TAG, "start preview!!!");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            CLLoger.trace(TAG, "init camera error!");
        }
    }


    private void setUpMainView(int glVer) {

        mGLMainView = (CLResizeSurfaceView) findViewById(R.id.mainview);
        mGLMainView.setEGLContextClientVersion(glVer);

        mCameraRender = new CLCameraSurfaceViewRender(this, new CLCameraSurfaceViewRender.CLCameraSurfaceViewCallback() {

            @Override
            public void onSurfaceTextureCreated(SurfaceTexture texture) {
                CLCameraManager.sharedCameraManager().setSurfaceTexture(texture);
            }

            @Override
            public void onRenderPixelBuffer(PixelBuffer pixelBuffer, int width, int height) {
                onReadData(pixelBuffer, width, height);
                onDataReture(pixelBuffer.buffer, width, height);
            }
        });

        mGLMainView.setRenderer(mCameraRender);
        mGLMainView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


    protected void onReadData(PixelBuffer pixelBuffer, int width, int height) {
        pixelBuffer.using = false;
    }


    protected void onDataReture(Buffer buffer, int width, int height) {
        mCameraRender.onDataReture(buffer, width, height);
    }



    private void rgbaImageTest(PixelBuffer pixelBuffer, int width, int height) {
        //Test Code
//        ImageView mIV = (ImageView)findViewById(R.id.flowImage);
//        byte[] bytes = pixelBuffer.buffer.array();
//        final Bitmap rgbaImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        rgbaImage.copyPixelsFromBuffer(ByteBuffer.wrap(bytes));
//        CLBIVideoDemoActivity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mIV.setImageBitmap(rgbaImage);
//            }
//        });
    }
}
