//package com.lzxmy.demo;
//
//import android.app.Activity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.TranslateAnimation;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//import com.support.loader.adapter.Holder;
//import com.support.loader.adapter.UIListAdapter;
//import com.support.loader.adapter.ViewItemData;
//
//public class ChoosePopItem implements ViewItemData {
//    String name = "";
//    int tag = 0;
//
//    public ChoosePopItem(String name, int tag) {
//        this.name = name;
//        this.tag = tag;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getTag() {
//        return tag;
//    }
//
//    public void setTag(int tag) {
//        this.tag = tag;
//    }
//
//    @Override
//    public View getView(LayoutInflater listContainer, int position, View convertView, ViewGroup arg2) {
//        convertView = listContainer.inflate(R.layout.listview, null);
//        return convertView;
//    }
//
//    @Override
//    public void updateView(UIListAdapter listImageAdapter, Holder vHolder, int position, View convertView) {
//        ChooseHolder viewHolder = (ChooseHolder) vHolder;
//        viewHolder.tv_name.setText(String.valueOf(name));
//        viewHolder.popupWindow.showAsDropDown(viewHolder.tv_name);
//        TranslateAnimation animation = new TranslateAnimation(-convertView.getWidth() / 8, 0, 0, 0);
//        animation.setDuration(600);
//        animation.setFillAfter(true);
//        convertView.setAnimation(animation);
//        animation.start();
//    }
//
//    @Override
//    public Holder setHolder(Activity activity, Holder vHolder, View convertView) {
////        if (vHolder == null) {
//            ChooseHolder viewHolder = new ChooseHolder();
//            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
//            Button button = new Button(activity);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, 20);
//            button.setLayoutParams(layoutParams);
//            button.setText("232323");
//            PopupWindow popupWindow = new PopupWindow(button,
//                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
//            viewHolder.popupWindow = popupWindow;
////            return viewHolder;
////        }
//        return vHolder;
//    }
//}
