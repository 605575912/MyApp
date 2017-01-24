package com.library.uiframe;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

import com.support.loader.utils.AppManager;

import java.lang.ref.WeakReference;

/**
 * Created by liangzhenxiong on 16/1/14.
 */
public class BaseFragmentActivity extends FragmentActivity implements BaseCallBack {
    boolean isdestroy = false;
    public Handler handler = new MyHandler(this);
    public String TAG;

    @Override
    public void initViewDelaod() {

    }

    static class MyHandler extends Handler {
        WeakReference<BaseFragmentActivity> mActivityReference;

        MyHandler(BaseFragmentActivity activity) {
            mActivityReference = new WeakReference<BaseFragmentActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final BaseFragmentActivity activity = mActivityReference.get();
            if (activity != null || !activity.isdestroy) {
                activity.handleMessage(msg);
            }
        }
    }

    @Override
    public void handleMessage(Message msg) {

    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        TAG = getClass().getSimpleName();
        super.onCreate(savedInstanceState);

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
