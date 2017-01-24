package com.lzxmy.demo.aidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;

import com.lzxmy.demo.R;

public class AidlActivity extends Activity implements OnClickListener,
		ChatListen {

	private MyService.MyServiceImpl myService = null;

	private ServiceConnection serviceConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName name, IBinder service) {
			// 获得服务对象
			myService = (MyService.MyServiceImpl) IMyService.Stub
					.asInterface(service);
			myService.setChatListens(AidlActivity.this);
		}

		public void onServiceDisconnected(ComponentName name) {
		}
	};

	public void onClick(View view) {
//		switch (view.getId()) {
//		case R.id.button1:
//			textView.setText(":-)");
//
//			// 绑定AIDL服务
//			Intent intent = new Intent();
//			intent.setClass(AidlActivity.this, MyService.class);
//			bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
//			break;
//		case R.id.button2:
//			try {
//				textView.setText(myService.getValue()); // 调用服务端的getValue方法
//			} catch (Exception e) {
//			}
//			break;
//		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		unbindService(serviceConnection);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.aild_layout);
//		mButton1 = (Button) findViewById(R.id.button1);
//		mButton1.setOnClickListener(this);
//		mButton2 = (Button) findViewById(R.id.button2);
//		mButton2.setOnClickListener(this);
//		mButton2.setEnabled(false);
//		textView = (TextView) findViewById(R.id.textView1);

	}

	@Override
	public void connectSuccess(Object string) {
		// TODO Auto-generated method stub
//		Toast.makeText(AidlActivity.this, string, Toast.LENGTH_LONG).show();
	}
}
