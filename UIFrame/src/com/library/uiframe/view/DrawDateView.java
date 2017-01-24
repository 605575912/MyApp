
package com.library.uiframe.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawDateView extends View {
    private Paint paint_green;
    private Paint paint_black;
    private Paint paint_blue;
    private Paint paint_white;
    private Paint paint_gray;
    private Paint paint_gray_fill;
    private Paint paint_green_fill;
    private float tb = 1.0F;
    Bitmap bitmap;
    int ver = 7;
    int hor = 4;
    int bitmapwith = 0;
    Rect headrect;
    Rect tablerect;
    RectF headrectf;
    String[] Objects;
    String[] strings;
    String[][] list;
    int padding_top = 0;
    float textsize = 17.0F;
    int rectwidth = 0;
    private float mFirstX;
    private float mFirstY;

    public DrawDateView(Context context) {
        super(context);
        this.init();
    }

    public DrawDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public DrawDateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init();
    }

    public String[][] getObjects() {
        return this.list;
    }

    public void init() {
        this.setWillNotDraw(false);
        this.list = new String[4][7];
        this.list[0][0] = "";
        this.list[0][1] = "";
        this.list[0][2] = "";
        this.list[0][3] = "";
        this.list[0][4] = "";
        this.list[0][5] = "";
        this.list[0][6] = "";
        this.list[1][0] = "";
        this.list[1][1] = "";
        this.list[1][2] = "";
        this.list[1][3] = "";
        this.list[1][4] = "";
        this.list[1][5] = "";
        this.list[1][6] = "";
        this.list[2][0] = "";
        this.list[2][1] = "";
        this.list[2][2] = "";
        this.list[2][3] = "";
        this.list[2][4] = "";
        this.list[2][5] = "";
        this.list[2][6] = "";
        this.Objects = new String[this.ver];
        this.Objects[0] = "一";
        this.Objects[1] = "二";
        this.Objects[2] = "三";
        this.Objects[3] = "四";
        this.Objects[4] = "五";
        this.Objects[5] = "六";
        this.Objects[6] = "日";
        this.strings = new String[this.hor];
        this.strings[0] = "上";
        this.strings[1] = "下";
        this.strings[2] = "晚";
        this.paint_black = new Paint();
        this.paint_black.setAntiAlias(true);
        this.paint_black.setColor(-16777216);
        this.paint_black.setStrokeWidth(this.tb);
        this.paint_black.setTextAlign(Align.CENTER);
        this.paint_gray = new Paint();
        this.paint_gray.setColor(Color.rgb(233, 233, 233));
        this.paint_gray.setStyle(Style.STROKE);
        this.paint_gray.setStrokeWidth(this.tb);
        this.paint_gray_fill = new Paint();
        this.paint_gray_fill.setColor(Color.rgb(237, 237, 237));
        this.paint_gray_fill.setStyle(Style.FILL);
        this.paint_gray_fill.setStrokeWidth(this.tb);
        this.paint_green_fill = new Paint();
        this.paint_green_fill.setColor(Color.rgb(241, 253, 217));
        this.paint_green_fill.setStyle(Style.FILL);
        this.paint_green_fill.setStrokeWidth(this.tb);
        this.paint_green = new Paint();
        this.paint_green.setStyle(Style.STROKE);
        this.paint_green.setColor(Color.rgb(187, 219, 124));
        this.paint_green.setStrokeWidth(this.tb);
        this.paint_blue = new Paint();
        this.paint_blue.setAntiAlias(true);
        this.paint_blue.setColor(Color.rgb(50, 160, 197));
        this.paint_blue.setStyle(Style.FILL);
        this.paint_blue.setStrokeWidth(this.tb);
        this.paint_blue.setTextAlign(Align.CENTER);
        this.paint_white = new Paint();
        this.paint_white.setAntiAlias(true);
        this.paint_white.setColor(-1);
        this.paint_white.setStrokeWidth(this.tb);
        this.paint_white.setTextAlign(Align.CENTER);
        this.headrectf = new RectF();
        this.setBackgroundColor(-1);
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public float getTextsize() {
        return this.textsize;
    }

    public void setTextsize(float textsize, int tb) {
        this.textsize = textsize;
        this.tb = (float)tb;
        this.paint_white.setTextSize(textsize);
        this.paint_black.setTextSize(textsize);
    }

    public void setPadding_top(int padding_top) {
        this.padding_top = padding_top;
    }

    void drawView(Canvas canvas) {
        if(this.tb == 0.0F) {
            ;
        }

        int maxwidth = this.getWidth() - this.padding_top * 2;
        this.rectwidth = maxwidth % this.ver;
        if(this.rectwidth == 0) {
            this.rectwidth = maxwidth / this.ver;
        } else {
            this.padding_top += this.rectwidth / 2;
            maxwidth -= this.rectwidth;
            this.rectwidth = maxwidth / this.ver;
        }

        if(this.bitmapwith == 0) {
            this.bitmapwith = this.bitmap.getWidth();
        }

        for(int i = 0; i < this.ver; ++i) {
            for(int j = 0; j < this.hor; ++j) {
                int left = this.padding_top + i * this.rectwidth;
                int top = this.padding_top + j * this.rectwidth;
                float FontSpace = this.paint_green.getFontSpacing();
                if(j == 0) {
                    if(this.tablerect == null) {
                        this.tablerect = new Rect();
                    }

                    this.tablerect.set(left, top, this.rectwidth + left + (int)this.tb, this.rectwidth + top);
                    if(i % 2 == 0) {
                        this.paint_blue.setColor(Color.rgb(50, 160, 197));
                    } else {
                        this.paint_blue.setColor(Color.rgb(48, 152, 187));
                    }

                    canvas.drawRect(this.tablerect, this.paint_blue);
                    canvas.drawText(this.Objects[i], (float)(left + this.rectwidth / 2), (float)(top + this.rectwidth / 2) + FontSpace / 2.0F, this.paint_white);
                } else {
                    if(this.headrect == null) {
                        this.headrect = new Rect();
                        if(this.bitmap != null) {
                            this.headrect.set(0, 0, this.bitmap.getWidth(), this.bitmap.getHeight());
                        }
                    }

                    if(!this.list[j - 1][i].equals("")) {
                        canvas.drawRect((float)left, (float)top, (float)(this.rectwidth + left), (float)(this.rectwidth + top), this.paint_green_fill);
                        canvas.drawRect((float)left, (float)top, (float)(this.rectwidth + left), (float)(this.rectwidth + top), this.paint_green);
                        int bitmapleft = this.padding_top + i * this.rectwidth + this.rectwidth / 2 - this.bitmapwith - (int)FontSpace * 2;
                        int bitmaptop = this.padding_top + j * this.rectwidth + this.rectwidth / 2 - this.bitmapwith / 2;
                        this.headrectf.set((float)bitmapleft, (float)bitmaptop, (float)(bitmapleft + this.bitmapwith), (float)(bitmaptop + this.bitmapwith));
                        if(this.bitmap != null) {
                            canvas.drawBitmap(this.bitmap, this.headrect, this.headrectf, (Paint)null);
                        }

                        canvas.drawText(this.strings[j - 1], (float)(left + this.rectwidth / 2), (float)(top + this.rectwidth / 2) + FontSpace, this.paint_black);
                    } else {
                        if(i % 2 != 0) {
                            canvas.drawRect((float)(left + 1), (float)top, (float)(this.rectwidth + left), (float)(this.rectwidth + top), this.paint_gray_fill);
                        }

                        canvas.drawRect((float)(left + 1), (float)top, (float)(this.rectwidth + left), (float)(this.rectwidth + top), this.paint_gray);
                        canvas.drawText(this.strings[j - 1], (float)(left + this.rectwidth / 2), (float)(top + this.rectwidth / 2) + FontSpace, this.paint_black);
                    }
                }
            }
        }

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.measureHeight(heightMeasureSpec);
        int measuredWidth = this.measureWidth(widthMeasureSpec);
        int measuredHeight = measuredWidth * 4 / 7 + 1;
        super.setMeasuredDimension(measuredWidth, measuredHeight);
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 500;
        if(specMode == -2147483648) {
            result = specSize;
        } else if(specMode == 1073741824) {
            result = specSize;
        }

        return result;
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 500;
        if(specMode == -2147483648) {
            result = specSize;
        } else if(specMode == 1073741824) {
            result = specSize;
        }

        return result;
    }

    public boolean onTouchEvent(MotionEvent event) {
        float lastX = event.getX();
        float lastY = event.getY();
        switch(event.getAction()) {
            case 0:
                this.mFirstX = lastX;
                this.mFirstY = lastY;
                int d = this.padding_top + this.rectwidth;
                if(this.mFirstY > (float)d) {
                    int x = (int)(this.mFirstX / (float)d);
                    int y = (int)(this.mFirstY / (float)d);
                    --y;
                    if(y < this.hor - 1 && x < this.ver) {
                        if(this.list[y][x].equals("")) {
                            this.list[y][x] = "-";
                        } else {
                            this.list[y][x] = "";
                        }

                        this.invalidate();
                    }
                }
            case 1:
            default:
                return super.onTouchEvent(event);
        }
    }

    protected void onDraw(Canvas canvas) {
        this.drawView(canvas);
        super.onDraw(canvas);
    }

    public int getPadding_top() {
        return this.padding_top;
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }
}
