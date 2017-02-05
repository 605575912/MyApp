package com.lzxmy.demo.singleton;

/**
 * 懒汉 单例模式  不安全,严格来说不算单例
 * Created by apple on 2017/2/6.
 */

public class Singleton1 {
    static Singleton1 singleton1;

    private Singleton1() {

    }

    public static Singleton1 getInstance() {
        if (singleton1 == null) {
            singleton1 = new Singleton1();
        }
        return singleton1;
    }
}
