package com.example.aidlclient;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by liangzhenxiong on 16/1/25.
 */
public class ACService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("TAG", "onCreate===" + getPackageName());
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i("TAG", "onStart===" + getPackageName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("TAG", "onStartCommand===" + getPackageName());
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
