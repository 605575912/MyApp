package com.auto.launcher;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class ShareService extends Service {


    private HashMap<String, Bean> mBeans = new HashMap<String, Bean>();
    //    MessageReceiver messageReceiver;
    IShareService.Stub iShareService;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("TAG", "==========oncreate");
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (iShareService != null) {
            return iShareService;
        }
//            serviceListenerBinder = new ServiceListenerBinder();
//        return serviceListenerBinder;
        iShareService = new IShareService.Stub() {
            @Override
            public Map getBean() throws RemoteException {
                Log.i("TAG", "==size" + mBeans.size());
                for (Map.Entry<String, Bean> entry : mBeans.entrySet()) {
//                    entry.getKey();
//                    entry.getValue();
                    Log.i("TAG", "==runservice" + entry.getKey());
                    Intent intentService = new Intent(entry.getKey());
                    intentService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startService(intentService);
                }
                return mBeans;
            }

            @Override
            public void addBean(Bean bean) throws RemoteException {
                Log.i("TAG", "addbea===========" + bean.getPkgname());
                /**
                 * 可以做加密验证数据的安全性
                 */
                mBeans.put(bean.getPkgname(), bean);
            }

            @Override
            public void registerServiceReceiver() throws RemoteException {
//                if (messageReceiver == null) {
//                    messageReceiver = new MessageReceiver();
//                    IntentFilter intentFilter = new IntentFilter();
//                    intentFilter.addAction(StringEnum.MESSAGE_AUTO);
//                    LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(messageReceiver, intentFilter);
//                }
            }

            @Override
            public void unregisterServiceReceiver() throws RemoteException {
//                if (messageReceiver != null) {
//                    LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(messageReceiver);
//                    messageReceiver = null;
//                }
            }
        };
//        }
        return iShareService;
    }

}
