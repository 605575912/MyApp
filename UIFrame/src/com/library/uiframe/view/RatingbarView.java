//package com.library.uiframe.view;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Rect;
//import android.graphics.RectF;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//
//import com.nostra13.universalimageloader.ServiceLoader;
//
///**
// * 五角星分数
// *
// * @author apple
// *
// */
//public class RatingbarView extends View {
//
//	private int score = 2;
//	Rect headrect;
//	RectF headrectf;
//	Bitmap star_green, star_gray;
//	float downx = 0;
//	int scoresum = 5;
//	int add = 0;
//	int padding = 40;
//	int paddingLeft = 0;
//	int imagewith;
//
//	public RatingbarView(Context context) {
//		super(context);
//		init();
//	}
//
//	public RatingbarView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		init();
//	}
//
//	public RatingbarView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		init();
//	}
//
//	void init() {
//	}
//
//	//
//	// @Override
//	// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//	// // TODO Auto-generated method stub
//	// int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//	// int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//	//
//	// int height;
//	// if (heightMode == MeasureSpec.EXACTLY) {
//	// height = heightSize;
//	// } else {
//	// height = startBitmap.getHeight();
//	//
//	// if (heightMode == MeasureSpec.AT_MOST) {
//	// height = Math.min(height, heightSize);
//	// }
//	// }
//	//
//	// super.onMeasure(widthMeasureSpec, startBitmap.getHeight());
//	// }
//	@Override
//	protected void dispatchDraw(Canvas canvas) {
//		// TODO Auto-generated method stub
//		drawRationg(canvas);
//		super.dispatchDraw(canvas);
//	}
//
//	void drawRationg(Canvas canvas) {
//		if (star_green != null && star_gray != null) {
//			if (headrectf == null) {
//				headrectf = new RectF();
//				headrect = new Rect();
//				headrect.set(0, 0, star_green.getHeight(),
//						star_green.getHeight());
//			}
//			if (imagewith <= 0) {
//				padding = getPaddingBottom();
//				imagewith = getHeight() - padding * 2;
//				int with = (getWidth() - scoresum * padding - padding)
//						/ scoresum;
//				imagewith = Math.min(with, imagewith);
//				paddingLeft = getWidth() - imagewith * scoresum - padding
//						* scoresum - padding;
//				paddingLeft = paddingLeft / 2;
//			}
//			if (score > 0 && score <= scoresum && downx == 0) {
//				downx = (imagewith + padding) * score - 1;
//			}
//			score = 0;
//			for (int i = 0; i < scoresum; i++) {
//				int left = imagewith * i + padding * i + paddingLeft + padding;
//				headrectf.set(left, padding, left + imagewith, padding
//						+ imagewith);
//				if (downx >= left) {
//					score++;
//					if (downx - left < imagewith) {
//						headrectf.set(left - add, padding - add, left
//								+ imagewith + add, padding + imagewith + add);
//						canvas.drawBitmap(star_green, headrect, headrectf, null);
//					} else {
//						canvas.drawBitmap(star_green, headrect, headrectf, null);
//					}
//
//				} else {
//
//					canvas.drawBitmap(star_gray, headrect, headrectf, null);
//				}
//			}
//		}
//	}
//
//	@Override
//	protected void onDraw(Canvas canvas) {
//		// TODO Auto-generated method stub
//
//		super.onDraw(canvas);
//	}
//
//
//	@SuppressLint("ClickableViewAccessibility")
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			add = 0;
//			downx = event.getX();
//			ServiceLoader.getInstance().submit(runnable);
//			break;
//		case MotionEvent.ACTION_MOVE:
//			// downx = event.getX();
//			// invalidate();
//			break;
//		case MotionEvent.ACTION_UP:
//
//			break;
//
//		default:
//			break;
//		}
//		return super.onTouchEvent(event);
//	}
//
//	public int getScore() {
//		return score;
//	}
//
//	public void setScore(int score) {
//		downx = 0;
//		this.score = score;
//	}
//
//	public int getScoresum() {
//		return scoresum;
//	}
//
//	public void setScoresum(int scoresum) {
//		this.scoresum = scoresum;
//	}
//
//	public void setStar_green(Bitmap star_green) {
//		this.star_green = star_green;
//	}
//
//	public void setStar_gray(Bitmap star_gray) {
//		this.star_gray = star_gray;
//	}
//
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		// TODO Auto-generated method stub
//		int measuredHeight = measureHeight(heightMeasureSpec);
//		int measuredWidth = measureHeight(widthMeasureSpec);
//		super.setMeasuredDimension(measuredWidth, measuredWidth / scoresum);
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
//	Runnable runnable = new Runnable() {
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			add = padding;
//			while (add > 0) {
//				add = add - 5;
//				try {
//					Thread.sleep(60);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				postInvalidate();
//			}
//			if (add != 0) {
//				add = 0;
//				postInvalidate();
//			}
//
//		}
//	};
//}
