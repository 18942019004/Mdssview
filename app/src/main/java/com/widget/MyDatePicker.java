package com.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.DatePicker;



public class MyDatePicker extends DatePicker
{
    public MyDatePicker(Context context, AttributeSet attrs, int defStyle)
    {
        super(context,attrs,defStyle);
    }
    
    public MyDatePicker(Context context, AttributeSet attrs)
    {
        super(context,attrs);
    }
    
    public MyDatePicker(Context context)
    {
        super(context);
    }
    
    private boolean once = true;
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        if(once)
        {
            once = false;
            MyTimePicker.travelsalChild(this);
        }
    }
}
