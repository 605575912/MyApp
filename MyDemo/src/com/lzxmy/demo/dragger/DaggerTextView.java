package com.lzxmy.demo.dragger;

import android.content.Context;
import android.widget.TextView;

import com.lzxmy.demo.marquee.DaggerAppComponentFactory;

import javax.inject.Inject;

/**
 * Created by apple on 2017/2/5.
 */

public class DaggerTextView extends TextView {
    @Inject
    protected String name = "";
    @Inject
    protected String tst = "";
    int tag = 0;

    @Inject
    public int getITag() {
        return tag;
    }

    public String getName() {
        return name;
    }

    public DaggerTextView(Context context, DaggerAppComponentFactory daggerAppComponentFactory) {
        super(context);
        ((DaggerAppComponentFactory) daggerAppComponentFactory).getappComponent().inject(this);
        setText(name);
    }


}
