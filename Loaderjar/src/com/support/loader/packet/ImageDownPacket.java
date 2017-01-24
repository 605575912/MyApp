package com.support.loader.packet;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.support.loader.ServiceLoader;
import com.support.loader.utils.DownloadUtil;
import com.support.loader.utils.ImageLoadingListener;
import com.support.loader.utils.ImageUtils;
import com.support.loader.utils.Util;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 图片下载
 */
public class ImageDownPacket extends TaskPacket {
    String url;
    int id = 0;
    //    View view;
    ReentrantLock uriLock;
    ImageOptions options;
    Context context;
    LruCache<String, Bitmap> mMemoryCache;
    Map<Integer, String> cacheKeysForImageViews;
    WeakReference<View> ViewReference;
    WeakReference<ImageLoadingListener> ImageLoadingListenerReference;

    public ImageDownPacket(LruCache<String, Bitmap> mMemoryCache,
                           ImageView imageView, String url, ReentrantLock uriLock,
                           Map<Integer, String> cacheKeysForImageViews, ImageOptions options, ImageLoadingListener imageLoadingListener) {
        this.url = url;
        ViewReference = new WeakReference<View>(imageView);
        ImageLoadingListenerReference = new WeakReference<ImageLoadingListener>(imageLoadingListener);
        this.mMemoryCache = mMemoryCache;
        this.uriLock = uriLock;
        this.cacheKeysForImageViews = cacheKeysForImageViews;
        this.options = options;
    }


    public ImageDownPacket(LruCache<String, Bitmap> mMemoryCache,
                           View imageView, int id, ReentrantLock uriLock,
                           Map<Integer, String> cacheKeysForImageViews, ImageOptions options,
                           Context context, ImageLoadingListener imageLoadingListener) {
        this.id = id;
        ViewReference = new WeakReference<View>(imageView);
        ImageLoadingListenerReference = new WeakReference<ImageLoadingListener>(imageLoadingListener);
        this.mMemoryCache = mMemoryCache;
        this.uriLock = uriLock;
        this.cacheKeysForImageViews = cacheKeysForImageViews;
        this.options = options;
        this.context = context;


    }

    View getView() {
        return ViewReference.get();
    }

    @Override
    public String getTaskId() {
        if (getView() != null) {
            if (url == null) {
                return "" + id + +getView().hashCode();
            } else {
                return "" + url + +getView().hashCode();
            }

        } else {
            if (url == null) {
                return "" + id;
            } else {
                return url;
            }


        }

    }

    @Override
    public void setPriority(TaskPacketType priority) {
        super.setPriority(TaskPacketType.Task_IMAGE);
    }

    @Override
    public void handle() {
        // TODO Auto-generated method stub
        down();
    }

    @Override
    public void stop() {

    }

    /**
     * 下载图片 created by Bear at 2015-1-5 下午6:20:44 TODO
     */
    void down() {

        uriLock.lock();
        Bitmap bmp = null;
        try {
            if (url != null && !url.equals("") && id == 0) {
                if (checkTaskIsNotActual(url, getView()))
                    return;
                bmp = mMemoryCache.get(url);
                if (bmp == null) {
                    String path;
                    if (url.contains("http")) {
                        path = DownloadUtil.getInstance().setPath(Util.getIndexUrl(url));
                        File file = new File(path);
                        if (file.exists()) {
                            if (file.length() <= 10) {
                                file.delete();
                            }
                        }
                        path = DownloadUtil.getInstance().downimage(Util.getIndexUrl(url));
                    } else {
                        path = url;
                    }

                    if (path != null && !path.equals("") && getView() != null) {
                        bmp = ImageUtils.getimage(getView(), Util.getIndexUrl(path), 200);
                        if (bmp == null) {

                            if (options != null) {
                                bmp = options.getImageOnFail();
                                mMemoryCache.put(url, bmp);
                            }
                            return;
                        } else {

                        }
                        if (checkTaskIsNotActual(url, getView()))
                            return;
                        if (bmp != null) {

                            /**
                             * 把图片旋转为正的方向
                             */
                            bmp = rotaingImageView(readPictureDegree(Util.getIndexUrl(path)), bmp);
                            if (getimageLoadingListener() != null) {
                                Bitmap temp = getimageLoadingListener().onLoadingBitmap(String.valueOf(id), getView(), bmp);
                                if (temp != null) {
                                    bmp = temp;
                                }
                            }
                            mMemoryCache.put(url, bmp);
                        }

                    } else {
                        if (options != null) {
                            bmp = options.getImageOnFail();
                            mMemoryCache.put(url, bmp);
                        }
                    }
                }
            } else {
                if (context != null && id != 0) {
                    if (checkTaskIsNotActual(String.valueOf(id), getView()))
                        return;

                    DisplayMetrics metric = new DisplayMetrics();
                    WindowManager wm = (WindowManager) context
                            .getSystemService(Context.WINDOW_SERVICE);
                    wm.getDefaultDisplay().getMetrics(metric);
                    bmp = ImageUtils.getimage(context.getResources(), id, metric.widthPixels,
                            metric.heightPixels);
                    if (getimageLoadingListener() != null) {
                        Bitmap temp = getimageLoadingListener().onLoadingBitmap(String.valueOf(id), getView(), bmp);
                        if (temp != null) {
                            bmp = temp;
                        }
                    }
                    mMemoryCache.put(String.valueOf(id), bmp);

                }

            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            uriLock.unlock();
        }


        if (url != null) {
            if (checkTaskIsNotActual(url, getView())) {
                return;
            }
            DisplayBitmapTask displayBitmapTask = new DisplayBitmapTask(bmp, getView(),
                    url, cacheKeysForImageViews, getimageLoadingListener());

            ServiceLoader.getInstance().getHandler().post(displayBitmapTask);
        } else {
            if (checkTaskIsNotActual(String.valueOf(id), getView())) {
                return;
            }
            DisplayBitmapTask displayBitmapTask = new DisplayBitmapTask(bmp, getView(),
                    String.valueOf(id), cacheKeysForImageViews, getimageLoadingListener());

            ServiceLoader.getInstance().getHandler().post(displayBitmapTask);
        }

    }

    ImageLoadingListener getimageLoadingListener() {
        return ImageLoadingListenerReference.get();
    }

    private boolean checkTaskIsNotActual(String url, View view) {
        if (view == null)
            return true;
        String currentCacheKey = cacheKeysForImageViews.get(view.hashCode());
        return !url.equals(currentCacheKey);
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    @TargetApi(5)
    public static int readPictureDegree(String path) {
        if (Build.VERSION.SDK_INT < 5) {
            return 0;
        }
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
 * 旋转图片
 * @param angle
 * @param bitmap
 * @return Bitmap
 */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        if (angle == 0) {
            return bitmap;
        }
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        return Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
