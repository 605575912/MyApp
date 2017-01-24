//package com.apk.dllibimage;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Matrix;
//import android.graphics.Rect;
//import android.media.ExifInterface;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup.LayoutParams;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.PopupWindow.OnDismissListener;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.nineoldandroids.view.ViewHelper;
//import com.ryg.dynamicload.DLBasePluginActivity;
//import com.ryg.dynamicload.internal.DLIntent;
//import com.support.loader.adapter.UIListAdapter;
//import com.support.loader.adapter.ViewItemData;
//
//import java.io.IOException;
//import java.lang.ref.WeakReference;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//public class LocationImageActivity extends DLBasePluginActivity {
//    public final static int REQUEST_CODE = 100001;
//    public final static String MAX_SELECT_IMAGE_COUNT = "max_select_image_count";
//    public final static String SELECTTED_IMAGES_PATH = "selectted_images_path";
//    //    private ProgressDialog mProgressDialog;
//
//
//    /**
//     * 所有的图片路径
//     */
//    private List<SelectImageFile> mImgsFilePath = new ArrayList<SelectImageFile>();
//    ArrayList<String> allPaths = new ArrayList<String>();//当前试图显示的图片路径集合
//
//
//    ListView list_view;
//    UIListAdapter adapter;
//    ArrayList<ViewItemData> listimage = new ArrayList<ViewItemData>();
//    /**
//     * 扫描拿到所有的图片文件夹
//     */
//    ArrayList<ViewItemData> mImageFloders = new ArrayList<ViewItemData>();
//    private RelativeLayout mBottomLy;
//
//    private TextView mChooseDir;
//
//    private int index = 0;
//    int statusBarHeight;
//    LinearLayout bt_imageline;
//    private ListImageDirPopupWindow mListImageDirPopupWindow;
//
//    public String theLarge;
//    private TextView mPreviewButton;
//    Button mSelectedButton;
//    private int mMaxSelectedCount = 3;
//    //    private IViewDrawableLoader mBitmapLoader;
//    ArrayList<String> selects;
//    static final int SELECT_ALLIMAGE = 1;
//    Float valuetemp;
//    Toast toast;
//    TextView messages;
//    MyHandler mHandler = new MyHandler(LocationImageActivity.this);
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.select_image_main_activity_layout);
//
//        list_view = (ListView) findViewById(R.id.listview);
//        adapter = new UIListAdapter(that, listimage);
//        list_view.setAdapter(adapter);
//        showComitButtons();
//        Intent intent = getIntent();
//        if (intent != null) {
//            mMaxSelectedCount = intent.getIntExtra(MAX_SELECT_IMAGE_COUNT, 3);
//        }
//        if (mMaxSelectedCount < 0) {
//            mMaxSelectedCount = 0;
//        }
//
//        selects = new ArrayList<String>();
//        initView();
//        if (savedInstanceState != null) {
//            theLarge = savedInstanceState.getString("theLarge");
//        }
//
//        if (!Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//            return;
//        }
//        new Thread(mGetImagesRunnable).start();
//
//    }
//
//    /**
//     * 读取图片属性：旋转的角度
//     *
//     * @param path 图片绝对路径
//     * @return degree旋转的角度
//     */
//    public static int readPictureDegree(String path) {
//        if (TextUtils.isEmpty(path)) {
//            return 0;
//        } else {
//            path = path.replace("file://", "");
//        }
//        int degree = 0;
//        try {
//            ExifInterface exifInterface = new ExifInterface(path);
//            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//            switch (orientation) {
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    degree = 90;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    degree = 180;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    degree = 270;
//                    break;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return degree;
//    }
//
//    /*
//     * 旋转图片
//     * @param angle
//     * @param bitmap
//     * @return Bitmap
//     */
//    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
//        if (angle == 0 || bitmap == null) {
//            return bitmap;
//        }
//        //旋转图片 动作
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
//        // 创建新的图片
//        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
//                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//        return resizedBitmap;
//    }
//
//
////    void camera() {
////        try {
////            String filepath = null;
////            if (Environment.getExternalStorageState().equals(
////                    Environment.MEDIA_MOUNTED)) {
////                File file = getExternalCacheDir();
////                if (file != null) {
////                    filepath = file.getPath();
////                } else {
////                    filepath = StorageSelector.getInstance().getCacheDirectory();
////                    File dir = new File(filepath);
////                    if (!dir.exists()) {
////                        dir.mkdirs();
////                    }
////                }
////            } else {
////                filepath = getCacheDir().getPath();
////            }
////
////            File out = new File(filepath, "head.jpg");
////            theLarge = out.getPath();
////            Uri uri = Uri.fromFile(out);
////            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
////            startActivityForResult(intent,
////                    1);
////        } catch (Exception e) {
////            // TODO: handle exception
//////            ShowToast.showTips(UserMSGActivity.this, "无法调用相机～");
////        }
////    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == ImagePagerActivity.EXTRA_CHOOSIMAGE) {
//                if (data != null) {
//                    ArrayList<String> chooselist = data.getStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_RESURE);
//                    if (chooselist != null) {
//                        selects.clear();
//                        selects.addAll(chooselist);
//
//                        if (0 == data.getIntExtra(ImagePagerActivity.BUTTON_CLOSE, 0)) {
//                            updateSelectedImageCount(selects.size());
//
//
//                            int count = adapter.getCount();
//                            index = 0;
//                            for (int i = 0; i < count; i++) {
//                                Object obj = adapter.getItem(i);
//                                if (obj != null && obj instanceof SelectImageItemData) {
//                                    SelectImageItemData selectImageItemData = (SelectImageItemData) obj;
//                                    SelectImageFile imagefile = selectImageItemData
//                                            .getImageFile();
//                                    if (imagefile == null) {
//                                        continue;
//                                    }
//                                    imagefile.mIsSelected = false;
//                                    for (String path : chooselist) {
//                                        if (path.equals(imagefile.mSelectImageFilePath)) {
//                                            chooselist.remove(path);
//                                            imagefile.mIsSelected = true;
//                                            break;
//                                        }
//                                    }
//
//
//                                }
//                            }
//                            adapter.notifyDataSetChanged();
//
//                        } else {
//                            Intent resultIntent = new Intent();
//                            resultIntent.putStringArrayListExtra(SELECTTED_IMAGES_PATH,
//                                    selects);
//                            setResult(Activity.RESULT_OK, resultIntent);
//                            finish();
//                        }
//
//                    }
//
//                }
//            }
//
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//
//    private void showComitButtons() {
//        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//
//            }
//        });
//        TextView image_title = (TextView) findViewById(R.id.image_title);
//        image_title.setText("图片1");
//        mSelectedButton = (Button) findViewById(R.id.title_image_button);
//        mSelectedButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_btn_gray));
//        mSelectedButton.setTextColor(getResources().getColor(R.color.image_textgray));
//        mSelectedButton.setPadding(getResources().getDimensionPixelOffset(R.dimen.priew_padding), getResources().getDimensionPixelOffset(R.dimen.image_top_padding), getResources().getDimensionPixelOffset(R.dimen.priew_padding), getResources().getDimensionPixelOffset(R.dimen.image_top_padding));
//
//        mSelectedButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (selects == null || selects.isEmpty()) {
////                                                    finish();
//                    return;
//                }
//                Intent resultIntent = new Intent();
//            /* 将数据打包到aintent Bundle 的过程 */
//                resultIntent.putStringArrayListExtra(SELECTTED_IMAGES_PATH,
//                        selects);
//                // 2个参数(int resultCode, Intent intent)
//                setResult(Activity.RESULT_OK, resultIntent);
//                that.finish();
//            }
//        });
//
//    }
//
//
//    /**
//     * 初始化View
//     */
//    private void initView() {
//        mChooseDir = (TextView)
//                findViewById(R.id.id_choose_dir);
//        mBottomLy = (RelativeLayout)
//                findViewById(R.id.id_bottom_ly);
//        bt_imageline = (LinearLayout)
//                findViewById(R.id.bt_imageline);
//
//        mPreviewButton = (TextView)
//                findViewById(R.id.preview_image_button);
//        mPreviewButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(that, "==", Toast.LENGTH_LONG).show();
//                showPreviewDialog(null, true);
//
//            }
//        });
//        bt_imageline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(that, "==" + v.getClass().getName(), Toast.LENGTH_LONG).show();
//                if (mListImageDirPopupWindow != null && mImageFloders != null && !mImageFloders.isEmpty()) {
//                    DisplayMetrics metric = new DisplayMetrics();
//                    getWindowManager().getDefaultDisplay().getMetrics(metric);
//                    int width = metric.widthPixels; // 屏幕宽度（像素）
//                    int height = metric.heightPixels; // 屏幕高度（像素）
//                    mListImageDirPopupWindow.showAtLocation(mBottomLy, Gravity.CENTER_HORIZONTAL, 0, height, mChooseDir, mPreviewButton);
//                }
//            }
//        });
//
//    }
//
//
//    void checkAnimation(final View imageView) {
//        com.nineoldandroids.animation.ValueAnimator widthAnimation = com.nineoldandroids.animation.ValueAnimator.ofFloat(0.9f, 1.3f, 1.0f, 0.9f, 1.0f);
//        widthAnimation.addUpdateListener(new com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener()
//
//                                         {
//                                             @Override
//                                             public void onAnimationUpdate(com.nineoldandroids.animation.ValueAnimator valueAnimator) {
//                                                 Float value = (Float) valueAnimator.getAnimatedValue();
//                                                 if (valuetemp == value)
//                                                     return;
//                                                 valuetemp = value;
//
//                                                 ViewHelper.setScaleX(imageView, value);
//                                                 ViewHelper.setScaleY(imageView, value);
//
//                                             }
//                                         }
//        );
//        widthAnimation.setDuration(400);
//        widthAnimation.start();
//    }
//
//    private Runnable mGetImagesRunnable = new Runnable() {
//        @Override
//        public void run() {
//
//            Log.i("TAG", "==");
//            ArrayList<String> listpath = ImageMap.getInstance().getListdata();
//            if (listpath.isEmpty()) {
//                listpath = ImageMap.getInstance().getImageCursorList();
//            }
//            Log.i("TAG", "==" + listpath.size());
//            mImageFloders.clear();
//            mImageFloders.addAll(ImageMap.mImageFloders);
//            ArrayList<ViewItemData> listdata = new ArrayList<ViewItemData>();
//            for (String path : listpath) {
//                SelectImageFile imgFile = new SelectImageFile(
//                        path);
//                mImgsFilePath.add(imgFile);
//                allPaths.add(path);
//                SelectImageItemData selectImageItemData = new SelectImageItemData(
//                        LocationImageActivity.this, imgFile);
//                listdata.add(selectImageItemData);
//            }
//            if (!listpath.isEmpty()) {
//                Collections.sort(mImageFloders, new CountComparator());
//                ImageFloder allimageFloder = new ImageFloder();
//                allimageFloder.setFirstImagePath(
//                        listpath.get(0));
//                allimageFloder.setName(that.getResources().getString(R.string.all_image));
//                allimageFloder.setCount(listpath.size());
//                mImageFloders.add(0, allimageFloder);
//            }
//            Message message = mHandler.obtainMessage(SELECT_ALLIMAGE);
//            message.obj = listdata;
//            // 通知Handler扫描图片完成
//            mHandler.sendMessage(message);
//        }
//    };
//
//    private static class CountComparator implements Comparator<ViewItemData> {
//
//        @Override
//        public int compare(ViewItemData lhs, ViewItemData rhs) {
//            if (((ImageFloder) lhs).getCount() < ((ImageFloder) rhs).getCount()) {
//                return 1;
//            } else {
//                return -1;
//            }
//        }
//    }
//
//    static class MyHandler extends Handler {
//        WeakReference<LocationImageActivity> mActivityReference;
//
//        MyHandler(LocationImageActivity activity) {
//            mActivityReference = new WeakReference<LocationImageActivity>(activity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            final LocationImageActivity activity = mActivityReference.get();
//            if (activity != null) {
//                if (msg.what == SELECT_ALLIMAGE) {
//                    // 为View绑定数据
//                    ArrayList<ViewItemData> listdata = (ArrayList<ViewItemData>) msg.obj;
//                    activity.listimage.addAll(listdata);
//                    activity.adapter.notifyDataSetChanged();
////                    if (activity.mImageFloders != null && !activity.mImageFloders.isEmpty()) {
////                    }
//                    if (listdata != null && !listdata.isEmpty() && activity.mImageFloders.get(0) != null) {
////                        activity.mChooseDir.setText(activity.mImageFloders.get(0).getName());
//                    } else {
//                        activity.mChooseDir.setText(activity.getResources().getString(R.string.all_image));
//                        activity.mChooseDir.setTextColor(activity.getResources().getColor(R.color.image_textgray));
////                        ToastUtil.show(getApplicationContext(),
////                                getResources().getString(R.string.no_image));
//                    }
//                    // 初始化展示文件夹的popupWindw
//                    activity.initListDirPopupWindw();
//                }
//            }
//        }
//    }
////    private  Handler mHandler = new Handler() {
////        public void handleMessage(Message msg) {
////
////
////        }
////    };
//
////    /**
////     * 为View绑定数据
////     */
////    private void data2View(ArrayList<AbstractListItemData> listdata, ImageFloder imageFloder) {
////        mListener.onEmptyListItem();
////        mListener.onListItemReady(listdata, "");
////        if (listdata != null && !listdata.isEmpty() && imageFloder != null) {
////            mChooseDir.setText(imageFloder.getName());
////        } else {
////            mChooseDir.setText(getResources().getString(R.string.all_image));
////            mChooseDir.setTextColor(getResources().getColor(R.color.image_textgray));
////            ToastUtil.show(getApplicationContext(),
////                    getResources().getString(R.string.no_image));
////        }
////
////    }
//
//
//    /**
//     * 初始化展示文件夹的popupWindw
//     */
//    private void initListDirPopupWindw() {
//        View contentView = LayoutInflater.from(
//                that).inflate(
//                R.layout.select_image_list_dir, null);
//        DisplayMetrics metric = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metric);
//        int width = metric.widthPixels; // 屏幕宽度（像素）
//        int height = metric.heightPixels; // 屏幕高度（像素）
//        mListImageDirPopupWindow = new ListImageDirPopupWindow(contentView,
//                LayoutParams.MATCH_PARENT, (int) (height * 0.7)
//                , that, mImageFloders);
//
//        mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {
//
//            @Override
//            public void onDismiss() {
//                // 设置背景颜色变暗
//                WindowManager.LayoutParams lp = getWindow()
//                        .getAttributes();
//                lp.alpha = 1.0f;
//                getWindow().setAttributes(lp);
//            }
//        });
//        // 设置选择文件夹的回调
//        mListImageDirPopupWindow.setOnImageDirSelected(new ListImageDirPopupWindow.OnImageDirSelected() {
//            @Override
//            public void selected(ViewItemData viewItemData) {
//                ImageFloder floder = (ImageFloder) viewItemData;
//                listimage.clear();
//                ArrayList<ViewItemData> listdata = new ArrayList<ViewItemData>();
//                allPaths.clear();
//                ArrayList<String> paths = (ArrayList<String>) selects.clone();
//                for (SelectImageFile imgFile : mImgsFilePath) {
//                    if (floder.getDir() == null || imgFile.mSelectImageFilePath.indexOf(floder.getDir()) > -1) {
//                        SelectImageItemData selectImageItemData;
//                        int indexpath = paths.indexOf(imgFile.mSelectImageFilePath);
//                        if (indexpath > -1) {
//                            paths.remove(indexpath);
//                            imgFile.mIsSelected = true;
//                        }
//                        selectImageItemData = new SelectImageItemData(LocationImageActivity.this, imgFile);
//                        listdata.add(selectImageItemData);
//                        allPaths.add(selectImageItemData.getImageFile().mSelectImageFilePath);
//                    }
//                }
//                if (!paths.isEmpty()) {
//                    for (int i = paths.size() - 1; i > -1; i--) {
//                        SelectImageFile file = new SelectImageFile(paths.get(i));
//                        file.mIsSelected = true;
//                        SelectImageItemData selectImageItemData = new SelectImageItemData(
//                                LocationImageActivity.this, file);
//                        listdata.add(0, selectImageItemData);
//                    }
//                }
//                listimage.clear();
//                adapter.notifyDataSetChanged();
////                mListener.onListItemReady(listdata, "");
//                mChooseDir.setText(floder.getName());
//                mListImageDirPopupWindow.dismiss();
//            }
//        });
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mImageFloders.clear();
//
//        if (mPreviewButton != null) {
//            mPreviewButton.setOnClickListener(null);
//        }
//        if (mSelectedButton != null) {
//            mSelectedButton.setOnClickListener(null);
//        }
//        if (bt_imageline != null) {
//            bt_imageline.setOnClickListener(null);
//        }
//        if (mListImageDirPopupWindow != null) {
//            mListImageDirPopupWindow.dismiss();
//            mListImageDirPopupWindow = null;
//        }
//
//        mImgsFilePath.clear();
//        selects.clear();
//        mHandler.removeCallbacksAndMessages(null);
//        mGetImagesRunnable = null;
//        mHandler = null;
//    }
//
//
//    public void notifyDataChanged(ViewItemData item, View view) {
//
////        if (mCallerActivity instanceof ListBrowserActivity) {
////            ListBrowserActivity listactivity = (ListBrowserActivity) mCallerActivity;
////            listactivity.notifyDataChanged(item);
////        }
//        adapter.notifyDataSetChanged();
//
//        SelectImageItemData selectImageItemData = (SelectImageItemData) item;
//        if (selects.indexOf(selectImageItemData.getImageFile().mSelectImageFilePath) > -1) {
//            selects.remove(selectImageItemData.getImageFile().mSelectImageFilePath);
//        } else {
//            selects.add(selectImageItemData.getImageFile().mSelectImageFilePath);
//            checkAnimation(view);
//        }
//        updateSelectedImageCount(selects.size());
//
//    }
//
//
//    /**
//     * 提示选择语
//     *
//     * @param context
//     * @param text
//     */
//    void showTipsView(Context context, String text) {
//
//        if (toast == null) {
//            toast = new Toast(context);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.setDuration(Toast.LENGTH_SHORT);
//            View view = LayoutInflater.from(context).inflate(R.layout.image_toast_dialog,
//                    null);
//            messages = (TextView) view.findViewById(R.id.textView);
//            toast.setView(view);
//        }
//        messages.setText(text);
//        toast.show();
//
//    }
//
//    public boolean isSelectedMaxCount() {
//        if (selects.size() >= mMaxSelectedCount) {
//            showTipsView(that, getResources().getString(R.string.max_image, mMaxSelectedCount));
//            return true;
//        }
//
//        return false;
//    }
//
//    private void updateSelectedImageCount(int select) {
//        if (mSelectedButton != null) {
//            if (select <= 0) {
//                mPreviewButton.setTextColor(getResources().getColor(R.color.image_textgray));
//                mPreviewButton.setOnClickListener(null);
//                CharSequence previewtext = getResources().getString(R.string.priview_no);
//                mPreviewButton.setText(previewtext);
//
//                mPreviewButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_btn_gray));
//                mPreviewButton.setPadding(getResources().getDimensionPixelOffset(R.dimen.priew_padding), getResources().getDimensionPixelOffset(R.dimen.image_top_padding), getResources().getDimensionPixelOffset(R.dimen.priew_padding), getResources().getDimensionPixelOffset(R.dimen.image_top_padding));
//
//                CharSequence sendtext = getResources().getString(R.string.send_image);
//                mSelectedButton.setTextColor(getResources().getColor(R.color.image_textgray));
//                mSelectedButton.setText(sendtext);
//                mSelectedButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_btn_gray));
//                mSelectedButton.setPadding(getResources().getDimensionPixelOffset(R.dimen.priew_padding), getResources().getDimensionPixelOffset(R.dimen.image_top_padding), getResources().getDimensionPixelOffset(R.dimen.priew_padding), getResources().getDimensionPixelOffset(R.dimen.image_top_padding));
//            } else {
//                mPreviewButton.setTextColor(getResources().getColor(R.color.image_textgreen));
//                mPreviewButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showPreviewDialog(null, true);
//                    }
//                });
//                CharSequence previewtext = getResources().getString(R.string.priview, select);
//                mPreviewButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_btn_green));
//                mPreviewButton.setText(previewtext);
//                mPreviewButton.setPadding(getResources().getDimensionPixelOffset(R.dimen.priew_padding), getResources().getDimensionPixelOffset(R.dimen.image_top_padding), getResources().getDimensionPixelOffset(R.dimen.priew_padding), getResources().getDimensionPixelOffset(R.dimen.image_top_padding));
//
//                CharSequence sendtext = getResources().getString(R.string.send_index, select, mMaxSelectedCount);
//                mSelectedButton.setTextColor(getResources().getColor(R.color.image_textgreen));
//                mSelectedButton.setText(sendtext);
//                mSelectedButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_btn_green));
//                mSelectedButton.setPadding(getResources().getDimensionPixelOffset(R.dimen.priew_padding), getResources().getDimensionPixelOffset(R.dimen.image_top_padding), getResources().getDimensionPixelOffset(R.dimen.priew_padding), getResources().getDimensionPixelOffset(R.dimen.image_top_padding));
//            }
//
//        }
//    }
//
//
//    public void showPreviewDialog(SelectImageItemData selectImageItemData, boolean priew) {
//        ArrayList<String> selectedImages;
//        if (priew) {
//            index = 0;
//            selectedImages = selects;
//        } else {
//            if (selectImageItemData != null) {
//                selectedImages = allPaths;
//                index = allPaths.indexOf(selectImageItemData.getImageFile().mSelectImageFilePath);
//
//            } else {
//                selectedImages = allPaths;
//            }
//        }
//
//        if (selectedImages == null || selectedImages.size() == 0) {
//            return;
//        }
//        Rect frame = new Rect();
//        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//        statusBarHeight = frame.top;
//        DLIntent intent = new DLIntent(getPackageName(),ImagePagerActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putStringArrayList(ImagePagerActivity.EXTRA_IMAGE_URLS, selectedImages);
//
//        bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, index);
//        bundle.putInt("type", ImagePagerActivity.EXTRA_CHOOSIMAGE);
//        bundle.putInt("statusBarHeight", frame.top);
//
//        bundle.putStringArrayList(ImagePagerActivity.EXTRA_IMAGE_RESURE, selects);
//        bundle.putInt("maximage", mMaxSelectedCount);
//
//        intent.putExtras(bundle);
//        startPluginActivityForResult(intent, ImagePagerActivity.EXTRA_CHOOSIMAGE);
//
//
//    }
//}
