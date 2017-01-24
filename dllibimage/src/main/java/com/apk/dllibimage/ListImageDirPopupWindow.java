//package com.apk.dllibimage;
//
//import android.app.Activity;
//import android.graphics.drawable.BitmapDrawable;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.WindowManager;
//import android.view.animation.Animation;
//import android.view.animation.LinearInterpolator;
//import android.view.animation.RotateAnimation;
//import android.view.animation.TranslateAnimation;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//import com.support.loader.adapter.UIListAdapter;
//import com.support.loader.adapter.ViewItemData;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 图片文件夹选择
// */
//public class ListImageDirPopupWindow extends
//        PopupWindow implements View.OnClickListener {
//    private ListView mListDir;
//    List<ViewItemData> datas;
//    Activity activity;
//    UIListAdapter listImageAdapter;
//    TextView id_choose_dir;
//    TextView preview_image_button;
//    ImageView imagev_choose_dir;
//
//    private ListImageDirPopupWindow(View contentView, int whith, int height) {
//        super(contentView,
//                whith, height, true);
//
//    }
//
//    public ListImageDirPopupWindow(View contentView, int whith, int height, Activity activity,
//                                   ArrayList<ViewItemData> datas) {
//        this(contentView, whith, height);
//
//
//        this.datas = datas;
//        this.activity = activity;
//        setpopupwindowsetting();
//        initViews();
//    }
//
//
//
//    void initViews() {
//
//        imagev_choose_dir = (ImageView) getContentView()
//                .findViewById(R.id.imagev_choose_dir);
//        id_choose_dir = (TextView) getContentView().findViewById(R.id.id_choose_dir);
//        preview_image_button = (TextView) getContentView().findViewById(R.id.preview_image_button);
//        mListDir = (ListView) getContentView().findViewById(R.id.id_list_dir);
//        listImageAdapter = new UIListAdapter(activity, datas);
//        mListDir.setAdapter(listImageAdapter);
//        mListDir.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                if (mImageDirSelected != null) {
//                    ViewItemData itemData = (ViewItemData) parent.getAdapter().getItem(position);
//                    mImageDirSelected.selected(itemData);
//                }
//            }
//        });
//        id_choose_dir.setOnClickListener(this);
//        preview_image_button.setOnClickListener(this);
//        if (imagev_choose_dir != null) {
//            imagev_choose_dir.setTag(0);
//        }
//
//    }
//
//    void setpopupwindowsetting() {
//        setBackgroundDrawable(new BitmapDrawable());
//        setTouchable(true);
//        setOutsideTouchable(true);
//        setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//                    dismiss();
//                    return true;
//                }
//                return false;
//            }
//        });
//    }
//
//    void positive(final View v, int fromy, int toy) {
//        TranslateAnimation anim = new TranslateAnimation(0, 0, fromy, toy);
//        LinearInterpolator lir = new LinearInterpolator();
//        anim.setInterpolator(lir);
//        anim.setDuration(300);
//        anim.setFillAfter(false);
//        anim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
////                v.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                listImageAdapter.notifyDataSetChanged();
//                WindowManager.LayoutParams lp = activity.getWindow()
//                        .getAttributes();
//                lp.alpha = .4f;
//                activity.getWindow().setAttributes(lp);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        v.startAnimation(anim);
//    }
//
//    void positiveGone(final View v, int fromy, int toy, final int type) {
//        TranslateAnimation anim = new TranslateAnimation(0, 0, fromy, toy);
//        LinearInterpolator lir = new LinearInterpolator();
//        anim.setInterpolator(lir);
//        anim.setDuration(300);
//        anim.setFillAfter(false);
//        anim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
////                v.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                dismiss();
////                if (type == 1) {
////                    if (activity instanceof ListBrowserActivity) {
////                        ListBrowserActivity listactivity = (ListBrowserActivity) activity;
////                        AbstractListDataFactory listfactory = listactivity.getListDataFactory();
////                        if (listfactory instanceof SelectImageDataFactory) {
////                            SelectImageDataFactory selectImageDataFactory = (SelectImageDataFactory) listfactory;
////                            selectImageDataFactory.showPreviewDialog(null, true);
////                        }
////                    }
////                }
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        v.startAnimation(anim);
//    }
//
//    public void showAtLocation(View parent, int gravity, int x, int y, TextView textView, TextView button) {
//        try {
//            super.showAtLocation(parent, gravity, x, y);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        for (ViewItemData viewItemData : datas) {
//            ImageFloder imageFloder = (ImageFloder) viewItemData;
//            if (imageFloder.getName().equals(textView.getText().toString())) {
//                imageFloder.ischeck = true;
//            } else {
//                imageFloder.ischeck = false;
//            }
//        }
//        id_choose_dir.setText(textView.getText());
//        preview_image_button.setText(button.getText());
//        preview_image_button.setTextColor(button.getTextColors());
//        preview_image_button.setBackgroundDrawable(button.getBackground());
//        preview_image_button.setPadding(activity.getResources().getDimensionPixelOffset(R.dimen.priew_padding), activity.getResources().getDimensionPixelOffset(R.dimen.image_top_padding), activity.getResources().getDimensionPixelOffset(R.dimen.priew_padding), activity.getResources().getDimensionPixelOffset(R.dimen.image_top_padding));
//
//        positive(mListDir, getHeight() - activity.getResources().getDimensionPixelOffset(R.dimen.botom_layout_height), 0);
//
//        rotateImageButton(imagev_choose_dir);
//    }
//
//    void rotateImageButton(ImageView imagev_choose_dir) {
//        if (imagev_choose_dir != null) {
//            RotateAnimation anim = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f,
//                    Animation.RELATIVE_TO_SELF, 0.5f);
//            /** 匀速插值器 */
//            LinearInterpolator lir = new LinearInterpolator();
//            anim.setInterpolator(lir);
//            anim.setDuration(300);
//            /** 动画完成后不恢复原状 */
//            anim.setFillAfter(true);
//            imagev_choose_dir.startAnimation(anim);
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        if (id == R.id.id_choose_dir) {
//            positiveGone(mListDir, 0, getHeight() - activity.getResources().getDimensionPixelOffset(R.dimen.botom_layout_height), 0);
//        } else if (id == R.id.preview_image_button) {
//            positiveGone(mListDir, 0, getHeight() - activity.getResources().getDimensionPixelOffset(R.dimen.botom_layout_height), 1);
//
//        }
//    }
//
//    public interface OnImageDirSelected {
//        void selected(ViewItemData floder);
//    }
//
//    private OnImageDirSelected mImageDirSelected;
//
//    public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected) {
//        this.mImageDirSelected = mImageDirSelected;
//    }
//
//    @Override
//    public void dismiss() {
//        super.dismiss();
//        rotateImageButton(imagev_choose_dir);
//    }
//}
