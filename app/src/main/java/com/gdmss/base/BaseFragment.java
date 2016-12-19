

package com.gdmss.base;


import com.gdmss.R;
import com.utils.L;
import com.widget.ShowProgress;
import com.widget.SlidingMenu;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.view.ViewGroup;

@SuppressLint("ValidFragment")
public class BaseFragment extends Fragment
{
    protected SlidingMenu menu;

    protected Button leftMenu;

    protected Context context;

    protected View view;

    protected APP app;

    public String TAG = "BaseFragment";

    public ShowProgress progress;

    public boolean requiresOnkeyDown = false;

//    public BaseFragment()
//    {
//        Bundle bundle = new Bundle();
//        setArguments(bundle);
//    }

    public BaseFragment()
    {
        super();
    }

    public static BaseFragment instance;

    public static BaseFragment getInstance(SlidingMenu menu)
    {
        if (null == instance)
        {
            instance = new BaseFragment();
        }
        instance.menu = menu;
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        app = (APP) getActivity().getApplication();
        context = app.getApplicationContext();
        progress = new ShowProgress(getActivity());
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onResume()
    {
        L.init(TAG);
        super.onResume();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if (null != view)
        {
            leftMenu = (Button) view.findViewById(R.id.btn_menu);
            if (null != leftMenu)
            {
                leftMenu.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View paramView)
                    {
                        menu.toggle();
                    }
                });
            }

            view.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (menu.isOpen)
                    {
                        menu.toggle();
                    }
                }
            });
//
//            view.setOnTouchListener(new View.OnTouchListener()
//            {
//                @Override
//                public boolean onTouch(View v,MotionEvent event)
//                {
//                    if (event.getAction() == MotionEvent.ACTION_DOWN)
//                    {
//                        if (menu.isOpen)
//                        {
//                            menu.toggle();
//                        }
//                    }
//                    return false;
//                }
//            });
        }
    }


    public void onKeyDown(int keyCode,KeyEvent event)
    {
        // TODO
    }
}
