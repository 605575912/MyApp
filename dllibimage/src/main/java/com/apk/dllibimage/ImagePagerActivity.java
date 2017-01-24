//package com.apk.dllibimage;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.view.animation.LinearInterpolator;
//import android.view.animation.TranslateAnimation;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.nineoldandroids.view.ViewHelper;
//import com.ryg.dynamicload.DLBasePluginFragmentActivity;
//
//import java.lang.ref.WeakReference;
//import java.util.ArrayList;
//
//public class ImagePagerActivity extends DLBasePluginFragmentActivity implements View.OnClickListener, ImageFragmentListener {
//    private static final String STATE_POSITION = "STATE_POSITION";
//    public static final String EXTRA_IMAGE_INDEX = "image_index";
//    public static final String EXTRA_IMAGE_URLS = "image_urls";
//    public static final String EXTRA_MINIMAGE_URLS = "imagemin_urls";
//    public static final String EXTRA_IMAGE_RESURE = "image_result";
//    static final String BUTTON_CLOSE = "closetype";
//    public static final int EXTRA_CODE_RESURE = 101;
//    public static final int EXTRA_PRIEW = 100;
//    public static final int EXTRA_CHOOSIMAGE = 102;
//    private HackyViewPager mPager;
//    ImagePagerAdapter mAdapter;
//    private int pagerPosition;
//    boolean fullscreen = false;
//    int statusBarHeight; //Title bar height 标题栏
//    RelativeLayout relative_bottom;
//    RelativeLayout re_title;
//    TextView image_title, tv_selecterindex;
//    Button tv_send;
//    ImageView iv_check;
//    MyHandler handler = new MyHandler(this);
//
//    ArrayList<String> list;
//    ArrayList<String> minlist; //缩略图
//    ArrayList<String> chooselist;
//    int type = EXTRA_CODE_RESURE; //101 the type of delete   102 the type of choose image  100 the type of preview
//    String selectImagePath; //the current path
//    int maximage = 0;
//    float valuetemp;
//    Toast toast;
//    TextView messages;
//    /**
//     * ATTENTION: This was auto-generated to implement the App Indexing API.
//     * See https://g.co/AppIndexing/AndroidStudio for more information.
//     */
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        if (id == R.id.image_back) {
//            close();
//        }
//    }
//
//    void showTitleDialog(Context context, String text) {
//        if (context == null) {
//            return;
//        }
//        final Dialog dialog = new Dialog(context, R.style.image_dialog_alert);
//        dialog.setContentView(R.layout.image_alertdialog_layout);
//        dialog.getWindow().setLayout(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        dialog.setCancelable(true);
//        TextView messages = (TextView) dialog.findViewById(R.id.textView);
//        messages.setText(text);
//        dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (list != null || selectImagePath != null) {
//                    deleteImage(selectImagePath);
//                } else {
//                    finish();
//                }
//            }
//        });
//        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        try {
//            dialog.show();
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//    }
//
//    void close() {
//        if (type == EXTRA_CHOOSIMAGE) {
//            if (chooselist != null) {
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(EXTRA_IMAGE_RESURE, chooselist);
//                intent.putExtras(bundle);
//                setResult(RESULT_OK, intent);
//            }
//        } else if (type == EXTRA_CODE_RESURE) {
//            Intent intent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(EXTRA_IMAGE_RESURE, list);
//            intent.putExtras(bundle);
//            setResult(RESULT_OK, intent);
//        }
//        finish();
//    }
//
//    /**
//     * 按下键盘上返回按钮退出程序
//     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {// 后退
//            // TODO Auto-generated method stub
//            close();
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    private void showComitButtons() {
//
//        tv_send = (Button) findViewById(R.id.title_image_button);
//        tv_send.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_btn_gray));
//        CharSequence sendtext = getResources().getString(R.string.send_image);
//        tv_send.setTextColor(getResources().getColor(R.color.image_textgray));
//        tv_send.setText(sendtext);
//        tv_send.setPadding(getResources().getDimensionPixelOffset(R.dimen.priew_padding), getResources().getDimensionPixelOffset(R.dimen.image_top_padding), getResources().getDimensionPixelOffset(R.dimen.priew_padding), getResources().getDimensionPixelOffset(R.dimen.image_top_padding));
//
//        tv_send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (type == EXTRA_CHOOSIMAGE) {
//                    if (chooselist != null && !chooselist.isEmpty()) {
//                        Intent intent = new Intent();
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable(EXTRA_IMAGE_RESURE, chooselist);
//                        bundle.putInt(BUTTON_CLOSE, 1); //
//                        intent.putExtras(bundle);
//                        setResult(RESULT_OK, intent);
//                        finish();
//                    }
//
//
//                } else if (type == EXTRA_CODE_RESURE) {
//                    showTitleDialog(that, getResources().getString(R.string.delete_image_tip));
//                }
//            }
//        });
//    }
//
//
//    void deleteImage(String selectImageFile) {
//        int postion = list.indexOf(selectImageFile);
//        if (postion > -1) {
//            if (list.size() <= 1) {
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(EXTRA_IMAGE_RESURE, new ArrayList<String>());
//                intent.putExtras(bundle);
//                setResult(RESULT_OK, intent);
//                finish();
//            } else {
//                if (postion == list.size() - 1) {
//                    postion = postion - 1;
////                    mPager.setCurrentItem(postion, true);
//                    selectImagePath = list.get(postion);
//                } else {
//                    selectImagePath = list.get(postion + 1);
////                    mPager.setCurrentItem(postion + 1, true);
//                }
////                ViewImageLoader;
////                ViewDrawableLoader;
//                CharSequence text = getResources().getString(R.string.pic_index, postion + 1, list.size() - 1);
//                image_title.setText(text);
//                Message msg = handler.obtainMessage(1);
//                msg.obj = selectImageFile;
//                msg.arg1 = postion;
//                handler.sendMessageDelayed(msg, 200);
//            }
//        }
//    }
//
//    @Override
//    public void ImageOnclick(View view) {
//        if (type == EXTRA_PRIEW) {
//            finish();
//        } else {
//            if (fullscreen) {
//                quitFullScreen();
//            } else {
//                setFullScreen();
//
//            }
//        }
//    }
//
//
//
//
//    static class MyHandler extends Handler {
//        WeakReference<ImagePagerActivity> mActivityReference;
//
//        MyHandler(ImagePagerActivity activity) {
//            mActivityReference = new WeakReference<ImagePagerActivity>(activity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            final ImagePagerActivity activity = mActivityReference.get();
//            if (activity != null) {
//                if (msg.what == 1) {
//                    activity.list.remove(msg.obj);
//                    activity.mAdapter.notifyDataSetChanged();
//                } else if (msg.what == 2) {
//                    activity.re_title.setVisibility(View.VISIBLE);
//                    activity.positive(activity.re_title, -activity.re_title.getHeight(), 0);
//                    if (activity.type == EXTRA_CHOOSIMAGE) {
//                        activity.relative_bottom.setVisibility(View.VISIBLE);
//                        activity.positive(activity.relative_bottom, activity.relative_bottom.getHeight(), 0);
//                    }
//                    activity.fullscreen = false;
//                }
//                super.handleMessage(msg);
//            }
//        }
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.image_detail_pager);
//        re_title = (RelativeLayout) findViewById(R.id.re_title);
//        relative_bottom = (RelativeLayout) findViewById(R.id.linear_bottom);
//        image_title = (TextView) findViewById(R.id.image_title);
//        tv_selecterindex = (TextView) findViewById(R.id.tv_selecterindex);
//        iv_check = (ImageView) findViewById(R.id.iv_check);
//        findViewById(R.id.image_back).setOnClickListener(this);
//        pagerPosition = 0;
//        showComitButtons();
//        if (getIntent().getExtras() != null) {
//            pagerPosition = getIntent().getExtras().getInt(EXTRA_IMAGE_INDEX, 0);
//            list = getIntent().getExtras().getStringArrayList(EXTRA_IMAGE_URLS);
//            minlist = getIntent().getExtras().getStringArrayList(EXTRA_MINIMAGE_URLS);
//            chooselist = getIntent().getExtras().getStringArrayList(EXTRA_IMAGE_RESURE);
//            statusBarHeight = getIntent().getExtras().getInt("statusBarHeight");
//            maximage = getIntent().getExtras().getInt("maximage");
//            type = getIntent().getExtras().getInt("type");
//            if (type == EXTRA_CHOOSIMAGE) {
//                relative_bottom.setVisibility(View.VISIBLE);
//                if (chooselist != null && !chooselist.isEmpty()) {
//                    CharSequence sendtext = getResources().getString(R.string.send_index, chooselist.size(), maximage);
//                    tv_send.setText(sendtext);
//                    tv_send.setTextColor(getResources().getColor(R.color.image_textgreen));
//                    tv_send.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_btn_green));
//                    tv_send.setPadding(getResources().getDimensionPixelOffset(R.dimen.priew_padding), getResources().getDimensionPixelOffset(R.dimen.image_top_padding), getResources().getDimensionPixelOffset(R.dimen.priew_padding), getResources().getDimensionPixelOffset(R.dimen.image_top_padding));
//
//                    CharSequence text = getResources().getString(R.string.pic_index, pagerPosition + 1, list.size());
//                    image_title.setText(text);
//                }
//                re_title.setVisibility(View.VISIBLE);
//            } else if (type == EXTRA_CODE_RESURE) {
//                tv_send.setText(getResources().getString(R.string.delete_image));
//                tv_send.setTextColor(Color.parseColor("#f59b31"));
//                tv_send.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_btn_orange));
//                tv_send.setPadding(getResources().getDimensionPixelOffset(R.dimen.priew_padding), getResources().getDimensionPixelOffset(R.dimen.image_top_padding), getResources().getDimensionPixelOffset(R.dimen.priew_padding), getResources().getDimensionPixelOffset(R.dimen.image_top_padding));
//                CharSequence text = getResources().getString(R.string.pic_index, pagerPosition + 1, list.size());
//                image_title.setText(text);
//                re_title.setVisibility(View.VISIBLE);
//            } else if (type == EXTRA_PRIEW) {
//                re_title.setVisibility(View.GONE);
//                tv_selecterindex.setVisibility(View.VISIBLE);
//                CharSequence text = getResources().getString(R.string.selecterindex, pagerPosition + 1, list.size());
//                tv_selecterindex.setText(text);
//
//            }
//
//
//        }
//        mPager = (HackyViewPager)
//                findViewById(R.id.pager);
//
//
////        mBitmapLoader = new ViewDrawableLoader(this, new ListViewDrawableListener(AspireUtils.getDefaultDisplay(this)[0], AspireUtils.getDefaultDisplay(this)[1])
////
////        {
////            @Override
////            public void onViewDrawableChanged(View view, boolean success) {
////                super.onViewDrawableChanged(view, success);
////
////            }
////
////            @Override
////            public Drawable onViewDrawablePrepare(View view, Drawable drawable) {
////                return super.onViewDrawablePrepare(view, drawable);
////            }
////        }
//
////        );
//        if (list == null)
//
//        {
//            return;
//        }
//
//        mAdapter = new ImagePagerAdapter(
//                getSupportFragmentManager(), minlist, list
//
//        );
//
//        mPager.setOnPageChangeListener(new
//
//                                               OnPageChangeListener() {
//
//                                                   @Override
//                                                   public void onPageScrollStateChanged(int arg0) {
//                                                   }
//
//                                                   @Override
//                                                   public void onPageScrolled(int arg0, float arg1, int arg2) {
//                                                   }
//
//                                                   @Override
//                                                   public void onPageSelected(int arg0) {
//                                                       pagerPosition = arg0;
//                                                       selectImagePath = list.get(arg0);
//                                                       PageSelected(arg0);
//
//
//                                                   }
//
//                                               }
//
//        );
//        if (savedInstanceState != null)
//
//        {
//            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
//        }
//
//
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPager.getLayoutParams();
////        params.bottomMargin = -statusBarHeight;
//        params.topMargin = -statusBarHeight;
////        params.leftMargin = -statusBarHeight;
////        params.rightMargin = -statusBarHeight;
//        mPager.setLayoutParams(params);
//        mPager.setAdapter(mAdapter);
//        findViewById(R.id.checkBox).setOnClickListener(new View.OnClickListener()
//
//                                                       {
//                                                           @Override
//                                                           public void onClick(View v) {
//                                                               if (chooselist == null) {
//                                                                   chooselist = new ArrayList<String>();
//                                                               }
//                                                               if (chooselist.indexOf(selectImagePath) > -1) {
//                                                                   chooselist.remove(selectImagePath);
//                                                                   iv_check.setImageResource(R.drawable.image_choose);
//                                                               } else {
//                                                                   if (chooselist.size() >= maximage) {
//                                                                       showTipsView(that, getResources().getString(R.string.max_image, maximage));
//                                                                       return;
//                                                                   }
//
//                                                                   chooselist.add(selectImagePath);
//                                                                   iv_check.setImageResource(R.drawable.image_check);
//                                                                   checkAnimation(iv_check);
//                                                               }
//                                                               CharSequence sendtext;
//                                                               if (chooselist.isEmpty()) {
//                                                                   sendtext = getResources().getString(R.string.send_image);
//                                                                   tv_send.setTextColor(that.getResources().getColor(R.color.image_textgray));
//                                                                   tv_send.setBackgroundDrawable(that.getResources().getDrawable(R.drawable.image_btn_gray));
//                                                               } else {
//                                                                   sendtext = getResources().getString(R.string.send_index, chooselist.size(), maximage);
//                                                                   tv_send.setTextColor(that.getResources().getColor(R.color.image_textgreen));
//                                                                   tv_send.setBackgroundDrawable(that.getResources().getDrawable(R.drawable.image_btn_green));
//                                                               }
//                                                               tv_send.setPadding(that.getResources().getDimensionPixelOffset(R.dimen.priew_padding),that.getResources().getDimensionPixelOffset(R.dimen.image_top_padding), that.getResources().getDimensionPixelOffset(R.dimen.priew_padding), that.getResources().getDimensionPixelOffset(R.dimen.image_top_padding));
//                                                               tv_send.setText(sendtext);
//                                                           }
//                                                       }
//
//        );
//
//        selectImagePath = list.get(pagerPosition);
//        mPager.setCurrentItem(pagerPosition);
//        PageSelected(pagerPosition);
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
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
//    void checkAnimation(final View imageView) {
//        com.nineoldandroids.animation.ValueAnimator widthAnimation =  com.nineoldandroids.animation.ValueAnimator.ofFloat(0.9f, 1.3f, 1.0f, 0.9f, 1.0f);
//        widthAnimation.addUpdateListener(new com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener() {
//                                             @Override
//                                             public void onAnimationUpdate(com.nineoldandroids.animation.ValueAnimator valueAnimator) {
//                                                 Float value = (Float) valueAnimator.getAnimatedValue();
//                                                 if (valuetemp == value)
//                                                     return;
//                                                 valuetemp = value;
//
//                                                 ViewHelper.setScaleX(imageView, value);
//                                                 ViewHelper.setScaleY(imageView, value);
//                                             }
//                                         }
//        );
//        widthAnimation.setDuration(400);
//        widthAnimation.start();
//    }
//
//    void PageSelected(int index) {
//        if (type == EXTRA_PRIEW) {
//            CharSequence text = getResources().getString(R.string.selecterindex, index + 1, list.size());
//            tv_selecterindex.setText(text);
//        } else {
//            CharSequence text = getResources().getString(R.string.pic_index, index + 1, list.size());
//            image_title.setText(text);
//            if (type == EXTRA_CHOOSIMAGE) {
//                String file = list.get(index);
//                if (chooselist.indexOf(file) > -1) {
//                    iv_check.setImageResource(R.drawable.image_check);
//                } else {
//                    iv_check.setImageResource(R.drawable.image_choose);
//                }
//            }
//
//        }
//
//    }
//
//    private void setFullScreen() {
//
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPager.getLayoutParams();
//        if (params.topMargin != 0) {
//            fullscreen = true;
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            params.topMargin = 0;
//            mPager.setLayoutParams(params);
//            re_title.setVisibility(View.GONE);
//            relative_bottom.setVisibility(View.GONE);
//            positive(re_title, 0, -re_title.getHeight());
//            if (type == EXTRA_CHOOSIMAGE) {
//                positive(relative_bottom, statusBarHeight, relative_bottom.getHeight() + statusBarHeight);
//            }
//        }
//    }
//
//    private void quitFullScreen() {
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPager.getLayoutParams();
//        if (params.topMargin == 0) {
//            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
//            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            getWindow().setAttributes(attrs);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            params.topMargin = -statusBarHeight;
//            mPager.setLayoutParams(params);
//            handler.sendEmptyMessageDelayed(2, 250);
//        } else {
//
//        }
//
//    }
//
//    void positive(final View v, int fromy, int toy) {
//        TranslateAnimation anim = new TranslateAnimation(0, 0, fromy, toy);
//        LinearInterpolator lir = new LinearInterpolator();
//        anim.setInterpolator(lir);
//        anim.setDuration(300);
//        anim.setFillAfter(true);
//        v.startAnimation(anim);
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
//    }
//
//    private class ImagePagerAdapter extends FragmentStatePagerAdapter {
//
//        ArrayList<String> fileList;
//        ArrayList<String> minList;
//
//        public ImagePagerAdapter(FragmentManager fm, ArrayList<String> minList, ArrayList<String> fileList) {
//            super(fm);
//            this.fileList = fileList;
//            this.minList = minList;
//        }
//
//        public ArrayList<String> getFileList() {
//            return fileList;
//        }
//
//        @Override
//        public int getItemPosition(Object object) {
//            return POSITION_NONE;
//        }
//
//
//        @Override
//        public int getCount() {
//            return fileList == null ? 0 : fileList.size();
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            String url = fileList.get(position);
//            String minurl = "";
//            if (minList != null && !minList.isEmpty()) {
//                minurl = minList.get(position);
//            }
//
//            return ImageDetailFragment.newInstance(minurl, url, type, ImagePagerActivity.this);
//        }
//
//    }
//
//    @Override
//    public void ImageShowTipTagurl(String url) {
//        ArrayList<String> fileList = mAdapter.getFileList();
//        if (fileList != null && !fileList.isEmpty()) {
//
//            if (url.indexOf(selectImagePath) > -1)//url 是框架添加过 前缀的URL
////                ToastUtil.show(this, "加载图片失败");
//            {
//
//            }
//        }
//
//    }
//}
