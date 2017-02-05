package com.lzxmy.demo.marquee;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzxmy.demo.SimpleFactory;
import com.lzxmy.demo.view.DlnaFrameLayout;
import com.support.loader.ServiceLoader;

/**
 * Created by apple on 2017/2/3.
 */

public class HandlerFactory extends SimpleFactory {

    public HandlerFactory(Activity activity) {
        super(activity);
    }

    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(mactivity);
        linearLayout.setBackgroundColor(Color.WHITE);
        TextView textView = new TextView(mactivity);
        textView.setText("使用 Looper.prepare");
        linearLayout.addView(textView);
        mactivity.setContentView(linearLayout);
        DlnaFrameLayout dlnaFrameLayout;

        ServiceLoader.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                h = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Log.i("TAG", "=====");
                        if (msg.what == 2) {
                            h.getLooper().quit();
                        }
                        sendEmptyMessageDelayed(1, 5000);
                    }
                };
                Looper.loop();
                Log.i("TAG", "==+++++++===");

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        h.getLooper().quit();
    }
}
