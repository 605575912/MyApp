package com.support.loader.packet;

/**
 * Created by apple on 15/7/5.
 */
public enum LoaderType {
    LOADER_CREATE(1), LOADER_PAUSE(2), LOADER_STOP(3), LOADER_START(4), LOAD_UPDATE(5), LOAD_HOMEDATA(6)
    , LOAD_MYCENTERDATA(7);
    int Priority = 0;

    // 构造方法
    LoaderType(int Priority) {
        this.Priority = Priority;
    }

}
