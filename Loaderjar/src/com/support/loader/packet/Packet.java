package com.support.loader.packet;

/**
 * 消息的总体，在程序中 传递消息的
 *
 * @author lzx
 */
public interface Packet {
    /**
     * 处理方法
     */
    void handle();

    void stop();

    void start();


}
