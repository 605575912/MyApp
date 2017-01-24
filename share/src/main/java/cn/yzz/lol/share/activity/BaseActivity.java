package cn.yzz.lol.share.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.support.loader.ServiceLoader;

import java.lang.ref.WeakReference;

public class BaseActivity extends Activity implements OnClickListener {


    boolean isdestroy = false;
    public MyHandler handler = new MyHandler(this);

    public static class MyHandler extends Handler {
        WeakReference<BaseActivity> mActivityReference;

        MyHandler(BaseActivity activity) {
            mActivityReference = new WeakReference<BaseActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final BaseActivity activity = mActivityReference.get();
            if (activity != null) {
                if (activity.isdestroy) {
                    return;
                }
                if (ServiceLoader.isexit) {
                    return;
                }
                activity.handleMessage(msg);
                super.handleMessage(msg);
            }
        }
    }

    /**
     * 每个Acitvity里面实现这个方法
     *
     * @param msg
     */
    public void handleMessage(Message msg) {
//        if (msg.what == Action.MSG_Loaction) {
//    }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        AppManager.getAppManager().addActivity(this);
    }


//    void exitacitvity() {
//        AppManager.getAppManager().finishActivity(BaseActivity.this);
//    }

    /**
     * 按下键盘上返回按钮退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// 后退
            // TODO Auto-generated method stub
//            exitacitvity();
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
//        if (arg0.getId() == R.id.bt_fclose) {
//
//            AppManager.getAppManager().finishActivity(BaseActivity.this);
//        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//        if (XswApplication.Release) {
//            // MobclickAgent.onPageStart(getTitle().toString());
//            MobclickAgent.onResume(this);
//        }
//        XswApplication.getInstance().getTaskPacketListenner().setNext(true);
//        XswApplication.getInstance().getTaskPacketListenner().DataNext();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // MobclickAgent.onPageEnd(getTitle().toString());
//        if (XswApplication.Release) {
//            MobclickAgent.onPause(this);
//        }
//        XswApplication.getInstance().getTaskPacketListenner().setNext(false);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        isdestroy = true;
    }

}
