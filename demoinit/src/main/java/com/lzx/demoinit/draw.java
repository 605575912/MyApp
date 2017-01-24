package com.lzx.demoinit;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by liangzhenxiong on 16/1/12.
 */
public class draw extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;

    public draw(Context context) {
        super(context);
        init();
    }

    public draw(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public draw(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
    }

    class DrawRun implements Runnable {

        @Override
        public void run() {
//            while (mPlayed) {
//                Canvas canvas = mHolder.lockCanvas();
//                if (canvas == null) {
//                    mPlayed = false;
//                }
                Rect r = new Rect(0,0,10,10);

                mHolder.lockCanvas(r);
                mHolder.lockCanvas(r);
//                mHolder.unlockCanvasAndPost(canvas);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//            }
        }
    }

    private boolean mPlayed = true;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new Thread(new DrawRun()).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
