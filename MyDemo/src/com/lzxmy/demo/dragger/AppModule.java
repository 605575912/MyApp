package com.lzxmy.demo.dragger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by apple on 2016/10/23.
 */
@Module
public class AppModule {
    Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    public String getName() {
        return "11哈哈注入1";
    }

    @Provides
    public int getITag() {
        return 100;
    }
}
