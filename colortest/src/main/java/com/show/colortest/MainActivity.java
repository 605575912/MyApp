package com.show.colortest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends Activity {
    final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
    ImageView imageView;
    View v_0, v_1, v_2, v_3, v_4, v_5;
    TextView tv_0, tv_1, tv_2, tv_3, tv_4, tv_5, tv_lint, tv_aph;
    SeekBar seekBar_lint, seekBar_aph;
    LinearLayout s_0, s_1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                imageView.setImageBitmap((Bitmap) msg.obj);
                s_0.setVisibility(View.VISIBLE);
                s_1.setVisibility(View.VISIBLE);
            } else if (msg.what == 1) {
                Toast.makeText(MainActivity.this, "图片读取失败，换一张图片", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        v_0 = findViewById(R.id.v_0);
        v_1 = findViewById(R.id.v_1);
        v_2 = findViewById(R.id.v_2);
        v_3 = findViewById(R.id.v_3);
        v_4 = findViewById(R.id.v_4);
        v_5 = findViewById(R.id.v_5);
        tv_0 = (TextView) findViewById(R.id.tv_0);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_5 = (TextView) findViewById(R.id.tv_5);
        tv_aph = (TextView) findViewById(R.id.tv_aph);
        tv_lint = (TextView) findViewById(R.id.tv_lint);
        s_0 = (LinearLayout) findViewById(R.id.s_0);
        s_1 = (LinearLayout) findViewById(R.id.s_1);
        seekBar_lint = (SeekBar) findViewById(R.id.seekBar_lint);
        seekBar_aph = (SeekBar) findViewById(R.id.seekBar_aph);
        seekBar_lint.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String s = String.format("亮度 +%d", progress);
                tv_lint.setText(s + "%");
                SeekBarLint(v_0, tv_0, progress, "Vibrant");
                SeekBarLint(v_1, tv_1, progress, "DarkVibrant");
                SeekBarLint(v_2, tv_2, progress, "LightVibrant");
                SeekBarLint(v_3, tv_3, progress, "Muted");
                SeekBarLint(v_4, tv_4, progress, "DarkMuted");
                SeekBarLint(v_5, tv_5, progress, "LightMuted");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar_aph.setProgress(100);
        tv_aph.setText("透明 100%");
        seekBar_aph.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                String s = String.format("透明 %d", progress);
                progress = 255 * progress / 100;
                tv_aph.setText(s + "%");
                SeekBarColor(v_0, tv_0, progress, "Vibrant");
                SeekBarColor(v_1, tv_1, progress, "DarkVibrant");
                SeekBarColor(v_2, tv_2, progress, "LightVibrant");
                SeekBarColor(v_3, tv_3, progress, "Muted");
                SeekBarColor(v_4, tv_4, progress, "DarkMuted");
                SeekBarColor(v_5, tv_5, progress, "LightMuted");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    void SeekBarLint(View view, TextView textView, int progress, String name) {
        int color = 0;
        if (view.getTag() != null) {
            color = (int) view.getTag();
        } else {
            return;
        }


        int a = (Color.alpha(color));
        int r = (Color.red(color));
        int g = (Color.green(color));
        int b = (Color.blue(color));
        int rl = 0;
        int gl = 0;
        int bl = 0;

        double scale = progress / 100.0;
        int maxc = Math.max(r, g);
        maxc = Math.max(maxc, b);
        double scaleB = maxc / 255.0;


        rl = (int) (r / scaleB * scale);
        gl = (int) (g / scaleB * scale);
        bl = (int) (b / scaleB * scale);


        int rgp = Color.argb(a, rl, gl, bl);
        view.setBackgroundColor(rgp);
        if (progress < 30) {
            textView.setTextColor(Color.argb(255, 120, 20, (int) (255 * scale)));
        } else if (progress > 80) {
            textView.setTextColor(Color.argb(255, 120, 20, (int) (255 * scale)));
        } else {
            textView.setTextColor(Color.argb(255, 255, 255, 255));
        }
        textView.setText(toHexFromColor(rgp) + "\n" + name);
    }


    void SeekBarColor(View view, TextView textView, int progress, String name) {
        int color = 0;
        if (view.getTag() != null) {
            color = (int) view.getTag();
        } else {
            return;
        }

        int a = progress;
        int r = (Color.red(color));
        int g = (Color.green(color));
        int b = (Color.blue(color));
        int rgp = Color.argb(a, r, g, b);


        view.setBackgroundColor(rgp);
        textView.setText(toHexFromColor(rgp) + "\n" + name);
        view.setTag(rgp);
    }

    void chooseImage() {
        Intent intentChoicePhoto = new Intent(
                "android.intent.action.GET_CONTENT");
        intentChoicePhoto.setType("image/*");
        intentChoicePhoto.addCategory("android.intent.category.OPENABLE");
        if (Build.VERSION.SDK_INT < 19) {
            startActivityForResult(Intent.createChooser(intentChoicePhoto,
                            "\u8BF7\u9009\u62E9\u7167\u7247\u8FDB\u884C\u4E0A\u4F20"),
                    REQUEST_CODE_GETIMAGE_BYSDCARD);
            return;
        }
        startActivityForResult(Intent.createChooser(intentChoicePhoto,
                        "\u8BF7\u9009\u62E9\u7167\u7247\u8FDB\u884C\u4E0A\u4F20"),
                REQUEST_CODE_GETIMAGE_BYSDCARD);
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode,
                                 final Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case REQUEST_CODE_GETIMAGE_BYSDCARD: {
                if (data != null) {

                    Uri thisUri = data.getData();
                    final String filepath = getPath(getApplicationContext(),
                            thisUri);
                    if (filepath == null) {
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = getimage(filepath, 300, 300);
                            if (bitmap != null) {
                                Message msg = handler.obtainMessage(0);
                                msg.obj = bitmap;
                                handler.sendMessage(msg);
                                PaletteColor(bitmap);
                            } else {
                                handler.sendEmptyMessage(1);
                            }
                        }
                    }).start();


                }
            }
            break;
        }
    }

    void PaletteColor(Bitmap bitmap) {
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                // palette为生成的调色板
                Palette.Swatch s1 = palette.getVibrantSwatch(); //充满活力的色板
                Palette.Swatch s2 = palette.getDarkVibrantSwatch(); //充满活力的暗色类型色板
                Palette.Swatch s3 = palette.getLightVibrantSwatch(); //充满活力的亮色类型色板
                Palette.Swatch s4 = palette.getMutedSwatch(); //黯淡的色板
                Palette.Swatch s5 = palette.getDarkMutedSwatch(); //黯淡的暗色类型色板（翻译过来没有原汁原味的赶脚啊！）
                Palette.Swatch s6 = palette.getLightMutedSwatch(); //黯淡的亮色类型色板
                if (s1 != null) {
                    v_0.setTag(s1.getRgb());

                    v_0.setBackgroundColor(s1.getRgb());

                    tv_0.setText(toHexFromColor(s1.getRgb()) + "\nVibrant");
                } else {
                    v_0.setTag(null);
                    v_0.setBackgroundColor(Color.parseColor("#ffeeee"));
                    tv_0.setText("无法获取改颜色" + "\nVibrant");
                }
                if (s2 != null) {
                    v_1.setTag(s2.getRgb());
                    v_1.setBackgroundColor(s2.getRgb());
                    tv_1.setText(toHexFromColor(s2.getRgb()) + "\nDarkVibrant");
                } else {
                    v_1.setTag(null);
                    v_1.setBackgroundColor(Color.parseColor("#ffeeee"));
                    tv_1.setText("无法获取改颜色" + "\nDarkVibrant");
                }
                if (s3 != null) {
                    v_2.setTag(s3.getRgb());
                    v_2.setBackgroundColor(s3.getRgb());
                    tv_2.setText(toHexFromColor(s3.getRgb()) + "\nLightVibrant");
                } else {
                    v_2.setTag(null);
                    v_2.setBackgroundColor(Color.parseColor("#ffeeee"));
                    tv_2.setText("无法获取改颜色" + "\nLightVibrant");
                }
                if (s4 != null) {
                    v_3.setTag(s4.getRgb());
                    v_3.setBackgroundColor(s4.getRgb());
                    tv_3.setText(toHexFromColor(s4.getRgb()) + "\nMuted");
                } else {
                    v_3.setBackgroundColor(Color.parseColor("#ffeeee"));
                    v_3.setTag(null);
                    tv_3.setText("无法获取改颜色" + "\nMuted");
                }
                if (s5 != null) {
                    v_4.setTag(s5.getRgb());
                    v_4.setBackgroundColor(s5.getRgb());
                    tv_4.setText(toHexFromColor(s5.getRgb()) + "\nDarkMuted");
                } else {
                    v_4.setBackgroundColor(Color.parseColor("#ffeeee"));
                    v_4.setTag(null);
                    tv_4.setText("无法获取改颜色" + "\nDarkMuted");
                }
                if (s6 != null) {
                    v_5.setTag(s6.getRgb());
                    v_5.setBackgroundColor(s6.getRgb());
                    tv_5.setText(toHexFromColor(s6.getRgb()) + "\nLightMuted");
                } else {
                    v_5.setBackgroundColor(Color.parseColor("#ffeeee"));
                    v_5.setTag(null);
                    tv_5.setText("无法获取改颜色" + "\nLightMuted");
                }


            }

        });
    }

    private static String toHexFromColor(int color) {
        String a, r, g, b;

        StringBuilder su = new StringBuilder();
        a = Integer.toHexString(Color.alpha(color));
        r = Integer.toHexString(Color.red(color));
        g = Integer.toHexString(Color.green(color));
        b = Integer.toHexString(Color.blue(color));
        a = a.length() == 1 ? "0" + a : a;
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        a = a.toUpperCase();
        r = r.toUpperCase();
        g = g.toUpperCase();
        b = b.toUpperCase();
        su.append("#");
        su.append(a);
        su.append(r);
        su.append(g);
        su.append(b);
        //0xFF0000FF
        return su.toString();
    }

    /**
     * 从本地获取广告图片
     *
     * @param path
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getimage(String path, int width, int height) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        try {
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            newOpts.inJustDecodeBounds = false;
            newOpts.inSampleSize = 1;
            Bitmap tempbitmap;
            try {
                tempbitmap = BitmapFactory.decodeFile(path, newOpts);
                return tempbitmap;
            } catch (OutOfMemoryError e) {
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            newOpts.inJustDecodeBounds = true;
            tempbitmap = BitmapFactory.decodeFile(path, newOpts);
            int sreen = width * height;
            int image = tempbitmap.getHeight() * tempbitmap.getWidth();
            if (sreen <= 720) {// 防止过小图
                sreen = 720 * 1080;
            }
            int samplesize = image / sreen;
            if (samplesize < 2) {
                samplesize = 2;
            }
            newOpts.inSampleSize = samplesize;
            newOpts.inJustDecodeBounds = false;
            try {
                tempbitmap = BitmapFactory.decodeFile(path, newOpts);
                return tempbitmap;
            } catch (OutOfMemoryError e) {

            } catch (Exception e) {
                e.printStackTrace();


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
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

}
