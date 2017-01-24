package com.library.uiframe.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.widget.TextView;

/**
 * 圆弧计分
 * 
 * @author Administrator
 * 
 */
public class DrawArcView extends TextView {

	private Paint paint_green, paint_black;
	private RectF rectf;
	private float arc_rodn, padding_top;
	private int score = 0;
	private float arc_y = 270;
	Bitmap headBitmap;

	public DrawArcView(Context context) {
		super(context);
		init();
	}

	public void init() {
		setWillNotDraw(false);

		paint_black = new Paint();
		paint_black.setAntiAlias(true);
		paint_black.setColor(Color.BLACK);
		paint_black.setStyle(Style.STROKE);

		paint_green = new Paint();
		paint_green.setAntiAlias(true);
		paint_green.setColor(Color.GREEN);
		paint_green.setStyle(Style.STROKE);

		rectf = new RectF();
		setGravity(Gravity.CENTER);
	}

	public void setPadding_top(float padding_top) {
		this.padding_top = padding_top;
	}

	public void setPcolor(int pcolor) {
		paint_green.setColor(pcolor);
	}

	public void setDcolor(int pcolor) {
		paint_black.setColor(pcolor);
	}

	public float getArc_rodn() {
		return arc_rodn;
	}

	public void setArc_rodn(float arc_rodn) {
		this.arc_rodn = arc_rodn;
		paint_black.setStrokeWidth(arc_rodn);
		paint_green.setStrokeWidth(arc_rodn);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		int w = getWidth();
		int h = getHeight();

		if (h > w) {
			float left = arc_rodn + padding_top;
			float arc = w - left * 2;
			float top = (h - arc) / 2;
			rectf.set(left, top, left + arc, top + arc);
		} else {
			float top = arc_rodn + padding_top;
			float arc = h - top * 2;
			float left = (w - arc) / 2;
			rectf.set(left, top, left + arc, top + arc);
		}
		canvas.drawArc(rectf, 0, 360, false, paint_black);
		canvas.drawArc(rectf, arc_y, (float) (score / 100.0) * 360, false,
				paint_green);

		super.onDraw(canvas);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		super.dispatchDraw(canvas);
	}

	public void setScore(int score) {
		this.score = score;
		String string = score + "%\n好评率";
		SpannableStringBuilder style = new SpannableStringBuilder(string);
		style.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
				string.indexOf("%"), Spanned.SPAN_POINT_MARK);
		setText(style);
	}

}
