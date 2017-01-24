package com.xsw.library.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.support.loader.utils.ImageUtils;
import com.xsw.library.R;


/**
 * 圆形ImageView，可设置最多两个宽度不同且颜色不同的圆形边框。
 *
 * @author Alan
 */
public class RoundanImageView extends ImageView {
    private int mBorderThickness = 4;
    private Context mContext;
    private int defaultColor = 0xFFFFFFFe;
    // 如果只有其中一个有值，则只画一个圆形边框
    private int mBorderOutsideColor = 0xFFFFFFFe;
    private int mBorderInsideColor = 0xFFFFFFFe;
    // 控件默认长、宽
    private int defaultWidth = 0;
    private int defaultHeight = 0;
    String text = "";
    Bitmap roundBitmap;
    Paint paint;
    int startangle = 0;

    public RoundanImageView(Context context) {
        super(context);
        mContext = context;
        mBorderThickness = (int) mContext.getResources().getDimension(
                R.dimen.borderoutside);
    }

    public RoundanImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mBorderThickness = (int) mContext.getResources().getDimension(
                R.dimen.borderoutside);
    }

    public RoundanImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mBorderThickness = (int) mContext.getResources().getDimension(
                R.dimen.borderoutside);
    }

    Paint getPaint() {
        if (paint == null) {
            paint = new Paint();
        /* 去锯齿 */
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);

        /* 设置paint的　style　为STROKE：空心 */
            paint.setStyle(Paint.Style.STROKE);
        }
        return paint;

    }

    public void setmBorderOutsideColor() {
        this.mBorderOutsideColor = 0xFFffffff;
    }

    public void setmBorderInsideColor() {
        this.mBorderInsideColor = 0xFF3b95b0;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        this.measure(0, 0);
        if (drawable.getClass() == NinePatchDrawable.class)
            return;
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//         Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        // if (defaultWidth == 0) {
        defaultWidth = getWidth();

        // }
        // if (defaultHeight == 0) {
        defaultHeight = getHeight();
        // }
        // 保证重新读取图片后不会因为图片大小而改变控件宽、高的大小（针对宽、高为wrap_content布局的imageview，但会导致margin无效）
        // if (defaultWidth != 0 && defaultHeight != 0) {
        // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        // defaultWidth, defaultHeight);
        // setLayoutParams(params);
        // }
        int radius = 0;
        if (mBorderInsideColor != defaultColor
                && mBorderOutsideColor != defaultColor) {// 定义画两个边框，分别为外圆边框和内圆边框
            radius = (defaultWidth < defaultHeight ? defaultWidth
                    : defaultHeight) / 2 - 2 * mBorderThickness;

            // 画内圆
//            drawCircleBorder(0, 360, canvas, mBorderThickness,
//                    mBorderInsideColor);


// 画外圆


            drawCircleBorder(360 - startangle, startangle, canvas,
                    mBorderThickness / 2, mBorderOutsideColor);

            drawCircleBorder(0, 360, canvas,
                    mBorderThickness * 3 / 2, mBorderInsideColor);


        } else if (mBorderInsideColor != defaultColor
                && mBorderOutsideColor == defaultColor) {// 定义画一个边框
//            radius = (defaultWidth < defaultHeight ? defaultWidth
//                    : defaultHeight) / 2 - mBorderThickness;
//            drawCircleBorder(canvas, radius + mBorderThickness / 2,
//                    mBorderInsideColor);
        } else if (mBorderInsideColor == defaultColor
                && mBorderOutsideColor != defaultColor) {// 定义画一个边框
//            radius = (defaultWidth < defaultHeight ? defaultWidth
//                    : defaultHeight) / 2 - mBorderThickness;
//            drawCircleBorder(canvas, radius + mBorderThickness / 2,
//                    mBorderOutsideColor);
        } else {// 没有边框
            radius = (defaultWidth < defaultHeight ? defaultWidth
                    : defaultHeight) / 2;
        }
        if (roundBitmap == null) {
            roundBitmap = ImageUtils.getCroppedRoundBitmap(bitmap, radius, text);
        }

        if (roundBitmap != null) {
            canvas.drawBitmap(roundBitmap, defaultWidth / 2 - radius,
                    defaultHeight / 2 - radius, null);
//            roundBitmap.recycle();

        }
//        if (bitmap != null) {
//            canvas.drawBitmap(bitmap, defaultWidth / 2 - radius,
//                    defaultHeight / 2 - radius, null);
////            roundBitmap.recycle();
//        }

    }


    /**
     * 边缘画圆
     */
    private void drawCircleBorder(int startangle, int endangle, Canvas canvas, int radius, int color) {
        getPaint().setColor(color);
        getPaint().setStrokeWidth(mBorderThickness);
        RectF rectFw = new RectF();
        rectFw.set(radius, radius, defaultWidth - radius, defaultWidth - radius);
        canvas.drawArc(rectFw, startangle, endangle, true, getPaint());
    }

    public void invalidate(int startangle) {
        this.startangle = startangle;
        invalidate();
    }


}
