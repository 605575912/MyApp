package com.library.uiframe.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * 圆弧计分
 * 
 * @author Administrator
 * 
 */
public class DrawTeachDateView extends View {

	private Paint paint_black, paint_graytext;
	private Paint paint_gray, paint_blue;
	private float tb = 1;
	// Bitmap bitmap;
	int ver = 7;
	int hor = 4;
	// int bitmapwith = 0;
	// Rect headrect;
	Rect tablerect;
	// RectF headrectf;
	String[] Objects;
	String[] strings;
	String[][] list;
	int padding_top = 0;
	float textsize = 17;
	int rectwidth = 0;
	int maxwith = 800;
	FontMetricsInt fontMetrics;

	public DrawTeachDateView(Context context, int maxwith) {
		super(context);
		init();
		this.maxwith = maxwith;
	}

	public DrawTeachDateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DrawTeachDateView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}


	public String[][] getObjects() {
		return list;
	}

	public void init() {
		setWillNotDraw(false);
		list = new String[4][7];
		list[0][0] = "";
		list[0][1] = "";
		list[0][2] = "";
		list[0][3] = "";
		list[0][4] = "";
		list[0][5] = "";
		list[0][6] = "";

		list[1][0] = "";
		list[1][1] = "";
		list[1][2] = "";
		list[1][3] = "";
		list[1][4] = "";
		list[1][5] = "";
		list[1][6] = "";

		list[2][0] = "";
		list[2][1] = "";
		list[2][2] = "";
		list[2][3] = "";
		list[2][4] = "";
		list[2][5] = "";
		list[2][6] = "";

		Objects = new String[ver];
		Objects[0] = "周一";
		Objects[1] = "周二";
		Objects[2] = "周三";
		Objects[3] = "周四";
		Objects[4] = "周五";
		Objects[5] = "周六";
		Objects[6] = "周日";
		//
		strings = new String[hor];
		strings[0] = "上";
		strings[1] = "下";
		strings[2] = "晚";

		paint_black = new Paint();
		paint_black.setAntiAlias(true);
		paint_black.setColor(Color.rgb(172, 172, 172));
		paint_black.setStrokeWidth(tb);
		paint_black.setTextAlign(Paint.Align.CENTER);

		paint_blue = new Paint();
		paint_blue.setAntiAlias(true);
		paint_blue.setColor(Color.rgb(50, 160, 197));
		paint_blue.setStrokeWidth(tb);
		paint_blue.setTextAlign(Paint.Align.CENTER);
		paint_graytext = new Paint();
		paint_graytext.setAntiAlias(true);
		paint_graytext.setColor(Color.rgb(189, 189, 189));
		paint_graytext.setStrokeWidth(tb);
		paint_graytext.setTextAlign(Paint.Align.CENTER);
		fontMetrics = paint_graytext.getFontMetricsInt();
		paint_gray = new Paint();
		paint_gray.setColor(Color.rgb(229, 229, 229));
		paint_gray.setStyle(Style.STROKE);
		paint_gray.setStrokeWidth(tb);

		setBackgroundColor(Color.WHITE);

	}

	//
	// public Bitmap getBitmap() {
	// return bitmap;
	// }
	//
	// public void setBitmap(Bitmap bitmap) {
	// this.bitmap = bitmap;
	// }

	public float getTextsize() {
		return textsize;
	}

	public void setTextsize(float textsize, int tb) {
		this.textsize = textsize;
		this.tb = tb;
		paint_black.setTextSize(textsize);
		paint_blue.setTextSize(textsize);
		paint_graytext.setTextSize(textsize);
	}

	public void setPadding_top(int padding_top) {
		this.padding_top = padding_top;
	}

	void drawView(Canvas canvas) {

		int maxwidth = getWidth() - padding_top * 2;
		/**
		 * 求整
		 */
		rectwidth = maxwidth % ver;
		if (rectwidth == 0) {
			rectwidth = maxwidth / ver;
		} else {
			padding_top = padding_top + rectwidth / 2;
			maxwidth = maxwidth - rectwidth;
			rectwidth = maxwidth / ver;
		}
		/**
		 * 求整
		 */
		canvas.drawRect(padding_top, padding_top, padding_top + maxwidth,
				getHeight() - padding_top, paint_gray); // 边框
		for (int j = 1; j < hor; j++) {
			canvas.drawLine(0, rectwidth * j, maxwidth, rectwidth * j,
					paint_gray);
		}

		for (int i = 0; i < ver; i++) {
			if (i != 0) {
				canvas.drawLine(rectwidth * i, rectwidth, rectwidth * i,
						getHeight(), paint_gray);
			}

			for (int j = 0; j < hor; j++) {
				int left = padding_top + i * rectwidth;
				int top = padding_top + j * rectwidth;
				float FontSpace = paint_gray.getFontSpacing();
				if (j == 0) {
					if (tablerect == null) {
						tablerect = new Rect();
					}
					tablerect.set(left, top, rectwidth + left + (int) tb,
							rectwidth + top);
					int baseline = top
							+ (rectwidth - fontMetrics.bottom + fontMetrics.top)
							/ 2 - fontMetrics.top;
					canvas.drawText(Objects[i], left + rectwidth / 2, baseline,
							paint_black);
				} else {
					int baseline = top
							+ (rectwidth - fontMetrics.bottom + fontMetrics.top)
							/ 2 - fontMetrics.top;
					if (!list[j - 1][i].equals("")) {

						canvas.drawText(strings[j - 1], left + rectwidth / 2,
								baseline, paint_blue);
					} else {

						canvas.drawText(strings[j - 1], left + rectwidth / 2,
								baseline, paint_graytext);
					}
				}

			}

		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int measuredHeight = measureHeight(heightMeasureSpec);
		int measuredWidth = measureHeight(widthMeasureSpec);
		if (measuredWidth > measuredHeight) {
			int Width = (measuredHeight - 1) * 7 / 4;
			if (Width > measuredWidth) {
				measuredHeight = measuredWidth * 4 / 7 + 1;
			} else {
				measuredWidth = Width;
			}
		} else {
			measuredHeight = measuredWidth * 4 / 7 + 1;
		}

		super.setMeasuredDimension(measuredWidth, measuredHeight);

	}

	private int measureHeight(int measureSpec) {

		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		// Default size if no limits are specified.

		int result = measureSpec;
		if (specMode == MeasureSpec.AT_MOST) {

			// Calculate the ideal size of your
			// control within this maximum size.
			// If your control fills the available
			// space return the outer bound.
			result = specSize;
		} else if (specMode == MeasureSpec.EXACTLY) {

			// If your control can fit within these bounds return that
			// value.
			result = specSize;
		}
		return result;
	}

	private float mFirstX;
	private float mFirstY;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float lastX = event.getX();
		float lastY = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			mFirstX = lastX;
			mFirstY = lastY;
			int d = padding_top + rectwidth;
			if (mFirstY > d) {
				int x = (int) (mFirstX / d);
				int y = (int) (mFirstY / d);
				y = y - 1;
				if (y < hor - 1 && x < ver) {
//					if (listener != null) {
//						listener.onclick(list[y][x], x, y);
//					}
					// if (list[y][x].equals("")) {
					// // list[y][x] = "-";
					// } else {
					// // list[y][x] = "";
					// }
					// invalidate();
				}
			}

			break;
		case MotionEvent.ACTION_UP:
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
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

	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		super.dispatchDraw(canvas);
	}

}
