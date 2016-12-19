

package com.gdmss.activities;


import com.gdmss.R;
import com.gdmss.adapter.ImageViewerAdapter;
import com.gdmss.base.BaseActivity;
import com.gdmss.fragment.FgFileManage;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class AcImageViewer extends BaseActivity implements OnTouchListener, OnPageChangeListener
{
    int index;

    private ViewPager pager;

    private ImageViewerAdapter adapter;

    private LinearLayout ll_menu;

    private RelativeLayout rl_title;

    private TextView tv_title;

    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_imageviewer);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        initParam();
        initViews();
    }

    void initParam()
    {
        index = getIntent().getIntExtra("index",0);
    }

    void initViews()
    {
        pager = (ViewPager) findViewById(R.id.pager);

        adapter = new ImageViewerAdapter(this,FgFileManage.file_photos);

        pager.setAdapter(adapter);

        pager.setCurrentItem(index,false);

        pager.setOnTouchListener(this);

        pager.addOnPageChangeListener(this);

        ll_menu = (LinearLayout) findViewById(R.id.rl_bottom);

        rl_title = (RelativeLayout) findViewById(R.id.rl_title);

        findViewById(R.id.leftBtn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //                finish();
                onKeyDown(KeyEvent.KEYCODE_BACK,new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_BACK));
            }
        });
    }


    void initAnimations()
    {
        // Animation a = AnimationUtils.loadAnimation(this, R.anim.anim_bottom);
        Animation a = new TranslateAnimation(0,0,2,0);
        LayoutAnimationController anim = new LayoutAnimationController(a);
        anim.setDelay(1);
        anim.setOrder(LayoutAnimationController.ORDER_NORMAL);
        ll_menu.setLayoutAnimation(anim);
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            //            adapter.setType(ScaleType.FIT_XY);
        }
        return super.onKeyDown(keyCode,event);
    }

    boolean isShow = false;

    @Override
    public boolean onTouch(View v,MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            //            if (isShow)
            //            {
            //                ScreenUtils.hideStatusBar(this);
            //                ll_menu.setVisibility(View.GONE);
            //                rl_title.setVisibility(View.GONE);
            //            }
            //            else
            //            {
            //                ScreenUtils.showStatusBar(this);
            //                ll_menu.setVisibility(View.VISIBLE);
            //                rl_title.setVisibility(View.VISIBLE);
            //                ll_menu.startLayoutAnimation();
            //            }
            //            isShow = !isShow;
        }
        return false;
    }

    @Override
    public void onPageScrolled(int paramInt1,float paramFloat,int paramInt2)
    {

    }

    @Override
    public void onPageSelected(int paramInt)
    {
        setTitle(FgFileManage.file_photos.get(paramInt).getName());
    }

    @Override
    public void onPageScrollStateChanged(int paramInt)
    {

    }
}
