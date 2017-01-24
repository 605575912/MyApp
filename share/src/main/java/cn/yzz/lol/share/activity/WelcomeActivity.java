package cn.yzz.lol.share.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.support.loader.ServiceLoader;
import com.support.loader.utils.DownloadUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.yzz.lol.share.CatchService;
import cn.yzz.lol.share.R;
import cn.yzz.lol.share.ShareActivity;
import cn.yzz.lol.share.ShareApplication;
import cn.yzz.lol.share.db.SQLiteHelper;

public class WelcomeActivity extends BaseActivity {
    ImageView progressbar;
    Animation anim;
    long starttime = 0;
    boolean isfirst = false;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd日HH时mm分ss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // 隐去电池等图标和一切修饰部分（状态栏部分）
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 隐去标题栏（程序的名字）
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome_layout);

//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) head.getLayoutParams();
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        params.width = dm.widthPixels;
//        params.height = (int) (params.width / 1.5);
//        head.setLayoutParams(params);

        starttime = System.currentTimeMillis();
        progressbar = (ImageView) findViewById(R.id.progressbar);
        handler.sendEmptyMessageDelayed(3, 1500);
//        positive(progressbar);

//        PackageInfo info = null;
//        try {
//            info = XswApplication.app.getPackageManager().getPackageInfo(
//                    XswApplication.app.getPackageName(), 0);
//
//        } catch (PackageManager.NameNotFoundException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }


//        final SharedPreferences preferences = XswApplication.app
//                .getSharedPreferences(APPData.SYS_XSW,
//                        Context.MODE_PRIVATE);

//            linear.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seach_blue_fullscreen));
//            ServiceLoader.getInstance().displayImage(
//                    null, R.drawable.launchimages,
//                    XswApplication.app, linear);


        ///mnt/sdcard/XSW/crash/超大横图.jpg
        ///mnt/sdcard/XSW/crash/超大图.jpg
        ///mnt/sdcard/Camera/P51209-152346.jpg
//        new Thread() {
//            @Override
//            public void run() {
//                DisplayMetrics metric = new DisplayMetrics();
//                getWindowManager().getDefaultDisplay().getMetrics(metric);
//                int width = metric.widthPixels; // 屏幕宽度（像素）
//                int height = metric.heightPixels; // 屏幕高度（像素）
//                int[] defaultDisplay = new int[]{width, height};
//                Log.i("TAG", "开始缩放" + simpleDateFormat.format(new Date(System.currentTimeMillis())));
//                Bitmap bitmap = getimage("/mnt/sdcard/XSW/crash/超长图副本.jpg", defaultDisplay[0], defaultDisplay[1]);
////        ImageView progressbar = (ImageView) findViewById(R.id.progressbar);
////        progressbar.setImageBitmap(bitmap);
//                Log.i("TAG", "开始压缩" + simpleDateFormat.format(new Date(System.currentTimeMillis())));
//                compressImage(bitmap, 200);// 压缩好比例大小后再进行质量压缩
//                Log.i("TAG", "结束事件" + simpleDateFormat.format(new Date(System.currentTimeMillis())));
//            }
//        }.start();
//
//        String name = "imgfile";

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] bytes = baos.toByteArray();


        if (DownloadUtil.getInstance().isNetworkConnected(WelcomeActivity.this)) {
            ServiceLoader.getInstance().submit(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub


                    handler.sendEmptyMessageDelayed(3, 1000);
                    handler.sendEmptyMessageDelayed(0, 4500);
                    SQLiteHelper.getInstance(ShareApplication.application);


                }
            });
        } else {
        handler.sendEmptyMessageDelayed(0, 2500);
        }
//
        Intent intent = new Intent();
        intent.setClass(WelcomeActivity.this, CatchService.class);
        startService(intent);
    }

    Bitmap getimage(String path, int width, int height) {

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int tempwith = Math.max(newOpts.outWidth, newOpts.outHeight);
        int scale = 1;
        if (tempwith > width) {  //只有图片长或宽比屏幕的宽大 才缩放图片


            //1:4

//            if (newOpts.outWidth > newOpts.outHeight && newOpts.outWidth > height) {
//                scale = (newOpts.outWidth / height);
//            } else if (newOpts.outWidth < newOpts.outHeight && newOpts.outHeight > height) {
//                scale = (newOpts.outHeight / height);
//            }
            scale = newOpts.outWidth * newOpts.outHeight / (width * height);

            Log.i("TAG", "最差比例" + scale);
            if (scale > 1) {
                newOpts.inSampleSize = scale / 2;
                Bitmap bitmap = null;
                while (newOpts.inSampleSize > 1 && newOpts.inSampleSize <= scale) {

                    try {
                        Bitmap tempbitmap = BitmapFactory.decodeFile(path, newOpts);
                        Log.i("TAG", "正常时候" + newOpts.inSampleSize);
                        bitmap = tempbitmap;
                        newOpts.inSampleSize--;
                    } catch (OutOfMemoryError e) {
                        // TODO: handle exception
                        Log.i("TAG", "OOM结束" + newOpts.inSampleSize);
                        if (bitmap != null) {
                            return bitmap;
                        }
                        newOpts.inSampleSize++;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;

                    }

                }
                Log.i("TAG", "读取图片" + newOpts.inSampleSize++);
                return bitmap;
//                scale = (newOpts.outWidth * newOpts.outHeight) / (width * height);
            }


        }
        newOpts.inSampleSize = 1;
//        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(path, newOpts);

        } catch (OutOfMemoryError e) {
            // TODO: handle exception
            bitmap = getimage(path, (int) (width * 0.9), (int) (height * 0.9));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bitmap == null) {
            return null;
        }
//        int degree = SelectImageDataFactory.readPictureDegree(path.replace("file://", ""));
//        bitmap = SelectImageDataFactory.rotaingImageView(degree, bitmap);

        return bitmap;


    }

    Bitmap compressImage(Bitmap image, int kb) {
        if (image == null) {
            return null;
        }
        int options = 100;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中


        while (baos.toByteArray().length / 1024 > kb) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            double f = (baos.toByteArray().length / 1024) / kb;
            Log.i("TAG", "倍数" + f);
//19
            if (f <= 2) {
                if (f < 1.5) {
                    options -= 1;
                } else {
                    options -= 5;
                }

            } else {
                options -= 10;
            }
            if (options <= 0 || options >= 100) {
                Log.i("TAG", "结束比例" + 1);
                baos.reset();// 重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, 1, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                break;
            }
            Log.i("TAG", "执行比例" + options);
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Log.i("TAG", "写入文件");
            String filename = simpleDateFormat.format(new Date(System.currentTimeMillis())) + ".jpg";
            String path1 = "/sdcard/XSW/crash/";
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {

                File dir = new File(path1);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
            FileOutputStream stream = new FileOutputStream(path1 + filename);
            stream.write(baos.toByteArray());
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    @Override
    public void handleMessage(android.os.Message msg) {
        if (isfirst)
            return;
        switch (msg.what) {
            case 0: {
//                showTitleDialog(WelcomeActivity.this, "", "", "");
//                showTipsView(this,"");
                long time = System.currentTimeMillis() - starttime;
                if (time < 2500) {
                    handler.sendEmptyMessageDelayed(0, 2500 - time);
                    return;
                }


                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this,
                        ShareActivity.class);
                startActivity(intent);
                if (anim != null) {
                    anim.cancel();
                }
                finish();
//                AppManager.getAppManager().finishActivity(
//                        WelcomeActivity.this);
            }
            break;
            case 2: {
                long time = System.currentTimeMillis() - starttime;
                if (time < 2500) {
                    handler.sendEmptyMessageDelayed(0, 2500 - time);
                    return;
                }

                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this,
                        ShareActivity.class);
                startActivity(intent);
                if (anim != null) {
                    anim.cancel();
                }
                finish();
//                AppManager.getAppManager().finishActivity(
//                        WelcomeActivity.this);
            }
            break;
            case 4: {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this,
                        ShareActivity.class);
                startActivity(intent);
                if (anim != null) {
                    anim.cancel();
                }
                finish();
//                AppManager.getAppManager().finishActivity(
//                        WelcomeActivity.this);
            }
            break;


            default:
                break;
        }

    }

    void checkAnimation(final View imageView) {
        ValueAnimator widthAnimation = com.nineoldandroids.animation.ValueAnimator.ofFloat(0, 80.0f);
        widthAnimation.addUpdateListener(new com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener()

                                         {
                                             @Override
                                             public void onAnimationUpdate(com.nineoldandroids.animation.ValueAnimator valueAnimator) {
                                                 Float value = (Float) valueAnimator.getAnimatedValue();


//                                                 ViewHelper.setScaleX(imageView, value);
                                                 ViewHelper.setTranslationX(imageView, value);
                                                 Log.i("TAG", "==" + value);

                                             }
                                         }
        );
        widthAnimation.setDuration(40000);
        widthAnimation.start();
    }



    void positive(View v) {
        anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        /** 匀速插值器 */
        LinearInterpolator lir = new LinearInterpolator();
        anim.setInterpolator(lir);
        anim.setRepeatCount(-1);
        anim.setDuration(10000);
        /** 动画完成后不恢复原状 */
        anim.setFillAfter(true);

        v.startAnimation(anim);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (anim != null) {
            anim.cancel();
        }
    }


    /**
     * 按下键盘上返回按钮退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// 后退
            // TODO Auto-generated method stub
            finish();
//            AppManager.getAppManager().AppExit(getApplicationContext());

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
