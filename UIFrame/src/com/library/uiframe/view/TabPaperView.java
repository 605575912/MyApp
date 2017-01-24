package com.library.uiframe.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class TabPaperView extends LinearLayout {
	Paint bgpaint;
	Rect targetRect;
	int index = 0;
	LinearLayout linearLayout;

	View oldview;

	public TabPaperView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public TabPaperView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	void init() {
		setWillNotDraw(false);
		setOrientation(LinearLayout.HORIZONTAL);
		bgpaint = new Paint();
		targetRect = new Rect(0, 0, 0, 0);
		bgpaint.setColor(Color.BLUE);
		linearLayout = new LinearLayout(getContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		addView(linearLayout, params);
	}

	public void setColor(int color) {
		bgpaint.setColor(color);
	}

	public void addViews(View view, LayoutParams params, int i) {
		view.setTag(i);
		if (oldview == null) {
			oldview = view;
		}
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				index = (Integer) arg0.getTag();
				if (onTabClickListener != null) {
					onTabClickListener.onClick(index);
					onTabClickListener.paperChanged(oldview, arg0,index);
				}
				invalidate();
				oldview = arg0;
			}
		});
		linearLayout.addView(view, i, params);
	}

	public void paperChanged(int index) {
		this.index = index;
		if (onTabClickListener != null) {
			onTabClickListener.paperChanged(oldview, linearLayout.getChildAt(index),index);
		}
		oldview = linearLayout.getChildAt(index);
		invalidate();
	}

	public OnTabClickListener getOnTabClickListener() {
		return onTabClickListener;
	}

	public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
		this.onTabClickListener = onTabClickListener;
	}

	OnTabClickListener onTabClickListener;

	public interface OnTabClickListener {
		public void onClick(int index);
		public void paperChanged(View oldview, View view, int index);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawColor(Color.rgb(247, 249, 249));
		int size = linearLayout.getChildCount() > 0 ? linearLayout
				.getChildCount() : 1;
		int lenght = getWidth() / size;
		int left = lenght * index;
		int right = left + lenght;
		targetRect.set(left, getHeight() - getPaddingBottom(), right,
				getHeight());
		canvas.drawRect(targetRect, bgpaint);

	}
}
