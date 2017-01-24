package com.support.loader.utils;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by apple on 15/7/13.
 */
public interface ImageLoadingListener {
    void onLoadingStarted(String var1, View var2);

    void onLoadingFailed(String var1, View var2);

    void onLoadingComplete(String var1, View var2, Bitmap var3);

    Bitmap onLoadingBitmap(String var1, View var2, Bitmap var3);

    void onLoadingCancelled(String var1, View var2);
}
