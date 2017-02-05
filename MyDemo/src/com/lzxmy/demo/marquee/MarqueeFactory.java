package com.lzxmy.demo.marquee;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewConfiguration;

import com.lzxmy.demo.R;
import com.lzxmy.demo.SimpleFactory;
import com.support.loader.utils.ReflectHelper;

/**
 * Created by apple on 2017/2/3.
 */

public class MarqueeFactory extends SimpleFactory {

    public MarqueeFactory(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFadingMarquee();
        mactivity.setContentView(R.layout.marquee_layout);
    }

    private void setFadingMarquee() {
        ViewConfiguration vc = ViewConfiguration.get(mactivity);
        ViewConfiguration viewConfiguration;
        Object obj = ReflectHelper.getDeclaredFieldValue(vc,
                ViewConfiguration.class.getName(), "mFadingMarqueeEnabled");
        if (obj != null && obj instanceof Boolean) {
            Boolean enabled = (Boolean) obj;
            if (!enabled) {
                ReflectHelper.setDeclaredFieldValue(vc,
                        ViewConfiguration.class.getName(),
                        "mFadingMarqueeEnabled", Boolean.TRUE);
            }
        }
    }
}
