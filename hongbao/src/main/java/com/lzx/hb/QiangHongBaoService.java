package com.lzx.hb;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.lzx.hb.job.AccessbilityJob;
import com.lzx.hb.job.QQAccessbilityJob;
import com.lzx.hb.job.UCAccessbilityJob;
import com.lzx.hb.job.VPNAccessbilityJob;
import com.lzx.hb.job.WechatAccessbilityJob;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class QiangHongBaoService extends AccessibilityService {


    private static final Class[] ACCESSBILITY_JOBS = {
            WechatAccessbilityJob.class, VPNAccessbilityJob.class,UCAccessbilityJob.class
    };

    private static QiangHongBaoService service;

    //    private Config mConfig;
    private List<AccessbilityJob> mAccessbilityJobs;

    @Override
    public void onCreate() {
        super.onCreate();

        mAccessbilityJobs = new ArrayList<AccessbilityJob>();
//        mConfig = new Config(this);

        //初始化辅助插件工作
        for (Class clazz : ACCESSBILITY_JOBS) {
            try {
                Object object = clazz.newInstance();
                if (object instanceof AccessbilityJob) {
                    AccessbilityJob job = (AccessbilityJob) object;
                    job.onCreateJob(this);
                    mAccessbilityJobs.add(job);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAccessbilityJobs != null && !mAccessbilityJobs.isEmpty()) {
            for (AccessbilityJob job : mAccessbilityJobs) {
                job.onStopJob();
            }
            mAccessbilityJobs.clear();
        }
        service = null;
        mAccessbilityJobs = null;
//        //发送广播，已经断开辅助服务
//        Intent intent = new Intent(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
//        sendBroadcast(intent);
    }

    @Override
    public void onInterrupt() {
        Toast.makeText(this, "中断抢红包服务", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        service = this;
        //发送广播，已经连接上了
//        Intent intent = new Intent(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
//        sendBroadcast(intent);
        Toast.makeText(this, "已连接抢红包服务", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String pkn = String.valueOf(event.getPackageName());
        if (mAccessbilityJobs != null && !mAccessbilityJobs.isEmpty()) {

            for (AccessbilityJob job : mAccessbilityJobs) {
                if ((job.getTargetPackageName().indexOf(pkn)>-1)) {
                    job.onReceiveJob(event);
                }
            }
        }
    }

    public static void lockHome() {
        if (service == null) {
            return;
        }
        SharedPreferences sharedPreferences = service.getSharedPreferences("hb_lzx", Context.MODE_PRIVATE);
        boolean islock = sharedPreferences.getBoolean("islock", false);
        if (islock) {
            ComponentName componet = new ComponentName("com.lzx.hb", "com.lzx.hb.MainActivity");
            Intent i = new Intent();
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setComponent(componet);
            service.startActivity(i);
        }
    }
//    public Config getConfig() {
//        return mConfig;
//    }

    /**
     * 判断当前服务是否正在运行
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isRunning() {
        if (service == null) {
            return false;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);
        AccessibilityServiceInfo info = service.getServiceInfo();
        if (info == null) {
            return false;
        }
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        Iterator<AccessibilityServiceInfo> iterator = list.iterator();

        boolean isConnect = false;
        while (iterator.hasNext()) {
            AccessibilityServiceInfo i = iterator.next();
            if (i.getId().equals(info.getId())) {
                isConnect = true;
                break;
            }
        }
        if (!isConnect) {
            return false;
        }
        return true;
    }

}
