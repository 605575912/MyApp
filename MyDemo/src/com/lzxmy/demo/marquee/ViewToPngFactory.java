package com.lzxmy.demo.marquee;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzxmy.demo.SimpleFactory;
import com.lzxmy.demo.view.DlnaFrameLayout;

/**
 * Created by apple on 2017/2/3.
 */

public class ViewToPngFactory extends SimpleFactory {

    public ViewToPngFactory(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(mactivity);
        linearLayout.setBackgroundColor(Color.WHITE);
        TextView textView = new TextView(mactivity);
        textView.setText("使用 DlnaFrameLayout  父控件 addview ，即可生成图片");
        linearLayout.addView(textView);
        mactivity.setContentView(linearLayout);
        DlnaFrameLayout dlnaFrameLayout;
    }


}
