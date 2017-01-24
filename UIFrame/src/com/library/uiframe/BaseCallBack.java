package com.library.uiframe;

import android.os.Message;

/**
 * Created by liangzhenxiong on 16/1/14.
 */
public interface BaseCallBack {

    void handleMessage(Message msg);

    /**
     * 延迟加载View
     */
    void initViewDelaod();
}
