//package com.library.uiframe.view;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.Config;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.Paint.FontMetricsInt;
//import android.graphics.Paint.Style;
//import android.os.Handler;
//import android.os.Looper;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 圆弧计分
// *
// * @author Administrator
// *
// */
//public class MinuteView extends View {
//
//	private Paint paint_gray, paint_LTGRAY, paint_black, paint_gray_text;
//	FontMetricsInt fontMetrics;
//	int rectheight = 0;
//	String hour = "分";
//	float disy = 0;
//
//	List<String> list = new ArrayList<String>();
//	List<Float> floats;
//	Matrix matrix;
//	int index = 0;
//	int line = 3;
//	float baseline;
//	float textbaseline;
//	int currentItem = 0;
//	boolean isgetvalue = false;
//
//	public MinuteView(Context context) {
//		super(context);
//		init(context);
//	}
//
//	public String getCurrentItem() {
//		isgetvalue = true;
//		isstop = true;
//		if (currentItem >= list.size()) {
//			currentItem = 0;
//		}
//		return list.get(currentItem);
//	}
//
//	public MinuteView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		init(context);
//	}
//
//	public MinuteView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		init(context);
//	}
//
//	public void init(Context context) {
//		setWillNotDraw(false);
//		paint_LTGRAY = new Paint();
//		paint_LTGRAY.setAntiAlias(true);
//		paint_LTGRAY.setColor(Color.LTGRAY);
//		paint_LTGRAY.setStrokeWidth(2);
//		paint_LTGRAY.setTextAlign(Paint.Align.CENTER);
//		paint_black = new Paint();
//		paint_black.setAntiAlias(true);
//		paint_black.setColor(Color.BLACK);
//		paint_black.setStrokeWidth(2);
//		paint_black.setTextAlign(Paint.Align.CENTER);
//
//		paint_gray_text = new Paint();
//		paint_gray_text.setAntiAlias(true);
//		paint_gray_text.setColor(Color.GRAY);
//		paint_gray_text.setStrokeWidth(1);
//		paint_gray_text.setTextAlign(Paint.Align.CENTER);
//
//		paint_gray = new Paint();
//		paint_gray.setColor(Color.rgb(229, 229, 229));
//		paint_gray.setStyle(Style.STROKE);
//		paint_gray.setStrokeWidth(1);
//		for (int i = 0; i < 60; i++) {
//			if (i < 10) {
//				list.add("0" + i);
//			} else {
//				list.add("" + i);
//			}
//		}
//
//	}
//
//	public List<String> getList() {
//		return list;
//	}
//
//	public void setList(List<String> list) {
//		this.list = list;
//	}
//
//	public void notifyDataSetChanged() {
//		invalidate();
//	}
//
//	public void setTextSize(float size) {
//		paint_black.setTextSize(size);
//		paint_LTGRAY.setTextSize(size);
//		paint_gray_text.setTextSize(size * 2 / 3);
//		fontMetrics = paint_black.getFontMetricsInt();
//		baseline = (float) (fontMetrics.descent - fontMetrics.ascent) / 3;
//		fontMetrics = paint_gray_text.getFontMetricsInt();
//		textbaseline = (float) (fontMetrics.descent - fontMetrics.ascent) / 3;
//	}
//
//	void drawView(Canvas canvas) {
//		canvas.drawColor(Color.WHITE);
//		canvas.drawLine(0, rectheight * line, getWidth(), rectheight * line,
//				paint_gray);
//		if (floats == null) {
//			floats = new ArrayList<Float>();
//			for (int i = 0; i < line + 1; i++) {
//
//				floats.add((float) (-0.5 * rectheight));
//			}
//
//			// Matrix matrix = new Matrix();
//			// sCanvas.setMatrix(matrix);
//		}
//		if (list.isEmpty()) {
//			list.add("01");
//		}
//		// float y = index;
//		// sumy = sumy + index;
//		for (int i = 0; i < line; i++) {
//
//			if (i == 0) {
//				canfirt(i, canvas);
//			} else if (i == 1) {
//				canmid(i, canvas);
//			} else if (i == 2) {
//				canend(i, canvas);
//			} else {
//			}
//
//			// matrix.reset();
//
//			canvas.drawLine(0, rectheight * i, getWidth(), rectheight * i + 1,
//					paint_gray);
//		}
//
//		// matrix.reset();
//		// Matrix matrix = new Matrix();
//
//	}
//
//	void canfirt(int i, Canvas canvas) {
//		Bitmap canvasbitmap = Bitmap.createBitmap(getWidth(), rectheight,
//				Config.ARGB_4444);
//		Canvas sCanvas = new Canvas(canvasbitmap);
//		float y0 = floats.get(i);
//		y0 = disy + y0;
//		if (y0 >= -rectheight / 2 && y0 <= rectheight / 2) {
//			// y0 = y0 ;
//		} else if (y0 < -rectheight / 2) {
//			y0 = rectheight / 2;
//			index++;
//			index = index % (list.size());
//		} else {
//			index--;
//			if (index < 0) {
//				index = list.size() - 1;
//			}
//
//			y0 = -rectheight / 2;
//		}
//		floats.set(i, y0);
//
//		matrix = new Matrix();
//
//		int e = (index + 1) % list.size();
//		y0 = (float) (rectheight + y0);
//
//		sCanvas.drawText(list.get(e), getPaddingLeft(), y0 + baseline,
//				paint_LTGRAY);
//		y0 = (float) (y0 - rectheight);
//		float scroe = y0 / rectheight / 2;
//		matrix.postScale(1f, scroe + 0.6f, getPaddingLeft(), y0 + baseline);
//		matrix.postSkew(0f, 0.2f, getPaddingLeft(), (y0 + baseline));
//		if (y0 != rectheight / 2) {
//			sCanvas.setMatrix(matrix);
//		}
//		e = (index) % list.size();
//		sCanvas.drawText(list.get(e), getPaddingLeft(), (int) (y0 + baseline),
//				paint_LTGRAY);
//
//		canvas.drawBitmap(canvasbitmap,
//				getWidth() / 2 - canvasbitmap.getWidth() / 2, (int) rectheight
//						* i + 1, null);
//
//	}
//
//	void canmid(int i, Canvas canvas) {
//		Bitmap canvasbitmap = Bitmap.createBitmap(getWidth(), rectheight,
//				Config.ARGB_4444);
//		Canvas sCanvas = new Canvas(canvasbitmap);
//		// sCanvas.setMatrix(matrix);
//		// sCanvas.drawColor(Color.LTGRAY);
//		float y0 = floats.get(i);
//		y0 = disy + y0;
//
//		if (y0 >= -rectheight / 2 && y0 <= rectheight / 2) {
//			// y0 = y0 ;
//		} else if (y0 < -rectheight / 2) {
//			y0 = rectheight / 2 + 1;
//		} else {
//			y0 = -rectheight / 2;
//		}
//		floats.set(i, y0);
//
//		int e = (index + 1) % list.size();
//		if (y0 < rectheight / 2) {
//			currentItem = (index + 2) % list.size();
//		} else {
//			currentItem = e;
//		}
//		sCanvas.drawText(list.get(e), getPaddingLeft(), (int) (y0 + baseline),
//				paint_black);
//		y0 = (float) (rectheight + y0);
//		e = (index + 2) % list.size();
//
//		sCanvas.drawText(list.get(e), getPaddingLeft(), y0 + baseline,
//				paint_black);
//		sCanvas.drawText(hour, getPaddingLeft() * 2f, rectheight / 2
//				+ textbaseline, paint_gray_text);
//		canvas.drawBitmap(canvasbitmap,
//				getWidth() / 2 - canvasbitmap.getWidth() / 2, (int) rectheight
//						* i + 1, null);
//	}
//
//	void canend(int i, Canvas canvas) {
//		Bitmap canvasbitmap = Bitmap.createBitmap(getWidth(), rectheight,
//				Config.ARGB_4444);
//		Canvas sCanvas = new Canvas(canvasbitmap);
//
//		// sCanvas.drawColor(Color.LTGRAY);
//		float y0 = floats.get(i);
//		y0 = disy + y0;
//
//		if (y0 >= -rectheight / 2 && y0 <= rectheight / 2) {
//			// y0 = y0 ;
//		} else if (y0 < -rectheight / 2) {
//			y0 = rectheight / 2 + 1;
//		} else {
//			y0 = -rectheight / 2;
//		}
//		floats.set(i, y0);
//		int e = (index + 2) % list.size();
//		matrix = new Matrix();
//		// matrix.postSkew(0.0f, 0.2f, getWidth() - getPaddingRight(),
//		// (y0 + +baseline / 2));
//		sCanvas.drawText(list.get(e), getPaddingLeft(), (int) (y0 + +baseline),
//				paint_LTGRAY);
//
//		y0 = (float) (rectheight + y0);
//
//		e = (index + 3) % list.size();
//		float scroe = 1 - y0 / rectheight;
//		matrix.setScale(1f, scroe + 0.6f, getPaddingLeft(), y0 + +baseline / 2);
//		matrix.postSkew(0.1f, 0f, getPaddingLeft(), (y0 + +baseline));
//		if (y0 != rectheight / 2) {
//			sCanvas.setMatrix(matrix);
//		}
//
//		sCanvas.drawText(list.get(e), getPaddingLeft(), y0 + baseline,
//				paint_LTGRAY);
//
//		canvas.drawBitmap(canvasbitmap,
//				getWidth() / 2 - canvasbitmap.getWidth() / 2, (int) rectheight
//						* i + 1, null);
//	}
//
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		// TODO Auto-generated method stub
//		int measuredHeight = measureHeight(heightMeasureSpec);
//		int measuredWidth = measureHeight(widthMeasureSpec);
//		rectheight = (measuredHeight - 1) / line;
//		setTextSize(rectheight / 2);
//		super.setMeasuredDimension(measuredWidth, measuredHeight);
//	}
//
//	@Override
//	protected void onDraw(Canvas canvas) {
//		// TODO Auto-generated method stub
//		drawView(canvas);
//		super.onDraw(canvas);
//	}
//
//	private float lastY;
//	long time;
//	Handler Handler = new Handler(Looper.getMainLooper()) {
//		public void handleMessage(android.os.Message msg) {
//			new Thread((runnable)).start();
////			ServiceLoader.getInstance().submit(runnable);
//		};
//	};
//	Runnable runnable = new Runnable() {
//		public void run() {
//			set(disy);
//			float y = floats.get(0);
//			if (y < 0) {
//				y = -y - rectheight / 2;
//			} else {
//				y = rectheight / 2 - y;
//			}
//			if (y <= 0) {
//				while (y < 0 && !isstop) {
//					y = y + 2;
//					disy = -2;
//					try {
//						Thread.sleep(25);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					postInvalidate();
//				}
//			} else {
//				while (y > 0 && !isstop) {
//					y = y - 2;
//					disy = 2;
//					try {
//						Thread.sleep(25);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					postInvalidate();
//				}
//			}
//			disy = 0;
//			y = floats.get(0);
//			if (y <= 0 || isgetvalue) {
//				floats.set(0, (float) -rectheight / 2);
//				floats.set(1, (float) -rectheight / 2);
//				floats.set(2, (float) -rectheight / 2);
//			} else {
//				floats.set(0, (float) rectheight / 2);
//				floats.set(1, (float) rectheight / 2);
//				floats.set(2, (float) rectheight / 2);
//			}
//			postInvalidate();
//			isgetvalue = false;
//		}
//	};
//
//	void set(float i) {
//		if (i < 0) {
//			while (i <= 0 && !isstop) {
//				i = i + 1;
//				if (i < -50) {
//					disy = -20;
//				} else if (i < -20) {
//					disy = -8;
//				} else {
//					disy = -2;
//				}
//				try {
//					Thread.sleep(25);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				postInvalidate();
//			}
//		} else {
//			while (i >= 0 && !isstop) {
//				i = i - 1;
//				if (i > 50) {
//					disy = 20;
//				} else if (i > 20) {
//					disy = 8;
//				} else {
//					disy = 4;
//				}
//				try {
//					Thread.sleep(25);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				postInvalidate();
//
//			}
//		}
//	}
//
//	boolean isstop = false;
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			isstop = true;
//			lastY = event.getY();
//			return true;
//		case MotionEvent.ACTION_UP: {
//			// if (System.currentTimeMillis() - time < 30) {
//			// isstop = false;
//			// Handler.sendEmptyMessage(0);
//			// } else {
//			isstop = false;
//			Handler.sendEmptyMessage(1);
//			// }
//
//		}
//			break;
//		case MotionEvent.ACTION_MOVE: {
//			float y = event.getY();
//
//			disy = y - lastY;
//			if (Math.abs(disy) > 20) {
//				time = System.currentTimeMillis();
//			}
//			lastY = y;
//			invalidate();
//		}
//			break;
//
//		default:
//			break;
//		}
//		return super.onTouchEvent(event);
//	}
//
//	private int measureHeight(int measureSpec) {
//
//		int specMode = MeasureSpec.getMode(measureSpec);
//		int specSize = MeasureSpec.getSize(measureSpec);
//
//		// Default size if no limits are specified.
//
//		int result = measureSpec;
//		if (specMode == MeasureSpec.AT_MOST) {
//
//			// Calculate the ideal size of your
//			// control within this maximum size.
//			// If your control fills the available
//			// space return the outer bound.
//			result = specSize;
//		} else if (specMode == MeasureSpec.EXACTLY) {
//
//			// If your control can fit within these bounds return that
//			// value.
//			result = specSize;
//		}
//		return result;
//	}
//
//}
