package com.support.loader.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;

import java.util.Stack;

//import com.nostra13.universalimageloader.ServiceLoader;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 *
 * @author lzx
 * @version 1.0
 * @created 2012-6-21
 */
public class AppManager {

     static Stack<Activity> activityStack;
     static Stack<Activity> baseactivityStack;
     static Stack<Activity> tempactivityStack;
    private volatile static AppManager instance;

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            synchronized (AppManager.class) {
                if (instance == null) {
                    instance = new AppManager();
                    if (activityStack == null) {
                        activityStack = new Stack<Activity>();
                    }
                    if (baseactivityStack == null) {
                        baseactivityStack = new Stack<Activity>();
                    }
                    if (tempactivityStack == null) {
                        tempactivityStack = new Stack<Activity>();
                    }
                }
            }
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 添加Activity到堆栈
     */
    public void addTempActivity(Activity activity) {
        if (tempactivityStack == null) {
            tempactivityStack = new Stack<Activity>();
        }
        if (activityStack != null) {
            if (activityStack.indexOf(activity) > -1) {
                activityStack.remove(activity);
            }
        }
        tempactivityStack.add(activity);
    }

    /**
     * 添加BaseActivity到堆栈
     */
    public void addbaseActivity(Activity activity) {
        if (baseactivityStack == null) {
            baseactivityStack = new Stack<Activity>();
        }

        baseactivityStack.add(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (activityStack.indexOf(activity) > -1) {
                activityStack.remove(activity);
            }
            if (tempactivityStack.indexOf(activity) > -1) {
                tempactivityStack.remove(activity);
            }
            if (baseactivityStack.indexOf(activity) > -1) {
                baseactivityStack.remove(activity);
            }
            if (activity != null) {
                activity.finish();
                activity = null;
            }

        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack != null) {
            if (activityStack.size() > 0) {
                for (Activity activity : activityStack) {
                    if (null != activity) {
                        activity.finish();
                        // activity.overridePendingTransition(R.anim.zoom_nor,
                        // R.anim.zoom_in);
                    }
                }
                activityStack.clear();
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity(Activity tempactivity) {
        if (activityStack != null) {
            if (activityStack.size() > 0) {
                for (Activity activity : activityStack) {
                    if (null != activity) {
                        if (tempactivity != activity)
                            activity.finish();
                        // activity.overridePendingTransition(R.anim.zoom_nor,
                        // R.anim.zoom_in);
                    }
                }
                activityStack.clear();
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllBaseActivity() {
        if (baseactivityStack != null)
            if (baseactivityStack.size() > 0) {
                for (Activity activity : baseactivityStack) {
                    if (null != activity) {
                        activity.finish();
                    }
                }
                baseactivityStack.clear();
            }

    }

    /**
     * 结束所有Activity
     */
    public void finishAllTempActivity() {
        if (tempactivityStack != null)
            if (tempactivityStack.size() > 0) {
                for (Activity activity : tempactivityStack) {
                    if (null != activity) {
                        activity.finish();
                    }
                }
                tempactivityStack.clear();
            }

    }

    /**
     * 退出应用程序
     */
    public void AppExit(final Context context) {
//        ShowToast.cancelTips();

        finishAllActivity();
        finishAllTempActivity();
        finishAllBaseActivity();

        try {
            new Thread() {
                @Override
                public void run() {
                    if (context != null) {

//                        Intent intent = new Intent();
//                        intent.setClass(context, MyService.class);
//                        context.stopService(intent);
                    }
//                    ServiceLoader.getInstance().exit();

                    try {
                        NotificationManager mNotificationManager = (NotificationManager) context
                                .getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.cancelAll();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    android.os.Process.killProcess(android.os.Process.myUid());
                    android.os.Process.killProcess(android.os.Process.myTid());
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);

                }

            }.start();

        } catch (Exception e) {
        }
    }
}