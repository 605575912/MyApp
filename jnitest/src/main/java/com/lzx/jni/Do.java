package com.lzx.jni;

/**
 * Created by liangzhenxiong on 16/2/6.
 */
public class Do {
    //JNI
    public native String getstringfromC(int i, String[] strings);

    public native int decode(String inputurl, String outputurl);

    public native int stream(String inputurl, String outputurl);

    public native int intream();

    static {
        System.loadLibrary("avutil55");
        System.loadLibrary("swresample2");
        System.loadLibrary("swscale4");
        System.loadLibrary("avcodec57");
        System.loadLibrary("avformat57");
        System.loadLibrary("avfilter6");
        System.loadLibrary("hello_jni");
    }
}
