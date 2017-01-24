package com.lzxmy.demo.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.support.loader.utils.ImageUtils;


/**
 * 图片上传自定义遮挡百分比 2015-1-29 @author lzx
 * 
 */
public class DrawArc extends TextView {

	private Paint paint_rectf_gray;
	int value = -1;

	public DrawArc(Context context) {
		super(context);
		init();
	}

	public DrawArc(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DrawArc(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	void init() {
		setGravity(Gravity.CENTER);
		paint_rectf_gray = new Paint();
		// paint_rectf_gray.setStrokeWidth(tb * 0.1f);
		paint_rectf_gray.setColor(Color.GRAY);
		// paint_rectf_gray.setColor(fineLineColor);
		paint_rectf_gray.setStyle(Style.FILL);
		paint_rectf_gray.setAntiAlias(true);

	}

	@Override
	protected void onDraw(Canvas c) {
		drawRectf(c);
		super.onDraw(c);

	}

	/**
	 * 绘制半圆
	 * 
	 * @param c
	 */
	void drawRectf(Canvas canvas) {

		Bitmap scaledSrcBmp = Bitmap.createBitmap(getWidth(), getHeight(),
				Config.ARGB_8888);
		Canvas ca = new Canvas(scaledSrcBmp);
		RectF rect = new RectF();
		rect.set(0, 0, getHeight(), getWidth() * (100 - value) / 100);
		ca.drawRect(rect, paint_rectf_gray);
		Bitmap roundBitmap = ImageUtils.getCroppedRoundBitmap(scaledSrcBmp,
				getWidth() / 2);
		canvas.drawBitmap(roundBitmap, 0, 0, null);
		scaledSrcBmp.recycle();
		roundBitmap.recycle();
	}

	public void setValue(int value) {
		this.value = value;
		if (value >= 0 & value < 100) {
			setText(value + "%");
		} else {
			setText("");
		}
	}

	public int getValue() {
		return value;
	}

}
