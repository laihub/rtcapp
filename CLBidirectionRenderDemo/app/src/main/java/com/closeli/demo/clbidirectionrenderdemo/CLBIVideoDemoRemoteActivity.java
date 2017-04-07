package com.closeli.demo.clbidirectionrenderdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.closeli.library.camera.tools.PixelBuffer;
import com.closeli.natives.CLWebRtcNativeBinder;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * Created by piaovalentin on 2017/4/5.
 */

public class CLBIVideoDemoRemoteActivity extends CLBIVideoDemoActivity implements CLWebRtcNativeBinder.onRetureCallback{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Toast.makeText(this, "start in 3 seconds...", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CLWebRtcNativeBinder.initStart(getApplicationContext());
                Toast.makeText(CLBIVideoDemoRemoteActivity.this, "works like a charm ~", Toast.LENGTH_SHORT).show();
            }
        }, 3000l);


        CLWebRtcNativeBinder.setCallback(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CLWebRtcNativeBinder.stop();
    }

    @Override
    protected void onReadData(PixelBuffer pixelBuffer, int width, int height) {
        super.onReadData(pixelBuffer, width, height);

        CLWebRtcNativeBinder.sendVideoData(pixelBuffer.buffer.array(), width, height);
    }

    @Override
    protected void onDataReture(Buffer buffer, int width, int height) {
        //重写，为了不直接渲染camera数据
    }

    @Override
    public void onVideoData(byte[] pdata, int width, int height) {
        mCameraRender.onDataReture(ByteBuffer.wrap(pdata), width, height);
    }
}
