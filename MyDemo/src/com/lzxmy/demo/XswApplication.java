package com.lzxmy.demo;


import android.app.Application;

import com.support.loader.ServiceLoader;


public class XswApplication extends Application {

	public static XswApplication app;




	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		ServiceLoader.getInstance().Init(app);
//		 CatchHandler.getInstance().init(app);

	}



	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
