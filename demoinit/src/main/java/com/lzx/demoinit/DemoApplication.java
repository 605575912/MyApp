package com.lzx.demoinit;

import android.app.Application;
import android.util.Log;

import java.io.File;

/**
 * Created by liangzhenxiong on 16/1/12.
 */
public class DemoApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        File file = new File("/proc/22887/net/netstat");
        if (file.exists()){
            Log.i("TAG","====111111");
        }else {
            Log.i("TAG","====");
        }
    }
}
