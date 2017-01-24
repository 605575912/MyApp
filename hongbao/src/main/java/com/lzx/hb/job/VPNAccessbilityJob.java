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
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.lzx.hb.QiangHongBaoService;

import java.util.List;

//import com.codeboy.qianghongbao.Config;

/**
 * <p>Created 16/1/16 上午12:40.</p>
 * <p><a href="mailto:codeboy2013@gmail.com">Email:codeboy2013@gmail.com</a></p>
 * <p><a href="http://www.happycodeboy.com">LeonLee Blog</a></p>
 *
 * @author LeonLee
 */
public class VPNAccessbilityJob extends BaseAccessbilityJob {


    /**
     * 微信的包名
     */
    private static final String WECHAT_PACKAGENAME = "com.qihoo.appstore";

    /**
     * 红包消息的关键字
     */

    /**
     * 不能再使用文字匹配的最小版本号
     */

    private boolean isFirstChecked;
    private Handler mHandler = null;
    BroadcastReceiver broadcastReceiver;

    @Override
    public void onCreateJob(QiangHongBaoService service) {
        super.onCreateJob(service);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.net.conn.Discon")) {
                    Action = 1;
                }
            }
        };
        IntentFilter filter = new IntentFilter();
//        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.conn.Discon");
//
        getContext().registerReceiver(broadcastReceiver, filter);
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
    int Action = -1;// -1 断开 0 查看  1 连接 2 连接中

    @Override
    public void onReceiveJob(AccessibilityEvent event) {
        final int eventType = event.getEventType();
        //通知栏事件

        CharSequence className = event.getClassName();
        Log.i("TAG", "className=======" + className);
        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
        } else if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {

            AccessibilityNodeInfo nodeInfo = getService().getRootInActiveWindow();
            if (event.getPackageName().equals("com.android.settings")) {
                if (event.getClassName().equals("com.android.settings.Settings$VpnSettingsActivity")) {
                    ClickVpn(nodeInfo);
                } else if (event.getClassName().equals("com.android.settings.vpn2.ConfigDialog") || event.getClassName().equals("com.android.settings.SubSettings")) {
                    showDialog(nodeInfo, Action);
                } else {
                    Intent vpnIntent = new Intent();
                    vpnIntent.setAction("android.net.vpn.SETTINGS");
                    vpnIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(vpnIntent);
                }


            } else {
            }
        }
    }

    /**
     * 点击VPN
     *
     * @param nodeInfo
     */
    void ClickVpn(AccessibilityNodeInfo nodeInfo) {
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
                a.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return;
            }

        }
    }

    /**
     * 弹出连接 或者断开窗口
     *
     * @param nodeInfo
     */
    void showDialog(final AccessibilityNodeInfo nodeInfo, int ActionType) {
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("已连接 VPN");
        if (list != null && list.size() > 0) {

            if (ActionType == -1) {
                List<AccessibilityNodeInfo> listclose = nodeInfo.findAccessibilityNodeInfosByText("断开连接");
                for (AccessibilityNodeInfo a : listclose) {
                    if (a.isClickable()) {
                        a.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ClickVpn(nodeInfo);
                            }
                        }, 500);
                        return;

                    }
                }
            }


        } else {
            list = nodeInfo.findAccessibilityNodeInfosByText("连接到vpn");
            if (list != null && list.size() > 0) {
                if (ActionType == 1) {
                    List<AccessibilityNodeInfo> clist = nodeInfo.findAccessibilityNodeInfosByText("连接");
                    for (AccessibilityNodeInfo a : clist) {
                        if (a.isClickable()) {
                            a.performAction(AccessibilityNodeInfo.ACTION_CLICK);

                            ConnectDelayed(nodeInfo);
                            return;

                        }
                    }
                }

            }
        }
    }

    boolean isVnpconnect(AccessibilityNodeInfo nodeInfo) {
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("已连接 VPN");
        if (list != null && list.size() > 0) {

            return true;
        }
        return false;
    }

    void ConnectDelayed(final AccessibilityNodeInfo nodeInfo) {
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ClickVpn(nodeInfo);
//                    HandDelayed(nodeInfo);

            }
        }, 5000);
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
