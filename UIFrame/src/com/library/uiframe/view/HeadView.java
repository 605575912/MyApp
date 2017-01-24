package com.library.uiframe.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class HeadView extends View {
	Paint paint, paint_black, paintText_black;
	private float arc_rodn, padding_top = -1, padding_bottom = -1;
	private int score = 0, radius = 0;
	private RectF rectf;
	float arc;
	Bitmap loading_icon;
	Rect rect, rectbitmap;
	RectF rectimageF;
	int baseline = -1;

	public HeadView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public HeadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HeadView(Context context) {
		super(context);
		init();
	}

	void init() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		paintText_black = new Paint();
		paintText_black.setAntiAlias(true);
		paintText_black.setColor(Color.BLACK);
		paintText_black.setStyle(Style.STROKE);
		paintText_black.setTextAlign(Align.CENTER);
		paintText_black.setTextSize(25f);
		paint_black = new Paint();
		paint_black.setAntiAlias(true);
		paint_black.setColor(Color.BLACK);
		paint_black.setStyle(Style.STROKE);
		paint_black.setAlpha(120);
		score = 0;
		setArc_rodn(10);
	}

	public Bitmap getLoading_icon() {
		return loading_icon;
	}

	public void setLoading_icon(Bitmap loading_icon) {
		this.loading_icon = loading_icon;
	}

	public float getPadding_top() {
		if (padding_top < 0) {
			padding_top = getHeight() / 4;
		}
		return padding_top;
	}

	public float getPadding_bottom() {
		if (padding_bottom < 0) {
			padding_bottom = getHeight() - padding_top - getARC_height();
		}
		return padding_bottom;
	}

	public void setArc_rodn(float arc_rodn) {
		this.arc_rodn = arc_rodn;
		paint_black.setStrokeWidth(arc_rodn);
		paint.setStrokeWidth(arc_rodn);
	}

	public void setPadding_top(float padding_top) {

		this.padding_top = padding_top;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public float getARC_height() {
		if (rectf != null) {
			return rectf.height();
		}
		return 0;
	}

	boolean refresh = false;

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (isRefresh()&&loading_icon!=null) {
			if (rect == null) {
				rect = new Rect(0, 0, loading_icon.getWidth(),
						loading_icon.getHeight());
				rectimageF = new RectF();
				rectbitmap = new Rect(0, 0, (int) arc, (int) arc);
				rectimageF.set(getWidth() / 2 - arc / 2, (int) padding_top,
						getWidth() / 2 + arc / 2, (int) padding_top + arc);
			}

			Bitmap output = Bitmap.createBitmap((int) arc, (int) arc,
					Config.ARGB_8888);
			Canvas canvas2 = new Canvas(output);
			Matrix matrix = new Matrix();
			matrix.postRotate(radius, canvas2.getWidth() / 2,
					canvas2.getHeight() / 2);
			canvas2.setMatrix(matrix);
			canvas2.drawBitmap(loading_icon, rect, rectbitmap, paint_black);
			canvas.drawBitmap(output, rectbitmap, rectf, paint_black);
			if (baseline < 0) {
				Rect targetRect = new Rect((int) rectf.left,
						(int) rectf.bottom, (int) rectf.width(),
						(int) (rectf.bottom + getPadding_bottom()));
				FontMetricsInt fontMetrics = paintText_black
						.getFontMetricsInt();
				baseline = targetRect.top
						+ (targetRect.bottom - targetRect.top
								- fontMetrics.bottom + fontMetrics.top) / 2
						- fontMetrics.top;
			}

			canvas.drawText("正在刷新...", getWidth() / 2, baseline,
					paintText_black);
		} else {
			int w = getWidth();
			if (padding_top < 0) {
				padding_top = getHeight() / 4;
			}
			if (rectf == null) {
				rectf = new RectF();
				if (getHeight() > w) {
					float left = arc_rodn + padding_top;
					arc = w - left * 2;
					float top = (getHeight() - arc) / 2;
					rectf.set(left, top, left + arc, top + arc);
				} else {
					float top = arc_rodn + padding_top;
					arc = getHeight() * 1 / 3;
					float left = (w - arc) / 2;
					rectf.set(left, top, left + arc, top + arc);
				}
			}
			float i = (float) (score / 100.0) * 360;
			canvas.drawArc(rectf, 90 + i, 360 - i, false, paint_black);
			canvas.drawArc(rectf, 90, i, false, paint);
			if (baseline < 0) {
				Rect targetRect = new Rect((int) rectf.left,
						(int) rectf.bottom, (int) rectf.width(),
						(int) (rectf.bottom + getPadding_bottom()));
				FontMetricsInt fontMetrics = paintText_black
						.getFontMetricsInt();
				baseline = targetRect.top
						+ (targetRect.bottom - targetRect.top
								- fontMetrics.bottom + fontMetrics.top) / 2
						- fontMetrics.top;
			}
			canvas.drawText("下拉刷新", getWidth() / 2, baseline, paintText_black);
		}

		super.onDraw(canvas);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			invalidate();
			radius = radius + 10;
			if (radius > 350) {
				radius = 0;
			}
		};
	};

	public void setrefresh(boolean refresh) {
		this.refresh = refresh;
		if (isRefresh()) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (isRefresh()) {
						try {
							Thread.sleep(70);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						handler.sendEmptyMessage(0);
					}

				}

			}).start();
		}

	}

	public boolean isRefresh() {
		return refresh;
	}

	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}

}
