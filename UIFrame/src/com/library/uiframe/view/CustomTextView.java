package com.library.uiframe.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView {


    // Attributes
    private Paint testPaint;
    private float maxTextSize;

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise();
    }

    private void initialise() {
        testPaint = this.getPaint();
        maxTextSize = this.getTextSize();
    }

    ;


    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        int availableWidth = getWidth() - this.getPaddingLeft()
                - this.getPaddingRight();


        float trySize = getTextSize();

        testPaint.setTextSize(trySize);
        while ((testPaint.measureText(getText().toString()) > availableWidth)) {
            trySize -= 1;
            testPaint.setTextSize(trySize);
        }
//		testPaint.setTextSize(trySize-4);
        super.onDraw(canvas);
    }


}
