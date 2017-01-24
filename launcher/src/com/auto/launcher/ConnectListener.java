package com.auto.launcher;

import android.content.ComponentName;

/**
 * Created by liangzhenxiong on 16/1/23.
 */
public interface ConnectListener {
    void onServiceConnected(ComponentName name, IShareService ishareservice);
}
