package com.lzxmy.demo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzxmy.demo.R;

/**
 * Created by lzx on 2016/12/1.
 */

public class TagContentView extends LinearLayout {
    TextView tv_name;
    RoundTextView round_0;
    RoundTextView round_1;

    public TagContentView(Context context) {
        super(context);
        init(context);
    }

    public TagContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TagContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TagContentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    void init(Context context) {
    }

    public void setName(CharSequence name, String[] plabels) {
        String plabel0 = null;
        String plabel1 = null;
        if (plabels != null) {
            if (plabels.length > 1) {
                plabel0 = plabels[0];
                plabel1 = plabels[1];
            } else if (plabels.length == 1) {
                plabel0 = plabels[0];
            }
        }
        setName(name, plabel0, plabel1);
    }

    public void setName(CharSequence name, String plabel0, String plabel1) {
        if (tv_name == null) {
            tv_name = (TextView) findViewById(R.id.tv_name);
            round_0 = (RoundTextView) findViewById(R.id.round_0);
            round_1 = (RoundTextView) findViewById(R.id.round_1);
            if (tv_name != null) {
                tv_name.setGravity(Gravity.CENTER);
            }
            if (round_0 != null) {
                round_0.setGravity(Gravity.NO_GRAVITY);
            }

            if (round_1 != null) {
                round_1.setGravity(Gravity.NO_GRAVITY);
            }
        }
        if (tv_name != null)
            tv_name.setText(name);
        setTypeText(round_0, plabel0);
        setTypeText(round_1, plabel1);
    }


    // TODO: 16/7/5 debug
    protected void setTypeText(RoundTextView roundTextView, String text) {
        if (roundTextView == null) {
            return;
        }
        if (TextUtils.isEmpty(text)) {
            roundTextView.setVisibility(View.GONE);
            return;
        } else {
            if ("官方".equals(text)) {
                roundTextView.setBackground_fillcolor(Color.parseColor("#4fb404"));
            } else if ("礼包".equals(text)) {
                roundTextView.setBackground_fillcolor(Color.parseColor("#f11111"));
            } else if ("福利".equals(text)) {
                roundTextView.setBackground_fillcolor(Color.parseColor("#fa46a0"));
            } else if ("试玩".equals(text)) {
                roundTextView.setBackground_fillcolor(Color.parseColor("#23a2c7"));
            } else if ("评测".equals(text)) {
                roundTextView.setBackground_fillcolor(Color.parseColor("#FF7800"));
            } else if ("预览".equals(text)) {
                roundTextView.setBackground_fillcolor(Color.parseColor("#45a2fa"));
            } else if ("视频".equals(text)) {
                roundTextView.setBackground_fillcolor(Color.parseColor("#5f04b4"));
            } else if ("AD".equals(text)) {
                roundTextView.setBackground_fillcolor(Color.parseColor("#ffc600"));
            } else {
                roundTextView.setBackground_fillcolor(Color.parseColor("#23a2c7"));
            }
            roundTextView.setText(text);
            roundTextView.setVisibility(View.VISIBLE);
        }
    }

    int getTextWith(View view) {
        if (view == null || !(view instanceof TextView)) {
            return 0;
        }
        TextView textView = (TextView) view;
        if (textView.getText().length() == 0) {
            return 0;
        }
        float tw = textView.getPaint().measureText(textView.getText().toString());
        tw = tw + textView.getPaddingLeft() + textView.getPaddingRight();
        return (int) tw;
    }

    int getTextHeigh(View view) {
        if (view == null || !(view instanceof TextView)) {
            return 0;
        }
        TextView textView = (TextView) view;
        float th = textView.getPaint().getFontMetricsInt().bottom - textView.getPaint().getFontMetricsInt().top;
        th = th + textView.getPaddingTop() + textView.getPaddingBottom();
        return (int) th;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measurewidth = measure(widthMeasureSpec);
        int allwith = 0;
        int maxheight = 0;
        int max = getChildCount() > 3 ? 3 : getChildCount();
        for (int i = 0; i < max; i++) {
            View childView = getChildAt(i);
            if (childView instanceof TextView && childView.getVisibility() != GONE) {
                LayoutParams params = (LayoutParams) childView.getLayoutParams();
                allwith = allwith + getTextWith(childView) + params.leftMargin;
                int h = getTextHeigh(childView);
                maxheight = h > maxheight ? h : maxheight;
            }
        }
//        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(measurewidth, maxheight);
        int parentw = getMeasuredWidth() >> 1;
        int outw;
        if (allwith > getMeasuredWidth() && max == 3) {
            View childView0 = getChildAt(0);
            View childView1 = getChildAt(1);
            View childView2 = getChildAt(2);
            int tagw_0 = getTextWith(childView0);
            int tagw_1 = getTextWith(childView1);
            int tagw_2 = getTextWith(childView2);
            LayoutParams params_1 = (LayoutParams) childView1.getLayoutParams();
            LayoutParams params_2 = (LayoutParams) childView2.getLayoutParams();
            if (tagw_0 > parentw) {
                outw = parentw - tagw_1 - params_1.leftMargin - tagw_2 - params_2.leftMargin;
                if (outw < 0) {
                    onChildMeasure(childView0, parentw, getTextHeigh(childView0));
                    outw = parentw - tagw_1 - params_1.leftMargin;
                    if (outw > 0) {
                        onChildMeasure(childView1, tagw_1, getTextHeigh(childView1));
                        if (outw >= tagw_2) { //tag 2 不足
                            onChildMeasure(childView2, tagw_2, getTextHeigh(childView2));
                        }
                    } else {
                        onChildMeasure(childView1, parentw - params_1.leftMargin, getTextHeigh(childView1));
                    }
                } else {
                    onChildMeasure(childView0, parentw + outw, getTextHeigh(childView0));
                    onChildMeasure(childView1, tagw_1, getTextHeigh(childView1));
                    onChildMeasure(childView2, tagw_2, getTextHeigh(childView2));
                }
            } else {
                onChildMeasure(childView0, tagw_0, getTextHeigh(childView0));
                outw = parentw - tagw_0;
                if (tagw_1 + params_1.leftMargin > parentw + outw) {
                    onChildMeasure(childView1, parentw + outw - params_1.leftMargin, getTextHeigh(childView1));
                } else {
                    onChildMeasure(childView1, tagw_1, getTextHeigh(childView1));

                    if ((parentw + outw - tagw_1 - params_1.leftMargin) >= tagw_2 + params_2.leftMargin) {
                        onChildMeasure(childView2, tagw_2, getTextHeigh(childView2));
                    } else {//tag 2 不足
                    }
                }
            }
        } else {
            for (int i = 0; i < max; i++) {
                View childView = getChildAt(i);
                if (childView instanceof TextView && childView.getVisibility() != GONE) {
                    int with = getTextWith(childView);

                    final int freeWidthSpec = MeasureSpec.makeMeasureSpec(
                            with, MeasureSpec.EXACTLY);
                    childView.measure(freeWidthSpec, getTextHeigh(childView));
                }
            }
        }
    }

    void onChildMeasure(View view, int w, int maxheight) {
        int freeWidthSpec = MeasureSpec.makeMeasureSpec(
                w, MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                maxheight, MeasureSpec.EXACTLY);
        view.measure(freeWidthSpec, heightMeasureSpec);
    }

    private int measure(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        if (specMode == MeasureSpec.EXACTLY || specMode == MeasureSpec.AT_MOST) {
            return MeasureSpec.getSize(measureSpec);
        }
        return measureSpec;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int last_r = 0;
        int max = getChildCount() > 3 ? 3 : getChildCount();
        for (int i = 0; i < max; i++) {
            View childView = getChildAt(i);
            last_r = layout(childView, last_r, getViewY(childView));
        }
    }

    int layout(View childView, int l, int t) {
        if (childView.getMeasuredWidth() == 0 || childView.getVisibility() == GONE) {
            childView.layout(0, 0, 0, 0);
            return l;
        }
        LayoutParams params = (LayoutParams) childView.getLayoutParams();
        l = l + params.leftMargin;
        int r = l + childView.getMeasuredWidth();
        childView.layout(l, t, r, t + childView.getMeasuredHeight());
        return r;
    }

    int getViewY(View childView) {
        return (getMeasuredHeight() - childView.getMeasuredHeight()) >> 1;
    }
}
