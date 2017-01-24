package com.library.uiframe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * 阻尼效果的scrollview
 */

public class DampView extends ScrollView {
	// private static final int LEN = 0xc8;
	// private static final int DURATION = 500;
	// private static final int MAX_DY = 200;
	// // int left, top;
	float startX, startY, currentX, currentY;
	ImageView imageView;
	private float imgHeight, imgWidth;
	/** 用于记录拖拉图片移动的坐标位置 */
	// private Matrix matrix = new Matrix();
	/** 用于记录图片要进行拖拉时候的坐标位置 */
	// private Matrix currentMatrix = new Matrix();
	// private Matrix defaultMatrix = new Matrix();
	RelativeLayout bg_view;
	int defaultheight = 0;
	RelativeLayout title_layout;

	public DampView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public DampView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DampView(Context context) {
		super(context);

	}

	@Override
	public int getOverScrollMode() {
		// TODO Auto-generated method stub
		return View.OVER_SCROLL_NEVER;
	}

	public void setTitle_layout(RelativeLayout title_layout) {
		this.title_layout = title_layout;
	}

	public void setImageView(ImageView imageView, RelativeLayout bg_view) {
		this.imageView = imageView;
		this.bg_view = bg_view;
		android.view.ViewGroup.LayoutParams params = bg_view.getLayoutParams();
		defaultheight = params.height;
		// matrix = imageView.getImageMatrix();
		// float scale = (float) params.width / (float) imageView.getWidth();//
		// 1080/1800
		android.view.ViewGroup.LayoutParams relativeLayout = imageView
				.getLayoutParams();
		imgWidth = relativeLayout.width;
		imgHeight = relativeLayout.height;
		// matrix.postScale(scale, scale, 0, 0);
		// imageView.setImageMatrix(matrix);
		// defaultMatrix.set(matrix);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		currentX = event.getX();
		currentY = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			startX = currentX;
			startY = currentY;
			break;
		case MotionEvent.ACTION_MOVE:
		// if (imageView.isShown() && imageView.getTop() >= 0) {
		{
			int t = (int) (currentY - startY) / 3;
			android.view.ViewGroup.LayoutParams params = bg_view
					.getLayoutParams();
			android.view.ViewGroup.LayoutParams relativeLayout = imageView
					.getLayoutParams();

			if (params.height > defaultheight) {
				params.height = defaultheight + t;
				if (params.height >= defaultheight) {
					relativeLayout.height = (int) (imgHeight + t);
					relativeLayout.width = (int) (t + imgWidth);
					// bg_view.setLayoutParams(params);
				} else {
					params.height = defaultheight;
					// bg_view.setLayoutParams(params);
					relativeLayout.height = (int) (imgHeight);
					relativeLayout.width = (int) (imgWidth);
				}
			} else if (params.height == defaultheight & t > 0) {
				params.height = defaultheight + t;
				relativeLayout.height = (int) (imgHeight + t);
				relativeLayout.width = (int) (t + imgWidth);
			}
			bg_view.setLayoutParams(params);
			imageView.setLayoutParams(relativeLayout);
			// float scale = (float) (t + params.height) / (params.height);//
			// 得到缩放倍数
			// android.view.ViewGroup.LayoutParams relativeLayout =
			// (android.view.ViewGroup.LayoutParams) imageView
			// .getLayoutParams();
			// relativeLayout.height = (int) (imgHeight + t);
			// relativeLayout.width = (int) (t + imgWidth);
			// imageView.setLayoutParams(relativeLayout);
			// matrix.postScale(scale, scale, imageView.getX() / 2,
			// 0);
			// imageView.setImageMatrix(matrix);
		}
			break;
		case MotionEvent.ACTION_UP: {
			android.view.ViewGroup.LayoutParams params = bg_view
					.getLayoutParams();
			params.height = defaultheight;
			bg_view.setLayoutParams(params);
			android.view.ViewGroup.LayoutParams relativeLayout = imageView
					.getLayoutParams();
			relativeLayout.height = (int) (imgHeight);
			relativeLayout.width = (int) (imgWidth);
			imageView.setLayoutParams(relativeLayout);
		}

			// scrollerType = true;
			// mScroller.startScroll(0, 0, 0, 100, 500);

			break;
		}

		return super.dispatchTouchEvent(event);
	}

	// public class TouchTool {
	//
	// private int startX, startY;
	//
	// public TouchTool(int startX, int startY, int endX, int endY) {
	// super();
	// this.startX = startX;
	// this.startY = startY;
	// }
	//
	// public int getScrollX(float dx) {
	// int xx = (int) (startX + dx / 2.5F);
	// return xx;
	// }
	//
	// public int getScrollY(float dy) {
	// int yy = (int) (startY + dy / 2.5F);
	// return yy;
	// }
	// }
}
