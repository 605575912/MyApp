package com.lzx.hb.job;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.lzx.hb.QiangHongBaoService;
import com.lzx.hb.util.AccessibilityHelper;

import java.util.List;

//import com.codeboy.qianghongbao.Config;

/**
 * <p>Created 16/1/16 上午12:40.</p>
 * <p><a href="mailto:codeboy2013@gmail.com">Email:codeboy2013@gmail.com</a></p>
 * <p><a href="http://www.happycodeboy.com">LeonLee Blog</a></p>
 *
 * @author LeonLee
 */
public class QQAccessbilityJob extends BaseAccessbilityJob {


    private static final String BUTTON_CLASS_NAME = "android.widget.Button";

    /**
     * 微信的包名
     */
    private static final String WECHAT_PACKAGENAME = "com.tencent.mobileqq";

    /**
     * 红包消息的关键字
     */
    private static final String HONGBAO_TEXT_KEY = "[QQ红包]";

    /**
     * 不能再使用文字匹配的最小版本号
     */
    private static final int USE_ID_MIN_VERSION = 700;// 6.3.8 对应code为680,6.3.9对应code为700

    private boolean isFirstChecked;
    private PackageInfo mWechatPackageInfo = null;
    private Handler mHandler = null;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新安装包信息
            updatePackageInfo();
        }
    };

    @Override
    public void onCreateJob(QiangHongBaoService service) {
        super.onCreateJob(service);

    }

    @Override
    public void onStopJob() {
        try {
            getContext().unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
        }
    }

//    @Override
//    public boolean isEnable() {
//        return getConfig().isEnableWechat();
//    }

    @Override
    public String getTargetPackageName() {
        return WECHAT_PACKAGENAME;
    }

//    boolean isnotify = false;

    @Override
    public void onReceiveJob(AccessibilityEvent event) {
        final int eventType = event.getEventType();
        //通知栏事件
        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            List<CharSequence> texts = event.getText();
            if (!texts.isEmpty()) {
                for (CharSequence t : texts) {
                    String text = String.valueOf(t);
                    if (text.contains(HONGBAO_TEXT_KEY)) {
                        openNotify(event);
//                        isnotify = true;
                        break;
                    }
                }
            }
        } else if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
//            if (isnotify) {
//                isnotify = false;
            openHongBao(event);
//            }
        }
    }

    /**
     * 打开通知栏消息
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openNotify(AccessibilityEvent event) {
        if (event.getParcelableData() == null || !(event.getParcelableData() instanceof Notification)) {
            return;
        }

        //以下是精华，将微信的通知栏消息打开
        Notification notification = (Notification) event.getParcelableData();
        PendingIntent pendingIntent = notification.contentIntent;

        isFirstChecked = true;
        try {
            pendingIntent.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openHongBao(AccessibilityEvent event) {
        if ("com.tencent.mobileqq.activity.SplashActivity".equals(event.getClassName())) {//聊天界面
            handleLuckyMoneyReceive();
        } else {
            home();
        }

    }

//    boolean ischeck = false;

    /**
     * 点击聊天里的红包后，显示的界面
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void handleLuckyMoneyReceive() {
        AccessibilityNodeInfo nodeInfo = getService().getRootInActiveWindow();
        if (nodeInfo == null && isFirstChecked) {
            return;
        }
        AccessibilityNodeInfo targetNode = null;
        int wechatVersion = getWechatVersion();

//        if (wechatVersion < USE_ID_MIN_VERSION) {
        targetNode = AccessibilityHelper.findNodeInfosByText(nodeInfo, "点击拆开");
//        } else {
//            String buttonId = null;
//
//            if (wechatVersion == 700) {
//                buttonId = "com.tencent.mm:id/b2c";
//            } else if (wechatVersion <= 740) {
//                buttonId = "com.tencent.mm:id/b43";
//            } else {
//                buttonId = "com.tencent.mm:id/b43";
//            }
//
//            if (buttonId != null) {
//                targetNode = AccessibilityHelper.findNodeInfosById(nodeInfo, buttonId);
//            }
//
//            if (targetNode == null) {
//                //分别对应固定金额的红包 拼手气红包
//                AccessibilityNodeInfo textNode = AccessibilityHelper.findNodeInfosByTexts(nodeInfo, "发了一个红包", "给你发了一个红包", "发了一个红包，金额随机");
//                if (textNode != null) {
//                    for (int i = 0; i < textNode.getChildCount(); i++) {
//                        AccessibilityNodeInfo node = textNode.getChild(i);
//                        if (BUTTON_CLASS_NAME.equals(node.getClassName())) {
//                            targetNode = node;
//                            break;
//                        }
//                    }
//                }
//            }
//
//            if (targetNode == null) { //通过组件查找
//                targetNode = AccessibilityHelper.findNodeInfosByClassName(nodeInfo, BUTTON_CLASS_NAME);
//            }
//        }

//        } else if (event == Config.WX_AFTER_OPEN_SEE) { //看一看
//            if (getWechatVersion() < USE_ID_MIN_VERSION) { //低版本才有 看大家手气的功能
//                targetNode = AccessibilityHelper.findNodeInfosByText(nodeInfo, "看看大家的手气");
//            }
//        } else {
//        }

        if (targetNode != null) {

            targetNode.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            home(8000);
        } else if ((targetNode = AccessibilityHelper.findNodeInfosByText(nodeInfo, "口令红包")) != null) {
            final AccessibilityNodeInfo n = targetNode;
            n.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);


            final AccessibilityNodeInfo targetNode1 = AccessibilityHelper.findNodeInfosByText(nodeInfo, "点击输入口令");


            if (targetNode1 == null) {
                home();
            } else {
                final AccessibilityNodeInfo targesend = AccessibilityHelper.findNodeInfosByText(nodeInfo, "发送");

                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        targetNode1.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        targesend.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        home();

                    }
                }, 200);

            }
        } else {
            home();
        }

    }

    void home() {
        home(3000);
    }

    void home(long time) {
        if (isFirstChecked) {
            isFirstChecked = false;
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getService().performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);

                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            QiangHongBaoService.lockHome();
                        }
                    }, 800);

                }
            }, time);
        }
    }


    private Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    /**
     * 获取微信的版本
     */
    private int getWechatVersion() {
        if (mWechatPackageInfo == null) {
            return 0;
        }
        return mWechatPackageInfo.versionCode;
    }

    /**
     * 更新微信包信息
     */
    private void updatePackageInfo() {
        try {
            mWechatPackageInfo = getContext().getPackageManager().getPackageInfo(WECHAT_PACKAGENAME, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
