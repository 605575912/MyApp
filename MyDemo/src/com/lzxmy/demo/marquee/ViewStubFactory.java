package com.lzxmy.demo.marquee;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.lzxmy.demo.R;
import com.lzxmy.demo.SimpleFactory;

/**
 * Created by apple on 2017/2/3.
 */

public class ViewStubFactory extends SimpleFactory {

    public ViewStubFactory(Activity activity) {
        super(activity);
    }

    ViewStub viewStub;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mactivity.setContentView(R.layout.viewstub_layout);
        CheckBox checkBox = (CheckBox) mactivity.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewStub = (ViewStub) mactivity.findViewById(R.id.viewstub);
                if (viewStub != null) {
                    view = viewStub.inflate();
                }
                view.setBackgroundColor(Color.argb(255, 255, 255, (255 * Math.round(10))));
            }
        });

    }


}
