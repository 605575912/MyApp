package com.library.uiframe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.support.loader.utils.AppManager;

import java.lang.ref.WeakReference;

/**
 * Created by liangzhenxiong on 16/1/14.
 */
public class BaseActivity extends Activity implements BaseCallBack {
    boolean isdestroy = false;
    public String TAG;


    public Handler handler = new MyHandler(this);

    static class MyHandler extends Handler {
        final WeakReference<BaseActivity> mActivityReference;

        MyHandler(BaseActivity activity) {
            mActivityReference = new WeakReference<BaseActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final BaseActivity activity = mActivityReference.get();
            if (activity != null || !activity.isdestroy) {
                activity.handleMessage(msg);
            }
        }
    }

    @Override
    public void initViewDelaod() {

    }

    @Override
    public void handleMessage(Message msg) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        getWindow().getDecorView().post(new Runnable() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        initViewDelaod();
//                    }
//                });
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isdestroy = true;
        handler.removeCallbacksAndMessages(null);
        handler = null;
        AppManager.getAppManager().finishActivity(this);
    }
}
