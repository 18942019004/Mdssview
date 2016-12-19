package com.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 *
 */

public class ScaleImageView extends ImageView
{
    private static final int MODE_NONE = 0;

    private static final int MODE_SCALE = 1;

    private static final int MODE_MOVE = 2;

    private Matrix matrix;

    public ScaleImageView(Context context)
    {
        super(context);
    }

    public ScaleImageView(Context context,AttributeSet attrs)
    {
        super(context,attrs);
    }

    public ScaleImageView(Context context,AttributeSet attrs,int defStyleAttr)
    {
        super(context,attrs,defStyleAttr);
    }

    Point sPoint, cPoint;

    //缩放时 两根手指的坐标
    Point p1, p2;

    private int mode = MODE_NONE;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                onDown(event);
                return false;
            case MotionEvent.ACTION_POINTER_2_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = MODE_SCALE;
                cPoint.set((int) event.getX(1),(int) event.getY(1));
                break;
            case MotionEvent.ACTION_POINTER_2_UP:
            case MotionEvent.ACTION_UP:
                mode = MODE_NONE;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = MODE_NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == MODE_MOVE)
                {
                    move(event);
                }
                else if (mode == MODE_SCALE)
                {
                    scale(event);
                }
                break;
        }
        Log.e("onTouchEvent","action:" + event.getAction());
        return true;
    }

    public void onDown(MotionEvent event)
    {
//        setScaleType(ScaleType.MATRIX);
        mode = MODE_MOVE;
        if (null == matrix)
        {
            matrix = new Matrix(getImageMatrix());
        }
        else
        {
            matrix.set(getImageMatrix());
        }
        sPoint = new Point((int) event.getX(0),(int) event.getY(0));
        cPoint = new Point((int) event.getX(0),(int) event.getY(0));
    }

    void move(MotionEvent event)
    {
        if (null == cPoint)
        {
            cPoint = new Point();
        }
        cPoint.set((int) event.getX(0),(int) event.getY(0));
        float disX, disY;
        disX = cPoint.x - sPoint.x;
        disY = cPoint.y - sPoint.y;
        matrix.postTranslate(disX,disY);
        setImageMatrix(matrix);
        sPoint.set(cPoint.x,cPoint.y);
    }

    void scale(MotionEvent event)
    {
        if (null == p1)
        {
            p1 = new Point((int) event.getX(0),(int) event.getY(0));
        }
        if (null == p2)
        {
            p2 = new Point((int) event.getX(1),(int) event.getY(1));
        }
        p1.set((int) event.getX(0),(int) event.getY(0));
        p2.set((int) event.getX(1),(int) event.getY(1));


        double distance0 = Math.sqrt(Math.pow(cPoint.x - sPoint.x,2) + Math.pow(cPoint.y - sPoint.y,2));
        double distance1 = Math.sqrt(Math.pow(p2.x - p1.x,2) + Math.pow(p2.y - p1.y,2));

        float scale = (float) (distance1 / distance0);

        matrix.postScale(scale,scale);
        setImageMatrix(matrix);


        cPoint.set(p2.x,p2.y);
        sPoint.set(p1.x,p1.y);
    }

    //math.pow(x-x0)^2+(y-y0)^2
}
