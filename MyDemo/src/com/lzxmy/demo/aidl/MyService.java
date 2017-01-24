package com.lzxmy.demo.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class MyService extends Service {
	MyServiceImpl iBinder;

	public class MyServiceImpl extends IMyService.Stub {
		ChatListen chatListen;

		public ChatListen getChatListen() {
			return chatListen;
		}

		public void setChatListens(ChatListen chatListen) {
			this.chatListen = chatListen;
		}
		public String getValue() throws RemoteException {
//			iBinder.getChatListen().connectSuccess("返回");
			return "Android 链接成功";
		}

		@Override
		public void answerRingingCall() throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean enableDataConnectivity() throws RemoteException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean disableDataConnectivity() throws RemoteException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isDataConnectivityPossible() throws RemoteException {
			// TODO Auto-generated method stub
			return false;
		}
	}


	public IBinder onBind(Intent intent) {
		if (iBinder == null) {
			iBinder = new MyServiceImpl();
		}
		return iBinder;
	}
}
