package com.example.objectdetectionapp_mlkit.Helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class RectOverlay extends GraphicOverlay.Graphic {
    private int mRectColor= Color.GREEN;
    private float mStrokeWidth = 4.0f;
    private Paint mReactPaint;
    private GraphicOverlay graphicOverlay;
    private Rect rect;


    public RectOverlay(GraphicOverlay graphicOverlay, Rect rect) {
        super(graphicOverlay);
        mReactPaint=new Paint();
        mReactPaint.setColor(mRectColor);
        mReactPaint.setStyle(Paint.Style.STROKE);
        mReactPaint.setStrokeWidth(mStrokeWidth);

        this.graphicOverlay = graphicOverlay;
        this.rect=rect;

        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        RectF rectF = new RectF(rect);
        rectF.left = translateX(rectF.left);
        rectF.right = translateX(rectF.right);
        rectF.top = translateX(rectF.top);
        rectF.bottom = translateX(rectF.bottom);

        canvas.drawRect(rectF,mReactPaint);
    }
}
