package com.support.loader.packet;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.support.loader.utils.ImageLoadingListener;

import java.util.Map;

public class DisplayBitmapTask implements Runnable {

    private final Bitmap bitmap;

    private final View view;
    private final String memoryCacheKey;
    Map<Integer, String> cacheKeysForImageViews;
    ImageLoadingListener imageLoadingListener;

    public DisplayBitmapTask(Bitmap bitmap, View view, String memoryCacheKey,
                             Map<Integer, String> cacheKeysForImageViews, ImageLoadingListener imageLoadingListener) {
        this.bitmap = bitmap;
        this.view = view;
        this.memoryCacheKey = memoryCacheKey;
        this.cacheKeysForImageViews = cacheKeysForImageViews;
        this.imageLoadingListener = imageLoadingListener;

    }

    public void run() {
        String currentCacheKey = cacheKeysForImageViews.get(view.hashCode());
        if (memoryCacheKey.equals(currentCacheKey)) {

            setImageBimap(view, bitmap);
            if (imageLoadingListener != null) {
                imageLoadingListener.onLoadingComplete("ShowMessage", view, bitmap);
            }
            cacheKeysForImageViews.remove(view.hashCode());
        }
    }

    public static void setImageBimap(View view, Bitmap bitmap) {
        setImageBimap(view, bitmap, true);

    }

    public static void setImageBimap(View view, final Bitmap bitmap, boolean isanimate) {
        if (isanimate) {
            animate(view, 1500);
        }
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            imageView.setImageBitmap(bitmap);
        } else {
            Drawable bd = new Drawable() {
                @Override
                public void draw(Canvas canvas) {
                    Rect src = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
                    Rect dst = new Rect(0,0,canvas.getWidth(),canvas.getHeight());
                    canvas.drawBitmap(bitmap,src,dst,new Paint());
                }

                @Override
                public void setAlpha(int alpha) {

                }

                @Override
                public void setColorFilter(ColorFilter cf) {

                }

                @Override
                public int getOpacity() {
                    return 0;
                }
            };
            view.setBackgroundDrawable(bd);
        }
    }

    /**
     * Animates {@link ImageView} with "fade-in" effect
     *
     * @param View           {@link View} which display image in
     * @param durationMillis The length of the animation in milliseconds
     */
    static void animate(View View, int durationMillis) {
        AlphaAnimation fadeImage = new AlphaAnimation(0.2f, 1);

        fadeImage.setDuration(durationMillis);
        fadeImage.setInterpolator(new DecelerateInterpolator());
        View.startAnimation(fadeImage);
    }

}
