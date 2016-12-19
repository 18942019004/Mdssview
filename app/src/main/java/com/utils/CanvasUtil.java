package com.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.gdmss.R;
import com.nineoldandroids.view.ViewHelper;
import com.widget.VideoCanvas;

/**
 * Created by Lee
 */

public class CanvasUtil
{
    public static CanvasUtil util;

    private int width = 0;

    private int height = 0;

    private int pageNumber = 1;

    private RelativeLayout[] pages;

    //总画面数
    private int canvasCount = 16;

    //显示模式(单页画面数)
    public int displayMode = 4;

    Context context;

    VideoCanvas[] canvases;

    public static CanvasUtil getInstance()
    {
        if (null == util)
        {
            util = new CanvasUtil();
        }
        return util;
    }


    public void createView(Context context)
    {
        pageNumber = canvasCount / displayMode;
        pages = new RelativeLayout[pageNumber];
        this.context = context;
    }

    public void resizeLayout(int canvasIndex,View v)
    {
        int rowCount = (int) Math.sqrt(displayMode);
        int inPageIndex = canvasIndex % displayMode;
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

    private void setPosition(View v,int left,int top,int width,int height)
    {
        ViewHelper.setX(v,left);
        ViewHelper.setY(v,top);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
        params.width = width;
        params.height = height;
        v.setLayoutParams(params);
    }

    public void initSize(Context context,final View v)
    {
        this.context = context;
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                int mWidth = v.getMeasuredWidth();
                int mHeight = v.getMeasuredHeight();
//                L.e("width:" + width + " height:" + height);
                if (!isEqualSize(mWidth,mHeight))
                {
                    width = mWidth;
                    height = mHeight;
//                    resizeLayout();
                }
            }
        });
    }


    private boolean isEqualSize(int width,int height)
    {
        return this.width == width && this.height == height;
    }
}
