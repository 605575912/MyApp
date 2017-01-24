package cn.yzz.lol.share;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.Vibrator;

import com.support.loader.ServiceLoader;

import java.lang.ref.WeakReference;

import cn.yzz.lol.share.bean.FiveItem;
import cn.yzz.lol.share.factory.ChatListen;
import cn.yzz.lol.share.factory.DataItem;
import cn.yzz.lol.share.packet.FivePacket;
import cn.yzz.lol.share.packet.SharePacket;


public class CatchService extends Service {
    ChatServerIBinder iBinder;
    String code = "";//3129  886
    boolean isrun = false;

    public class MyServiceImpl extends IMyService.Stub {

        public String getValue(boolean isrun) throws RemoteException {
            if (!CatchService.this.isrun) {
                if (isrun) {
                    isVibrator = false;
                    handler.sendEmptyMessageDelayed(1, 1000);
                }
            }
            if (!isrun) {
                handler.sendEmptyMessage(110);
            }
            CatchService.this.isrun = isrun;
            return "Android 链接成功";
        }

        @Override
        public void answerRingingCall(String code) throws RemoteException {
            CatchService.this.code = code;
        }


    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (handler == null) {
            handler = new MyHandler(this);
        }
    }

    public class ChatServerIBinder extends MyServiceImpl {
        ChatListen chatListen;

        public ChatListen getChatListen() {
            return chatListen;
        }

        public void setChatListens(ChatListen chatListen) {
            this.chatListen = chatListen;
        }

    }

    boolean isVibrator = false;
    MyHandler handler;
    Vibrator vibrator;

    void setVibrator(Context context) {
        if (vibrator == null) {
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
        if (!vibrator.hasVibrator())
            return;
        long[] pattern = {1000, 400, 1000, 400};   // 停止 开启 停止 开启
        vibrator.vibrate(pattern, 2);

    }

    public IBinder onBind(Intent intent) {
        if (iBinder == null) {
            iBinder = new ChatServerIBinder();
        }
        return iBinder;
    }

    SharePacket sharePacket = new SharePacket();
    FivePacket fivePacket = new FivePacket();
    DataItem dataItem = new DataItem();
    FiveItem fiveItem = new FiveItem();

    void updatedata() {
        if (code.equals("")) {
            code = "2318";
        }
        sharePacket.setHandler(handler, 1, code);
        sharePacket.setDataItem(dataItem);
        ServiceLoader.getInstance().sendPacket(sharePacket);
        if (code.equals("2318")) {
            return;
        }
        fivePacket.setHandler(handler, 3, code);
        fivePacket.setFiveItem(fiveItem);
        ServiceLoader.getInstance().sendPacket(fivePacket);
    }

    static class MyHandler extends Handler {
        WeakReference<CatchService> mActivityReference;

        MyHandler(CatchService activity) {
            mActivityReference = new WeakReference<CatchService>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final CatchService activity = mActivityReference.get();
            if (activity != null) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    if (activity.isrun) {
                        activity.handler.sendEmptyMessageDelayed(2, 2000);
                    }
                    activity.iBinder.getChatListen().connectSuccess(msg.obj);
                } else if (msg.what == 2) {
                    if (activity.isrun) {
                        activity.updatedata();
                    }
                } else if (msg.what == 3) {
                    activity.iBinder.getChatListen().connectSuccess(msg.obj);
                    if (msg.arg1 == 1) {
                        if (!activity.isVibrator) {
                            activity.isVibrator = true;
                            activity.setVibrator(activity);
                            activity.handler.removeMessages(110);
                            activity.handler.sendEmptyMessageDelayed(110, 30000);
                        }
                    } else {
                        activity.handler.sendEmptyMessage(110);
                    }
                    if (!activity.isrun) {
                        activity.handler.sendEmptyMessage(110);
                    }
                } else if (msg.what == 110) {
                    activity.isVibrator = false;
                    if (activity.vibrator != null) {
                        activity.vibrator.cancel();
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
