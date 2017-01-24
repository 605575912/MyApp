package com.lzx.hb.job;

import android.view.accessibility.AccessibilityEvent;

import com.lzx.hb.QiangHongBaoService;


public interface AccessbilityJob {
    String getTargetPackageName();
    void onCreateJob(QiangHongBaoService service);
    void onReceiveJob(AccessibilityEvent event);
    void onStopJob();
//    boolean isEnable();
}
