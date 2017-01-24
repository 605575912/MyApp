package com.library.uiframe.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class Bg_framelayout extends LinearLayout {
	Paint paint;
	Path path;
	RectF rect;

	public Bg_framelayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public Bg_framelayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	void init() {
		setWillNotDraw(false);
		paint = new Paint();
		paint.setColor(Color.rgb(75, 190, 225));
		paint.setStrokeWidth(2);
		path = new Path();
		PathEffect effects = new DashPathEffect(new float[] { 5, 2 }, 0);
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(false);
		paint.setPathEffect(effects);
		rect = new RectF();
	}

	public void setpatheffect(float maxlen, float minlen) {
		PathEffect effects = new DashPathEffect(new float[] { maxlen, minlen },
				0);
		paint.setPathEffect(effects);
	}

	public void setPaintColor(int color) {
		paint.setColor(color);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		path.reset();
		rect.set(0, 0, getWidth(), getHeight());
		path.addRect(rect, Path.Direction.CCW);
		canvas.drawPath(path, paint);
		super.onDraw(canvas);
	}
}
