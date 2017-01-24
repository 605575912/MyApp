package com.lzxmy.demo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.support.loader.utils.ImageUtils;


/**
 * 底图缩放，浮层不变
 *
 * @author yanglonghui
 */
public class CropImageView extends View {

    // 单点触摸的时候
    private float oldX = 0;
    private float oldY = 0;

    // // 多点触摸的时候
    // private float oldx_0 = 0;
    // private float oldy_0 = 0;
    //
    // private float oldx_1 = 0;
    // private float oldy_1 = 0;
    float olddis = 0;
    float numdis = 0;
    // 状态
    private final int STATUS_Touch_SINGLE = 1;// 单点
    private final int STATUS_TOUCH_MULTI_START = 2;// 多点开始
    private final int STATUS_TOUCH_MULTI_TOUCHING = 3;// 多点拖拽中

    private int mStatus = STATUS_Touch_SINGLE;

    // 默认的裁剪图片宽度与高度
    private int cropWidth = 400;
    private int cropHeight = 400;
    protected Rect bitmapFloat;// 浮层选择框，就是头像选择框

    int cropleft = -1;
    int croptop = -1;
    float bm_with = -1;
    float bm_height = -1;
    int Dsttop = -1;
    int Dstleft = -1;
    int Rotate = 0;
    protected boolean isFrist = true;
    Path mPath = new Path();
    Bitmap lastbitmap;
    Bitmap initbitmap;
    int adding = 0;
    private Paint mLinePaint;

    public CropImageView(Context context) {
        super(context);
        init(context);
    }

    public CropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CropImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);

    }

    @SuppressLint("NewApi")
    private void init(Context context) {
        adding = cropWidth / 10;
        isFrist = true;
        mLinePaint = new Paint();
        mLinePaint.setStrokeWidth(2F);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.WHITE);
        if (Build.VERSION.SDK_INT > 10) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

    }

    public void setDrawable(Bitmap bitmap, int cropWidth, int cropHeight) {
        this.cropWidth = cropWidth;
        this.cropHeight = cropHeight;
        this.isFrist = true;
        if (bitmap != null) {
            this.lastbitmap = bitmap;
            this.initbitmap = bitmap;
            bm_with = cropWidth;
            bm_height = cropHeight;
            if (bitmap.getWidth() > cropWidth) {
                bm_with = bitmap.getWidth();
            }
            if (bitmap.getHeight() > cropHeight) {
                bm_height = bitmap.getHeight();
            }
            invalidate();
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

        }


    };
    Runnable runnableexit = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            invalidate();
        }
    };
    Runnable doubleonclick = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (lastbitmap.getHeight() != initbitmap.getHeight()) {
                sacle(-numdis);
                numdis = 0;
                minscale();
            } else {
                numdis = -300;
                sacle(numdis);
            }
        }
    };
    Runnable initrunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            init();
        }
    };

    void init() {
        bm_with = cropWidth;
        bm_height = cropHeight;
        if (initbitmap.getWidth() > cropWidth) {
            bm_with = initbitmap.getWidth();
        }
        if (initbitmap.getHeight() > cropHeight) {
            bm_height = initbitmap.getHeight();
        }
        croptop = -1;
        lastbitmap = initbitmap;
        invalidate();
    }

    long onclicketime = 0;
    long doubleonclicketime = 0;
    int delaytime = 200;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (lastbitmap == null) {
            return true;
        }
        if (event.getPointerCount() > 1) {
            if (mStatus == STATUS_Touch_SINGLE) {
                mStatus = STATUS_TOUCH_MULTI_START;

                // oldx_0 = event.getX(0);
                // oldy_0 = event.getY(0);
                //
                // oldx_1 = event.getX(1);
                // oldy_1 = event.getY(1);
            } else if (mStatus == STATUS_TOUCH_MULTI_START) {
                mStatus = STATUS_TOUCH_MULTI_TOUCHING;
            }
        } else {
            if (mStatus == STATUS_TOUCH_MULTI_START
                    || mStatus == STATUS_TOUCH_MULTI_TOUCHING) {
                oldX = event.getX();
                oldY = event.getY();
            }

            mStatus = STATUS_Touch_SINGLE;
        }

        // Log.v("count currentTouch"+currentTouch, "-------");

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Log.v("count ACTION_DOWN", "-------");
                mStatus = STATUS_Touch_SINGLE;
                oldX = event.getX();
                oldY = event.getY();
                onclicketime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                // Log.v("count ACTION_UP", "-------");
            {
                float dx = Math.abs(event.getX() - oldX);
                float dy = Math.abs(event.getY() - oldY);
                long newtime = System.currentTimeMillis();
                if (newtime - onclicketime < 100 && dx < 8 && dy < 8) {
                    if (newtime - doubleonclicketime < 200) {
                        handler.removeCallbacksAndMessages(null);
                        handler.postDelayed(doubleonclick, delaytime);
                    } else {
                        handler.removeCallbacksAndMessages(null);
                        handler.postDelayed(initrunnable, delaytime);
                    }
                    doubleonclicketime = newtime;

                } else {

                    minscale();
                }
            }
            break;

            case MotionEvent.ACTION_POINTER_UP:
                // Log.v("count ACTION_POINTER_UP", "-------");
                break;

            case MotionEvent.ACTION_MOVE:
                // Log.v("count ACTION_MOVE", "-------");
                if (mStatus == STATUS_TOUCH_MULTI_TOUCHING) {
                    float newdis = distance(event);
                    if (olddis == 0) {
                        olddis = newdis;
                    }
                    float dis = (olddis - newdis);
                    if ((bm_with <= cropWidth || bm_height <= cropHeight)
                            && dis > 0) {
                        return true;
                    }
                    olddis = newdis;
                    sacle(dis);
                    numdis = numdis + dis;
                } else if (mStatus == STATUS_Touch_SINGLE) {
                    int dx = (int) (event.getX() - oldX);
                    int dy = (int) (event.getY() - oldY);
                    oldX = event.getX();
                    oldY = event.getY();
                    move(dx, dy);
                }
                break;
        }

        // Log.v("event.getAction()："+event.getAction()+"count："+event.getPointerCount(),
        // "-------getX:"+event.getX()+"--------getY:"+event.getY());
        return true;
    }

    void move(int dx, int dy) {
        if (Dsttop + lastbitmap.getHeight() < bitmapFloat.bottom - adding) {

            return;
        }
        if (Dsttop > bitmapFloat.top + adding) {
            return;
        }
        if (Dstleft > bitmapFloat.left + adding) {
            return;
        }
        if (Dstleft + lastbitmap.getWidth() < bitmapFloat.right - adding) {
            return;
        }
        Dstleft = Dstleft + dx;
        Dsttop = Dsttop + dy;
        invalidate();
    }

    void minscale() {
        olddis = 0;
        boolean isinit = false;
        if (Dsttop + lastbitmap.getHeight() < bitmapFloat.bottom) {
            Dsttop = bitmapFloat.bottom - lastbitmap.getHeight();
            isinit = true;
        }
        if (Dstleft + lastbitmap.getWidth() < bitmapFloat.right) {

            Dstleft = bitmapFloat.right - lastbitmap.getWidth();
            isinit = true;
        }
        if (Dsttop > bitmapFloat.top) {
            Dsttop = bitmapFloat.top;
            isinit = true;
        }
        if (Dstleft > bitmapFloat.left) {
            Dstleft = bitmapFloat.left;
            isinit = true;
        }
        if (bm_with < cropWidth || bm_height < cropHeight) {
            if (bm_with < cropWidth) {
                bm_with = cropWidth;
            }
            if (bm_height < cropHeight) {
                bm_height = cropHeight;
            }
            Bitmap bitmap = ImageUtils.zoomBitmap(initbitmap, bm_with,
                    bm_height);
            if (bitmap != null) {
                lastbitmap = bitmap;
                Dstleft = bitmapFloat.left
                        - (lastbitmap.getWidth() - cropWidth) / 2;
                Dsttop = bitmapFloat.top
                        - (lastbitmap.getHeight() - cropHeight) / 2;
                isinit = true;
            }
        }

        if (isinit) {
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(runnableexit, delaytime);
        }
    }

    void sacle(float newdis) {

        bm_with = (bm_with - newdis);
        if (bm_with < cropWidth) {
            bm_with = cropWidth;
        } else {
            Dstleft = (int) (Dstleft + newdis / 2);
        }
        bm_height = (bm_height - newdis);
        if (bm_height < cropHeight) {
            bm_height = cropHeight;
        } else {
            Dsttop = (int) (Dsttop + newdis / 2);
        }
        Bitmap bitmap = ImageUtils.zoomBitmap(initbitmap, bm_with, bm_height);
        if (bitmap == null) {
            Dstleft = (int) (Dstleft - newdis / 2);
            Dsttop = (int) (Dsttop - newdis / 2);
        } else {
            lastbitmap = bitmap;
            invalidate();
        }
    }

    public void Rotate() {

        if (Rotate < 270) {
            Rotate = Rotate + 90;
        } else {
            Rotate = 0;
        }
        if (Rotate != 0) {
            Bitmap bitmap = null;
            Matrix matrix = null;
            Canvas bcanvas = null;
            if (Rotate == 90) {
                bitmap = Bitmap.createBitmap((int) bm_height, (int) bm_with,
                        Config.ARGB_8888);
                bcanvas = new Canvas(bitmap);
                bcanvas.drawColor(Color.GREEN);
                matrix = new Matrix();
                matrix.setRotate(Rotate, bm_height / 2, bm_height / 2);
            } else if (Rotate == 180) {
                bitmap = Bitmap.createBitmap((int) bm_with, (int) bm_height,
                        Config.ARGB_8888);
                bcanvas = new Canvas(bitmap);
                bcanvas.drawColor(Color.GREEN);
                matrix = new Matrix();
                matrix.setRotate(Rotate, bm_with / 2, bm_height / 2);
            } else if (Rotate == 270) {
                bitmap = Bitmap.createBitmap((int) bm_height, (int) bm_with,
                        Config.ARGB_8888);
                bcanvas = new Canvas(bitmap);
                bcanvas.drawColor(Color.GREEN);
                matrix = new Matrix();
                matrix.setRotate(Rotate, bm_with / 2, bm_with / 2);
            }
            bcanvas.drawBitmap(initbitmap, matrix, null);
            lastbitmap = bitmap;
            Dstleft = (getWidth() - lastbitmap.getWidth()) / 2;
            Dsttop = (getHeight() - lastbitmap.getHeight()) / 2;
            invalidate();
        } else {
            init();
        }

    }

    /**
     * 计算两个触摸点之间的距离
     *
     * @param event
     * @return
     */
    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        if (lastbitmap == null) {
            return; // couldn't resolve the URI
        }

        // Dstleft = (getWidth() - lastbitmap.getWidth()) / 2;
        // Dsttop = (getHeight() - lastbitmap.getHeight()) / 2;
        if (cropleft < 0) {
            if (getWidth() > cropWidth) {
                cropleft = (getWidth() - cropWidth) / 2;
            }
        }
        if (croptop < 0) {
            if (getHeight() > cropHeight) {
                croptop = (getHeight() - cropHeight) / 2;
            }
            Dstleft = (getWidth() - lastbitmap.getWidth()) / 2;
            Dsttop = (getHeight() - lastbitmap.getHeight()) / 2;
        }

        if (lastbitmap.getHeight() == 0 || lastbitmap.getWidth() == 0) {
            return; // nothing to draw (empty bounds)
        }
        isFrist = false;

        canvas.drawBitmap(lastbitmap, Dstleft, Dsttop, null);

        circle(canvas);
    }

    /**
     * 画圆 2015-3-31 @author lzx
     */
    void circle(Canvas canvas) {
        if (bitmapFloat == null) {
            bitmapFloat = new Rect();
            bitmapFloat.set(cropleft, croptop, cropWidth + cropleft, cropHeight
                    + croptop);
            mPath.reset();
            mPath.addCircle(bitmapFloat.left
                            + (bitmapFloat.right - bitmapFloat.left) / 2, bitmapFloat.top
                            + (bitmapFloat.bottom - bitmapFloat.top) / 2, cropWidth / 2,
                    Path.Direction.CCW);
        }
        canvas.save();
        canvas.clipPath(mPath, Region.Op.XOR);
        canvas.drawColor(Color.parseColor("#a0000000"));
        canvas.restore();
        canvas.drawPath(mPath, mLinePaint);
    }


//    private void drawScene(Canvas canvas) {
////        canvas.clipRect(0, 0, 100, 100);
////
////        canvas.drawColor(Color.WHITE);
//
//        mPaint.setColor(Color.RED);
//        canvas.drawLine(0, 0, 100, 100, mPaint);
//
//        mPaint.setColor(Color.GREEN);
//        canvas.drawCircle(30, 70, 30, mPaint);
//
//        mPaint.setColor(Color.BLUE);
//        canvas.drawText("Clipping", 100, 30, mPaint);
//    }



//    @Override
//    protected void onDraw(Canvas canvas) {
//        if (mPaint == null) {
//            mPaint = new Paint();
//            mPaint.setAntiAlias(true);
//            mPaint.setStrokeWidth(6);
//            mPaint.setTextSize(16);
//            mPaint.setTextAlign(Paint.Align.RIGHT);
//        }
//
//
////        canvas.drawColor(Color.WHITE);
////        canvas.save();
////        canvas.translate(10, 10);
////        drawScene(canvas);
////        canvas.restore();
////
////        canvas.save();
////        canvas.translate(160, 10);
////        canvas.clipRect(10, 10, 90, 90);
////        canvas.clipRect(30, 30, 70, 70, Region.Op.DIFFERENCE);
////        drawScene(canvas);
////        canvas.restore();
////
////        canvas.save();
////        canvas.translate(10, 160);
////        mPath.reset();
////        canvas.clipPath(mPath); // makes the clip empty
////        mPath.addCircle(50, 50, 50, Path.Direction.CCW);
////        canvas.clipPath(mPath, Region.Op.REPLACE);
////        drawScene(canvas);
////        canvas.restore();
////
////        canvas.save();
////        canvas.translate(160, 160);
////        canvas.clipRect(0, 0, 60, 60);
////        canvas.clipRect(40, 40, 100, 100, Region.Op.UNION);
////        drawScene(canvas);
////        canvas.restore();
////
////        canvas.save();
////        canvas.translate(10, 310);
////        canvas.clipRect(0, 0, 60, 60);
////        canvas.clipRect(40, 40, 100, 100, Region.Op.XOR);
////        drawScene(canvas);
////        canvas.restore();
//
//        canvas.save();
////        canvas.translate(160, 310);
//        canvas.clipRect(0, 0, getWidth()/2, getHeight()/2);
//        canvas.clipRect(160, 160, getWidth(), getHeight(), Region.Op.REVERSE_DIFFERENCE);
////        canvas.clipRect(0, 0, 100, 100);
//        canvas.drawColor(Color.WHITE);
//        mPaint.setColor(Color.RED);
//        canvas.drawLine(0, 0, getWidth()/2, getHeight()/2, mPaint);
//
////        mPaint.setColor(Color.GREEN);
////        canvas.drawCircle(30, 70, 30, mPaint);
//
////        mPaint.setColor(Color.BLUE);
////        canvas.drawText("Clipping", 100, 30, mPaint);
//
//        canvas.restore();
//
//    }

    // protected void configureBounds() {
    // if (isFrist) {
    // oriRationWH = ((float) mDrawable.getIntrinsicWidth())
    // / ((float) mDrawable.getIntrinsicHeight());
    //
    // final float scale = mContext.getResources().getDisplayMetrics().density;
    // int w = Math.min(getWidth(), (int) (mDrawable.getIntrinsicWidth()
    // * scale + 0.5f));
    // int h = (int) (w / oriRationWH);
    //
    // int left = (getWidth() - w) / 2;
    // int top = (getHeight() - h) / 2;
    // int right = left + w;
    // int bottom = top + h;
    //
    // mDrawableSrc.set(left, top, right, bottom);
    // mDrawableDst.set(mDrawableSrc);
    //
    // // int floatWidth = dipTopx(mContext, cropWidth);
    // // int floatHeight = dipTopx(mContext, cropHeight);
    // if (cropWidth > getWidth()) {
    // cropWidth = getWidth() - 10;
    // }
    // if (cropHeight > getWidth()) {
    // cropHeight = getHeight() - 10;
    // }
    // int floatLeft = (getWidth() - cropWidth) / 2;
    // int floatTop = (getHeight() - cropHeight) / 2;
    //
    // // mDrawableFloat.set(floatLeft, floatTop, floatLeft + 400,
    // // floatTop + 400);
    // mDrawableFloat.set(floatLeft, floatTop, cropWidth + floatLeft,
    // cropHeight + floatTop);
    // isFrist = false;
    // }
    //
    // }

    // protected void checkBounds() {
    // int newLeft = mDrawableDst.left;
    // int newTop = mDrawableDst.top;
    //
    // boolean isChange = false;
    // if (mDrawableDst.left < -mDrawableDst.width()) {
    // newLeft = -mDrawableDst.width();
    // isChange = true;
    // }
    //
    // if (mDrawableDst.top < -mDrawableDst.height()) {
    // newTop = -mDrawableDst.height();
    // isChange = true;
    // }
    //
    // if (mDrawableDst.left > getWidth()) {
    // newLeft = getWidth();
    // isChange = true;
    // }
    //
    // if (mDrawableDst.top > getHeight()) {
    // newTop = getHeight();
    // isChange = true;
    // }
    //
    // mDrawableDst.offsetTo(newLeft, newTop);
    // if (isChange) {
    // invalidate();
    // }
    // }

    public Bitmap getCropImage() {
        if (lastbitmap == null) {
            return null;
        }
        Bitmap tmpBitmap = null;
        try {
            tmpBitmap = Bitmap.createBitmap(lastbitmap, bitmapFloat.left
                    - Dstleft, bitmapFloat.top - Dsttop, cropWidth, cropWidth);
        } catch (Exception e) {
            // TODO: handle exception
        }

        if (tmpBitmap == null) {
            return lastbitmap;
        }
//		setDrawable(tmpBitmap, cropWidth, cropWidth);
        // bitmap.recycle();
        return tmpBitmap;
    }

    public int dipTopx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);

    }
}
