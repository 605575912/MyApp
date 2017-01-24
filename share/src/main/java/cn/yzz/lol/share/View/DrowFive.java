package cn.yzz.lol.share.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.support.loader.ServiceLoader;

import cn.yzz.lol.share.bean.FiveItem;

/**
 * Created by liangzhenxiong on 15/11/8.
 */
public class DrowFive extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;

    public DrowFive(Context context) {
        super(context);
        init();
    }

    public DrowFive(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public void invalidate(boolean f) {
        if (wr != null) {
            play = true;
            synchronized (wr) {
                wr.notifyAll();
            }
        }
    }

    boolean play = true;
    Paint gpaint;
    Paint tpaint;
    Paint rpaint;
    static Object wr;
    PlayThread playThread;
    //    Path path;
    FiveItem fiveItem;

    public FiveItem getFiveItem() {
        return fiveItem;
    }

    public void setFiveItem(FiveItem fiveItem) {
        this.fiveItem = fiveItem;
    }

    void init() {
        wr = new Object();
        playThread = new PlayThread();
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
        gpaint = new Paint();
        gpaint.setStrokeWidth(1.0f);
        gpaint.setAntiAlias(true);

        gpaint.setTextSize(22.0f);
        gpaint.setStyle(Paint.Style.FILL);

        tpaint = new Paint();
        tpaint.setColor(Color.rgb(20, 140, 8));
        tpaint.setStrokeWidth(1.0f);
        tpaint.setAntiAlias(true);
        tpaint.setTextSize(22.0f);
//        tpaint.setTextAlign(Paint.Align.CENTER);

        rpaint = new Paint();
        rpaint.setColor(Color.RED);
        rpaint.setStrokeWidth(1.0f);
        rpaint.setAntiAlias(true);
        rpaint.setTextSize(22.0f);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (playThread != null) {
            play = true;
            ServiceLoader.getInstance().submit(playThread);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        play = false;
        synchronized (wr) {
            wr.notifyAll();
        }
    }


    class PlayThread implements Runnable {
        @Override
        public void run() {
            while (play) {
                Canvas canvas = mHolder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(Color.WHITE);
                    gpaint.setColor(Color.argb(155, 220, 216, 216));
                    canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, gpaint);
                    gpaint.setColor(Color.argb(205, 105, 104, 104));
                    if (fiveItem != null) {

                        Rect rect = new Rect();
                        tpaint.getTextBounds(fiveItem.getAsk1p(), 0, fiveItem.getAsk1p().length(), rect);
                        float height = (float) (getHeight() / 10.0);

                        drawText(canvas, rect, height, "\t卖5", fiveItem.getAsk5p(), fiveItem.getAsk5v(), 0);
                        drawText(canvas, rect, height, "\t卖4", fiveItem.getAsk4p(), fiveItem.getAsk4v(), 1);
                        drawText(canvas, rect, height, "\t卖3", fiveItem.getAsk3p(), fiveItem.getAsk3v(), 2);
                        drawText(canvas, rect, height, "\t卖2", fiveItem.getAsk2p(), fiveItem.getAsk2v(), 3);
                        drawText(canvas, rect, height, "\t卖1", fiveItem.getAsk1p(), fiveItem.getAsk1v(), 4);


                        int h = getHeight() / 2;
                        drawText(canvas, rect, height, "\t买1", fiveItem.getBid1p(), fiveItem.getBid1v(), 1, h);
                        drawText(canvas, rect, height, "\t买2", fiveItem.getBid2p(), fiveItem.getBid2v(), 2, h);
                        drawText(canvas, rect, height, "\t买3", fiveItem.getBid3p(), fiveItem.getBid3v(), 3, h);
                        drawText(canvas, rect, height, "\t买4", fiveItem.getBid4p(), fiveItem.getBid4v(), 4, h);
                        drawText(canvas, rect, height, "\t买5", fiveItem.getBid5p(), fiveItem.getBid5v(), 5, h);


                    }

                }
                mHolder.unlockCanvasAndPost(canvas);
                try {
                    synchronized (wr) {
                        wr.wait();
                    }
                } catch (InterruptedException ie) {
                    // Do nothing
                }
            }

        }
    }

    void drawText(Canvas canvas, Rect rect, float height, String name, String value, String askv, int index) {
        canvas.drawText(name, -rect.left, -rect.top + index * height + 5,
                gpaint);

        if (value.indexOf("-") > -1) {
            canvas.drawText(value.replace("-", ""), 2 * -rect.left + rect.right, -rect.top + index * height + 5,
                    tpaint);
        } else if (value.indexOf("+") > -1) {
            canvas.drawText(value.replace("+", ""), 2 * -rect.left + rect.right, -rect.top + index * height + 5,
                    rpaint);
        } else {

            canvas.drawText(value, 2 * -rect.left + rect.right, -rect.top + index * height + 5,
                    gpaint);
        }

        Rect rectv = new Rect();
        gpaint.getTextBounds(askv, 0, askv.length(), rectv);
        canvas.drawText(askv, (float) (getWidth() + rectv.left - rectv.right - 5), -rect.top + index * height + 5,
                gpaint);
    }

    void drawText(Canvas canvas, Rect rect, float height, String name, String value, String askv, int index, float indexh) {
        canvas.drawText(name, -rect.left, +index * height + indexh - 5,
                gpaint);
        if (value.indexOf("-") > -1) {
            canvas.drawText(value.replace("-", ""), 2 * -rect.left + rect.right, index * height + indexh - 5,
                    tpaint);
        } else if (value.indexOf("+") > -1) {
            canvas.drawText(value.replace("+", ""), 2 * -rect.left + rect.right, index * height + indexh - 5,
                    rpaint);
        } else {
            canvas.drawText(value, 2 * -rect.left + rect.right, index * height + indexh - 5,
                    gpaint);
        }


        Rect rectv = new Rect();
        gpaint.getTextBounds(askv, 0, askv.length(), rectv);
        canvas.drawText(askv, (float) (getWidth() + rectv.left - rectv.right - 5), +index * height + indexh - 5,
                gpaint);
    }

    static float getvalue(float value, float indexvalue, int index) {
        return (indexvalue - value) / index + value;
    }


}
