package com.support.loader.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * Created by liangzhenxiong on 15/11/7.
 */
public interface ItemView {
    void onItemClick(AdapterView<?> parent, View view, int position, Context context);

    View getView(LayoutInflater listContainer, int position, View convertView, ViewGroup arg2);

    void updateView(UIListAdapter listImageAdapter, Holder vHolder, int position, View convertView);

    Holder setHolder(Activity activity, Holder vHolder, View convertView);
    void onScrollStateChanged(int i);
}
