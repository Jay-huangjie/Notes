package com.jie.notes.widge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class DashedLine extends View {

    private final Path path;
    private final DashPathEffect effects;
    Paint paint;

    private int X;
    private int Y=480;
    public DashedLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        path = new Path();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#FFFFFF"));
        effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        paint.setPathEffect(effects);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        path.moveTo(0, 0);
        path.lineTo(X, Y);
        canvas.drawPath(path, paint);
    }

    public void setColor(int color){
        paint.setColor(color);
        invalidate();
    }

    public void setWidth(int width){
        paint.setStrokeWidth(width);
        invalidate();
    }

    public void setPosition(int X,int Y){
        this.X = X;
        this.Y = Y;
        invalidate();
    }
}
