package com.lzxmy.demo.newInstance;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.lzxmy.demo.R;
import com.lzxmy.demo.SimpleFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by apple on 2017/2/3.
 */

public class NewInstanceFactory extends SimpleFactory {

    public NewInstanceFactory(Activity activity) {
        super(activity);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mactivity.setContentView(R.layout.aild_layout);


        Class<?> clazz = null;
        try {
            clazz = Class.forName(StringIntstance.class.getName());
            Constructor c1 = clazz.getConstructor(new Class[]{String.class});
            c1.setAccessible(true);
            IntstanceInterFace intstanceInterFace = (IntstanceInterFace) c1.newInstance(new Object[]{"1"});
            intstanceInterFace.setString();

            Class<?> clazz1 = Class.forName(StringIntstance.class.getName());
            StringIntstance stringIntstance = (StringIntstance) clazz1.newInstance();
            //getDeclaredMethod（）  可获取 公共、保护、默认（包）访问和私有方法，但不包括继承的方法。
            //getMethod（） 只可获取公共的方法
            Method method = clazz1.getDeclaredMethod("getString");
            //值为true时 反射的对象在使用时 应让一切已有的访问权限取消
            method.setAccessible(true);
            method.invoke(stringIntstance);
            Field field = clazz1.getDeclaredField("proprety");
            field.setAccessible(true);
            field.set(stringIntstance, "Java反射机制");
            Log.i("TAG", "proprety" + stringIntstance.proprety);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

    }


}
