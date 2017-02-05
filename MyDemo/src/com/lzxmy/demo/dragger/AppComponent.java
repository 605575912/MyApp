package com.lzxmy.demo.dragger;

import com.lzxmy.demo.marquee.DaggerAppComponentFactory;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by niuxiaowei on 16/3/19.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    MainFragmentComponent mainFragmentComponent();

    //对MainActivity进行依赖注入
    void inject(DaggerAppComponentFactory daggerAppComponentFactory);


}
