package com.ticket.validation.terminal.custom;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.widget.TextView;

public class ReflectTextView extends TextView {

    private Matrix mMatrix;
    private Paint mPaint;

    public ReflectTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mMatrix = new Matrix();
        mMatrix.preScale(1, -1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), (int)(getMeasuredHeight()*1.67));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        setDrawingCacheEnabled(true);
        Bitmap originalImage = Bitmap.createBitmap(getDrawingCache());
        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height/5, width, height/2, mMatrix, false);
        canvas.drawBitmap(reflectionImage, 0, height/3f, null);
        if(mPaint == null)  {
            mPaint = new Paint();
            LinearGradient shader = new LinearGradient(0, height/2, 0,
                    height, 0x7fffffff, 0x0fffffff, TileMode.CLAMP);
            mPaint.setShader(shader);
            mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        }
        canvas.drawRect(0, height/2f, width, height, mPaint);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start,
                                 int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        buildDrawingCache();
        postInvalidate();
    }
}
