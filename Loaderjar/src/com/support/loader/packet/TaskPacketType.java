package com.support.loader.packet;

/**
 * Created by apple on 15/7/5.
 */
public enum TaskPacketType {
    Task_IMAGE(1),Task_SAVE(2),Task_DATA(3);
    int Priority = 0;

    // 构造方法
     TaskPacketType(int Priority) {
        this.Priority = Priority;
    }

}
