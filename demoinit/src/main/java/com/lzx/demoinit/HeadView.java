package com.lzx.demoinit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
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
 * Created by liangzhenxiong on 16/1/7.
 */
public class HeadView extends ImageView {
    public HeadView(Context context) {
        super(context);
    }

    public HeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Bitmap temp;

    void drawHead(Canvas canvas, Bitmap bitmap, RectF rectF, int with, int height, int radio) {
        if (temp == null) {
            temp = DrawHeadBitmap(bitmap, with, height, radio);
        }
        Rect rect = new Rect(0, 0, temp.getWidth(), temp.getHeight());
        canvas.drawBitmap(temp, rect, rectF, null);
    }

    Bitmap DrawHeadBitmap(Bitmap bitmap, int defaultWidth, int defaultHeight, int radius_width) {
        Bitmap output = Bitmap.createBitmap(defaultWidth, defaultHeight,
                Bitmap.Config.ARGB_8888);
        Canvas canvastemp = new Canvas(output);
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        float scale;
        float dx = 0, dy = 0;

        if (bitmap.getWidth() * defaultHeight > defaultWidth * bitmap.getHeight()) {
            scale = (float) defaultHeight / (float) bitmap.getHeight();
            dx = (defaultWidth - bitmap.getWidth() * scale) * 0.5f;
        } else {
            scale = (float) defaultWidth / (float) bitmap.getWidth();
            dy = (defaultHeight - bitmap.getHeight() * scale) * 0.5f;
        }
        matrix.setScale(scale, scale);
        matrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));
        canvastemp.drawBitmap(bitmap, matrix, null);
        drawRoundAngle(canvastemp, radius_width);
        return output;

    }

    private void drawRoundAngle(Canvas canvas, int radius) {
        Paint maskPaint = new Paint();
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        Path maskPath = new Path();
        maskPath.addRoundRect(new RectF(0.0F, 0.0F, canvas.getWidth(), canvas.getHeight()),
                radius, radius, Path.Direction.CW);
        maskPath.setFillType(Path.FillType.INVERSE_WINDING);
        canvas.drawPath(maskPath, maskPaint);
    }

    RectF getRectF(int radio) {
        int left = (getWidth() >> 1) - radio;
        int top = (getHeight() >> 1) - radio;
        int right = left + (radio << 1);
        int bottom = top + (radio << 1);
        RectF rectF = new RectF(left, top, right, bottom);
        return rectF;
    }

    int headWith = 150;

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            if (getWidth() == 0 || getHeight() == 0) {
                return;
            }
            this.measure(0, 0);
            if (drawable.getClass() == NinePatchDrawable.class)
                return;

            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null) {
                RectF rectF = getRectF(headWith >> 1);
                drawHead(canvas, bitmap, rectF, headWith, headWith, headWith >> 1);
            }
        } else {
            super.onDraw(canvas);
        }
    }
}
