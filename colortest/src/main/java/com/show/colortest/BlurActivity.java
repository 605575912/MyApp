package com.show.colortest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by liangzhenxiong on 16/3/15.
 */
public class BlurActivity extends Activity {
    final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
    ImageView imageView2, imageView3;
    SeekBar seekBar_, alphaBar_;
    TextView tv_seek, tv_alpha, textView2;
    int x = 0;
    int y = 0;
    int w = 0;
    int h = 0;
    CheckBox rb_0, rb_1, rb_2, rb_3, rb_4;
    EditText et_w, et_h;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                final Bitmap bitmap = (Bitmap) msg.obj;
                imageView2.setImageBitmap(bitmap);

                imageView2.setDrawingCacheEnabled(true);

                w = bitmap.getWidth() / 2;
                h = bitmap.getHeight() / 2;
                x = bitmap.getWidth() / 2;
                y = bitmap.getHeight() / 2;
                textView2.setText("目标模糊区域大小   最长" + bitmap.getWidth() + "最高" + bitmap.getHeight());
                setBulr(bitmap, 1, 255, x, y, w, h);


            } else if (msg.what == 1) {
                Toast.makeText(BlurActivity.this, "图片读取失败，换一张图片", Toast.LENGTH_SHORT).show();
            }
        }
    };

    void setBulr(Bitmap bitmap, int radius, int alpha, int x, int y, int w, int h) {
        if (bitmap == null) {
            return;
        }
        if (radius <= 0) {
            Toast.makeText(BlurActivity.this, "模糊半径不能为0", Toast.LENGTH_SHORT).show();

            return;
        }
        if (w <= 0 || h <= 0) {
            w = bitmap.getWidth();
            h = bitmap.getHeight();
        }
        x = x - w / 2;
        y = y - h / 2;

        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (x + w > bitmap.getWidth()) {
            x = bitmap.getWidth() - w;
        }
        if (y + h > bitmap.getHeight()) {
            y = bitmap.getHeight() - h;
        }

//        Bitmap bitmap1 = blur(bitmap, radius, x, y, w, h);

        Bitmap temp = Bitmap.createBitmap(bitmap, x, y, w, h);
        Bitmap overlay = Bitmap.createBitmap((temp.getWidth()),
                (temp.getHeight()), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        Paint paint = new Paint();
        float scaleFactor = 1.0f;
        canvas.scale(scaleFactor, scaleFactor);
        canvas.translate(-0, -0);
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(temp, 0, 0, paint);
        Bitmap overlay1 = FastBlur.doBlur(overlay, radius, false);
        overlay.recycle();
        temp.recycle();

//        Bitmap overlay_db = Bitmap.createBitmap((overlay1.getWidth()),
//                (overlay1.getHeight()), Bitmap.Config.ARGB_8888);
//        Canvas canvas_db = new Canvas(overlay_db);
//                                Paint paint_db = new Paint();
//        float scaleFactor = 1.0f;
//        canvas_db.scale(scaleFactor, scaleFactor);
//        canvas_db.translate(-0, -0);
//                                paint_db.setFlags(Paint.FILTER_BITMAP_FLAG);

//        paint.setAlpha(alpha);   //
        {
            int[] argb = new int[overlay1.getWidth() * overlay1.getHeight()];

            overlay1.getPixels(argb, 0, overlay1.getWidth(), 0, 0, overlay1

                    .getWidth(), overlay1.getHeight());// 获得图片的ARGB值

            alpha = alpha << 24;
            for (int i = 0; i < argb.length; i++) {

                if (argb[i] != 0) {
                    argb[i] = alpha| (argb[i] & 0x00FFFFFF);
                }

            }

            overlay1 = Bitmap.createBitmap(argb, overlay1.getWidth(), overlay1

                    .getHeight(), Bitmap.Config.ARGB_8888);
        }
//        canvas_db.drawBitmap(overlay1, 0, 0, paint);

        imageView3.setImageBitmap(overlay1);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.blur_layout);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(REQUEST_CODE_GETIMAGE_BYSDCARD);
            }
        });
        imageView2.setDrawingCacheEnabled(true);
        textView2 = (TextView) findViewById(R.id.textView2);
        tv_alpha = (TextView) findViewById(R.id.tv_alpha);
        tv_seek = (TextView) findViewById(R.id.tv_seek);
        alphaBar_ = (SeekBar) findViewById(R.id.alphaBar_);
        seekBar_ = (SeekBar) findViewById(R.id.seekBar_);
        seekBar_.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                tv_seek.setText("模糊度 " + progress);
//                pro();
                setBulr(imageView2.getDrawingCache(), progress, alphaBar_.getProgress(), x, y, w, h);
            }
        });
        alphaBar_.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                tv_alpha.setText("透明度 " + progress);
//                pro();
                setBulr(imageView2.getDrawingCache(), seekBar_.getProgress(), progress, x, y, w, h);
            }
        });
        rb_0 = (CheckBox) findViewById(R.id.rb_0);
        rb_0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check(rb_0);
            }
        });
        rb_1 = (CheckBox) findViewById(R.id.rb_1);
        rb_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check(rb_1);
            }
        });
        rb_2 = (CheckBox) findViewById(R.id.rb_2);
        rb_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check(rb_2);
            }
        });
        rb_3 = (CheckBox) findViewById(R.id.rb_3);
        rb_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check(rb_3);
            }
        });
        rb_4 = (CheckBox) findViewById(R.id.rb_4);
        rb_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check(rb_4);
            }
        });
        et_w = (EditText) findViewById(R.id.et_w);
        et_h = (EditText) findViewById(R.id.et_h);
        et_w.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int actionId,
                                          KeyEvent arg2) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(BlurActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    pro();
                }
                return false;
            }
        });
        et_h.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int actionId,
                                          KeyEvent arg2) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(BlurActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    pro();

                }
                return false;
            }
        });

        imageView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        x = (int) motionEvent.getX();
                        y = (int) motionEvent.getY();
                        return true;
                    }
                    case MotionEvent.ACTION_MOVE: {

                    }
                    break;
                    case MotionEvent.ACTION_UP: {
                        pro();
                    }
                    break;

                }
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.pic);
                if (bitmap != null) {
                    Message msg = handler.obtainMessage(0);
                    msg.obj = bitmap;
                    handler.sendMessage(msg);

//                                PaletteColor(bitmap);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    CheckBox checkBox_temp;

    void check(CheckBox checkBox) {
        if (checkBox_temp != null) {
            if (checkBox != checkBox_temp) {
                checkBox_temp.setChecked(!checkBox.isChecked());
            }
        }
        checkBox_temp = checkBox;
        final Bitmap bitmap = imageView2.getDrawingCache();
        if (bitmap == null) {
            return;
        }
        if (checkBox.isChecked()) {
            w = bitmap.getWidth() / 2;
            h = bitmap.getHeight() / 2;
            switch (checkBox.getId()) {
                case R.id.rb_0:
                    x = bitmap.getWidth() / 4;
                    y = bitmap.getHeight() / 4;
                    break;
                case R.id.rb_1:
                    x = bitmap.getWidth() * 3 / 4;
                    y = bitmap.getHeight() / 2;
                    break;
                case R.id.rb_2:
                    x = bitmap.getWidth() / 4;
                    y = bitmap.getHeight() * 3 / 4;
                    break;
                case R.id.rb_3:
                    x = bitmap.getWidth() * 3 / 4;
                    y = bitmap.getHeight() * 3 / 4;
                    break;
                case R.id.rb_4:

                    x = bitmap.getWidth() / 2;
                    y = bitmap.getHeight() / 2;
                    break;
            }
        } else {
            x = bitmap.getWidth() / 2;
            y = bitmap.getHeight() / 2;
            String ew = et_w.getText().toString();
            String eh = et_h.getText().toString();
            if (!TextUtils.isEmpty(ew) || !TextUtils.isEmpty(eh)) {
                w = Integer.valueOf(ew);
                h = Integer.valueOf(eh);
            } else {
                w = bitmap.getWidth();
                h = bitmap.getHeight();
            }
        }


//        if (!isedit) {
//            w = bitmap.getWidth() / 2;
//            h = bitmap.getHeight() / 2;
//            if (R.id.rb_4 == checkBox.getId()) {
//                x = bitmap.getWidth() / 4;
//                y = bitmap.getHeight() / 4;
//            }
//        } else {
//            if (R.id.rb_4 == checkBox.getId()) {
//                x = bitmap.getWidth() / 2 - w / 2;
//                y = bitmap.getHeight() / 2 - h / 2;
//            }
//        }

        setBulr(bitmap, seekBar_.getProgress(), alphaBar_.getProgress(), x, y, w, h);
    }

    void pro() {
        final Bitmap bitmap = imageView2.getDrawingCache();
        if (bitmap != null) {
            String ew = et_w.getText().toString();
            String eh = et_h.getText().toString();
            int iew = 0;
            int ieh = 0;
            if (TextUtils.isEmpty(ew) || TextUtils.isEmpty(eh)) {

            } else {
                iew = Integer.valueOf(ew);
                ieh = Integer.valueOf(eh);
            }

            if (iew > bitmap.getWidth()) {
                Toast.makeText(BlurActivity.this, "长度超过图片", Toast.LENGTH_SHORT).show();
                return;
            }
            if (ieh > bitmap.getWidth()) {
                Toast.makeText(BlurActivity.this, "高度度超过图片", Toast.LENGTH_SHORT).show();
                return;
            }
            w = iew;
            h = iew;

            setBulr(bitmap, seekBar_.getProgress(), alphaBar_.getProgress(), x, y, w, h);
        }
    }


    void chooseImage(int tag) {
        Intent intentChoicePhoto = new Intent(
                "android.intent.action.GET_CONTENT");
        intentChoicePhoto.setType("image/*");
        intentChoicePhoto.addCategory("android.intent.category.OPENABLE");
        if (Build.VERSION.SDK_INT < 19) {
            startActivityForResult(Intent.createChooser(intentChoicePhoto,
                            "\u8BF7\u9009\u62E9\u7167\u7247\u8FDB\u884C\u4E0A\u4F20"),
                    tag);
            return;
        }
        startActivityForResult(Intent.createChooser(intentChoicePhoto,
                        "\u8BF7\u9009\u62E9\u7167\u7247\u8FDB\u884C\u4E0A\u4F20"),
                tag);
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
                    final String filepath = MainActivity.getPath(getApplicationContext(),
                            thisUri);
                    if (filepath == null) {
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = MainActivity.getimage(filepath, 300, 300);
                            if (bitmap != null) {
                                Message msg = handler.obtainMessage(0);
                                msg.obj = bitmap;
                                handler.sendMessage(msg);

//                                PaletteColor(bitmap);
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
}
