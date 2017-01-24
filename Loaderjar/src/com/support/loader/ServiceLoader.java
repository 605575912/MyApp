package com.support.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.support.loader.packet.DisplayBitmapTask;
import com.support.loader.packet.ImageDownPacket;
import com.support.loader.packet.ImageOptions;
import com.support.loader.packet.LoaderType;
import com.support.loader.packet.TaskPacket;
import com.support.loader.packet.TaskPacketType;
import com.support.loader.utils.ImageLoadingListener;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.ReentrantLock;

public enum  ServiceLoader implements Serializable {
    INSTANCE;
    public Context app;
    ExecutorService listenerExecutor = null;
     LruCache<String, Bitmap> mMemoryCache;
    public static boolean isexit = false;
    private final BlockingQueue<TaskPacket> queue = new ArrayBlockingQueue<TaskPacket>(
            100, true);
    private final Map<String, ReentrantLock> uriLocks = new WeakHashMap<String, ReentrantLock>();
    private final Map<Integer, String> cacheKeysForImageViews = Collections
            .synchronizedMap(new HashMap<Integer, String>());
    private final ArrayList<String> runningclassname = new ArrayList<String>();//真正执行任务的类名
    Map<String, LoaderType> classnames = new HashMap<String, LoaderType>();//任务对于状态
    Map<String, BlockingQueue> typequeue = new HashMap<String, BlockingQueue>(); //任务队列
    public Map<LoaderType, Object> listennerHashMap = new HashMap<LoaderType, Object>(); //监听任务队列

    public Handler getHandler() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }

    Handler handler;

    private ServiceLoader() {
        if (mMemoryCache == null) {
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            int cacheSize = maxMemory / 7;
//            mMemoryCache = new LruCache<String, Bitmap>(cacheSize);
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    if (Build.VERSION.SDK_INT >= 19) {    //API 19
                        return bitmap.getAllocationByteCount();
                    }
                    if (Build.VERSION.SDK_INT >= 12) {
                        return bitmap.getByteCount() / 1024;
                    }
                    return bitmap.getRowBytes() * bitmap.getHeight() / 1024;


                }

                @Override
                protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
//                    super.entryRemoved(evicted, key, oldValue, newValue);
                    oldValue.recycle();
                }
            };
        }
        init();
    }


    public void createLoader(String classname) {
        if (!classnames.containsKey(classname)) {
            classnames.put(classname, LoaderType.LOADER_CREATE);

            BlockingQueue<TaskPacket> queue = new ArrayBlockingQueue<TaskPacket>(
                    20, true);
            typequeue.put(classname, queue);
            runningclassname.add(0, classname);
        }
    }

    public void pauseLoader(String classname) {
        if (classnames.containsKey(classname)) {
            classnames.put(classname, LoaderType.LOADER_PAUSE);
        }
    }

    public void startLoader(String classname) {
        if (classnames.containsKey(classname)) {
            classnames.put(classname, LoaderType.LOADER_START);
        }
    }

    public void stopLoader(String classname) {
        if (classnames.containsKey(classname)) {
            classnames.put(classname, LoaderType.LOADER_STOP);
            runningclassname.remove(classname);
        }
    }

    public void setListenerLoader(LoaderType loaderType, Object o) {

        if (!listennerHashMap.containsKey(loaderType)) {
            listennerHashMap.put(loaderType, o);
//            listennerHashMap.remove(classname);
        }

    }

    public Object getListenerLoader(LoaderType loaderType) {

        if (listennerHashMap.containsKey(loaderType)) {
            return listennerHashMap.get(loaderType);
        }

        return null;
    }

//    private static class SingletonHolder {
//        /**
//         * 这种方式同样利用了类加载机制来保证只创建一个instance实例。
//         * 它与饿汉模式一样，也是利用了类加载机制，因此不存在多线程并发的问题。
//         * 不一样的是，它是在内部类里面去创建对象实例。
//         * 这样的话，只要应用中不使用内部类，JVM就不会去加载这个单例类，也就不会创建单例对象，从而实现懒汉式的延迟加载。
//         * 也就是说这种方式可以同时保证延迟加载和线程安全。
//         */
//        static ServiceLoader instance = new ServiceLoader();
//    }

    public static ServiceLoader getInstance() {
        return INSTANCE;
    }

    public void Init(Context context) {
        app = context.getApplicationContext();
    }

    /**
     * 获取图片缓存 2015-4-1 @author lzx
     */
    public Bitmap getBitmap(String uri) {
        return mMemoryCache.get(uri);
    }

    /**
     * 添加图片缓存 2015-4-1 @author lzx
     */
    public void addBitmap(String uri, Bitmap bitmap) {
        mMemoryCache.put(uri, bitmap);
    }

    /**
     * 异步加载图片 2015-4-1 @author lzx
     */
    public void displayImage(ImageOptions Options, String uri,
                             ImageView imageView, ImageLoadingListener imageLoadingListener) {


        if (TextUtils.isEmpty(uri) && imageView != null) {
            cacheKeysForImageViews.remove(imageView.hashCode());
            if (Options != null) {
                DisplayBitmapTask.setImageBimap(imageView, Options.getImageOnFail());
            }
        } else {
            if (imageView != null) {
//                int width = getImageViewFieldValue(imageView, "mMaxWidth"); // Check maxWidth parameter
//                int height = getImageViewFieldValue(imageView, "mMaxHeight"); // Check maxHeight parameter
//                uri = uri + Util.URLIDEX + width + height;
                cacheKeysForImageViews.put(imageView.hashCode(), uri);
            }
            Bitmap headBitmap = mMemoryCache.get(uri);
            if (headBitmap != null && imageView != null) {
//                cacheKeysForImageViews.remove(imageView.hashCode());
//                DisplayBitmapTask.setImageBimap(imageView, headBitmap, false);
                DisplayBitmapTask displayBitmapTask = new DisplayBitmapTask(headBitmap, imageView,
                        uri, cacheKeysForImageViews, imageLoadingListener);

                ServiceLoader.getInstance().getHandler().post(displayBitmapTask);
            } else {
                if (Options != null && imageView != null) {

                    DisplayBitmapTask.setImageBimap(imageView, Options.getStubImageBitmap());
                }
                ServiceLoader.getInstance().sendPacket(
                        new ImageDownPacket(mMemoryCache, imageView, uri,
                                getLockForUri(uri), cacheKeysForImageViews,
                                Options, imageLoadingListener));
            }
        }
    }

    //    public static String generateKey(String imageUri, ImageSize targetSize) {
//        return new StringBuilder(imageUri).append(URI_AND_SIZE_SEPARATOR).append(targetSize.getWidth()).append(WIDTH_AND_HEIGHT_SEPARATOR).append(targetSize.getHeight()).toString();
//    }
    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = (Integer) field.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {

        }
        return value;
    }

    /**
     * 异步加载图片 2015-4-1 @author lzx
     */
    public void displayImage(ImageOptions Options, String uri,
                             ImageView imageView) {

        displayImage(Options, uri, imageView, null);

    }

    /**
     * 异步加载图片 2015-4-1 @author lzx
     */
    public void displayImage(ImageOptions Options, int drawableid,
                             Context context, View view, ImageLoadingListener imageLoadingListener) {
        if (view == null) {
            ServiceLoader.getInstance().sendPacket(
                    new ImageDownPacket(mMemoryCache, view, drawableid,
                            getLockForUri(drawableid + ""),
                            cacheKeysForImageViews, Options, context, imageLoadingListener));
            return;
        }
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            displayImage(Options, drawableid, context, imageView, imageLoadingListener);
            return;
        } else {
            if (drawableid == 0) {
                cacheKeysForImageViews.remove(view.hashCode());
                if (Options != null) {
                    DisplayBitmapTask.setImageBimap(view, Options.getImageOnFail());
//                    BitmapDrawable bd = new BitmapDrawable(
//                            context.getResources(), Options.getImageOnFail());
//                    view.setBackgroundDrawable(bd);

                }
            } else {
                Bitmap headBitmap = mMemoryCache
                        .get(String.valueOf(drawableid));
                if (headBitmap != null) {
                    if (view != null) {
                        cacheKeysForImageViews.remove(view.hashCode());
//                        BitmapDrawable bd = new BitmapDrawable(
//                                context.getResources(), headBitmap);
//                        view.setBackgroundDrawable(bd);
                        DisplayBitmapTask.setImageBimap(view, headBitmap, false);

                    }
                } else {
                    if (Options != null) {
//                        BitmapDrawable bd = new BitmapDrawable(
//                                context.getResources(),
//                                Options.getStubImageBitmap());
//                        view.setBackgroundDrawable(bd);
                        DisplayBitmapTask.setImageBimap(view, Options.getStubImageBitmap());
                    }
                    cacheKeysForImageViews.put(view.hashCode(),
                            String.valueOf(drawableid));
                    ServiceLoader.getInstance().sendPacket(
                            new ImageDownPacket(mMemoryCache, view, drawableid,
                                    getLockForUri(drawableid + ""),
                                    cacheKeysForImageViews, Options, context, imageLoadingListener));
                }
            }
        }

    }

    /**
     * 异步加载图片 2015-4-1 @author lzx
     */
    public void displayImage(ImageOptions Options, int drawableid,
                             Context context, View view) {
        displayImage(Options, drawableid, context, view, null);

    }

    /**
     * 异步加载图片 2015-4-1 @author lzx
     */
    void displayImage(ImageOptions Options, int drawableid, Context context,
                      ImageView imageView, ImageLoadingListener imageLoadingListener) {
        if (imageView == null) {
            return;
        }
        if (drawableid == 0) {
            if (Options != null) {
                cacheKeysForImageViews.remove(imageView.hashCode());
                DisplayBitmapTask.setImageBimap(imageView, Options.getImageOnFail());
//                imageView.setImageBitmap(Options.getImageOnFail());
            }
        } else {
            Bitmap headBitmap = mMemoryCache.get(String.valueOf(drawableid));
            if (headBitmap != null) {
                cacheKeysForImageViews.remove(imageView.hashCode());
                DisplayBitmapTask.setImageBimap(imageView, headBitmap, false);
//                imageView.setImageBitmap(headBitmap);
            } else {
                if (Options != null) {
                    DisplayBitmapTask.setImageBimap(imageView, Options.getStubImageBitmap());
//                    imageView.setImageBitmap(Options.getStubImageBitmap());
                }
                cacheKeysForImageViews.put(imageView.hashCode(),
                        String.valueOf(drawableid));

                ServiceLoader.getInstance().sendPacket(
                        new ImageDownPacket(mMemoryCache, imageView,
                                drawableid, getLockForUri(drawableid + ""),
                                cacheKeysForImageViews, Options, context, imageLoadingListener));
            }
        }
    }

    /**
     * 异步加载图片 2015-4-1 @author lzx
     */
    void displayImage(ImageOptions Options, int drawableid, Context context,
                      ImageView imageView) {
        displayImage(Options, drawableid, context, imageView, null);

    }

    ReentrantLock getLockForUri(String uri) {
        if (uriLocks.size() > 30) {
            uriLocks.clear();
        }
        ReentrantLock lock = uriLocks.get(uri);
        if (lock == null) {
            lock = new ReentrantLock();
            uriLocks.put(uri, lock);
        }
        return lock;
    }

    void init() {
        if (listenerExecutor == null) {
            listenerExecutor = Executors.newFixedThreadPool(20,
                    new ThreadFactory() {

                        @Override
                        public Thread newThread(Runnable runnable) {
                            Thread thread = new Thread(runnable, "Loader");
                            return thread;
                        }
                    });
        }
        if (!isexit) {
            listenerExecutor.submit(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    runpacket(queue);
                }
            });
        }

    }

    void runpacket(BlockingQueue<TaskPacket> taskPackets) {
        while (!isexit) {
            final TaskPacket packet = nextPacket(taskPackets);
            if (packet != null) {
                packet.handle();
                packet.stop();
            }
        }
    }

    /**
     */

    public boolean submit(Runnable runnable) {
        if (listenerExecutor != null) {
            if (!listenerExecutor.isShutdown()) {
                listenerExecutor.submit(runnable);
                return true;
            }
        }
        return false;
    }

    public void exit() {
        isexit = true;
        uriLocks.clear();
        queue.clear();
        listenerExecutor.shutdown();
    }

    /**
     * @return packet
     */
    private TaskPacket nextPacket(BlockingQueue<TaskPacket> taskPackets) {
        TaskPacket packet = null;
        while (true && (packet = taskPackets.poll()) == null) {

            try {
                synchronized (taskPackets) {
                    taskPackets.wait();
                }
            } catch (InterruptedException ie) {
                // Do nothing
            }
        }
        return packet;
    }

    /**
     * @param packet
     */
    public void sendPacket(final TaskPacket packet) {
        // if (queue.size() > 25) {
        // queue.clear();
        // }
        if (isexit) {
            return;
        }


        if (!runningclassname.isEmpty()) {
            String classname = runningclassname.get(0);
            if (classnames.containsKey(packet.getTaskTypeName())) {
                LoaderType loaderType = classnames.get(packet.getTaskTypeName());
                if (loaderType == LoaderType.LOADER_CREATE) {

                }
                if (loaderType == LoaderType.LOADER_PAUSE) {

                }
            }
            final BlockingQueue blockingQueue = typequeue.get(classname);
            if (!blockingQueue.isEmpty()) {
                listenerExecutor.submit(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        runpacket(blockingQueue);
                    }
                });
            }

        }

        if (TextUtils.isEmpty(packet.getTaskTypeName())) {
//            packet.setTaskTypeName(runningname.);
        }

//        packet.getTaskTypeName();
        boolean iseixt = false;
        for (TaskPacket temp : queue) {
            if (temp.getTaskId().equals(packet.getTaskId())) {

                iseixt = true;
                break;
            }
        }

        if (!iseixt) {
            packet.start();
            if (packet.getPriority() != TaskPacketType.Task_IMAGE || queue.size() > 10) {
                listenerExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        packet.handle();
                        packet.stop();
                    }
                });
            } else {

                queue.offer(packet);
                synchronized (queue) {
                    queue.notifyAll();
                }
            }
        } else {

        }
    }
}
