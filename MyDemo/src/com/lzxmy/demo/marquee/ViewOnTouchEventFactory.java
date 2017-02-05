package com.lzxmy.demo.marquee;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import com.lzxmy.demo.SimpleFactory;
import com.lzxmy.demo.view.ARDialogView;

/**
 * Created by apple on 2017/2/3.
 */

public class ViewOnTouchEventFactory extends SimpleFactory {

    public ViewOnTouchEventFactory(Activity activity) {
        super(activity);
    }

    ViewStub viewStub;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARDialogView arDialogView = new ARDialogView(mactivity);
        mactivity.setContentView(arDialogView);
        arDialogView.showTip("23232");
    }


}
