//package com.apk.dllibimage;
//
//import android.app.Activity;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.nineoldandroids.view.ViewHelper;
//import com.support.loader.ServiceLoader;
//import com.support.loader.adapter.Holder;
//import com.support.loader.adapter.UIListAdapter;
//import com.support.loader.adapter.ViewItemData;
//import com.support.loader.packet.ImageOptions;
//
//
//public class SelectImageItemData implements ViewItemData,
//        OnClickListener {
//    private SelectImageFile mSelectImageFile;
//    LocationImageActivity activity;
//
//    public SelectImageItemData(LocationImageActivity activity, SelectImageFile selectImageFile) {
//        this.activity = activity;
//        mSelectImageFile = selectImageFile;
////        mBitmapLoader = bitmapLoader;
//    }
//
//    public SelectImageFile getImageFile() {
//        return mSelectImageFile;
//    }
//
//    ImageView selectButton;
//
//    @Override
//    public void onClick(View view) {
//        if (mSelectImageFile == null) {
//            return;
//        }
//        int viewId = view.getId();
//        if (R.id.id_item_image == viewId) {
//            showPreviewDialog(mSelectImageFile.mSelectImageFilePath);
//        } else if (R.id.onclick == viewId) {
//
//            if (mSelectImageFile.mIsSelected) {
//                mSelectImageFile.mIsSelected = false;
//
//            } else
//
//            {
//                if (!isSelectedMaxCount()) {
//                    mSelectImageFile.mIsSelected = true;
//
//                } else {
//                    return;
//                }
//            }
//            notifyDataFactoryChanged(selectButton);
//
//
//        }
//    }
//
//
//    //    @Override
////    public View getView(int position, ViewGroup parent) {
////        View viewContainer = mActivity.getLayoutInflater().inflate(R.layout.select_image_grid_item, parent, false);
////        ViewGroup.LayoutParams params = viewContainer.getLayoutParams();
////        params.height = (AspireUtils.getDefaultDisplay(mActivity)[0] / 3) - 10;
////        viewContainer.setLayoutParams(params);
////        updateView(viewContainer, position, parent);
////        return viewContainer;
////    }
////
////    ImageView selectButton;
////
////    @Override
////    public void updateView(View view, int position, ViewGroup parent) {
////        ImageView itemImage = (ImageView) view.findViewById(R.id.id_item_image);
////        view.findViewById(R.id.onclick).setOnClickListener(this);
////        if (itemImage != null) {
////            if (mSelectImageFile == null
////                    || mBitmapLoader == null
////                    || TextUtils.isEmpty(mSelectImageFile.mSelectImageFilePath)) {
////                itemImage.setImageResource(R.drawable.select_image_no_pictures);
////                itemImage.setBackgroundResource(0);//
////                itemImage.setColorFilter(null);
////            } else {
////                final String imagefilepath = "file://" + mSelectImageFile.mSelectImageFilePath;
////                if (!ViewImageLoader.isMyViewBitmap(itemImage, imagefilepath)) {
////
////                    itemImage.setImageResource(R.drawable.select_image_no_pictures);
////                    itemImage.setBackgroundResource(0);//
////                    mBitmapLoader.startImageLoader(itemImage, imagefilepath, null, true);
////                }
////
//////                if (mSelectImageFile.mIsSelected) {
//////                    itemImage.setColorFilter(Color.parseColor("#77000000"));
//////                } else {
//////                    itemImage.setColorFilter(null);
//////                }
////            }
////            itemImage.setOnClickListener(this);
////        }
////
////        selectButton = (ImageView) view.findViewById(R.id.id_item_select);
////        if (selectButton != null) {
////            if (mSelectImageFile == null || !mSelectImageFile.mIsSelected) {
////                selectButton.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.image_uncheck));
//////                selectButton.setImageResource(R.drawable.image_uncheck);
////            } else {
////                selectButton.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.image_check));
//////                selectButton.setImageResource(R.drawable.image_check);
////            }
////        }
////
////    }
////
//    private boolean isSelectedMaxCount() {
//        if (activity instanceof LocationImageActivity) {
//            return activity.isSelectedMaxCount();
//        }
//        return false;
//    }
//
//    //
//    private void notifyDataFactoryChanged(View view) {
//        activity.notifyDataChanged(this, view);
//    }
//
//    //
////    private SelectImageDataFactory getfactory() {
////        if (factory == null && mActivity instanceof ListBrowserActivity) {
////            ListBrowserActivity listactivity = (ListBrowserActivity) mActivity;
////            AbstractListDataFactory listfactory = listactivity.getListDataFactory();
////            if (listfactory instanceof SelectImageDataFactory) {
////                factory = (SelectImageDataFactory) listfactory;
////            }
////        }
////        return factory;
////    }
////
//    private void showPreviewDialog(String selecttedImage) {
//        if (selecttedImage == null || selecttedImage.length() == 0) {
//            return;
//        }
//
//        activity.showPreviewDialog(this, false);
//
//
//    }
//
//    @Override
//    public View getView(LayoutInflater listContainer, int position, View convertView, ViewGroup arg2) {
//        View viewContainer = listContainer.inflate(R.layout.select_image_grid_item, arg2, false);
//        ViewGroup.LayoutParams params = viewContainer.getLayoutParams();
//
//        params.height = (500 / 3) - 10;
//        viewContainer.setLayoutParams(params);
//        return viewContainer;
//    }
//
//    float valuetemp = -1;
//
//    void checkAnimation(final View imageView) {
//        com.nineoldandroids.animation.ValueAnimator widthAnimation = com.nineoldandroids.animation.ValueAnimator.ofFloat(0.8f, 1.0f);
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
//        widthAnimation.setDuration(600);
//        widthAnimation.start();
//    }
//
//    @Override
//    public void updateView(UIListAdapter listImageAdapter, Holder vHolder, int position, View convertView) {
////        checkAnimation(convertView);
//        SelectViewHolder viewHolder = (SelectViewHolder) vHolder;
//        viewHolder.onclick.setOnClickListener(this);
//        if (viewHolder.id_item_image != null) {
//            if (mSelectImageFile == null
//
//                    || TextUtils.isEmpty(mSelectImageFile.mSelectImageFilePath)) {
//                viewHolder.id_item_image.setImageResource(R.drawable.select_image_no_pictures);
//                viewHolder.id_item_image.setBackgroundResource(0);//
//                viewHolder.id_item_image.setColorFilter(null);
//            } else {
//
//                ImageOptions imageOptions = new ImageOptions(convertView.getContext(), R.drawable.select_image_no_pictures);
//                ServiceLoader.getInstance().displayImage(imageOptions, mSelectImageFile.mSelectImageFilePath, viewHolder.id_item_image);
//
//            }
//            viewHolder.id_item_image.setOnClickListener(this);
//        }
//        selectButton = viewHolder.id_item_select;
//        if (mSelectImageFile == null || !mSelectImageFile.mIsSelected) {
//            viewHolder.id_item_select.setBackgroundDrawable(convertView.getContext().getResources().getDrawable(R.drawable.image_uncheck));
////                selectButton.setImageResource(R.drawable.image_uncheck);
//        } else {
//            viewHolder.id_item_select.setBackgroundDrawable(convertView.getContext().getResources().getDrawable(R.drawable.image_check));
////                selectButton.setImageResource(R.drawable.image_check);
//        }
//    }
//
//    @Override
//    public Holder setHolder(Activity activity, Holder vHolder, View convertView) {
//        if (vHolder == null) {
//            SelectViewHolder viewHolder = new SelectViewHolder();
//            viewHolder.id_item_image = (ImageView) convertView.findViewById(R.id.id_item_image);
//            viewHolder.id_item_select = (ImageView) convertView.findViewById(R.id.id_item_select);
//            viewHolder.onclick = (LinearLayout) convertView.findViewById(R.id.onclick);
//            return viewHolder;
//        }
//        return null;
//    }
//}
