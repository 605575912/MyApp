package com.apk.dllibimage;

import android.content.Intent;
import android.os.Bundle;

import com.ryg.dynamicload.DLBasePluginActivity;
import com.ryg.dynamicload.internal.DLIntent;


/**
 * Created by liangzhenxiong on 15/12/21.
 */
public class MainActivity extends DLBasePluginActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
//        ImageMap.getInstance().Init(that);
//        ImageMap.getInstance().onCreateContentObserver();
        if (BuildConfig.DEBUG) {
//            setContentView(R.layout.main_layout);
//            Intent intent = new Intent();
//            intent.setClassName(this, LocationImageActivity.class.getName());
//            mProxyActivity.startActivityForResult(intent, 0);
        } else {
//            DLIntent intent = new DLIntent(getPackageName(), LocationImageActivity.class);
//            // 传递Parcelable类型的数据
////            intent.putExtra("person", new Person("plugin-a", 22));
////            intent.putExtra("dl_extra", "from DL framework");
//            startPluginActivityForResult(intent, 0);

        }
//        that.finish();

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
//        ImageMap.getInstance().onDestroyContentObserver();
    }
}
