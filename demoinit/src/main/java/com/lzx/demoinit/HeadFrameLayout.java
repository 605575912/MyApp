package com.lzx.demoinit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class HeadFrameLayout extends ViewGroup {
    public HeadFrameLayout(Context context) {
        super(context);
    }

    public HeadFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 动态获取子View实例
        for (int i = 0, size = getChildCount(); i < size; i++) {
            View childView = getChildAt(i);
            int measureHeight = childView.getMeasuredHeight();
            int measuredWidth = childView.getMeasuredWidth();
            if ((childView instanceof TextView)) {
                TextView textView = (TextView) childView;
                XPoint xpoint = pointHashMap.get(textView.getText().toString());
                if (xpoint == null) {
                    continue;
                }
                Point point = getpoint(xpoint.r, xpoint.ao);

                childView.layout(point.x - (measuredWidth >> 1), point.y - (measureHeight >> 1), ((measuredWidth >> 1) + point.x), point.y
                        + (measureHeight >> 1));
                xpoint.ao = xpoint.ao + 1;
                pointHashMap.put(textView.getText().toString(), xpoint);
            } else {
                childView.layout(getWidth() / 2 - (measuredWidth >> 1), getHeight() / 2 - (measureHeight >> 1), ((measuredWidth >> 1) + getWidth() / 2), getHeight() / 2
                        + (measureHeight >> 1));
            }

        }

        handler.sendEmptyMessageDelayed(0, 60);

    }

    HashMap<String, XPoint> pointHashMap = new HashMap<String, XPoint>();
    long temptime = 0;
    boolean ismove = false;
    float tempx = 0;
    float tempy = 0;
    float moveheight = 200;
    boolean animmoving = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                tempx = x;
                tempy = y;
                temptime = System.currentTimeMillis();
                Log.i("TAG", "ACTION_DOWN==");
                return true;
            }
            case MotionEvent.ACTION_MOVE: {

                if ((y - tempy) < moveheight) {
                    Log.i("TAG", "ACTION_MOVE==");
                    ismove = true;
                    animmoving = false;
                    handler.removeCallbacksAndMessages(null);
                    for (int i = 0; i < getChildCount(); i++) {
                        View view = getChildAt(i);
                        if (view instanceof TextView) {
                            ViewHelper.setTranslationY(view, y - tempy);
                        }
                    }
                } else {
//                    ismove = false;
//                    handler.sendEmptyMessage(0);
                }
            }
            break;
            case MotionEvent.ACTION_UP: {
                up();
                return true;
//                }

            }
        }
        return super.onTouchEvent(event);
    }

    void up() {
        if (ismove) {

            ArrayList<View> views = new ArrayList<View>();
            ArrayList<Point> points = new ArrayList<Point>();
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    views.add(textView);
                    XPoint xpoint = pointHashMap.get(textView.getText().toString());
                    Point point = getpoint(xpoint.r, xpoint.ao);
                    points.add(point);
                }
            }
            checkAnimation(views, 1, points);


        }
        ismove = false;

//                if (System.currentTimeMillis() - temptime < 500) {
//                    return false;
//                } else {
    }

    float temp = -1;

    void checkAnimation(final List<View> imageViews, float tox, final List<Point> points) {
        animmoving = true;
        ValueAnimator widthAnimation = ValueAnimator.ofFloat(1, 0);
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()

                                         {
                                             @Override
                                             public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                                 if (!animmoving) {
                                                     return;
                                                 }
                                                 Float value = (Float) valueAnimator.getAnimatedValue();
                                                 if (temp == value) {
                                                     return;
                                                 }
                                                 Log.i("TAG","====onAnimationUpdate");
                                                 temp = value;
//                                                 if (type == 0) {
//
//                                                     ViewHelper.setTranslationX(imageView, value);
//                                                 } else {

                                                 for (int i = 0; i < imageViews.size(); i++) {
                                                     View view = imageViews.get(i);
                                                     float d = ((ViewHelper.getY(view) + view.getHeight() / 2) - points.get(i).y);
                                                     ViewHelper.setTranslationY(view, d * value);
                                                 }
//                                                 }

                                             }

                                         }
        );
        widthAnimation.setDuration(2000);
        widthAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animmoving = false;
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        widthAnimation.start();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                return false;
            }
            case MotionEvent.ACTION_MOVE: {
                return true;
            }
//            case MotionEvent.ACTION_UP: {
//                return true;
//            }
        }


        return super.onInterceptTouchEvent(event);
    }

    //    onInterceptTouchEvent
    @Override
    public void removeAllViews() {
        super.removeAllViews();
        pointHashMap.clear();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LayoutParams layoutParams = getLayoutParams();

        int measuredHeight = measureWidth(layoutParams.height, heightMeasureSpec);
        int measuredWith = measureWidth(layoutParams.width, widthMeasureSpec);

        int count = getChildCount();
        int add = 0;
        int j = 1;
        setMeasuredDimension(measuredWith, measuredHeight);
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            measureChild(view, 400, 400);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if (!pointHashMap.containsKey(textView.getText().toString())) {
                    int radio = (headWith >> 1) + maxdapping * (j) + add;
                    XPoint xPoint = new XPoint();
                    xPoint.r = radio;
                    xPoint.ao = (360 * Math.random());
                    pointHashMap.put(textView.getText().toString(), xPoint);
                    add = add + j * addoffext;
                    j++;
                }

            }
        }

//        super.onMeasure(measuredWith, measuredHeight);

    }

    class XPoint {
        double ao = 0;
        double r = 0;

    }

    final double sc = 3.1415 / 180;

    Point getpoint(double r, double ao) {
        int x1 = (int) ((getWidth() >> 1) + r * Math.cos(ao * sc));
        int y1 = (int) ((getHeight() >> 1) + r * Math.sin(ao * sc));
        Point point = new Point(x1, y1);
        return point;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler.removeCallbacksAndMessages(null);
            requestLayout();
            animmoving = false;
        }
    };


    Paint paint;
    int with = 0;
    int Arcstrokewidth = 6;
    int maxdapping = 20;
    int headWith = 150;
    int addoffext = 12;
    int SUM = 5;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        with = Math.min(getWidth(), getHeight());

        int add = 0;
        for (int i = 0; i < SUM; i++) {
            RectF rectF;
            if (i == 0) {
                rectF = getRectF(headWith >> 1);
            } else {
                rectF = getRectF(headWith / 2 + maxdapping * (i) + add);
                add = add + i * addoffext;
            }

            Paint paint = initpaint();
            canvas.drawArc(rectF, 0, 360, true, paint);
        }
    }

    RectF getRectF(int radio) {
        int left = (getWidth() >> 1) - radio;
        int top = (getHeight() >> 1) - radio;
        int right = left + (radio << 1);
        int bottom = top + (radio << 1);
        RectF rectF = new RectF(left, top, right, bottom);
        return rectF;
    }


    Paint initpaint() {
        if (paint == null) {
            paint = new Paint();
            paint.setColor(Color.argb(50, 239, 245, 255));
            paint.setStrokeWidth(Arcstrokewidth);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
        }
        return paint;
    }

    private int measureWidth(int size, int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = specSize;
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = size;
        }
        return result;
    }
}
