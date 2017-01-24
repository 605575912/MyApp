package com.lzxmy.demo.jar;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by apple on 15/8/31.
 */
public class JarDexActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        loadUninstallApk();
//        loadUninstallApk();

        Bundle paramBundle = new Bundle();
        paramBundle.putBoolean("KEY_START_FROM_OTHER_ACTIVITY", true);
        String dexpath = "/mnt/sdcard/XSW/hello-jni-debug.apk";
        String dexoutputpath = "/mnt/sdcard/";
        File dexOutputDir = getDir("dex", 0);
        LoadAPK(paramBundle, dexpath, dexOutputDir.getPath());
    }

    /**
     * @return void
     * @throws
     * @Title: loadUninstallApk
     * @Description: 动态加载未安装的apk
     */
    private void loadUninstallApk() {
        String path = Environment.getExternalStorageDirectory() + File.separator+"XSW/";
        Log.i("TAG", "Path" + path);
        String filename = "hello-jni-debug.apk";

        // 4.1以后不能够将optimizedDirectory设置到sd卡目录， 否则抛出异常.
        File optimizedDirectoryFile = getDir("dex", 0);
        DexClassLoader classLoader = new DexClassLoader(path + filename, optimizedDirectoryFile.getAbsolutePath(),
                null, getClassLoader());

        try {
            // 通过反射机制调用， 包名为com.example.loaduninstallapkdemo, 类名为UninstallApkActivity
            String pkgName = "com.example.hellojni";
            Class mLoadClass = classLoader.loadClass(pkgName+".HelloJni");
            Constructor constructor = mLoadClass.getConstructor(new Class[]{});
            Object testActivity = constructor.newInstance(new Object[]{});

            // 获取sayHello方法
//            Method helloMethod = mLoadClass.getMethod("sayHello", null);
//            helloMethod.setAccessible(true);
//            Object content = helloMethod.invoke(testActivity, null);
//            Toast.makeText(JarDexActivity.this, content.toString(), Toast.LENGTH_LONG).show();
            Class cls = getClassLoader().loadClass(pkgName + ".HelloJni") ;
            // 跳转到该Activity
            startActivity(new Intent(this, cls)) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void LoadAPK(Bundle paramBundle, String dexpath, String dexoutputpath) {
        ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
        DexClassLoader localDexClassLoader = new DexClassLoader(dexpath,
                dexoutputpath, null, localClassLoader);
        try {
            PackageInfo plocalObject = getPackageManager()
                    .getPackageArchiveInfo(dexpath, 1);

            if ((plocalObject.activities != null)
                    && (plocalObject.activities.length > 0)) {
                String activityname = plocalObject.activities[0].name;
//                Log.d(TAG, "activityname = " + activityname);

                Class localClass = localDexClassLoader.loadClass(activityname);
                Constructor localConstructor = localClass
                        .getConstructor(new Class[] {});
                Object instance = localConstructor.newInstance(new Object[] {});
//                Log.d(TAG, "instance = " + instance);

                Method localMethodSetActivity = localClass.getDeclaredMethod(
                        "setActivity", new Class[] { Activity.class });
                localMethodSetActivity.setAccessible(true);
                localMethodSetActivity.invoke(instance, new Object[] { this });

                Method methodonCreate = localClass.getDeclaredMethod(
                        "onCreate", new Class[] { Bundle.class });
                methodonCreate.setAccessible(true);
                methodonCreate.invoke(instance, new Object[] { paramBundle });
            }
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
