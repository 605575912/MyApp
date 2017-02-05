package com.lzxmy.demo.marquee;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.lzxmy.demo.SimpleFactory;
import com.lzxmy.demo.dragger.AppComponent;
import com.lzxmy.demo.dragger.AppModule;
import com.lzxmy.demo.dragger.DaggerTextView;
import com.lzxmy.demo.dragger.MainFragmentComponent;

/**
 * Created by apple on 2017/2/3.
 */

public class DaggerAppComponentFactory extends SimpleFactory {

    public DaggerAppComponentFactory(Activity activity) {
        super(activity);
    }

    AppComponent appComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(mactivity);
        linearLayout.setBackgroundColor(Color.WHITE);
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(mactivity)).build();
        appComponent.inject(this);
        DaggerTextView textView = new DaggerTextView(mactivity, this);
//        textView.setText("使用 DlnaFrameLayout  父控件 addview ，即可生成图片");
        linearLayout.addView(textView);
        mactivity.setContentView(linearLayout);


    }

    public MainFragmentComponent getappComponent() {
        return appComponent.mainFragmentComponent();
    }

}
