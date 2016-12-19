package com.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TimePicker;



public class MyTimePicker extends TimePicker
{
    public MyTimePicker(Context context, AttributeSet attrs, int defStyle)
    {
        super(context,attrs,defStyle);
    }
    
    public MyTimePicker(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        this.setPadding(0,0,0,0);
        setIs24HourView(true);
    }
    
    public MyTimePicker(Context context)
    {
        super(context);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        if(once)
        {
            once = false;
            travelsalChild(this);
        }
    }
    
    boolean once = true;
    
    int count = 0;
    
    public static void travelsalChild(View v)
    {
        if(v instanceof ViewGroup)
        {
            ViewGroup parent = (ViewGroup) v;
            
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++)
            {
                travelsalChild(parent.getChildAt(i));
            }
            MarginLayoutParams params = (MarginLayoutParams) v.getLayoutParams();
            params.leftMargin = 0;
            params.rightMargin = 0;
            v.setLayoutParams(params);
            v.setPadding(0,0,0,0);
        }
        else
        {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
            params.leftMargin = 0;
            params.rightMargin = 0;
            v.setLayoutParams(params);
            v.setPadding(0,0,0,0);
        }
    }
}
