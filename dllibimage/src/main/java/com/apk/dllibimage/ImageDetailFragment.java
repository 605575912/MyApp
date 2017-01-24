//package com.apk.dllibimage;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.LinearInterpolator;
//import android.view.animation.RotateAnimation;
//import android.widget.ImageView;
//
//import com.library.uiframe.view.PhotoViewAttacher;
//import com.support.loader.ServiceLoader;
//import com.support.loader.packet.ImageOptions;
//import com.support.loader.utils.ImageLoadingListener;
//
//
//public class ImageDetailFragment extends Fragment {
//    private String mImageUrl, minimageUrl;
//    private ImageView iv_image, progressbar;
////    protected IViewDrawableLoader mBitmapLoader;
//    private int type = ImagePagerActivity.EXTRA_CODE_RESURE;
//    ImageFragmentListener imageFragmentListener;
//    boolean isexit = false;
//    Animation anim;
//
//    public static ImageDetailFragment newInstance(String minimageUrl, String imageUrl, int type, ImageFragmentListener imageFragmentListener) {
//        final ImageDetailFragment f = new ImageDetailFragment();
//        f.imageFragmentListener = imageFragmentListener;
//        f.type = type;
//        final Bundle args = new Bundle();
//        args.putString("url", imageUrl);
//        args.putString("minurl", minimageUrl);
//        f.setArguments(args);
//
//        return f;
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
//        if (getArguments() != null) {
//            mImageUrl = getArguments().getString("url");
//            minimageUrl = getArguments().getString("minurl");
//        }
//
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
//        iv_image = (ImageView) v.findViewById(R.id.iv_image);
//        progressbar = (ImageView) v.findViewById(R.id.progressbar);
//        if (type == ImagePagerActivity.EXTRA_PRIEW) {
//            v.setBackgroundColor(Color.BLACK);
//
////            iv_image.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.select_image_default));
//        }
//
//        iv_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (imageFragmentListener != null) {
//                    imageFragmentListener.ImageOnclick(iv_image);
//                }
//            }
//        });
//
//
//        return v;
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
////        mBitmapLoader = new ViewImageLoader(getActivity(), new ViewImageLoader.ViewImageListener() {
////            @Override
////            public Bitmap onViewImagePrepare(View view, Bitmap bitmap) {
////                String path = (String) view.getTag();
////                if (!TextUtils.isEmpty(path)) {
////                    Bitmap cachebmp = BitmapReference.getCache(getReferenceTag(path));
////                    if (cachebmp == null || cachebmp != bitmap) {
////                        return SelectImageDataFactory.rotaingImageView(SelectImageDataFactory.readPictureDegree(path.replace("file://", "")), bitmap);
////                    }
////                }
////                return bitmap;
////            }
////
////            @Override
////            public void onViewImageChanged(View view, Bitmap target_bmp, boolean success) {
////
////                if (isexit)
////                    return;
////
////
////                if (anim != null) {
////                    anim.cancel();
////                    progressbar.clearAnimation();
////                    progressbar.setVisibility(View.GONE);
////                }
////                if (view.getTag(R.id.iv_image) != null) {
////                    if ((Integer) view.getTag(R.id.iv_image) == 0) {//缩略图
////                        iv_image.setTag(R.id.iv_image, 1);  // 代表缩略图加载过一次了
////                        return;
////                    } else {  //原图
////
////                    }
////                }
////                if (success) {
////                    PhotoViewAttacher mAttacher = new PhotoViewAttacher((ImageView) view);
////                    mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
////                        @Override
////                        public void onViewTap(View view, float x, float y) {
////                            if (imageFragmentListener != null) {
////                                imageFragmentListener.ImageOnclick(view);
////                            }
////                        }
////                    });
////
////                } else {
////
////                    if (view instanceof ImageView) {
////                        ImageView iv_image = (ImageView) view;
////                        iv_image.setScaleType(ImageView.ScaleType.CENTER);
////                        iv_image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.imageview_default));
////                    }
//////                    view.setBackgroundDrawable(ImagePagerActivity.this.getResources().getDrawable(R.drawable.imageview_default));
////                   if (imageFragmentListener!=null){
////                       if (view.getTag()!=null&&view.getTag() instanceof String){
////                           imageFragmentListener.ImageShowTipTagurl((String) view.getTag());
////                       }
////
////                   }
////
////                }
////
////            }
////
////            @Override
////            public String getReferenceTag(String url) {
////                return url + ImagePagerActivity.class.getSimpleName(); //图片发生改变，用当前类名来标记缓存
////            }
////        });
////
////        StringBuilder stringBuilder = new StringBuilder();
////        if (mImageUrl.indexOf("http://") > -1 || mImageUrl.indexOf("asset") > -1) {
////            stringBuilder.append(mImageUrl);
////            positive(progressbar);
////            Bitmap cachebmp = BitmapReference.getCache(minimageUrl + ImagePagerActivity.class.getSimpleName());
////            if (cachebmp != null) {
////                iv_image.setImageBitmap(cachebmp);
////            } else {
////                iv_image.setScaleType(ImageView.ScaleType.CENTER);
////                iv_image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.imageview_default));
//////            iv_image.setTag(R.id.iv_image, 0);
//////            mBitmapLoader.startImageLoader(iv_image, minimageUrl, null, true);
////            }
////        } else {
////            if (mImageUrl.indexOf("file://") > -1) {
////                stringBuilder.append(mImageUrl);
////            } else {
////                stringBuilder.append("file://" + mImageUrl);
////            }
////        }
////
////        mBitmapLoader.startImageLoader(iv_image, stringBuilder.toString(), null, true);
//        ImageOptions imageOptions = new ImageOptions(getActivity(), R.drawable.select_image_no_pictures);
//        ServiceLoader.getInstance().displayImage(imageOptions, mImageUrl, iv_image, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String var1, View var2) {
//                positive(progressbar);
//            }
//
//            @Override
//            public void onLoadingFailed(String var1, View view) {
//                if (isexit)
//                    return;
//
//
//                if (anim != null) {
//                    anim.cancel();
//                    progressbar.clearAnimation();
//                    progressbar.setVisibility(View.GONE);
//                }
//                if (view instanceof ImageView) {
//                    ImageView iv_image = (ImageView) view;
//                    iv_image.setScaleType(ImageView.ScaleType.CENTER);
//                    iv_image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.select_image_no_pictures));
//                }
////                    view.setBackgroundDrawable(ImagePagerActivity.this.getResources().getDrawable(R.drawable.imageview_default));
//                if (imageFragmentListener!=null){
//                    if (view.getTag()!=null&&view.getTag() instanceof String){
//                        imageFragmentListener.ImageShowTipTagurl((String) view.getTag());
//                    }
//
//                }
//            }
//
//            @Override
//            public void onLoadingComplete(String var1, View view, Bitmap var3) {
//                if (isexit)
//                    return;
//
//
//                if (anim != null) {
//                    anim.cancel();
//                    progressbar.clearAnimation();
//                    progressbar.setVisibility(View.GONE);
//                }
//                if (view.getTag(R.id.iv_image) != null) {
//                    if ((Integer) view.getTag(R.id.iv_image) == 0) {//缩略图
//                        iv_image.setTag(R.id.iv_image, 1);  // 代表缩略图加载过一次了
//                        return;
//                    } else {  //原图
//
//                    }
//                }
//                    PhotoViewAttacher mAttacher = new PhotoViewAttacher((ImageView) view);
//                    mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
//                        @Override
//                        public void onViewTap(View view, float x, float y) {
//                            if (imageFragmentListener != null) {
//                                imageFragmentListener.ImageOnclick(view);
//                            }
//                        }
//                    });
//
//
//
//
//            }
//
//            @Override
//            public void onLoadingCancelled(String var1, View var2) {
//
//            }
//        });
//    }
//
//    void positive(final ImageView v) {
//        if (v != null) {
//            v.setVisibility(View.VISIBLE);
//            v.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.select_progress));
//            anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
//                    Animation.RELATIVE_TO_SELF, 0.5f);
//            /** 匀速插值器 */
//            LinearInterpolator lir = new LinearInterpolator();
//            anim.setInterpolator(lir);
//            anim.setRepeatCount(-1);
//            anim.setDuration(1000);
//            /** 动画完成后不恢复原状 */
//            anim.setFillAfter(true);
//            v.startAnimation(anim);
//
//        }
//
//    }
//
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        isexit = true;
//        if (anim != null) {
//            anim.cancel();
//        }
//    }
//}
