package com.support.loader.proguard;

/**
 * Created by liangzhenxiong on 15/12/28.
 */
public interface IProguard {
    /**
     * 用于保护指定类的成员不能被混淆，所有JSON数据类
     * @author lhy
     *
     */
     interface ProtectMembers{}
    /**
     * 保护类名及成员，主要保护含native方法、实现了Android API接口的类
     * @author lhy
     *
     */
     interface ProtectClassAndMembers{}

    /**
     * 保护类名及构造函数
     * @author lhy
     *
     */
     interface ProtectClassAndConstruct{}
    /**
     * 用于保护指定类名不能被混淆
     * @author lhy
     *
     */
     interface ProtectClass{}

    /**
     * 用于保护构造函数不能被混淆
     * @author lhy
     *
     */
     interface ProtectConstructs{}
}
