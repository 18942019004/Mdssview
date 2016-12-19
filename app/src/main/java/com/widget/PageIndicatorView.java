package com.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdmss.R;


public class PageIndicatorView extends LinearLayout
{

    int pageTotal = 1;
    int currentPage = 0;

    public PageIndicatorView(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public PageIndicatorView(Context context,AttributeSet attrs)
    {
        super(context,attrs);
        // TODO Auto-generated constructor stub
    }

    public PageIndicatorView(Context context,AttributeSet attrs,int defStyle)
    {
        super(context,attrs,defStyle);
        // TODO Auto-generated constructor stub
        setData(0,4);
    }

    public void setData(int currentPage,int total)
    {
        this.pageTotal = total;
        this.currentPage = currentPage;
        removeAllViews();
        if (total > 4)
        {
            TextView iv = new TextView(getContext());
            iv.setTextSize(15);
            iv.setText((currentPage + 1) + "/" + total);
            iv.setTextColor(this.getResources().getColor(R.color.black));
            addView(iv);
        }
        else
        {

            for (int i = 0; i < total; i++)
            {
                ImageView iv = new ImageView(getContext());
                int w = (int) getResources().getDimension(
                        R.dimen.indicator_width);
                int s = (int) getResources().getDimension(
                        R.dimen.gallery_spacing);
                LayoutParams lp = new LayoutParams(w,w);
                lp.leftMargin = s;
                lp.rightMargin = s;
                lp.topMargin = s;
                lp.bottomMargin = s;
                iv.setLayoutParams(lp);
                if (currentPage == i)
                {
//                    iv.setImageResource(R.drawable.liveview_skate_bright);
                }
                else
                {
//                    iv.setImageResource(R.drawable.liveview_skate_dark);
                }

                addView(iv);
            }
        }

    }

    public int getPageTotal()
    {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal)
    {
        this.pageTotal = pageTotal;
    }

    public int getCurrentPage()
    {
        return currentPage;
    }

    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
    }

}
