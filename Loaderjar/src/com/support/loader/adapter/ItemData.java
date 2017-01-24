package com.support.loader.adapter;

/**
 * Created by liangzhenxiong on 16/3/29.
 */
public abstract class ItemData implements ItemView {
    Class aclass;

    public Class getAclass() {
        return aclass;
    }

    public void setAclass(Class aclass) {
        this.aclass = aclass;
    }
}
