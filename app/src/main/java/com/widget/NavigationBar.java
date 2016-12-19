package com.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;



public class NavigationBar extends ViewGroup
{
    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context,attrs,defStyleAttr);
    }
    
    public NavigationBar(Context context, AttributeSet attrs)
    {
        super(context,attrs);
    }
    
    public NavigationBar(Context context)
    {
        super(context);
    }
    
    @Override
    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
        
    }
    
}
