package com.lzxmy.demo.marquee;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzxmy.demo.SimpleFactory;
import com.lzxmy.demo.singleton.Singleton1;
import com.lzxmy.demo.singleton.Singleton2;
import com.lzxmy.demo.singleton.Singleton3;
import com.lzxmy.demo.singleton.Singleton4;
import com.lzxmy.demo.singleton.Singleton5;
import com.lzxmy.demo.singleton.Singleton6;

/**
 * Created by apple on 2017/2/3.
 */

public class SingleFactory extends SimpleFactory {

    public SingleFactory(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(mactivity);
        linearLayout.setBackgroundColor(Color.WHITE);
        TextView textView = new TextView(mactivity);
        textView.setText("各种单例 Singleton1 的包下");
        linearLayout.addView(textView);
        mactivity.setContentView(linearLayout);
        Singleton1.getInstance();
        Singleton2.getInstance();
        Singleton3.INSTANCE.whateverMethod();
        Singleton4.getInstance();
        Singleton5.getSingleton();
        Singleton6.getInstance();
    }


}
