

package com.gdmss.adapter;


import java.io.File;
import java.util.LinkedList;

import com.gdmss.R;
import com.utils.L;
import com.widget.ScaleImageView;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;


public class ImageViewerAdapter extends PagerAdapter
{
    //    private File[] files;
    LinkedList<File> files;
    private LayoutInflater inflater;

    private Context context;

    public ImageViewerAdapter(Context context,LinkedList<File> files)
    {
        this.files = files;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount()
    {
        L.e("getCount");
        return files.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container,int position)
    {
        ScaleImageView img = (ScaleImageView) inflater.inflate(R.layout.item_imageview,container,false);
        img.setImageURI(Uri.parse(files.get(position).getPath()));
        img.setScaleType(type);
        container.addView(img);
        return img;
    }

    @Override
    public boolean isViewFromObject(View paramView,Object paramObject)
    {
        return paramView == paramObject;
    }

    @Override
    public void destroyItem(ViewGroup container,int position,Object object)
    {
        container.removeView((View) object);
    }

    public void setType(ScaleType type)
    {
        this.type = type;
        notifyDataSetChanged();
    }

    private ScaleType type = ScaleType.FIT_CENTER;
}
