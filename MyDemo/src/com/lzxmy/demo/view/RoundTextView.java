package com.lzxmy.demo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.lzxmy.demo.R;


/**
 * 圆弧对称 背景填充
 * Created by lzx on 16/6/2.
 */
public class RoundTextView extends TextView {
    RectF fillrect;
    Paint mpaint;
    int background_fillcolor;
    float _roundy = 5, _roundx;
    int strokewidth_color = 0;
    float strokewidth = 5;

    public RoundTextView(Context context) {
        super(context);
        init(context, null);
    }

    public RoundTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(21)
    public RoundTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundTextView);
            background_fillcolor = a.getColor(R.styleable.RoundTextView_background_fillcolor, 0);
            strokewidth = a.getDimension(R.styleable.RoundTextView_v_strokewidth, 1);
            strokewidth_color = a.getColor(R.styleable.RoundTextView_strokewidth_color, 0);
            _roundy = a.getDimension(R.styleable.RoundTextView_t_roundy, context.getResources().getDimension(R.dimen.hpv6_roundxy));
            _roundx = a.getDimension(R.styleable.RoundTextView_t_roundx, context.getResources().getDimension(R.dimen.hpv6_roundxy));
            a.recycle();
        }
        mpaint = new Paint();
        mpaint.setColor(background_fillcolor);
        mpaint.setStyle(Paint.Style.FILL);
        mpaint.setAntiAlias(true);
        setGravity(Gravity.CENTER);
    }

    public void setBackground_fillcolor(int background_fillcolor) {
        this.background_fillcolor = background_fillcolor;
    }


    public void setSingleSTROKEColor(int color) {
        strokewidth_color = color;
        background_fillcolor = 0;
    }

    public int getSingleSTROKEColor() {
        return strokewidth_color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("TAG", background_fillcolor + "==" + getWidth() + "=====" + getMeasuredWidth() + "==" + canvas.getWidth());
        if (background_fillcolor != 0) {
            if (fillrect == null || fillrect.width() != getWidth()) {
                fillrect = new RectF(0, 0, getWidth(), getHeight());
            }
            mpaint.setColor(background_fillcolor);
            mpaint.setStyle(Paint.Style.FILL);
            canvas.drawRoundRect(fillrect, _roundx, _roundy, mpaint);
        }
        if (strokewidth_color != 0) {
            mpaint.setColor(strokewidth_color);
            mpaint.setStyle(Paint.Style.STROKE);
            mpaint.setStrokeWidth(strokewidth);
            RectF strokerect = new RectF(strokewidth / 2.0f, strokewidth / 2.0f, getWidth() - strokewidth / 2.0f, getHeight() - strokewidth / 2.0f);
            canvas.drawRoundRect(strokerect, _roundx, _roundy, mpaint);
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measure(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        if (specMode == MeasureSpec.EXACTLY || specMode == MeasureSpec.AT_MOST) {
            return MeasureSpec.getSize(measureSpec);
        }
        return measureSpec;
    }

    public void setRoundX(float roundX) {
        _roundx = roundX;
    }

    public void setRoundY(float roundY) {
        _roundy = roundY;
    }

    public void setStrokewidth(float width) {
        strokewidth = width;
    }
}
