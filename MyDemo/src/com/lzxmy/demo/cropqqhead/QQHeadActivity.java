//package com.lzxmy.demo.cropqqhead;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.widget.Button;
//import android.widget.LinearLayout;
//
//import com.lzxmy.demo.BaseActivity;
//import com.lzxmy.demo.cirelist.RoundImageView;
//import com.lzxmy.demo.utils.ShowToast;
//import com.support.serviceloader.util.DownloadUtil;
//import com.support.serviceloader.util.ImageUtils;
//
//import java.io.File;
//
///**
// * Created by apple on 15/9/10.
// */
//public class QQHeadActivity extends BaseActivity {
//    private String theLarge = "";// 照相原图路径
//    String imagename = "head.jpg";
//    final int photo = 2;
//    RoundImageView roundImageView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        LinearLayout linearLayout = new LinearLayout(this);
//        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//        roundImageView = new RoundImageView(this);
//        linearLayout.addView(roundImageView, 100, 100);
//        Button button = new Button(this);
//        button.setText("相机");
//        Button buttonimage = new Button(this);
//        buttonimage.setText("图库");
//        linearLayout.addView(button);
//        linearLayout.addView(buttonimage);
//        setContentView(linearLayout);
//    }
//
//
//    void Camera() {
//        // 判断是否挂载了SD卡
//        // String storageState = Environment.getExternalStorageState();
//        // if (storageState.equals(Environment.MEDIA_MOUNTED)) {
//        // savePath = Environment.getExternalStorageDirectory()
//        // .getAbsolutePath() + DownloadUtil.IMAGE_PATH;// 存放照片的文件夹
//        // File savedir = new File(savePath);
//        // if (!savedir.exists()) {
//        // savedir.mkdirs();
//        // }
//        // }
//
//        // 没有挂载SD卡，无法保存文件
//        if (DownloadUtil.getInstance().getFILEPATH().equals("")) {
//            ShowToast.showTips(QQHeadActivity.this, "无法调用相机～");
//            // UIHelper.ToastMessage(AnomalyCreate.this,
//            // "无法保存照片，请检查SD卡是否挂载");
//            return;
//        }
//
//
//        try {
//            File out = new File(DownloadUtil.getInstance().getFILEPATH(), imagename);
//            theLarge = out.getPath();
//            Uri uri = Uri.fromFile(out);
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//            startActivityForResult(intent,
//                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
//        } catch (Exception e) {
//            // TODO: handle exception
//            ShowToast.showTips(QQHeadActivity.this, "无法调用相机～");
//        }
//
//    }
//
//    void chooseImage() {
//        Intent intentChoicePhoto = new Intent(
//                "android.intent.action.GET_CONTENT");
//        intentChoicePhoto.setType("image/*");
//        intentChoicePhoto.addCategory("android.intent.category.OPENABLE");
//        if (Build.VERSION.SDK_INT < 19) {
//            startActivityForResult(Intent.createChooser(intentChoicePhoto,
//                            "\u8BF7\u9009\u62E9\u7167\u7247\u8FDB\u884C\u4E0A\u4F20"),
//                    ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
//            return;
//        }
//        startActivityForResult(Intent.createChooser(intentChoicePhoto,
//                        "\u8BF7\u9009\u62E9\u7167\u7247\u8FDB\u884C\u4E0A\u4F20"),
//                ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
//    }
//
//    @Override
//    public void onActivityResult(final int requestCode, int resultCode,
//                                 final Intent data) {
//        if (resultCode != Activity.RESULT_OK)
//            return;
//        switch (requestCode) {
//            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA: {
//                if (theLarge != null && !theLarge.equals("")) {
//                    Intent intent = new Intent(QQHeadActivity.this,
//                            CropImageActivity.class);
//                    intent.putExtra(CropImageActivity.PATH, theLarge);
//                    startActivityForResult(intent, photo);
//                    return;
//                } else {
//                    ShowToast.showTips(QQHeadActivity.this, "不能保存图片");
//                }
//            }
//            break;
//            case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD: {
//                if (data != null) {
//                    // savePath = Environment.getExternalStorageDirectory()
//                    // .getAbsolutePath() + DownloadUtil.IMAGE_PATH;
//                    // File savedir = new File(savePath);
//                    // if (!savedir.exists()) {
//                    // savedir.mkdirs();
//                    // }
//                    Uri thisUri = data.getData();
//                    String filepath = ImageUtils.getPath(QQHeadActivity.this,
//                            thisUri);
//                    if (filepath == null) {
//                        return;
//                    }
//                    Intent intent = new Intent(QQHeadActivity.this,
//                            CropImageActivity.class);
//                    intent.putExtra(CropImageActivity.PATH, filepath);
//                    startActivityForResult(intent, photo);
//                }
//            }
//            break;
//            case photo: {
//                if (data != null) {
//                    Bundle extras = data.getExtras();
//                    if (extras != null) {
//                        String path = extras.getString("data");
//                        roundImageView.setImageBitmap(ImageUtils.getBitmapPath(path));
////                        photocorp(path);
//                    }
//                }
//
//            }
//            break;
//
//
//            default:
//                break;
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//}
