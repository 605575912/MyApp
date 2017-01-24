package com.support.loader.packet;

/**
 * 主要是监听事件的
 * Created by apple on 15/7/8.
 */
public interface TaskPacketListenner {
    void start(Object object);

    void stop();

    void handle(Object object);
}
