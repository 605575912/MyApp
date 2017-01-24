package cn.yzz.lol.share.utils;

import android.content.Context;
import android.content.SharedPreferences;

import cn.yzz.lol.share.ShareApplication;

/**
 * Created by liangzhenxiong on 15/11/16.
 */
public class ShareUtil {
    public static String highcode;
    public static float lowprice;
    public static float highprice;

    public static String gethighcode() {
        if (highcode == null) {
            SharedPreferences sharedPreferences = ShareApplication.application.getSharedPreferences(Action.sharename, Context.MODE_PRIVATE);
            highcode = sharedPreferences.getString(Action.HighCODE, "");
        }
        return highcode;
    }

    public static float getLowprice() {
        if (lowprice <= 0) {
            SharedPreferences sharedPreferences = ShareApplication.application.getSharedPreferences(Action.sharename, Context.MODE_PRIVATE);
            lowprice = sharedPreferences.getFloat(Action.LOWPRICE, 0);
        }
        return lowprice;
    }

    public static float getHighprice() {
        if (highprice <= 0) {
            SharedPreferences sharedPreferences = ShareApplication.application.getSharedPreferences(Action.sharename, Context.MODE_PRIVATE);
            highprice = sharedPreferences.getFloat(Action.HighPRICE, 0);
        }
        return highprice;
    }
}
