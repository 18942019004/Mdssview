package com.gdmss.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdmss.entity.PlayNode;
import com.utils.L;
import com.widget.PlayPiece;
import com.widget.VideoCanvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lee
 */

public class PlayerAdapter extends PagerAdapter
{
    Context context;

    List<PlayNode> nodeList;

    VideoCanvas[] canvasArray;

    LayoutInflater inflater;

    public int displayMode = 4;

    private int lastDisplayMode = 4;

    PlayPiece[] pages;

    public PlayerAdapter(Context context,List<PlayNode> list)
    {
        L.init("PlayerAdapter");
        this.context = context;
        this.nodeList = list;
        this.inflater = LayoutInflater.from(context);
        canvasArray = new VideoCanvas[16];
        for (int i = 0; i < 16; i++)
        {
            nodeList.add(new PlayNode());
            VideoCanvas can = new VideoCanvas(context);
            canvasArray[i] = can;//.add(can);
        }
    }

    int pageSize = 0;

    @Override
    public int getCount()
    {
        int count = getPageCount();
        L.e("getCount:" + count);
        return count;
    }


    //根据传入的数组计算需要显示的页数
    private int getPageCount()
    {
        int count = nodeList.size();
        if (count / displayMode > 0)
        {
            if (count % displayMode > 0)
            {
                return count / displayMode + 1;
            }
            return count / displayMode;
        }
        return 1;
    }

    @Override
    public boolean isViewFromObject(View view,Object object)
    {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container,int position)
    {
        PlayPiece piece = new PlayPiece(context);
        piece.setCanvas(Arrays.copyOfRange(canvasArray,position * displayMode,(position + 1) * displayMode));
        piece.layoutCanvas();
        container.addView(piece);
        return piece;
    }


    public void setDisplayMode(int displayMode)
    {
        lastDisplayMode = this.displayMode;
        this.displayMode = displayMode;
    }


    @Override
    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
        lastDisplayMode = displayMode;
    }

    int currentPosition = 0;

    @Override
    public int getItemPosition(Object object)
    {
        currentPosition = ((PlayPiece) object).position;
        L.e("currentPosition:" + currentPosition);
        if (displayMode != lastDisplayMode)
        {
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }


    public void setNodeList(List<PlayNode> nodeList)
    {
        if (this.nodeList.size() % displayMode != 0)
        {
            int size = (nodeList.size() / displayMode + 1) * displayMode;
            canvasArray = new VideoCanvas[size];
            List<PlayNode> list = new ArrayList<>(size);
            for (int i = 0; i < list.size(); i++)
            {
                if (i < nodeList.size())
                {
                    list.set(i,nodeList.get(i));
                }
                else
                {
                    nodeList.set(i,new PlayNode());
                }
                canvasArray[i] = new VideoCanvas(context);
                PlayNode tempNode = list.get(i);
                canvasArray[i].Prepare(tempNode.getName(),tempNode.getDeviceId(),true,"");
            }
        }
        else
        {
            this.nodeList = nodeList;
            canvasArray = new VideoCanvas[nodeList.size()];
            for (int i = 0; i < nodeList.size(); i++)
            {
                canvasArray[i] = new VideoCanvas(context);
                PlayNode tempNode = nodeList.get(i);
                canvasArray[i].Prepare(tempNode.getName(),tempNode.getDeviceId(),true,"");
            }
        }
    }

    public void PlayAll()
    {
        for (VideoCanvas canvas : canvasArray)
        {
            if (null != canvas && canvas.getView().getVisibility() == View.VISIBLE)
            {
                canvas.Play();
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container,int position,Object object)
    {
        L.e("destroyItem");
        PlayPiece piece = (PlayPiece) object;
        piece.removeAllViews();
        container.removeView((View) object);
    }
}