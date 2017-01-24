package com.xsw.library.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

/**
 * 圆弧计分
 * 
 * @author Administrator
 * 
 */
public class DrawTitleView extends View {
	private RectF imgRectF;
	private int image_left;
	Paint paint_white, paint_gray, paint_red;
	Bitmap bg_Bitmap;
	Rect rect;
	Rect headrect;
	RectF headrectf;
	Rect sexrect;
	RectF sexrectf;
	int image_padding = 0;
	Bitmap headBitmap, sexbitmap, startBitmap;
	String name = "刘胡兰";
	float FontSpace;
	int sexHeight;
	float left;
	int headHeight;
	String price_lowest = "100";

	public DrawTitleView(Context context, Bitmap bg_Bitmap, int image_left,
			int headHeight, int sexHeight) {
		super(context);
		this.bg_Bitmap = bg_Bitmap;
		this.image_left = image_left;
		this.headHeight = headHeight;
		this.sexHeight = sexHeight;
		init();
	}

	public void setHeadBitmap(Bitmap headBitmap) {
		this.headBitmap = headBitmap;
	}

	public void setSexbitmap(Bitmap sexbitmap) {
		this.sexbitmap = sexbitmap;
	}

	public void init() {
		setWillNotDraw(false);
		imgRectF = new RectF();
		rect = new Rect();

		headrectf = new RectF();
		headrect = new Rect();

		sexrectf = new RectF();
		sexrect = new Rect();
		image_padding = (int) getResources()
				.getDimension(com.xsw.library.R.dimen.image_padding);
		paint_white = new Paint();
		paint_white.setAntiAlias(true);
		paint_white.setColor(Color.WHITE);
		paint_white.setStrokeWidth(1);
		paint_white.setTextSize(getResources().getDimension(com.xsw.library.R.dimen.namesize));
//		paint_white.setStyle(Style.STROKE);
		FontSpace = paint_white.getFontSpacing();

		paint_gray = new Paint();
		paint_gray.setAntiAlias(true);

		paint_gray.setColor(Color.GRAY);
		paint_gray.setTextSize(getResources().getDimension(com.xsw.library.R.dimen.startsize));

		paint_red = new Paint();
		paint_red.setAntiAlias(true);
		paint_red.setColor(Color.RED);
		paint_red.setTextSize(getResources().getDimension(com.xsw.library.R.dimen.startsize));

		imgRectF.set(0, 0, bg_Bitmap.getWidth(), bg_Bitmap.getHeight());
		rect.set(0, 0, bg_Bitmap.getWidth(), bg_Bitmap.getHeight());

		headrectf.set(image_left, rect.bottom - headHeight / 2, image_left
				+ headHeight, rect.bottom + headHeight / 2);
		headrect.set(0, 0, headHeight, headHeight);

		sexrect.set(0, 0, sexHeight, sexHeight);

	}

	public void setStartBitmap(Bitmap startBitmap) {
		this.startBitmap = startBitmap;
	}

	public void setTextColor(int gray) {
		paint_gray.setColor(gray);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(bg_Bitmap, rect, imgRectF, null);
		if (headBitmap != null) {
			canvas.drawBitmap(headBitmap, headrect, headrectf, null);
		}

		if (left == 0) {
			
			left = headrectf.right + image_padding + paint_white.measureText(name)*5/3;
			float top = imgRectF.bottom - image_padding / 2 - sexHeight;
			sexrectf.set(left, top, left + sexHeight, top + sexHeight);
		}
		canvas.drawText(name, headrectf.right + image_padding, imgRectF.bottom
				- image_padding, paint_white);
		if (sexbitmap != null) {
			canvas.drawBitmap(sexbitmap, sexrect, sexrectf, null);
		}
		if (startBitmap != null) {
			canvas.drawBitmap(startBitmap, headrectf.right + image_padding,
					imgRectF.bottom + image_padding, null);

			canvas.drawText("4.0", headrectf.right + image_padding * 2
					+ startBitmap.getWidth(), imgRectF.bottom + image_padding
					* 2, paint_gray);
		}

		canvas.drawText(price_lowest, headrectf.right + image_padding,
				getHeight() - image_padding, paint_red);
		canvas.drawText("元起/小时", headrectf.right + price_lowest.length()
				* FontSpace, getHeight() - image_padding, paint_gray);

		super.onDraw(canvas);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice_lowest(String price_lowest) {
		this.price_lowest = price_lowest;
	}

}
