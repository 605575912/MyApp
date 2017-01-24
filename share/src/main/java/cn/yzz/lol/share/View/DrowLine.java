package cn.yzz.lol.share.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.support.loader.ServiceLoader;

import cn.yzz.lol.share.bean.Timedata;
import cn.yzz.lol.share.factory.DataItem;

/**
 * Created by liangzhenxiong on 15/11/8.
 */
public class DrowLine extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;

    public DrowLine(Context context) {
        super(context);
        init();
    }

    public DrowLine(Context context, AttributeSet attrs) {
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
    Paint paint;
    Paint meanpaint;
    Paint tpaint;
    Paint Npaint;
    static Object wr;
    PlayThread playThread;
    //    Path path;
    DataItem dataItem;

    public void setDataItem(DataItem dataItem) {
        this.dataItem = dataItem;
    }

    void init() {
        wr = new Object();
        playThread = new PlayThread();
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2.0f);
        paint.setAntiAlias(true);
        meanpaint = new Paint();
        meanpaint.setColor(Color.argb(177, 58, 187, 247));
        meanpaint.setStrokeWidth(2.0f);
        meanpaint.setAntiAlias(true);

        tpaint = new Paint();
        tpaint.setColor(Color.DKGRAY);
        tpaint.setStrokeWidth(1.0f);
        tpaint.setAntiAlias(true);
        tpaint.setTextSize(22.0f);
        Npaint = new Paint();
        Npaint.setColor(Color.RED);
        Npaint.setStrokeWidth(1.0f);
        Npaint.setAntiAlias(true);
        Npaint.setTextSize(25.0f);
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
                    paint.setColor(Color.argb(155, 220, 216, 216));
                    canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paint);
                    canvas.drawLine(0, getHeight(), getWidth(), getHeight(), paint);
                    if (dataItem != null && dataItem.getTimedata() != null) {
                        canvas.drawText(dataItem.getHighp(), 1, 50, Npaint);
                        canvas.drawText(dataItem.getLowp(), 1, 80, Npaint);

                        float nowv = Float.valueOf(dataItem.getNowv().replace("+", "")) * 100;
                        float preclose = Float.valueOf(dataItem.getPreclose().replace("+", "")) * 100;
                        float highp = Float.valueOf(dataItem.getHighp().replace("+", "")) * 100;
                        float lowp = Float.valueOf(dataItem.getLowp().replace("-", "")) * 100;
                        float temph = Math.max(Math.abs(preclose - lowp), Math.abs(preclose - highp));


                        highp = preclose + temph;
                        lowp = preclose - temph;
                        float temp = temph * 2;
                        canvas.drawText("" + highp / 100, 1, 20, tpaint);
                        paint.setColor(Color.argb(255, 248, 111, 111));


                        Timedata timedata0 = dataItem.getTimedata()[0];
                        float value0 = timedata0.getCurp() - lowp;
                        float y0 = getHeight() - value0 / temp * getHeight();
                        float indexx = 0, indexy = 0, meanidex = 0, meanindexy = 0;
                        meanidex = indexx = 0;
                        meanindexy = indexy = y0;
                        float meanvalue = timedata0.getCurp();
                        float wt = getWidth() / (float) (240 - 1);
                        for (int i = 1; i < dataItem.getTimedata().length; i++) {
                            Timedata timedata = dataItem.getTimedata()[i];
                            meanvalue = getvalue(meanvalue, timedata.getCurp(), i + 1);
                            float meany = getHeight() - (meanvalue - lowp) / temp * getHeight();
                            canvas.drawLine(meanidex, meanindexy, i * wt, meany, meanpaint);
                            meanidex = i * wt;
                            meanindexy = meany;

                            float y = getHeight() - (timedata.getCurp() - lowp) / temp * getHeight();
                            canvas.drawLine(indexx, indexy, i * wt, y, paint);
                            indexx = i * wt;
                            indexy = y;
                        }

                        float y = getHeight() - (nowv - lowp) / temp * getHeight();
                        canvas.drawLine(indexx, indexy, (dataItem.getTimedata().length - 1) * wt, y, paint);

                        canvas.drawText("实时均价" + meanvalue / 100, 180, getHeight() - 20, tpaint);
                        canvas.drawText("" + lowp / 100, 1, getHeight() - 20, tpaint);
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

    static float getvalue(float value, float indexvalue, int index) {
        return (indexvalue - value) / index + value;
    }


}
