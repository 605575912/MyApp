//package com.apk.dllibimage;
//
//import android.app.Activity;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.support.loader.adapter.Holder;
//import com.support.loader.adapter.UIListAdapter;
//import com.support.loader.adapter.ViewItemData;
//
//
//public class ImageFloder implements ViewItemData {
//    /**
//     * 图片的文件夹路径
//     */
//    private String dir;
//
//    /**
//     * 第一张图片的路径
//     */
//    private String firstImagePath;
//
//    /**
//     * 文件夹的名称
//     */
//    private String name = "";
//
//    /**
//     * 图片的数量
//     */
//    private int count;
//    //    boolean ischeck = false;
////    private IViewDrawableLoader mBitmapLoader;
//
//    public String getDir() {
//        return dir;
//    }
//
////    public ImageFloder(IViewDrawableLoader mBitmapLoader) {
////        this.mBitmapLoader = mBitmapLoader;
////    }
//
////    public boolean ischeck() {
////        return ischeck;
////    }
////
////    public void setIscheck(boolean ischeck) {
////        this.ischeck = ischeck;
////    }
//
//    Activity activity;
//
//    public void setDir(String dir) {
//        this.dir = dir;
//
//
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getFirstImagePath() {
//        return firstImagePath;
//    }
//
//    public void setFirstImagePath(String firstImagePath) {
//        this.firstImagePath = firstImagePath;
//    }
//
//    boolean ischeck = false;
//
//
//    public int getCount() {
//        return count;
//    }
//
//    public void setCount(int count) {
//        this.count = count;
//    }
//
//    @Override
//    public View getView(LayoutInflater listContainer, int position, View convertView, ViewGroup arg2) {
//        convertView = listContainer.inflate(R.layout.select_image_list_dir_item, null);
//        return convertView;
//    }
//
//    @Override
//    public void updateView(UIListAdapter listImageAdapter, Holder vHolder, int position, View convertView) {
//        ImageViewHolder viewHolder = (ImageViewHolder) vHolder;
//        viewHolder.id_dir_item_count.setText(String.valueOf(count));
//        viewHolder.id_dir_item_name.setText(getName());
//        viewHolder.id_dir_item_image.setImageResource(R.drawable.select_image_no_pictures);
//        viewHolder.id_dir_item_image.setBackgroundResource(0);//
////        mBitmapLoader.startImageLoader(viewHolder.id_dir_item_image, "file://" + firstImagePath, null, true);
//        if (ischeck) {
//            viewHolder.id_dir_selectted.setBackgroundDrawable(convertView.getContext().getResources().getDrawable(R.drawable.image_check));
//        } else {
//            viewHolder.id_dir_selectted.setBackgroundDrawable(null);
//        }
//    }
//
//    public String getName() {
//        if (TextUtils.isEmpty(name)) {
//            int lastIndexOf = this.dir.lastIndexOf("/");
//            if (lastIndexOf <= dir.length() - 1) {
//                return this.dir.substring(lastIndexOf + 1, dir.length());
//            }
//        }
//        return name;
//    }
////    @Override
////    public void updateView(ListImageAdapter listImageAdapter,Holder vHolder, int position, View convertView) {
////        ImageViewHolder viewHolder = (ImageViewHolder) vHolder;
////        viewHolder.id_dir_item_count.setText(String.valueOf(count));
////        viewHolder.id_dir_item_name.setText(getName());
////        viewHolder.id_dir_item_image.setImageResource(R.drawable.select_image_no_pictures);
////        viewHolder.id_dir_item_image.setBackgroundResource(0);//
////        mBitmapLoader.startImageLoader(viewHolder.id_dir_item_image, "file://" + firstImagePath, null, true);
////        if (ischeck) {
////            viewHolder.id_dir_selectted.setBackgroundDrawable(convertView.getContext().getResources().getDrawable(R.drawable.image_check));
////        } else {
////            viewHolder.id_dir_selectted.setBackgroundDrawable(null);
////        }
////
//////        codeViewHolder.tv_name.setText(name);
//////        convertView.setOnClickListener(this);
//////        convertView.setOnLongClickListener(new View.OnLongClickListener() {
//////            @Override
//////            public boolean onLongClick(View v) {
//////                Intent intent = new Intent();
//////                intent.putExtra("code", code);
//////                intent.putExtra("name", name);
//////                intent.setClass(activity, SettingMusicActivity.class);
//////                activity.startActivityForResult(intent, 0);
//////                return false;
//////            }
//////        });
////
////    }
//
//    @Override
//    public Holder setHolder(Activity activity, Holder vHolder, View convertView) {
//        this.activity = activity;
//        if (vHolder == null) {
//            ImageViewHolder viewHolder = new ImageViewHolder();
//            viewHolder.id_dir_item_count = (TextView) convertView.findViewById(R.id.id_dir_item_count);
//            viewHolder.id_dir_item_name = (TextView) convertView.findViewById(R.id.id_dir_item_name);
//            viewHolder.id_dir_item_image = (ImageView) convertView.findViewById(R.id.id_dir_item_image);
//            viewHolder.id_dir_selectted = (ImageView) convertView.findViewById(R.id.id_dir_selectted);
//            return viewHolder;
//        }
//        return vHolder;
//
//    }
//}
