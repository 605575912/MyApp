package com.lzxmy.demo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.TextView;

import com.support.loader.adapter.Holder;
import com.support.loader.adapter.ItemData;
import com.support.loader.adapter.UIListAdapter;

public class ChooseItem extends ItemData {
	String name = "";
	int tag = 0;

	public ChooseItem(String name, int tag) {
		this.name = name;
		this.tag = tag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, Context context) {

	}

	@Override
	public View getView(LayoutInflater listContainer, int position, View convertView, ViewGroup arg2) {
		convertView = listContainer.inflate(R.layout.listview, null);
		return convertView;
	}

	@Override
	public void updateView(UIListAdapter listImageAdapter, Holder vHolder, int position, View convertView) {
		ChooseHolder viewHolder = (ChooseHolder) vHolder;
		viewHolder.tv_name.setText(String.valueOf(name));
        TranslateAnimation animation = new TranslateAnimation(-convertView.getWidth() / 8 , 0, 0, 0);
        animation.setDuration(600);
        animation.setFillAfter(true);
        convertView.setAnimation(animation);
        animation.start();
	}

	@Override
	public Holder setHolder(Activity activity, Holder vHolder, View convertView) {
		if (vHolder == null) {
			ChooseHolder viewHolder = new ChooseHolder();
			viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			return viewHolder;
		}
		return vHolder;
	}

	@Override
	public void onScrollStateChanged(int i) {

	}
}
