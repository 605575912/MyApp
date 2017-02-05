package com.lzxmy.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lzx on 2017/1/7.
 */

public class DlnaFrameLayout extends FrameLayout {
    Handler handler;
    HandlerThread handlerThread;

    public DlnaFrameLayout(Context context) {
        super(context);
        init();
    }

    public DlnaFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DlnaFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DlnaFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    View rootView;
    int width;
    int height;

    private void init() {
        rootView = this;
        setBackgroundColor(Color.WHITE);
    }

    private void retWidth(View rootView) {
        ViewGroup.LayoutParams params = rootView.getLayoutParams();
        if (params == null) {
            DisplayMetrics dm = getResources().getDisplayMetrics();
            width = dm.widthPixels;
            height = dm.heightPixels;
        } else {
            width = params.width;
            height = params.height;
        }
    }

    private void createImg(Bitmap cacheBitmap) {
        if (cacheBitmap == null) {
            Log.e("DlnaFrameLayout", "cacheBitmap is null! can not creat ImageFile");
            return;
        }
        File f = new File("/mnt/sdcard/view_" + System.currentTimeMillis() + ".png");
        FileOutputStream fos = null;
        ByteArrayOutputStream bos = null;
        try {
            f.createNewFile();
            bos = new ByteArrayOutputStream();
            cacheBitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            fos = new FileOutputStream(f);
            fos.write(bitmapdata);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cacheBitmap.recycle();
    }

    int ishasRet = 0;
    int DrawingCache = 0;

    private Bitmap createBitmap(View rootView) {
        if (rootView == null) {
            Log.e("DlnaFrameLayout", "rootView is null! can not creat ImageFile");
            return null;
        }
        android.view.ViewParent parent = rootView.getParent();
        if (parent != null) {
            if (ishasRet < 1) {
                retWidth(rootView);
            }
            ishasRet = 1;
        } else {
            if (ishasRet > -1) {
                retWidth(rootView);
            }
            ishasRet = -1;
        }
        if (DrawingCache == 0) {
            return getCacheBitmap(rootView);
        } else if (DrawingCache == 1) {
            return getCacheBitmap(rootView);
        } else {
            return getMeasureBitmap(rootView);
        }
    }

    private Bitmap getCacheBitmap(View rootView) {
        boolean willNotCache = rootView.willNotCacheDrawing();
        rootView.setWillNotCacheDrawing(false);
        rootView.buildDrawingCache();
        Bitmap cacheBitmap = rootView.getDrawingCache();
        if (cacheBitmap != null) {
            DrawingCache = 1;
            Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
            rootView.setWillNotCacheDrawing(willNotCache);
            return bitmap;
        } else {
            DrawingCache = -1;
            return getMeasureBitmap(rootView);
        }
    }

    private Bitmap getMeasureBitmap(View rootView) {
        if (width <= 0 || height <= 0) {
            Log.e("DlnaFrameLayout", "rootView width and height must be > 0");
            return null;
        }
        rootView.measure(width, height);
        rootView.layout(0, 0, width, height);
        Bitmap cacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(cacheBitmap);
        rootView.draw(c);
        return cacheBitmap;
    }

    Object object = new Object();

    public void startCath(final long delayed) {
        synchronized (object) {
            if (handler == null && handlerThread == null) {
                handlerThread = new HandlerThread("DnlaThread");
                handlerThread.start();
                handler = new Handler(handlerThread.getLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        createImg(createBitmap(rootView));
                        handler.sendEmptyMessageDelayed(0, delayed);
                    }
                };
                handler.sendEmptyMessage(0);
            }
        }

    }

    public void quit() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (handlerThread != null) {
            handlerThread.quit();
        }
        ishasRet = 0;
        handler = null;
        handlerThread = null;
    }

    public void startCath() {
        startCath(10000);
    }

    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child == null) {
            return;
        }
        if (child.getParent() != null) {
            if (rootView instanceof ViewGroup) {
                ((ViewGroup) rootView).removeAllViews();
            }
            rootView = child.getRootView();
            return;
        }
        super.addView(child, index, params);
    }
}
