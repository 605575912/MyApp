package com.lzxmy.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by lzx on 2016/11/11.
 */

public class PoxyActivity extends Activity {
    public static String fromclassname = "fromclassname";
    SimpleFactory simpleFactory;

    public static Intent startIntent(Intent intent, Context context, String factroyclassname) {
        if (intent == null) {
            intent = new Intent();
        }
        intent.putExtra(PoxyActivity.fromclassname, factroyclassname);
        intent.setClass(context, PoxyActivity.class);
        return intent;
    }

    public static Intent startIntent(Context context, String factroyclassname) {
        Intent intent = new Intent();
        return startIntent(intent,context,factroyclassname);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        String classname = getIntent().getStringExtra(fromclassname);
        initClass(classname, savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        simpleFactory.onSaveInstanceState(outState);
    }

    void initClass(String classname, Bundle savedInstanceState) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(classname);
            try {
                Constructor c1 = clazz.getConstructor(new Class[]{Activity.class});
                c1.setAccessible(true);
                simpleFactory = (SimpleFactory) c1.newInstance(new Object[]{this});
                simpleFactory.onCreate(savedInstanceState);
                return;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        simpleFactory = new SimpleFactory();
    }

    @Override
    public void onPause() {
        super.onPause();
        simpleFactory.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        simpleFactory.onStop();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        simpleFactory.onPostCreate(savedInstanceState);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        simpleFactory.onRestart();
    }

    @Override
    public void onStart() {
        super.onStart();
        simpleFactory.onStart();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        simpleFactory.onNewIntent(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean h = simpleFactory.onKeyDown(keyCode, event);
        if (h) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        simpleFactory.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        simpleFactory.onConfigurationChanged(newConfig);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        simpleFactory.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean h = simpleFactory.dispatchKeyEvent(event);
        if (h) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean h = simpleFactory.onKeyUp(keyCode, event);
        if (h) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return simpleFactory.onTouchEvent(event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        simpleFactory.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        simpleFactory.onDestroy();
    }
}
