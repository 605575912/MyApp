//package com.lzxmy.demo.cropqqhead;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.os.Message;
//import android.util.DisplayMetrics;
//import android.view.View;
//
//import com.library.uiframe.view.CropImageView;
//import com.lzxmy.demo.BaseActivity;
//import com.lzxmy.demo.R;
//import com.lzxmy.demo.utils.AppManager;
//import com.nostra13.universalimageloader.ServiceLoader;
//import com.support.serviceloader.util.DownloadUtil;
//import com.support.serviceloader.util.ImageUtils;
//
//import java.io.IOException;
//
///**
// * 剪裁类
// */
//public class CropImageActivity extends BaseActivity {
//    public final static String PATH = "PATH";// 图片路径
//    private CropImageView cropImageView;
//    // private String path =
//    // "/storage/sdcard/Android/data/com.xsw.teacher/cache/z5SHosd41427255153_4128_2322_1427103236166.jpg";
//    private String path = "/storage/emulated/0/DCIM/Camera/20150317_160507.jpg";
//    String imageup = "head_up.jpg";
//    String imagename = "head.jpg";
//    int width;
//    int height;
//
//    @Override
//    public void handleMessage(Message msg) {
//        if (msg.what == 0) {
//            Bitmap bitmap = (Bitmap) msg.obj;
//            if (bitmap != null) {
//                cropImageView.setDrawable(bitmap, width, width);
//            }
////            ShowProgressBar.removeDiolog();
//        } else if (msg.what == 1) {
//            String path = (String) msg.obj;
//            if (!path.equals("")) {
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putString("data", path);
//                intent.putExtras(bundle);
//                setResult(RESULT_OK, intent);
//            }
//            AppManager.getAppManager().finishActivity(
//                    CropImageActivity.this);
//        }
//
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cropimage);
////        ShowProgressBar.showDiolog(CropImageActivity.this, "加载中....");
//        cropImageView = (CropImageView) findViewById(R.id.cropImg);
//        if (getIntent().getExtras() != null) {
//            path = getIntent().getStringExtra(PATH);
//        }
////        handler.sendMessageDelayed(handler.obtainMessage(), 500);
//        findViewById(R.id.cancel).setOnClickListener(this);
//        findViewById(R.id.save).setOnClickListener(this);
//        findViewById(R.id.tv_rate).setOnClickListener(this);
//        ServiceLoader.getInstance().submit(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                DisplayMetrics metric = new DisplayMetrics();
//                getWindowManager().getDefaultDisplay().getMetrics(metric);
//                width = metric.widthPixels; // 屏幕宽度（像素）
//                height = metric.heightPixels; // 屏幕高度（像素）
//                Bitmap bitmap = ImageUtils.getimage(cropImageView, path, width,
//                        height);
////                Bitmap bitmap = ImageUtils.getimage(getResources(), R.drawable.welcome02, width, height);
//                if (bitmap == null) {
//                    return;
//                }
//                width = Math.min(width, height);
//                if (width > 720) {
//                    width = 720;
//                } else {
//                    width = width * 9 / 10;
//                }
//                int btimapwidth = Math.min(bitmap.getWidth(), bitmap.getHeight());
//                if (btimapwidth < width) {
//                    float scaleWidht = (float)width / btimapwidth;
//                    bitmap = ImageUtils.zoomBitmap(bitmap, scaleWidht * bitmap.getWidth(), scaleWidht * bitmap.getHeight());
//                }
//                Message msg = handler.obtainMessage();
//                msg.obj = bitmap;
//                msg.what = 0;
//                handler.sendMessage(msg);
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View arg0) {
//        // TODO Auto-generated method stub
//        super.onClick(arg0);
//        switch (arg0.getId()) {
//            case R.id.cancel: {
//                AppManager.getAppManager().finishActivity(CropImageActivity.this);
//            }
//
//            break;
//            case R.id.save: {
//                if (cropImageView != null) {
//                    photocorp(cropImageView.getCropImage());
//                } else {
//                    AppManager.getAppManager().finishActivity(
//                            CropImageActivity.this);
//                }
//
//            }
//
//            break;
//            case R.id.tv_rate: {
//                if (cropImageView != null) {
//                    cropImageView.Rotate();
//                }
//            }
//
//            break;
//
//            default:
//                break;
//        }
//
//    }
//
//    void photocorp(final Bitmap extras) {
//        ServiceLoader.getInstance().submit(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                String string = "";
//                if (extras != null) {// 存放照片的文件夹
//                    // 压缩上传的图片
//                    try {
//                        ImageUtils.createImage(extras, DownloadUtil.getInstance().getFILEPATH()
//                                + "/" + imageup, DownloadUtil.getInstance().getFILEPATH() + "/"
//                                + imagename, 400, 150, 100);
//                        string = DownloadUtil.getInstance().getFILEPATH() + "/" + imageup;
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//
//                }
//                Message msg = new Message();
//                msg.what = 1;
//                msg.obj = string;
//                handler.sendMessage(msg);
//
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        // TODO Auto-generated method stub
//        super.onDestroy();
//        if (handler != null) {
//            handler.removeCallbacksAndMessages(null);
//        }
//    }
//}
