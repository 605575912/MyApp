package com.lzx.demoinit;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {
    HeadFrameLayout head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐去电池等图标和一切修饰部分（状态栏部分）
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 隐去标题栏（程序的名字）
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome_layout);
        head = (HeadFrameLayout) findViewById(R.id.head);
        Bitmap headbimtip = BitmapFactory.decodeResource(getResources(), R.drawable.head);
        ImageView imageView = new HeadView(MainActivity.this);
        imageView.setImageBitmap(headbimtip);
        head.addView(getRtextView(MainActivity.this, getItem("撸管冠军")));
        head.addView(getRtextView(MainActivity.this, getItem("游戏")));
        head.addView(getRtextView(MainActivity.this, getItem("主播")));
        head.addView(getRtextView(MainActivity.this, getItem("草类")));
        head.addView(imageView, head.getChildCount() - 1);

    }



    public ItemData getItem(String name) {
        ItemData itemData = new ItemData();
        itemData.setName(name);
        return itemData;
    }

    int strokewidth = 3;
    int textsize = 10;
    int textpadding = 30;

    TextView getRtextView(Context context, final ItemData itemData) {
        final TextView textView = new TextView(context);
        textView.setText(itemData.getName());
        textView.setTextSize(textsize);
        textView.setTextColor(Color.argb(250, 255, (int) (155 * Math.random()), (int) (225 * Math.random())));
        final int size = (int) (textpadding + textView.getPaint().measureText(itemData.getName()));
        final int color = Color.argb(250, (int) (150 * Math.random()), (int) (255 * Math.random()), (int) (225 * Math.random()));
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        Shape shape = new Shape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {
                paint.setColor(color);
                paint.setStrokeWidth(strokewidth);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.STROKE);
                RectF rectF = new RectF(strokewidth, strokewidth, size - strokewidth, size - strokewidth);
                canvas.drawArc(rectF, 0, 360, true, paint);
            }
        };
        shapeDrawable.setShape(shape);
        textView.setBackgroundDrawable(shapeDrawable);
        textView.setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(size, size);
        textView.setLayoutParams(layoutParams);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击＝" + itemData.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        return textView;
    }
}
