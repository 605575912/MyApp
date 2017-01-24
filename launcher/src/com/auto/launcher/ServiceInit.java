package com.auto.launcher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by liangzhenxiong on 16/1/22.
 */
public class ServiceInit {
    static IShareService ishareservice;
    static ServiceConnection mServiceConnection;
    static final String ServiceNAME = "com.auto.launcher.ShareService";
    static boolean isrun = true;

    //    Context context;
    public static void start(final Context context, final ConnectListener connectListener) {
        Intent intentService = new Intent();
        ComponentName componetName = new ComponentName("com.auto.launcher", "com.auto.launcher.ShareService");
        intentService.setComponent(componetName);
        intentService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (mServiceConnection == null) {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    ishareservice = IShareService.Stub.asInterface(service);
                    if (connectListener != null) {
                        connectListener.onServiceConnected(name, ishareservice);
                    }
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                ishareservice = null;
            }
        };
        context.bindService(intentService,
                mServiceConnection, Context.BIND_AUTO_CREATE);
//        }

        context.startService(intentService);
    }

    public static void unbindService(Context context) {
        if (ishareservice != null && mServiceConnection != null) {
            try {
                context.unbindService(mServiceConnection);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

        }
    }

    public static void stop(Context context) {
        isrun = false;
        unbindService(context);
        try {
            ishareservice.unregisterServiceReceiver();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(ServiceNAME);
        context.stopService(intent);
    }
}
