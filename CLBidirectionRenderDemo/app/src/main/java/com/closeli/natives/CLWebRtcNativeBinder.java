package com.closeli.natives;

import android.content.Context;

/**
 * Created by piaovalentin on 2017/4/5.
 */

public class CLWebRtcNativeBinder {


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public interface onRetureCallback {
        void onVideoData(byte[] pdata, int width, int height);
    }


    //temp tools
    private static onRetureCallback mCallback;
    public static void setCallback(onRetureCallback callback) {
        mCallback = callback;
    }

    public static native void initStart(Object context);
    public static native void sendVideoData(byte[] data, int width, int height);
    public static native void stop();

    public static void onVideoData(byte[] pdata, int width, int height) {
        mCallback.onVideoData(pdata, width, height);
    }

}
