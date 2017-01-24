package com.xsw.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * 圆 2015-4-22 @author lzx
 */
public class RadiusRoundImageView extends ImageView {
    // 控件默认长、宽
    private int defaultWidth = 0;
    private int defaultHeight = 0;
    float radius_width = 0;
    Rect src;
    Rect dst;

    public RadiusRoundImageView(Context context) {
        super(context);
    }

    public RadiusRoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                com.xsw.library.R.styleable.RadiusRoundImageView);
        radius_width = a.getDimensionPixelSize(
                com.xsw.library.R.styleable.RadiusRoundImageView_radius_width, 36);
        a.recycle();
    }

    public RadiusRoundImageView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs,
                com.xsw.library.R.styleable.RadiusRoundImageView);
        radius_width = a.getDimensionPixelSize(
                com.xsw.library.R.styleable.RadiusRoundImageView_radius_width, 36);
        a.recycle();
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
        if (bitmap != null) {
            defaultWidth = getWidth();
            defaultHeight = getHeight();

            Bitmap output = getCroppedRoundBitmap(bitmap, defaultWidth);

            canvas.drawBitmap(output, 0, 0, null);
            // bitmaptemp.recycle();
        }

    }

    /**
     * 获取裁剪后的圆形图片
     *
     */
    public Bitmap getCroppedRoundBitmap(Bitmap bmp, int diameter) {
        if (bmp == null) {
            return null;
        }
        // Bitmap scaledSrcBmp;

        // 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        int squareWidth = 0, squareHeight = 0;
        int x = 0, y = 0;
        Bitmap squareBitmap;
        if (bmpHeight > bmpWidth) {// 高大于宽
            squareWidth = squareHeight = bmpWidth;
            x = 0;
            y = (bmpHeight - bmpWidth) / 2;
            // 截取正方形图片
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
                    squareHeight);
        } else if (bmpHeight < bmpWidth) {// 宽大于高
            squareWidth = squareHeight = bmpHeight;
            x = (bmpWidth - bmpHeight) / 2;
            y = 0;
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
                    squareHeight);
        } else {
            squareBitmap = bmp;
        }

        if (squareBitmap.getWidth() != diameter
                || squareBitmap.getHeight() != diameter) {
            bmp = Bitmap.createScaledBitmap(squareBitmap, diameter, diameter,
                    true);

        } else {
            bmp = squareBitmap;
        }
        src = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        dst = new Rect(0, 0, defaultWidth, defaultHeight);
        Bitmap output = Bitmap.createBitmap(defaultWidth, defaultHeight,
                Config.ARGB_8888);
        Canvas canvastemp = new Canvas(output);
        canvastemp.drawBitmap(bmp, src, dst, null);
//		drawCircle(canvastemp, (int) diameter);
        drawRoundAngle(canvastemp, (int) radius_width);
        // bitmap回收(recycle导致在布局文件XML看不到效果)
//		bmp.recycle();
        // squareBitmap.recycle();
        // scaledSrcBmp.recycle();
        // bmp = null;
        // squareBitmap = null;
        // scaledSrcBmp = null;
        return output;
    }

    private void drawRoundAngle(Canvas canvas, int radius) {
        Paint maskPaint = new Paint();
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        Path maskPath = new Path();
        maskPath.addRoundRect(new RectF(0.0F, 0.0F, getWidth(), getHeight()),
                radius, radius, Path.Direction.CW);

        // 这是设置了填充模式，非常关键
        maskPath.setFillType(Path.FillType.INVERSE_WINDING);
        canvas.drawPath(maskPath, maskPaint);
    }

    private void drawCircle(Canvas canvas, int diameter) {
        Paint maskPaint = new Paint();
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        Path maskPath = new Path();
        maskPath.addCircle(diameter / 2, diameter / 2, diameter / 2, Path.Direction.CW);
        // 这是设置了填充模式，非常关键
        maskPath.setFillType(Path.FillType.INVERSE_WINDING);
        canvas.drawPath(maskPath, maskPaint);
    }

    // /**
    // * 边缘画圆
    // */
    // private void drawCircleBorder(Canvas canvas, int radius, int color) {
    // Paint paint = new Paint();
    // /* 去锯齿 */
    // paint.setAntiAlias(true);
    // paint.setFilterBitmap(true);
    // paint.setDither(true);
    // paint.setColor(color);
    // /* 设置paint的　style　为STROKE：空心 */
    // paint.setStyle(Paint.Style.STROKE);
    // /* 设置paint的外框宽度 */
    // paint.setStrokeWidth(2);
    // canvas.drawCircle(defaultWidth / 2, defaultHeight / 2, radius, paint);
    // }

}
