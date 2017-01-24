package com.support.loader.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.support.loader.proguard.IProguard;

import junit.framework.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片操作工具包
 */
public class ImageUtils implements IProguard {
    static int hashcode = 0;
    /**
     * 请求相册
     */
    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
    /**
     * 请求相机
     */
    public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;

    /**
     * 写图片文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
     *
     * @throws IOException
     */
    public static void saveImage(Context context, String fileName, Bitmap bitmap)
            throws IOException {
        saveImage(context, fileName, bitmap, 100);
    }

    public static void saveImage(Context context, String fileName,
                                 Bitmap bitmap, int quality) throws IOException {
        if (bitmap == null)
            return;

        FileOutputStream fos = context.openFileOutput(fileName,
                Context.MODE_PRIVATE);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, quality, stream);
        byte[] bytes = stream.toByteArray();
        fos.write(bytes);
        fos.close();
    }

    public static ImageSize defineTargetSizeForView(View view,
                                                    int maxImageWidth, int maxImageHeight) {
        if (view == null) {
            return new ImageSize(400, 400);
        }
        final DisplayMetrics displayMetrics = view.getContext().getResources()
                .getDisplayMetrics();

        final LayoutParams params = view.getLayoutParams();
        int width = 0; // Get actual image width

        // if (width <= 0) width = getImageViewFieldValue(imageView,
        // "mMaxWidth"); // Check maxWidth parameter
        if (width <= 0)
            width = maxImageWidth;
        if (width <= 0)
            width = params.width == LayoutParams.WRAP_CONTENT ? 0 : view
                    .getWidth(); // Get layout width parameter
        if (width <= 0)
            width = displayMetrics.widthPixels;

        int height = 0; // Get actual image height

        // if (height <= 0) height = getImageViewFieldValue(imageView,
        // "mMaxHeight"); // Check maxHeight parameter
        if (height <= 0)
            height = maxImageHeight;
        if (height <= 0)
            height = params.height == LayoutParams.WRAP_CONTENT ? 0 : view
                    .getHeight(); // Get layout height parameter
        if (height <= 0)
            height = displayMetrics.heightPixels;

        return new ImageSize(width, height);
    }

    public static int computeImageSampleSize(ImageSize srcSize,
                                             ImageSize targetSize, ViewScaleType viewScaleType,
                                             boolean powerOf2Scale) {
        int srcWidth = srcSize.getWidth();
        int srcHeight = srcSize.getHeight();
        int targetWidth = targetSize.getWidth();
        int targetHeight = targetSize.getHeight();

        int scale = 1;

        int widthScale = srcWidth / targetWidth;
        int heightScale = srcHeight / targetHeight;

        switch (viewScaleType) {
            case FIT_INSIDE:
                if (powerOf2Scale) {
                    while (srcWidth / 2 >= targetWidth
                            || srcHeight / 2 >= targetHeight) { // ||
                        srcWidth /= 2;
                        srcHeight /= 2;
                        scale *= 2;
                    }
                } else {
                    scale = Math.max(widthScale, heightScale); // max
                }
                break;
            case CROP:
                if (powerOf2Scale) {
                    while (srcWidth / 2 >= targetWidth
                            && srcHeight / 2 >= targetHeight) { // &&
                        srcWidth /= 2;
                        srcHeight /= 2;
                        scale *= 2;
                    }
                } else {
                    scale = Math.min(widthScale, heightScale); // min
                }
                break;
        }

        if (scale < 1) {
            scale = 1;
        }

        return scale;
    }

    /**
     * 写图片文件到SD卡
     *
     * @throws IOException
     */
    public static void saveImageToSD(String filePath, Bitmap bitmap, int quality)
            throws IOException {
        if (bitmap != null) {
            FileOutputStream fos = new FileOutputStream(filePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, quality, stream);
            byte[] bytes = stream.toByteArray();
            fos.write(bytes);
            fos.close();
        }
    }

    /**
     * 获取照相机使用的目录
     *
     * @return
     */
    public static String getCamerPath() {
        return Environment.getExternalStorageDirectory() + File.separator
                + "FounderNews" + File.separator;
    }

    /**
     * 通过uri获取文件的绝对路径
     *
     * @param uri
     * @return
     */
    public static String getAbsoluteImagePath(Activity context, Uri uri) {
        String imagePath = "";
        String[] proj = {MediaColumns.DATA};
        Cursor cursor = context.managedQuery(uri, proj, // Which columns to
                // return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                imagePath = cursor.getString(column_index);
            }
        }

        return imagePath;
    }

    @TargetApi(19)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= 19;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    /**
     * 计算缩放图片的宽高
     *
     * @param img_size
     * @param square_size
     * @return
     */
    public static int[] scaleImageSize(int[] img_size, int square_size) {
        if (img_size[0] <= square_size && img_size[1] <= square_size)
            return img_size;
        double ratio = square_size
                / (double) Math.max(img_size[0], img_size[1]);
        return new int[]{(int) (img_size[0] * ratio),
                (int) (img_size[1] * ratio)};
    }

    /**
     * 创建缩略图
     *
     * @param thumbfilePath  输出缩略图路径
     * @param square_size    输出图片宽度
     * @param quality        输出图片质量
     * @throws IOException
     */
    public static Bitmap createImage(Bitmap cur_bitmap, String bigfilePath,
                                     String thumbfilePath, int big_size, int square_size, int quality)
            throws IOException {
        // 原始图片bitmap
        // Bitmap cur_bitmap = createImageThumbnail(largeImagePath);

        if (cur_bitmap == null)
            return null;
        // square_size = Math.min(square_size, cur_bitmap.getWidth());
        // square_size = Math.min(square_size, cur_bitmap.getHeight());
        // 生成缩放后的bitmap
        Bitmap big_bitmap = zoomBitmap(cur_bitmap, big_size, big_size);
        Bitmap thb_bitmap = zoomBitmap(cur_bitmap, square_size, square_size);
        // 生成缩放后的图片文件
        // Bitmap thb_bitmap = Bitmap.createBitmap(cur_bitmap);
        saveImageToSD(thumbfilePath, thb_bitmap, quality);
        saveImageToSD(bigfilePath, big_bitmap, quality);
        return thb_bitmap;
    }

    /**
     * 创建缩略图
     *

     * @throws IOException
     */
    public static Bitmap createImage(Bitmap cur_bitmap, String bigfilePath,
                                     String thumbfilePath, int big_size) throws IOException {
        // 原始图片bitmap
        // Bitmap cur_bitmap = createImageThumbnail(largeImagePath);

        if (cur_bitmap == null)
            return null;
        // square_size = Math.min(square_size, cur_bitmap.getWidth());
        // square_size = Math.min(square_size, cur_bitmap.getHeight());
        // 生成缩放后的bitmap
        Bitmap big_bitmap = zoomBitmap(cur_bitmap, big_size, big_size);
        // 生成缩放后的图片文件
        // Bitmap thb_bitmap = Bitmap.createBitmap(cur_bitmap);
        saveImageToSD(bigfilePath, big_bitmap, 1);
        return big_bitmap;
    }

    /**
     * 图片压缩为100KB created by Bear at 2015-1-6 上午10:30:21 TODO
     */
    public static Bitmap compressImage(Bitmap image, int kb) {
        if (image == null) {
            return null;
        }
        int options = 100;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, options, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中

        while (baos.toByteArray().length / 1024 > kb) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 根据路径读取 一定高宽的图片 created by Bear at 2015-1-6 上午10:29:46 TODO
     */
    public static Bitmap getimage(View view, String srcPath, int kb) {
        return getimage(view, srcPath, 0, 0, kb);
    }

    /**
     * 根据路径读取 一定高宽的图片 created by Bear at 2015-1-6 上午10:29:46 TODO
     */
    public static Bitmap getimage(View view, String srcPath) {
        return getimage(view, srcPath, 0, 0, 0);
    }

    /**
     * 根据路径读取 一定高宽的图片 created by Bear at 2015-1-6 上午10:29:46 TODO
     */
    public static Bitmap getimage(View view, String srcPath, int ih, int iw) {
        return getimage(view, srcPath, ih, iw, 0);
    }

    /**
     * 根据路径读取 一定高宽的图片 created by Bear at 2015-1-6 上午10:29:46 TODO
     */
    public static Bitmap getimage(View view, String srcPath, int ih, int iw,
                                  int kb) {
        if (srcPath.equals("")) {
            return null;
        }
        File file = new File(srcPath);
        if (!file.exists()) {
            return null;
        }
        ImageSize targetSize = ImageUtils.defineTargetSizeForView(view, iw, ih);
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        int scale = 1;
        ImageSize imageSize = new ImageSize(newOpts.outWidth, newOpts.outHeight);
        scale = ImageUtils.computeImageSampleSize(imageSize, targetSize,
                ViewScaleType.FIT_INSIDE, false);
        int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;
        // while (options.outHeight * options.outWidth / options.inSampleSize >
        // MAX_DECODE_PICTURE_SIZE) {
        // options.inSampleSize++;
        // }
        newOpts.inSampleSize = scale;
        newOpts.inJustDecodeBounds = false;
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
            if (bitmap == null && hashcode == 0 && view != null) {
                hashcode = view.hashCode();
                bitmap = getimage(view, srcPath, 600, 600, kb);
            }
        } catch (OutOfMemoryError e) {
            // TODO: handle exception
            hashcode = 0;
            return null;
        }
        hashcode = 0;
        if (bitmap == null) {
            return null;
        }
//        if (kb <= 0) {
            return bitmap;
//        } else {
//            return compressImage(bitmap, kb);// 压缩好比例大小后再进行质量压缩
//        }

    }
    public static int calculateInSampleSize_(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            long totalPixels = width / inSampleSize * height / inSampleSize;

            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
        return inSampleSize;
    }
    /**
     * 根据路径读取 一定高宽的图片 created by Bear at 2015-1-6 上午10:29:46 TODO
     */
    public static Bitmap getimage(Resources res, int id, int ih, int iw) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeResource(res, id, newOpts);

        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        int be = 1;// be=1表示不缩放

        // Matrix matrix = new Matrix();
        // float scaleWidht = ((float) iw / w);
        // float scaleHeight = ((float) ih / h);
        // matrix.postScale(scaleWidht, scaleHeight);
        // bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix,
        // true);

        if (w * h > ih * iw) {
            be = (w * h) / (ih * iw);
            if (w > h && w > iw) {// 如果宽度大的话根据宽度固定大小缩放
                be = newOpts.outWidth / iw;
            } else if (h > w && h > ih) {// 如果高度高的话根据宽度固定大小缩放
                be = newOpts.outHeight / iw;

            }
            if (be <= 0){
                be = 1;
            }

        }

        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
//         newOpts.inSampleSize = computeSampleSize(newOpts, -1, ih * iw);
        newOpts.inJustDecodeBounds = false;
        int i = 5;
        while (i > 0 & bitmap == null) {

            try {

                    bitmap = BitmapFactory.decodeResource(res, id, newOpts);
                } catch (Exception e) {
                    e.printStackTrace();
                    bitmap = null;
                }
            newOpts.inSampleSize = be++;// 设置缩放比例
            i--;
        }

        return bitmap;// 压缩好比例大小后再进行质量压缩
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 根据文件路径获取Bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapPath(String filePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);
        opts.inSampleSize = computeSampleSize(1);
        opts.inJustDecodeBounds = false;
        try {
            bitmap = BitmapFactory.decodeFile(filePath, opts);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bitmap;
    }

    public static BitmapFactory.Options getBitmapOptions(String filePath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);
        opts.inJustDecodeBounds = false;
        return opts;
    }

    public static int computeSampleSize(float quality) {
        int initialSize = (quality == 1) ? 1 : (int) Math.ceil(Math
                .sqrt(1 / quality));
//         int initialSize = computeInitialSampleSize(options, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    /**
     * 返回Bitmap的大小
     *
     * @param bitmap
     * @return
     */
    public static int getBitmapSize(Bitmap bitmap) {
        int size = 0;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        size = bytes.length;
        return size;
    }

    /**
     * 放大缩小图片
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, float w, float h) {
        Bitmap newbmp = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            float scaleWidht = (w / width);
            float scaleHeight = (h / height);
            matrix.postScale(scaleWidht, scaleHeight);
            try {
                newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                        matrix, true);
            } catch (OutOfMemoryError e) {
                // TODO: handle exception
            }

        }
        return newbmp;
    }

    /**
     * (缩放)重绘图片
     *
     * @param context Activity
     * @param bitmap
     * @return
     */
    public static Bitmap reDrawBitMap(Activity context, Bitmap bitmap) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int rHeight = dm.heightPixels;
        int rWidth = dm.widthPixels;
        // float rHeight=dm.heightPixels/dm.density+0.5f;
        // float rWidth=dm.widthPixels/dm.density+0.5f;
        // int height=bitmap.getScaledHeight(dm);
        // int width = bitmap.getScaledWidth(dm);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        float zoomScale;
        /** 方式1 **/
        // if(rWidth/rHeight>width/height){//以高为准
        // zoomScale=((float) rHeight) / height;
        // }else{
        // //if(rWidth/rHeight<width/height)//以宽为准
        // zoomScale=((float) rWidth) / width;
        // }
        /** 方式2 **/
        // if(width*1.5 >= height) {//以宽为准
        // if(width >= rWidth)
        // zoomScale = ((float) rWidth) / width;
        // else
        // zoomScale = 1.0f;
        // }else {//以高为准
        // if(height >= rHeight)
        // zoomScale = ((float) rHeight) / height;
        // else
        // zoomScale = 1.0f;
        // }
        /** 方式3 **/
        if (width >= rWidth)
            zoomScale = ((float) rWidth) / width;
        else
            zoomScale = 1.0f;
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(zoomScale, zoomScale);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 获取图片类型
     *
     * @param file
     * @return
     */
    public static String getImageType(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            String type = getImageType(in);
            return type;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * detect bytes's image type by inputstream
     *
     * @param in
     * @return
     * @see #getImageType(byte[])
     */
    public static String getImageType(InputStream in) {
        if (in == null) {
            return null;
        }
        try {
            byte[] bytes = new byte[8];
            in.read(bytes);
            return getImageType(bytes);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * detect bytes's image type
     *
     * @param bytes 2~8 byte at beginning of the image file
     * @return image mimetype or null if the file is not image
     */
    public static String getImageType(byte[] bytes) {
        if (isJPEG(bytes)) {
            return "image/jpeg";
        }
        if (isGIF(bytes)) {
            return "image/gif";
        }
        if (isPNG(bytes)) {
            return "image/png";
        }
        if (isBMP(bytes)) {
            return "application/x-bmp";
        }
        return null;
    }

    private static boolean isJPEG(byte[] b) {
        if (b.length < 2) {
            return false;
        }
        return (b[0] == (byte) 0xFF) && (b[1] == (byte) 0xD8);
    }

    private static boolean isGIF(byte[] b) {
        if (b.length < 6) {
            return false;
        }
        return b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8'
                && (b[4] == '7' || b[4] == '9') && b[5] == 'a';
    }

    private static boolean isPNG(byte[] b) {
        if (b.length < 8) {
            return false;
        }
        return (b[0] == (byte) 137 && b[1] == (byte) 80 && b[2] == (byte) 78
                && b[3] == (byte) 71 && b[4] == (byte) 13 && b[5] == (byte) 10
                && b[6] == (byte) 26 && b[7] == (byte) 10);
    }

    private static boolean isBMP(byte[] b) {
        if (b.length < 2) {
            return false;
        }
        return (b[0] == 0x42) && (b[1] == 0x4d);
    }

    /**
     * 按比例缩放bitmap
     *
     * @param bm
     *            原bitmap
     * @param newWidth
     *            新的宽度
     * @param newHeight
     *            新的高度
     * @return 缩放后的bitmap
     */
    // public static Bitmap zoomImg(Bitmap bm, float newWidth, float newHeight)
    // {
    // if (newHeight == 0 || newWidth == 0) {
    // return bm;
    // }
    // // float scale = 0;
    // // 获得图片的宽高
    // int width = bm.getWidth();
    // int height = bm.getHeight();
    // // 计算缩放比例，优先判断高度
    // // if (newHeight > 0) {
    // // scale = ((float) newHeight) / height;
    // // newWidth = (int) (scale * width);
    // //
    // // } else {
    // // scale = ((float) newWidth) / width;
    // // newHeight = (int) (scale * height);
    // // }
    // float scalewith = newWidth / (float) (width);
    // float scaleheight = newHeight / (float) (height);
    // float scale = Math.max(scalewith, scaleheight);
    // // 取得想要缩放的matrix参数
    // Matrix matrix = new Matrix();
    // matrix.postScale(scale, scale);
    // // 得到新的图片
    // try {
    // Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
    // true);
    // return newbm;
    // } catch (OutOfMemoryError e) {
    // // TODO: handle exception
    // return null;
    // }
    // }

    /**
     * 获取裁剪后的圆形图片
     *
     * @param radius 半径
     */
    public static Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
        if (bmp == null) {
            return null;
        }
        // Bitmap scaledSrcBmp;
        int diameter = radius * 2;

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
        Bitmap output = Bitmap.createBitmap(diameter, diameter,
                Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, diameter, diameter);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bmp, rect, rect, paint);
        // bitmap回收(recycle导致在布局文件XML看不到效果)
        // squareBitmap.recycle();
        // scaledSrcBmp.recycle();
        // bmp = null;
        // squareBitmap = null;
        // scaledSrcBmp = null;
        return output;
    }

    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

    public static Bitmap extractThumbNail(final String path, final int height,
                                          final int width, final boolean crop) {
        Assert.assertTrue(path != null && !path.equals("") && height > 0
                && width > 0);

        BitmapFactory.Options options = new BitmapFactory.Options();

        try {
            options.inJustDecodeBounds = true;
            Bitmap tmp = BitmapFactory.decodeFile(path, options);
            if (tmp != null) {
                tmp.recycle();
                tmp = null;
            }

            Log.d("TAG", "extractThumbNail: round=" + width + "x" + height
                    + ", crop=" + crop);
            final double beY = options.outHeight * 1.0 / height;
            final double beX = options.outWidth * 1.0 / width;
            Log.d("TAG", "extractThumbNail: extract beX = " + beX + ", beY = "
                    + beY);
            options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY)
                    : (beY < beX ? beX : beY));
            if (options.inSampleSize <= 1) {
                options.inSampleSize = 1;
            }

            // NOTE: out of memory error
            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++;
            }

            int newHeight = height;
            int newWidth = width;
            if (crop) {
                if (beY > beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            } else {
                if (beY < beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            }

            options.inJustDecodeBounds = false;

            Bitmap bm = BitmapFactory.decodeFile(path, options);
            if (bm == null) {
                return null;
            }

            final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth,
                    newHeight, true);
            if (scale != null) {
                bm.recycle();
                bm = scale;
            }

            if (crop) {
                final Bitmap cropped = Bitmap.createBitmap(bm,
                        (bm.getWidth() - width) >> 1,
                        (bm.getHeight() - height) >> 1, width, height);
                if (cropped == null) {
                    return bm;
                }

                bm.recycle();
                bm = cropped;
            }
            return bm;

        } catch (final OutOfMemoryError e) {
            Log.e("TAG", "decode bitmap failed: " + e.getMessage());
            options = null;
        }

        return null;
    }

    /**
     * 获取裁剪后的圆形图片
     *
     * @param radius 半径
     */
    public static Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius,
                                               String text) {
        if (bmp == null) {
            return null;
        }
//         Bitmap scaledSrcBmp;
        int diameter = radius * 2;

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
//            squareBitmap.recycle();

        } else {
            bmp = squareBitmap;
        }

        Bitmap output = Bitmap.createBitmap(diameter, diameter,
                Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        // Paint paint = new Paint();
        Rect rect = new Rect(0, 0, diameter, diameter);
        //
        // paint.setAntiAlias(true);
        // paint.setFilterBitmap(true);
        // paint.setDither(true);
        // canvas.drawARGB(0, 0, 0, 0);
        // canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, paint);
        // paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        canvas.drawBitmap(bmp, rect, rect, null);

        if (!text.equals("")) {
            Rect targetRect = new Rect(0, diameter * 3 / 4, diameter, diameter);
            Paint paintt = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintt.setTextSize(diameter * 1 / 7);
            paintt.setColor(Color.rgb(199, 199, 199));
            FontMetricsInt fontMetrics = paintt.getFontMetricsInt();
            int baseline = targetRect.top
                    + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top)
                    / 2 - fontMetrics.top;
            // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
            paintt.setTextAlign(Paint.Align.CENTER);
            Paint bgpaint = new Paint();
            bgpaint.setColor(Color.rgb(45, 45, 45));
            bgpaint.setAlpha(180);
            canvas.drawRect(targetRect, bgpaint);
            canvas.drawText(text, targetRect.centerX(), baseline, paintt);
        }
        Paint maskPaint = new Paint();
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        Path maskPath = new Path();
        maskPath.addCircle(diameter / 2, diameter / 2, diameter / 2,
                Path.Direction.CW);
        // 这是设置了填充模式，非常关键
        maskPath.setFillType(Path.FillType.INVERSE_WINDING);
        canvas.drawPath(maskPath, maskPaint);
        // bitmap回收(recycle导致在布局文件XML看不到效果)
//         squareBitmap.recycle();
//        bmp.recycle();
//         bmp = null;
//         squareBitmap = null;
//         scaledSrcBmp = null;
        return output;
    }

    /**
     * 圆 2015-4-27 @author lzx
     */
    private void drawCircle(Canvas canvas, int diameter) {
        Paint maskPaint = new Paint();
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        Path maskPath = new Path();
        maskPath.addCircle(diameter / 2, diameter / 2, diameter / 2,
                Path.Direction.CW);
        // 这是设置了填充模式，非常关键
        maskPath.setFillType(Path.FillType.INVERSE_WINDING);
        canvas.drawPath(maskPath, maskPaint);
    }

    // 微信需要对图片进行处理，否则微信会在log中输出thumbData检查错误
    public static byte[] getBitmapBytes(Bitmap bitmap, boolean paramBoolean) {
        Bitmap localBitmap = Bitmap.createBitmap(80, 80, Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);
        int i;
        int j;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            i = bitmap.getWidth();
            j = bitmap.getWidth();
        } else {
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
        while (true) {
            localCanvas.drawBitmap(bitmap, new Rect(0, 0, i, j), new Rect(0, 0,
                    80, 80), null);
            if (paramBoolean)
                bitmap.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(CompressFormat.JPEG, 100,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
            }
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
    }

    /**
     * 计算inSampleSize，用于压缩图片
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        // 源图片的宽度
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;

        if (width > reqWidth && height > reqHeight) {
            // 计算出实际宽度和目标宽度的比率
            int widthRatio = Math.round((float) width / (float) reqWidth);
            int heightRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = Math.max(widthRatio, heightRatio);
        }
        return inSampleSize;
    }
}
