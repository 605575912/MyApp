package com.support.loader.packet;


import com.support.loader.ServiceLoader;
import com.support.loader.proguard.IProguard;

/**
 * Created by apple on 15/7/5.
 */
public class TaskPacket implements Packet, IProguard {

    public TaskPacketType getPriority() {
        return Priority;
    }

    public void setPriority(TaskPacketType priority) {
        Priority = priority;
    }

    TaskPacketType Priority = TaskPacketType.Task_IMAGE;
    long time = 0;

    public TaskPacket() {
        time = System.currentTimeMillis();
    }


    public String getTaskId() {
        if (TaskId.equals("")) {
            TaskId = getClass().toString() + System.currentTimeMillis();
        }
        return TaskId;
    }




    public void setTaskId(String taskId) {
        TaskId = taskId;
    }

    String TaskId = "";
    String TaskTypeName = "";

    public String getTaskTypeName() {
        return TaskTypeName;
    }

    public void setTaskTypeName(String taskTypeName) {
        TaskTypeName = taskTypeName;
    }

    @Override
    public void start() {

    }

    @Override
    public void handle() {

    }

    @Override
    public void stop() {

        ServiceLoader.getInstance().stopLoader(getTaskTypeName());
    }
}
