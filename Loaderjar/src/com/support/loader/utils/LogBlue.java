package com.support.loader.utils;

import android.util.Log;

/**
 * Created by liangzhenxiong on 16/3/6.
 */
public class LogBlue {
    private static boolean isopen = true;

    public static void setIsopen(boolean isopen) {
        LogBlue.isopen = isopen;
    }

    public static void i(String TAG, String string) {
        if (!isopen) {
            return;
        }
        Log.i(TAG, string);
    }

    public static void i(String TAG, int inv) {
        i(TAG, "" + inv);
    }
    public static void e(String TAG, String string,Throwable e) {
        if (!isopen) {
            return;
        }
        Log.e(TAG, string,e);
    }
}
