package cn.yzz.lol.share;

import android.app.Application;

import com.support.loader.ServiceLoader;


/**
 * Created by liangzhenxiong on 15/11/15.
 */
public class ShareApplication extends Application {
   public static ShareApplication  application;
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        ServiceLoader.getInstance().Init(this);
    }
}
