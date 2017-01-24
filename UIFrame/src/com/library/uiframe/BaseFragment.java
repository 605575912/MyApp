package com.library.uiframe;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;

/**
 * Created by liangzhenxiong on 16/4/4.
 */
public class BaseFragment extends Fragment implements  BaseCallBack{
    public Handler handler = new MyHandler(this);
    boolean isdestroy = false;
    public String TAG;
    static class MyHandler extends Handler {
        WeakReference<BaseFragment> mActivityReference;

        MyHandler(BaseFragment activity) {
            mActivityReference = new WeakReference<BaseFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final BaseFragment activity = mActivityReference.get();
            if (activity != null || !activity.isdestroy) {
                activity.handleMessage(msg);
            }
        }
    }
    @Override
    public void handleMessage(Message msg) {

    }

    @Override
    public void initViewDelaod() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        TAG = getClass().getSimpleName();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isdestroy = true;
        if (handler!=null){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }

    }
}
