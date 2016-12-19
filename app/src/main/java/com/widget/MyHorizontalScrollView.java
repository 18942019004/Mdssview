package com.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 *
 */

public class MyHorizontalScrollView extends HorizontalScrollView
{
    public MyHorizontalScrollView(Context context)
    {
        super(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        if (null != listener)
        {
            if (l + getWidth() >= computeHorizontalScrollRange())
            {
                //滑动到最右侧了
                listener.scrollInEnd();
            }
            else if (l == 0)
            {
                //滑动到最左侧了
                listener.scrollInStart();
            }
            else
            {
                listener.inScroll();
            }
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    OnScrollListener listener;

    public void setOnscrollListener(OnScrollListener listener)
    {
        this.listener = listener;
    }

    public interface OnScrollListener
    {
        void scrollInStart();

        void scrollInEnd();

        void inScroll();
    }
}
