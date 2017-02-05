package com.lzxmy.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by lzx on 2016/11/11.
 */

public class SimpleFactory {
    public Activity mactivity;

    public SimpleFactory(Activity mactivity) {
        this.mactivity = mactivity;
    }

    public SimpleFactory() {

    }

    protected void onCreate(Bundle savedInstanceState) {
    }

    protected void onSaveInstanceState(Bundle outState) {
    }

    protected void onPause() {

    }


    protected void onStop() {

    }


    protected void onPostCreate(Bundle savedInstanceState) {

    }


    protected void onRestart() {

    }


    protected void onStart() {

    }


    protected void onNewIntent(Intent intent) {

    }

    public void onConfigurationChanged(Configuration newConfig) {
    }

    public void onWindowFocusChanged(boolean hasFocus) {
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }


    protected void onResume() {

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }


    protected void onDestroy() {
    }
}
