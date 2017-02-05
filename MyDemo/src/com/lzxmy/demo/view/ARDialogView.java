package com.lzxmy.demo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lzxmy.demo.R;
import com.support.loader.utils.LogBlue;

/**
 * Created by lzx on 16/11/17.
 */
public class ARDialogView extends ViewGroup {
    public ARDialogView(Context context) {
        super(context);
        init(context);
    }

    public ARDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ARDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public ARDialogView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
        setOnGenericMotionListener(new OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                return false;
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogBlue.i("TAG", "执行    onTouchEvent" + event.getAction());
        boolean s = super.onTouchEvent(event);
        LogBlue.i("TAG", s + "    onTouchEvent" + event.getAction());
        return s;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogBlue.i("TAG", "执行    onInterceptTouchEvent" + ev.getAction());
        boolean s = super.onInterceptTouchEvent(ev);
        LogBlue.i("TAG", s + "    onInterceptTouchEvent" + ev.getAction());
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogBlue.i("TAG", "执行    dispatchTouchEvent" + ev.getAction());
        boolean s = super.dispatchTouchEvent(ev);
        LogBlue.i("TAG", s + "    dispatchTouchEvent" + ev.getAction());
        return s;
    }

    public static Bitmap getAssetBitmap(Context context, String assetpath) {
//        try {
//            InputStream inputStream = context.getAssets().open(assetpath);
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = false;
//            options.inSampleSize = 2; //width，hight设为原来的十分之一
//            Bitmap btp =BitmapFactory.decodeStream(is,null,options);
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow1);

//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Message message = obtainMessage(1);
                imageView.setImageBitmap(bear_0);
                sendMessageDelayed(message, 1200);
            } else {
                if (!ARDialogView.this.isShown()) {
                    return;
                }
                imageView.setImageBitmap(bear_1);
                Message message = obtainMessage(0);
                sendMessageDelayed(message, 1200);
            }
        }
    };
    Bitmap bear_0;
    Bitmap bear_1;
    ImageView imageView;

    void init(Context context) {
        setWillNotDraw(false);


        imageView = new ImageView(context);

        addView(imageView, 0);

        ImageView imageViewbg = new ImageView(context);
        imageViewbg.setImageBitmap( BitmapFactory.decodeResource(context.getResources(), R.drawable.car4));
        addView(imageViewbg, 1);
        ARScrollView srollview = new ARScrollView(context);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        srollview.addView(linearLayout);
        textView = new VTextView(context);
        textView.setSingleLine(false);
        textView.setGravity(Gravity.CENTER);
        int density = (int) getResources().getDisplayMetrics().density;
        textView.setTextSize(10 * density);

        textView.setTextColor(Color.parseColor("#6f5539"));
        textView.setText("30M");
        linearLayout.addView(textView);
        addView(srollview);
        ImageView imageViewbt = new ImageView(context);
        imageViewbt.setImageBitmap( BitmapFactory.decodeResource(context.getResources(), R.drawable.check_sm));
        addView(imageViewbt);
        bear_0 = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy01);
        bear_1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.actionbar_add_icon);


        handler.sendEmptyMessageDelayed(1, 1200);
        setBackgroundColor(Color.GRAY);
    }

    VTextView textView;

    public void showtracffic(String tracffic) {
        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
        SpannableStringBuilder builder = new SpannableStringBuilder("恭喜您已挑战成功，获得\n" + tracffic + "M\n流量奖励");
        builder.setSpan(span, 11, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    public void showTip(String tip) {
//        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
//        SpannableStringBuilder builder = new SpannableStringBuilder("恭喜您已挑战成功，获得\n" + tracffic + "M\n流量奖励");
//        builder.setSpan(span, 11, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(tip);
    }

    double getw(double mw) {
        float w = textView.getPaint().measureText("恭") * 11;
        if (mw > w) {
            return mw;
        } else {
            setpaint();
            return getw(mw);
        }
    }

    void setpaint() {
        float s = textView.getPaint().getTextSize();
        textView.getPaint().setTextSize(--s);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (i == 2) {
                double mw = (getChildAt(1).getMeasuredWidth() * 0.8);
                mw = getw(mw);
                final int freeWidthSpec = MeasureSpec.makeMeasureSpec(
                        (int) mw, MeasureSpec.AT_MOST);
                childView.measure(freeWidthSpec, heightMeasureSpec);
            } else {
                childView.measure(widthMeasureSpec, heightMeasureSpec);
            }
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int centerx = getMeasuredWidth() >> 1;
        View childView = getChildAt(1);
        int vl = centerx - (childView.getMeasuredWidth() >> 1);
        int centery = getMeasuredHeight() >> 1;
        int vt = (int) (centery - (childView.getMeasuredHeight() * 0.45));
        layout(childView, vl, vt);
        View childView0 = getChildAt(0);
        int vl0 = centerx - (childView0.getMeasuredWidth() >> 1);
        layout(childView0, vl0, (int) (vt - (childView0.getMeasuredHeight() * 0.6)));
        View childView2 = getChildAt(2);
        int vl2 = centerx - (childView2.getMeasuredWidth() >> 1);
        int top = (int) (vt + childView.getMeasuredHeight() * 0.28);
        childView2.layout(vl2, top, vl2 + childView2.getMeasuredWidth(), (int) (top + childView.getMeasuredHeight() * 0.4));
        View childView3 = getChildAt(3);
        int vl3 = centerx - (childView3.getMeasuredWidth() >> 1);
        layout(childView3, vl3, (int) (vt + childView.getMeasuredHeight() * 0.65));

    }

    void layout(View childView, int l, int t) {
        childView.layout(l, t, l + childView.getMeasuredWidth(), t + childView.getMeasuredHeight());
    }
}
