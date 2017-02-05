package com.lzxmy.demo.marquee;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import com.lzxmy.demo.R;
import com.lzxmy.demo.SimpleFactory;
import com.lzxmy.demo.view.TagContentView;

/**
 * Created by apple on 2017/2/3.
 */

public class ViewInitFactory extends SimpleFactory {

    public ViewInitFactory(Activity activity) {
        super(activity);
    }

    ViewStub viewStub;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mactivity.setContentView(R.layout.viewinit_layout);
        TagContentView tagContentView = (TagContentView) mactivity.findViewById(R.id.linear);
        tagContentView.setName("title", new String[]{"121", "3434", "---==e=r=er=er="});
    }


}
