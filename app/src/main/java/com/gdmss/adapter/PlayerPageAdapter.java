package com.gdmss.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.utils.L;
import com.widget.PlayPiece;

/**
 *
 */

public class PlayerPageAdapter extends PagerAdapter
{
    PlayPiece[] pieces;

    public PlayerPageAdapter(PlayPiece[] pieces)
    {
        this.pieces = pieces;
    }

    @Override
    public int getCount()
    {
        return pieces.length;
    }

    @Override
    public boolean isViewFromObject(View view,Object object)
    {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container,int position)
    {
        container.addView(pieces[position]);
        return pieces[position];
    }

    @Override
    public int getItemPosition(Object object)
    {
//        if (needRefresh)
//        {
//        return POSITION_NONE;
//        }
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container,int position,Object object)
    {
        container.removeView((PlayPiece) object);
    }

    public void refresh(PlayPiece[] pieces)
    {
        this.pieces = pieces;
        notifyDataSetChanged();
    }
}
