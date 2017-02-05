package com.lzxmy.demo.singleton;

/**
 * 懒汉
 * 这种写法能够在多线程中很好的工作，而且看起来它也具备很好的lazy loading，但是，遗憾的是，效率很低，99%情况下不需要同步。
 * <p>
 * Created by apple on 2017/2/6.
 */

public class Singleton6 {
    private static Singleton6 instance;

    private Singleton6() {
    }

    public static synchronized Singleton6 getInstance() {
        if (instance == null) {
            instance = new Singleton6();
        }
        return instance;
    }
}

