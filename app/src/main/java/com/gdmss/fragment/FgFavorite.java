package com.gdmss.fragment;

import com.gdmss.R;
import com.gdmss.base.BaseFragment;
import com.widget.SlidingMenu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;


@SuppressLint("ValidFragment")
public class FgFavorite extends BaseFragment implements OnClickListener
{
    private Button ibtn_device;

    public static FgFavorite getInstance(SlidingMenu menu)
    {
        FgFavorite instance = new FgFavorite();
        instance.menu = menu;
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if(null == view)
        {
            view = inflater.inflate(R.layout.fg_favorite,container,false);
            initViews();
        }
        return view;
    }
    
    private void initViews()
    {
        ibtn_device = (Button) view.findViewById(R.id.ibtn_device);
    }
    
    @Override
    public void onClick(View paramView)
    {
        switch (paramView.getId())
        {
            case R.id.ibtn_device:
                
                break;
        }
    }
    
}
