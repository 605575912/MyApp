package com.lzxmy.demo.newInstance;

import android.util.Log;

/**
 * Created by apple on 2017/2/3.
 */

public class StringIntstance implements IntstanceInterFace {
    String proprety = "proprety";

    public StringIntstance(String s) {

    }

    public StringIntstance() {

    }

    private void getString() {
        Log.i("TAG", "getString");
    }

    @Override
    public void setString() {
        Log.i("TAG", "Success");
    }
}
