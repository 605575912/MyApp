package com.library.uiframe.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * 圆弧计分
 * 
 * @author Administrator
 * 
 */
public class DrawTableView extends View {

	private Paint paint_green, paint_black;
	private float tb = 1;
	// private int blackColor = 0x70000000; // 底黑色

	int heng = 4;
	int shu = 8;
	Bitmap sexbitmap;
	Rect headrect;
	RectF headrectf;
	String[] Objects;
	String[] strings;
	ArrayList<String> list;
	int l = 0;
	int padding_top = 0;
	float textsize = 0;

	public DrawTableView(Context context) {
		super(context);
		init();
	}

	public DrawTableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DrawTableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void setObjects() {
		// if (Objects == null) {

		list = new ArrayList<String>();
		
		for (int i = 0; i < 21; i++) {
			list.add("");
		}

	}

	public void init() {
		setWillNotDraw(false);
		Objects = new String[shu];
		Objects[0] = "";
		Objects[1] = "一";
		Objects[2] = "二";
		Objects[3] = "三";
		Objects[4] = "四";
		Objects[5] = "五";
		Objects[6] = "六";
		Objects[7] = "日";
		//
		strings = new String[heng];
		strings[0] = "";
		strings[1] = "上午";
		strings[2] = "下午";
		strings[3] = "晚上";

		// tb = getResources().getDimension(R.dimen.table_rode);
		paint_black = new Paint();
		paint_black.setAntiAlias(true);
		paint_black.setColor(Color.GRAY);
		paint_black.setStyle(Style.STROKE);
		paint_black.setStrokeWidth(tb);

		//
		paint_green = new Paint();
		paint_green.setAntiAlias(true);
		paint_green.setColor(Color.GRAY);
		paint_green.setStrokeWidth(1);
		paint_green.setTextAlign(Paint.Align.CENTER);

		paint_green.setTextSize(textsize);

		headrectf = new RectF();

		setBackgroundColor(Color.WHITE);
	}

	public float getTextsize() {
		return textsize;
	}

	public void setTextsize(float textsize) {
		this.textsize = textsize;
		paint_green.setTextSize(textsize);
	}

	public void setPadding_top(int padding_top) {
		this.padding_top = padding_top;
	}

	void drawView(Canvas canvas) {
		int w = getWidth() - padding_top * 2;
		if (l <= 0) {
			l = w / shu;
		}
		int imagewith = l;
		if (imagewith > sexbitmap.getWidth()) {
			imagewith = sexbitmap.getWidth();
		}

		for (int i = 0; i < heng; i++) {
			if (i > 0) {
				canvas.drawLine(padding_top, i * l + padding_top, w
						+ padding_top, i * l + padding_top, // 横
						paint_black);
			}

			float FontSpace = paint_green.getFontSpacing();
			float left = 0 * l + l / 2;
			float top = ((l + FontSpace) / 2 + i * l - tb);
			canvas.drawText(strings[i], left + padding_top, top + padding_top,
					paint_green);
		}
		if (list != null && sexbitmap != null) {
			if (headrect == null) {
				headrect = new Rect();
				headrect.set(0, 0, sexbitmap.getWidth(), sexbitmap.getWidth());
			}

			int sum = (shu - 1) * (heng - 1);
			int j = 0;
			int z = 0;
			float t = (l - imagewith) / 2;
			for (int i = 0; i < list.size(); i++) {
				if (i < sum) {

					if (i % (shu - 1) == 0) {
						j++;
						z = 0;
					}
					z++;
					if (list.get(i).equals("1")) {
						float left = z * l + t;
						float top = t + j * l;
						headrectf.set(left + padding_top, top + padding_top,
								left + imagewith + padding_top, top + imagewith
										+ padding_top);
						canvas.drawBitmap(sexbitmap, headrect, headrectf, null);
					}
				} else {
					break;
				}

			}
		}
		for (int i = 0; i < shu; i++) {
			int left = i * l;
			if (i > 0) {
				canvas.drawLine(left + padding_top, padding_top, left
						+ padding_top, heng * l + padding_top, paint_black);
			}
			float FontSpace = paint_green.getFontSpacing();
			left = left + l / 2;
			float top = ((l + FontSpace) / 2 + 0 * l - tb);
			canvas.drawText(Objects[i], left + padding_top, top + padding_top,
					paint_green);

			// float left = i * l + (l - imagewith) / 2;
			// float top = (l - imagewith) / 2 + 0 * l;
			// headrectf.set(left + padding_top, top + padding_top,
			// left + imagewith + padding_top, top + imagewith
			// + padding_top);
			// canvas.drawBitmap(sexbitmap, headrect, headrectf, null);

			// for (int j = 0; j < heng; j++) {
			// String string = Objects[j][i];
			// if (string != null) {
			// if (string.equals("")) {
			// float left = i * l + (l - imagewith) / 2;
			// float top = (l - imagewith) / 2 + j * l;
			// headrectf.set(left + padding_top, top + padding_top,
			// left + imagewith + padding_top, top + imagewith
			// + padding_top);
			// canvas.drawBitmap(sexbitmap, headrect, headrectf, null);
			// } else {
			//
			// float FontSpace = paint_green.getFontSpacing();
			// float left = i * l + l / 2;
			// float top = ((l + FontSpace) / 2 + j * l - tb);
			// canvas.drawText(string, left + padding_top, top
			// + padding_top, paint_green);
			// }
			// } else {
			// // 不画
			// }
			//
			// }
		}
		canvas.drawRect(padding_top, padding_top, w + padding_top, heng * l
				+ padding_top, paint_black); // 边框

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		drawView(canvas);
		super.onDraw(canvas);
	}

	public int getPadding_top() {
		return padding_top;
	}

	public int getHeng() {
		return heng;
	}

	public int getShu() {
		return shu;
	}

	public Bitmap getSexbitmap() {
		return sexbitmap;
	}

	public void setSexbitmap(Bitmap sexbitmap) {
		this.sexbitmap = sexbitmap;
	}

	public ArrayList<String> getList() {
		return list;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		super.dispatchDraw(canvas);
	}

}
