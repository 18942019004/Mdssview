package com.gdmss.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class PlayViewPagerAdapter extends PagerAdapter
{

    private View[] mImageViews;

    public PlayViewPagerAdapter(View[] imageViews)
    {
        this.mImageViews = imageViews;

    }

    @Override
    public int getCount()
    {
        return mImageViews.length;
    }

    @Override
    public boolean isViewFromObject(View arg0,Object arg1)
    {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container,int position)
    {
        // Log.d("PagerAdapter", "instantiateItem position" + position);

        ((ViewPager) container).addView(mImageViews[position]);

        return mImageViews[position];
    }

    @Override
    public void destroyItem(ViewGroup container,int position,Object object)
    {
        // Log.d("PagerAdapter", "destroyItem position" + position);

        ((ViewPager) container).removeView(mImageViews[position]);

    }

    @Override
    public int getItemPosition(Object object)
    {
        // TODO Auto-generated method stub
        return super.getItemPosition(object);
    }

}

