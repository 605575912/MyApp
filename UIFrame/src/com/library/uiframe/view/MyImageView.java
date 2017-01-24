package com.library.uiframe.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 图片缩放 自定义View created by Bear at 2015-1-4 下午3:45:03 TODO
 */
public class MyImageView extends ImageView {
	private Matrix mMatrix;
	private float bWidth;// 图片宽度
	private float bHeight;
	private float bitmapWidth;// 图片宽度
	private float bitmapHeight;

	private int dWidth;// 屏幕宽度
	private int dHeight;// 屏幕高度
	private float xScale;
	// private float yScale;
	float scale;
	ImageState mapState = new ImageState();
	private float oldDist;
	float newDist;
	PointF mStart = new PointF();
	float[] values = new float[9];
	Matrix initMatrix;
	Matrix mSavedMatrix;

	public MyImageView(Context context) {
		super(context);
		this.setScaleType(ScaleType.MATRIX);
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setScaleType(ScaleType.MATRIX);
	}

	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setScaleType(ScaleType.MATRIX);
	}

	public void init(MotionEvent event) {
		if (mSavedMatrix != null) {
			mStart.set(event.getX(), event.getY());
			mSavedMatrix.set(mMatrix);
		}
	}

	float rate = 1.0f;

	// 刷新界面
	public void setView() {
		// rate=rate/10*9;
		// UserUtils.log(TAG, "set view", "set view");
		// mMatrix.postScale(scale,scale,0,0);
		this.setImageMatrix(mMatrix);

		Rect rect = this.getDrawable().getBounds();
		this.getImageMatrix().getValues(values);
		bWidth = rect.width() * values[0];
		bHeight = rect.height() * values[0];

		mapState.left = values[2];
		mapState.top = values[5];
		mapState.right = mapState.left + bWidth;
		mapState.bottom = mapState.top + bHeight;
	}

	// private float s = 0.9f;

	// public void setScale() {
	//
	// float sX = dWidth / 2;
	// float sY = dHeight / 2;
	//
	// mMatrix.postScale(s, s, sX, sY);
	// setView();
	// }

	// @Override
	// public void setImageBitmap(Bitmap bm) {
	// // TODO Auto-generated method stub
	// // mBitmap =bm;
	// super.setImageBitmap(bm);
	// }

	public void setScreenSize(int width, int height, Bitmap bitmap) {
		bWidth = bitmap.getWidth();
		bHeight = bitmap.getHeight();
		bitmapWidth = bWidth;
		bitmapHeight = bHeight;
		setImageBitmap(bitmap);
		if (width <= 0) {
			dWidth = getWidth();
		} else {
			dWidth = width;
		}
		if (height <= 0) {
			dHeight = getHeight();
		} else {
			dHeight = height;
		}

		scale = dWidth / bWidth;
		float yScale = dHeight / bHeight;
		scale = scale > yScale ? yScale : scale;
		// yScale = xScale > yScale ? yScale : xScale;
		// xScale = xScale < 1 ? xScale : 1;

		mMatrix = new Matrix();
		mSavedMatrix = new Matrix();
		// 平移
		mMatrix.postTranslate((dWidth - bWidth) / 2, (dHeight - bHeight) / 2);
		xScale = scale;
		float sX = dWidth / 2;
		float sY = dHeight / 2;
		mSavedMatrix.set(mMatrix);
		mMatrix.postScale(scale, scale, sX, sY);// 前两个是xy变换，后两个是对称轴中心点
		setView();

	}

	/** 计算移动距离 */
	private float spacing(MotionEvent event) {
		float x = 0;
		float y = 0;
		try {
			x = event.getX(0) - event.getX(1);
			y = event.getY(0) - event.getY(1);
			return (float)Math.sqrt(x * x + y * y);
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			return newDist;
		}
	}

	// float backScale;

	// 缩放
	public void zoom(MotionEvent event) {
		newDist = spacing(event);
		if ((mapState.right - mapState.left) > 4 * dWidth && newDist > oldDist)
			return;
		if (newDist > 10f && Math.abs((newDist - oldDist)) > 10f) {
			xScale = newDist / oldDist;
			mMatrix.postScale(xScale, xScale, event.getX(), event.getY());
			// if (xScale < 1) {
			// mMatrix.postScale(xScale, xScale, dWidth / 2, dHeight / 2);
			// } else {
			// mMatrix.postScale(xScale, xScale, dWidth / 2, dHeight / 2);
			// }
			oldDist = newDist;
		}
		setView();
	}

	public void backScale() {
		scale = dWidth / bWidth;
		float yScale = dHeight / bHeight;
		scale = scale > yScale ? yScale : scale;
		mMatrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
		setView();
		float x = (dWidth - bWidth) / 2 - mapState.left;
		float y = (dHeight - bHeight) / 2 - mapState.top;
		mMatrix.postTranslate(x, y);
		this.setImageMatrix(mMatrix);

	}

	public boolean isdrag() {
		float x = mapState.left - (dWidth - bWidth) / 2;
		float y = mapState.top - (dHeight - bHeight) / 2;
		if (Math.abs(x) > 5 || Math.abs(y) > 5) {
			return true;
		}
		return false;
	}

	public boolean isScale() {
		if (bWidth >= bitmapWidth) {
			return true;
		}
		if (bHeight >= bitmapHeight) {
			return true;
		}
		return false;

	}

	/**
	 * @return the oldDist
	 */
	public float getOldDist(MotionEvent event) {
		this.oldDist = this.spacing(event);
		if (oldDist > 10f) {
			mSavedMatrix.set(mMatrix);
		}
		// backScale = oldDist;
		return oldDist;
	}

	public void Scale(MotionEvent event) {

		// scale = backScale / oldDist;
		// if (scale < 1) {
		xScale = (float) (scale + 0.5);
		mMatrix.postScale(xScale, xScale, event.getX(), event.getY());
		// } else {
		//
		// if (mapState.right - mapState.left <= dWidth) {
		// scale = dWidth / (mapState.right - mapState.left);
		// mMatrix.postScale(scale, scale, dWidth / 2, dHeight / 2);
		// float h = (dHeight - (mapState.bottom - mapState.top)) / 2;
		// float w = (dWidth - (mapState.right - mapState.left)) / 2;
		// mMatrix.postTranslate(w - mapState.left, h - mapState.top);
		// }
		// }
		setView();
	}

	public void backDrag() {
		if (mapState.left >= 0 || mapState.right <= dWidth || mapState.top >= 0
				|| mapState.bottom <= dHeight) {
			float h = (dHeight - (mapState.bottom - mapState.top)) / 2;
			float w = (dWidth - (mapState.right - mapState.left)) / 2;
			mMatrix.postTranslate(w - mapState.left, h - mapState.top);
			setView();
		}
	}

	// 拖动
	public void drag(MotionEvent event) {
		if (mMatrix == null) {
			return;
		}
		float x = event.getX() - mStart.x;
		float y = event.getY() - mStart.y;
		if (mapState.left > getWidth() - 100 && x > 0) {
			return;
		}
		if (mapState.right < 100 && x < 0) {
			return;
		}
		if (mapState.bottom < 100 && y < 0) {
			return;
		}
		if (mapState.top > getHeight() - 100 && y > 0) {
			return;
		}

		mMatrix.set(mSavedMatrix);
		xScale = scale - 1;
		if ((mapState.left <= 0 || mapState.right >= dWidth)
				&& (mapState.top <= 0 || mapState.bottom >= dHeight)) {
			mMatrix.postTranslate(x, event.getY() - mStart.y);
		} else if (mapState.top <= 0 || mapState.bottom >= dHeight) {
			mMatrix.postTranslate(0, event.getY() - mStart.y);
		} else if (mapState.left <= 0 || mapState.right >= dWidth) {
			mMatrix.postTranslate(x, event.getY() - mStart.y);
		} else {
			mMatrix.postTranslate(x, event.getY() - mStart.y);
		}
		setView();

	}

	private class ImageState {
		private float left;
		private float top;
		private float right;
		private float bottom;
	}

	public interface OnCloseLister {
		public void onClose();
	}
}
