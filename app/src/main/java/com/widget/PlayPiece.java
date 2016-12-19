

package com.widget;


import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nineoldandroids.view.ViewHelper;
import com.utils.L;


public class PlayPiece extends RelativeLayout
{
    public int position;

    private PlayerImpl impl;

    private Context context;

    public PlayPiece(Context context)
    {
        super(context);
        this.context = context;

//        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
//        {
//            @Override
//            public boolean onPreDraw()
//            {
//                getViewTreeObserver().removeOnPreDrawListener(this);
//                int mWidth = getMeasuredWidth();
//                int mHeight = getMeasuredHeight();
//                L.e("width:" + width + " height:" + height);
//                if (!isEqualSize(mWidth,mHeight))
//                {
//                    width = mWidth;
//                    height = mHeight;
//                    layoutCanvas();
////                    resizeLayout();
//                }
//                return true;
//            }
//        });

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int mWidth = getMeasuredWidth();
                int mHeight = getMeasuredHeight();
                L.e("width:" + width + " height:" + height);
                if (!isEqualSize(mWidth,mHeight))
                {
                    width = mWidth;
                    height = mHeight;
                    layoutCanvas();
//                    resizeLayout();
                }
            }
        });
    }

    private int width = 0;

    private int height = 0;

    private boolean isEqualSize(int width,int height)
    {
        return this.width == width && this.height == height;
    }

    int displayMode = 4;

    VideoCanvas[] canvas;

    public void setCanvas(VideoCanvas[] canvas)
    {
        if (null == this.canvas || this.canvas.length != canvas.length)
        {
            this.canvas = canvas;
            displayMode = canvas.length;
            layoutCanvas();
        }
    }

    public void layoutCanvas()
    {
        removeAllViews();
        for (int i = 0; i < displayMode; i++)
        {
            if (i < canvas.length)
            {
                if (null == canvas[i])
                {
                    return;
                }
                resizeLayout(i,canvas[i]);
                addView(canvas[i].getView());
                canvas[i].imgVideo.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }

    public void resizeLayout(int inPageIndex,VideoCanvas v)
    {
        int rowCount = (int) Math.sqrt(displayMode);
        int column = 0;
        int row = 0;
        if (inPageIndex != 0)
        {
            column = inPageIndex % rowCount;
            row = inPageIndex / rowCount;
        }
        int mWidth = width / rowCount;
        int mHeight = height / rowCount;
        setPosition(v,column * mWidth,row * mHeight,mWidth,mHeight);
    }

    private void setPosition(VideoCanvas v,int left,int top,int width,int height)
    {
        v.deal = Imagedeal.getdeal(v.imgVideo);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,height);
        params.width = width;
        params.height = height;
        params.leftMargin = left;
        params.topMargin = top;
        v.getView().setLayoutParams(params);

    }

    /**
     * 获取当前点击画面
     *
     * @param event 手势
     *
     * @return 画面下表，获取失败返回-1
     */
    public int getIndex(MotionEvent event)
    {
        Rect canvasRec = new Rect();
        for (int i = 0; i < canvas.length; i++)
        {
            canvas[i].getView().getGlobalVisibleRect(canvasRec);

            if (canvasRec.contains((int) event.getRawX(),(int) event.getRawY()))
            {
                return i;
            }
        }
        return -1;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
//        int action = ev.getAction();
//        switch (action)
//        {
//            case MotionEvent.ACTION_DOWN:
//                int canvasIndex = getIndex(ev);
//                if (canvasIndex != -1)
//                {
//                    canvas[lastIndex].setHighLight(false);
//                    canvas[canvasIndex].setHighLight(true);
//                    lastIndex = canvasIndex;
//                }
//                break;
//        }
        return super.dispatchTouchEvent(ev);
    }

    int index = 0;

    int lastIndex = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:

                break;
        }
        return super.onTouchEvent(event);
    }

    public void PlayAll()
    {
        for (VideoCanvas canvas : this.canvas)
        {
            if (null != canvas && canvas.getView().getVisibility() == View.VISIBLE)
            {
//                if (canvas.getState() == VideoCanvas.READY)
//                {
                canvas.Play();
//                }
//                else
//                {
//                    canvas.Resume();
//                }
            }
        }
    }

    public void stopAll()
    {
        for (VideoCanvas canvas : this.canvas)
        {
            if (null != canvas && canvas.getView().getVisibility() == View.VISIBLE)
            {
                canvas.stop(false);
            }
        }
    }

    public void stopAll(boolean cleanDisplay)
    {
        for (VideoCanvas canvas : this.canvas)
        {
            if (null != canvas && canvas.getView().getVisibility() == View.VISIBLE)
            {
                canvas.stop(cleanDisplay);
            }
        }
    }

    public void pauseAll()
    {
        for (VideoCanvas canvas : this.canvas)
        {
            if (null != canvas && canvas.getView().getVisibility() == View.VISIBLE)
            {
                canvas.Pause();
            }
        }
    }

    public void resumeAll()
    {
        for (VideoCanvas canvas : this.canvas)
        {
            if (null != canvas && canvas.getView().getVisibility() == View.VISIBLE)
            {
                canvas.Resume();
            }
        }
    }

    public void setHighLight(int index,boolean isHightLight)
    {
        if (index >= 0 && index < canvas.length)
        {
            canvas[index].setHighLight(isHightLight);
        }
    }
}
