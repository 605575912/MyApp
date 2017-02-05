package com.lzxmy.demo.view;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.support.loader.utils.LogBlue;

/**
 * Created by lzx on 2016/11/17.
 */

public class ARScrollView extends ScrollView {
    public ARScrollView(Context context) {
        super(context);
        setVerticalScrollBarEnabled(false);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogBlue.i("TAG", "执行 =   onTouchEvent" + event.getAction());
        boolean s = super.onTouchEvent(event);
        LogBlue.i("TAG", s + " =   onTouchEvent" + event.getAction());
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogBlue.i("TAG", "执行  =  onInterceptTouchEvent" + ev.getAction());
//        boolean s = super.onInterceptTouchEvent(ev);
//        LogBlue.i("TAG", s + "    onInterceptTouchEvent" + ev.getAction());
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogBlue.i("TAG", "执行 =   dispatchTouchEvent" + ev.getAction());
        boolean s = super.dispatchTouchEvent(ev);
        LogBlue.i("TAG", s + "  =  dispatchTouchEvent" + ev.getAction());
        return s;
    }
}

