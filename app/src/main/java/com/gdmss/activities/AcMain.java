

package com.gdmss.activities;


import com.gdmss.R;
import com.gdmss.base.APP;
import com.gdmss.base.BaseActivity;
import com.gdmss.base.BaseFragment;
import com.utils.Utils;
import com.widget.SlidingMenu;

import android.app.ActivityManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class AcMain extends BaseActivity implements OnClickListener
{
    private SlidingMenu menu;

    private RadioGroup leftMenu;

    private BaseFragment currentFg;

    private APP app;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        initParameters();
        initViews();
        initLeftMenu();
        app.initAllFragment(menu);
        fragmentTransaction(0);
    }

    private void initParameters()
    {
        app = (APP) getApplication();
    }

    private void initViews()
    {
        leftMenu = (RadioGroup) findViewById(R.id.rg_leftmenu);
        if (app.client.isLocalList())
            leftMenu.findViewById(R.id.rbtn_alarmlist).setVisibility(View.GONE);
        menu = (SlidingMenu) findViewById(R.id.menu);
    }

    // 设置侧滑栏点击事件
    private void initLeftMenu()
    {
        for (int i = 0; i < leftMenu.getChildCount(); i++)
        {
            if (i != 0)
            {
                leftMenu.getChildAt(i).setOnClickListener(new FragmentChangeListener(i - 1));
            }
            else
            {
                leftMenu.getChildAt(0).setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View paramView)
                    {
                        finish();
                    }
                });
            }
        }
    }


    // 切换fragment
    private void fragmentTransaction(int index)
    {
        if (!Utils.isEmpty(app.fragments_live) && app.fragments_live.size() >= index)
        {
            currentFg = (BaseFragment) app.fragments_live.get(index);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.container, currentFg).commit();
            ((RadioButton) leftMenu.getChildAt(index + 1)).setChecked(true);
        }
    }

    // 侧滑栏点击监听(切换fragment)
    class FragmentChangeListener implements OnClickListener
    {
        int index;

        public FragmentChangeListener(int index)
        {
            this.index = index;
        }

        @Override
        public void onClick(View arg0)
        {
            fragmentTransaction(index);
            menu.toggle();
        }
    }

    @Override
    public void onClick(View arg0)
    {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (currentFg.requiresOnkeyDown)
            {
                currentFg.onKeyDown(keyCode, event);
                return false;
            }
            else if (!menu.isOpen)
            {
                menu.openMenu();
                return true;
            }
            else
            {
                showExitDialog();
                return true;
            }
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }
    }

    void showExitDialog()
    {
        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this).setTitle(R.string.msg_title).setMessage(R.string.msg_exit).setNegativeButton(R.string.msg_cancel, null).setPositiveButton(R.string.msg_ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                progress.show();
                app.client.logoutServer(1, logoutHan);
            }
        }).create();
        dialog.show();
    }

    void logout()
    {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        manager.restartPackage(getPackageName());
    }

    private Handler logoutHan = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            progress.dismiss();
            Process.killProcess(Process.myPid());
            return false;
        }
    });


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        currentFg.onActivityResult(requestCode, resultCode, data);
    }
}
