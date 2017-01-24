package com.library.uiframe.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ScorelineView extends View {
	Paint paint, graypaint;
	Rect rect, grayrect;
	int h = 4;
	List<Integer> listvalue;
	Path path;
	float radian;
	private float radius = 40;
	float radius_in;

	public ScorelineView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();
	}

	public ScorelineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public ScorelineView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	void init() {
		paint = new Paint();
		paint.setColor(Color.rgb(150, 150, 150));
		graypaint = new Paint();
		graypaint.setColor(Color.rgb(218, 218, 218));
		rect = new Rect();
		grayrect = new Rect();
		listvalue = new ArrayList<Integer>();
		listvalue.add(80);
		listvalue.add(30);
		listvalue.add(0);
		listvalue.add(10);
		listvalue.add(20);

		path = new Path();
		// LayoutParams params = super.getLayoutParams();
		float DEGREE = 36; // 锟斤拷锟斤拷墙嵌锟�
		radian = degree2Radian(DEGREE);
	}

	private float degree2Radian(float degree) {
		return (float) (Math.PI * degree / 180);
	}

	public void setListvalue(List<Integer> listvalue) {
		 this.listvalue = listvalue;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (listvalue != null && !listvalue.isEmpty()) {
			int with = getHeight();
			float top = 0;
			this.radius = (float) (with / 10);
			h = (int) (radius / 2);
			float dx = (float) (radius * Math.cos(radian / 2)) * 2;
			float hadd = ((float) with / listvalue.size() - dx);
			float dy = (float) (radius * Math.cos(radian / 2)) * 2 + hadd;
			radius_in = (float) (radius * Math.sin(radian / 2) / Math
					.cos(radian));
			for (int l = listvalue.size() - 1; l >= 0; l--) {
				grayrect.set(with, (int) (top + dx / 2), getWidth(), (int) (top
						+ dx / 2 + h));

				rect.set(with, (int) (top + dx / 2), (int) ((getWidth() - with)
						* listvalue.get(l) / 100.0 + with),
						(int) (top + dx / 2 + h));
				 canvas.drawRect(grayrect, graypaint);
				canvas.drawRect(rect, paint);

				{
					int index = listvalue.size() - l - 1;
					int sum = l + 1;
					float x = 0 + index * dy;
					for (int i = 1; i < sum + 1; i++) {
						drawstart(top, x, canvas);
						x = dy + x;
					}
					top = top + dy;
				}
			}

		}
	}

	void drawstart(float top, float x, Canvas canvas) {
		path.reset();
		path.moveTo((float) (radius * Math.cos(radian / 2)) + x, 0 + top);
		//
		path.lineTo(
				(float) (radius * Math.cos(radian / 2) + radius_in
						* Math.sin(radian))
						+ x,
				(float) (radius - radius * Math.sin(radian / 2) + top));
		path.lineTo((float) (radius * Math.cos(radian / 2) * 2) + x,
				(float) (radius - radius * Math.sin(radian / 2) + top));
		path.lineTo(
				(float) (radius * Math.cos(radian / 2) + radius_in
						* Math.cos(radian / 2))
						+ x,
				(float) (radius + radius_in * Math.sin(radian / 2) + top));
		path.lineTo(
				(float) (radius * Math.cos(radian / 2) + radius
						* Math.sin(radian))
						+ x, (float) (radius + radius * Math.cos(radian) + top));
		//
		path.lineTo((float) (radius * Math.cos(radian / 2)) + x,
				(float) (radius + radius_in) + top);
		path.lineTo(
				(float) (radius * Math.cos(radian / 2) - radius
						* Math.sin(radian))
						+ x, (float) (radius + radius * Math.cos(radian) + top));
		path.lineTo(
				(float) (radius * Math.cos(radian / 2) - radius_in
						* Math.cos(radian / 2))
						+ x,
				(float) (radius + radius_in * Math.sin(radian / 2) + top));
		path.lineTo(0 + x,
				(float) (radius - radius * Math.sin(radian / 2) + top));
		path.lineTo(
				(float) (radius * Math.cos(radian / 2) - radius_in
						* Math.sin(radian))
						+ x,
				(float) (radius - radius * Math.sin(radian / 2) + top));

		path.close();
		canvas.drawPath(path, paint);
	}
}
