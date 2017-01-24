package com.support.loader.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.support.loader.proguard.IProguard;
import com.support.loader.utils.LogBlue;

import java.util.HashMap;
import java.util.List;

public class UIListAdapter extends BaseAdapter implements IProguard {
    List<ItemData> datas;
    Activity activity;
    private LayoutInflater listContainer;// 视图容器
    HashMap<String, Integer> typehash;

    public UIListAdapter(Activity activity, List<ItemData> datas) {
        this.listContainer = LayoutInflater.from(activity); // 创建视图容器并设置上下文
        this.datas = datas;
        this.activity = activity;
        typehash = new HashMap<String, Integer>();
    }

    Object object;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return datas.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return datas.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    int sumtype = 0;

    @Override
    public int getItemViewType(int position) {
        ItemData viewItemData = datas.get(position);
        int type;
        String classname = viewItemData.getClass().getName();
        if (typehash.containsKey(classname)) {
            type = typehash.get(classname);
        } else {
            typehash.put(classname, sumtype);
            type = sumtype;
            sumtype = sumtype + 1;
        }
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return 10;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ItemData viewItemData = datas.get(position);
        Holder vHolder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            convertView = viewItemData.getView(listContainer, position, convertView, arg2);
            vHolder = viewItemData.setHolder(activity, vHolder, convertView);
            convertView.setTag(vHolder);
        } else {
            vHolder = (Holder) convertView.getTag();
        }
        viewItemData.updateView(this, vHolder, position, convertView);
        return convertView;
    }


}
