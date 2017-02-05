package com.lzxmy.demo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.support.loader.utils.LogBlue;

/**
 * Created by apple on 2016/11/2.
 */

public class VTextView extends TextView {
    public VTextView(Context context) {
        super(context);
    }

    public VTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int measurewidth = measure(widthMeasureSpec);
//        int measureheight = measure(heightMeasureSpec);
//        setMeasuredDimension(100, 100);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            LogBlue.i("TAG", "onTouchEvent===ACTION_DOWN");
//            return true;
        }
        LogBlue.i("TAG",  "执行    onTouchEvent======" + event.getAction());
        boolean s = super.onTouchEvent(event);
        LogBlue.i("TAG", s + "    onTouchEvent======" + event.getAction());
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogBlue.i("TAG", "执行    dispatchTouchEvent======" + ev.getAction());
        boolean s = super.dispatchTouchEvent(ev);
        LogBlue.i("TAG", s + "    dispatchTouchEvent======" + ev.getAction());
        return s;
    }

    private int measure(int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        // Default size if no limits are specified.

        int result = measureSpec;
        if (specMode == MeasureSpec.EXACTLY) {

            /**
             * 表示父视图希望子视图的大小应该是由specSize的值来决定的，
             * 系统默认会按照这个规则来设置子视图的大小，
             * 开发人员当然也可以按照自己的意愿设置成任意的大小;
             * 精准 和 match_parent时候()
             */

            result = specSize;
            LogBlue.i("TAG", "==EXACTLY==");
        } else if (specMode == MeasureSpec.AT_MOST) {

            /**
             * 表示子视图最多只能是specSize中指定的大小，开发人员应该尽可能小得去设置这个视图，并且保证不会超过specSize。
             * 系统默认会按照这个规则来设置子视图的大小，开发人员当然也可以按照自己的意愿设置成任意的大小
             * 最大这个数，注意父控件
             */
            result = specSize;
            LogBlue.i("TAG", "===AT_MOST=" + specSize);
        } else {
            LogBlue.i("TAG", "==UNSPECIFIED==");
        }
        return result;
    }
}
