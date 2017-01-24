package com.lzx.hb.job;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.Toast;

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
public class UCAccessbilityJob extends BaseAccessbilityJob {


    /**
     * 微信的包名
     */
    private static final String WECHAT_PACKAGENAME = "cn.ninegame.gamemanager";
//    private static final String WECHAT_PACKAGENAME = "com.show.blue";

    /**
     * 红包消息的关键字
     */
    private static final String HONGBAO_TEXT_KEY = "[QQ红包]";

    /**
     * 不能再使用文字匹配的最小版本号
     */

    private boolean isFirstChecked;
    private Handler mHandler = null;

    BroadcastReceiver broadcastReceiver;

    @Override
    public void onCreateJob(QiangHongBaoService service) {
        super.onCreateJob(service);
        broadcastReceiver = new VPN();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.conn.Discon");
//
        getContext().registerReceiver(broadcastReceiver, filter);
    }

    class VPN extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("TAG", "className==dddddd=====");
            if (intent.getAction().indexOf("android.net.conn.Discon") > -1) {
                trycon = 0;
            } else {
                Bundle bundle = intent.getExtras();
                Log.i("TAG", "className=======");
                String KeyNI = "networkInfo";

                android.net.NetworkInfo bundleNetworkInfo = (android.net.NetworkInfo) bundle.get(KeyNI);

                if (bundleNetworkInfo.getTypeName().toString().equalsIgnoreCase("VPN")) {


                    vnpcon = bundleNetworkInfo.isConnected();

                    Log.i("TAG", "vnpcon=======" + vnpcon);
                    if (!vnpcon) {
                        trycon = 0;
                    }
                }
            }


        }
    }

    @Override
    public void onStopJob() {
        if (broadcastReceiver != null) {
            getContext().unregisterReceiver(broadcastReceiver);
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
    int C = 0;
    boolean vnpcon = false;
    int trycon = 0;
    int ActionType = 0;//0 查看  1 连接 -1 断开

    @Override
    public void onReceiveJob(AccessibilityEvent event) {
        final int eventType = event.getEventType();
        //通知栏事件

        CharSequence className = event.getClassName();
        Log.i("TAG", "className=======" + className);
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

            AccessibilityNodeInfo nodeInfo = getService().getRootInActiveWindow();
            if (event.getPackageName().equals("com.android.settings")) {
                if (vnpcon || trycon > 0) {
                    return;
                }
                if (event.getClassName().equals("com.android.settings.Settings$VpnSettingsActivity")) {

                    AccessibilityNodeInfo accessibilityNodeInfo = findChild(nodeInfo, "android.support.v7.widget.RecyclerView");
                    if (accessibilityNodeInfo == null) {
                        accessibilityNodeInfo = findChild(nodeInfo, "android.widget.ListView");
                        if (accessibilityNodeInfo == null) {
                            Toast.makeText(getContext(), "无VPN", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                        AccessibilityNodeInfo a = accessibilityNodeInfo.getChild(i);
                        if (a.isClickable()) {
                            trycon++;
                            a.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            return;
                        }

                    }

                } else if (event.getClassName().equals("com.android.settings.vpn2.ConfigDialog") || event.getClassName().equals("com.android.settings.SubSettings")) {


                    List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("已连接 VPN");
                    if (list != null && list.size() > 0) {

                        if (ActionType == -1) {
                            List<AccessibilityNodeInfo> listclose = nodeInfo.findAccessibilityNodeInfosByText("断开连接");
                            for (AccessibilityNodeInfo a : listclose) {
                                if (a.isClickable()) {
                                    a.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    return;

                                }
                            }
                        } else if (ActionType == 0) {

                        } else if (ActionType == 1) {
                            List<AccessibilityNodeInfo> clist = nodeInfo.findAccessibilityNodeInfosByText("连接");
                            for (AccessibilityNodeInfo a : clist) {
                                if (a.isClickable()) {
                                    a.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    return;

                                }
                            }
                        }


                    } else {
                        list = nodeInfo.findAccessibilityNodeInfosByText("已连接VPN");

                    }

                } else {
                    Intent vpnIntent = new Intent();
                    vpnIntent.setAction("android.net.vpn.SETTINGS");
                    vpnIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(vpnIntent);
                }


            } else {
                openHongBao(event);
            }
//            if (isnotify) {
//                isnotify = false;

//            if (C == 0) {
//                Resit(event);
//            } else {

//            }

//            }
        }
//        else if(eventType ==AccessibilityEvent.){
//
//        }
    }

    AccessibilityNodeInfo click(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo.isClickable()) {
            return accessibilityNodeInfo;
        } else {
            return click(accessibilityNodeInfo);
        }
    }

    private void Resit(AccessibilityEvent event) {
        CharSequence className = event.getClassName();
        final AccessibilityNodeInfo nodeInfo = getService().getRootInActiveWindow();
        if ("cn.ninegame.gamemanager.activity.HomeActivity".equals(className)) {//聊天界面


            if (nodeInfo == null) {
                return;
            }
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("九游");
            list.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);// 弹出注册
        } else if ("cn.ninegame.gamemanager.activity.DialogAccountActivity".equals(className)) {//注册框

            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("快速注册");
            if (list == null || list.isEmpty()) {

            } else {
                list.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);// 弹出注册
            }
            final List<AccessibilityNodeInfo> UClist = nodeInfo.findAccessibilityNodeInfosByText("UC号注册");
            if (UClist == null || UClist.isEmpty()) {

            } else {
                UClist.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);// 弹出注册
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AccessibilityNodeInfo accessibilityNodeInfo = findChild(nodeInfo, "android.widget.EditText");
                        if (accessibilityNodeInfo != null) {
                            SetText(accessibilityNodeInfo, "123456");
                        }
                    }
                }, 1000);
            }

        } else if (className.equals("com.show.blue.TestActivity")) {
            AccessibilityNodeInfo accessibilityNodeInfo = findChild(nodeInfo, "android.widget.EditText");
            SetText(accessibilityNodeInfo, "11111");
        }

    }

    void SetText(AccessibilityNodeInfo accessibilityNodeInfo, String text) {

        if (accessibilityNodeInfo != null) {
            accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            ClipboardManager clipboardManager = (ClipboardManager) getService().getSystemService(getService().CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", text);
            clipboardManager.setPrimaryClip(clip);
            accessibilityNodeInfo.performAction(AccessibilityNodeInfoCompat.ACTION_PASTE);
        }
    }


    AccessibilityNodeInfo findChild(AccessibilityNodeInfo nodeInfo, String reid) {
        if (nodeInfo == null) {
            return nodeInfo;
        }
        for (int t = 0; t < nodeInfo.getChildCount(); t++) {
            AccessibilityNodeInfo accessibilityNodeInfo = nodeInfo.getChild(t);
            if (accessibilityNodeInfo.getClassName().equals(reid)) {
                return accessibilityNodeInfo;
            }
            if (accessibilityNodeInfo.getChildCount() > 0) {
                findChild(accessibilityNodeInfo, reid);
            }


        }
        return null;
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
        CharSequence className = event.getClassName();
        if ("cn.ninegame.gamemanager.activity.HomeActivity".equals(className)) {//聊天界面
            handleLuckyMoneyReceive();
        } else if ("cn.ninegame.gamemanager.NinegameBizActivity".equals(className)) {//阅读量
            handleSearchReceive();
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
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("九游");
        for (AccessibilityNodeInfo accessibilityNodeInfo : list) {
            CharSequence charSequence = accessibilityNodeInfo.getClassName();
            if (charSequence.equals("android.widget.ImageButton")) {
                accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return;
            }
        }

    }

    private void handleSearchReceive() {
        final AccessibilityNodeInfo nodeInfo = getService().getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("论坛");
        for (AccessibilityNodeInfo accessibilityNodeInfo : list) {
            CharSequence charSequence = accessibilityNodeInfo.getClassName();
            if (charSequence.equals("android.widget.TextView")) {

                accessibilityNodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);

                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("吃完饭好想吃甜的");
                        for (AccessibilityNodeInfo accessibilityNodeInfo1 : list) {
                            accessibilityNodeInfo1.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    getService().performGlobalAction(AccessibilityService.GESTURE_SWIPE_UP);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    handleSearchReceive();
                                }
                            }, 2000);
                            return;
                        }
                    }
                }, 300);
//                accessibilityNodeInfo1.setText("妖神：山海经传说");

//                accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return;
            }
        }
//        targetNode = AccessibilityHelper.findNodeInfosByText(nodeInfo, "九游");


//        if (targetNode != null) {
//
////            targetNode.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
////            home(8000);
//        }

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
            mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    if (msg.what == 100) {


                    }
                }
            };
        }
        return mHandler;
    }


}
