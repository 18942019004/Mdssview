

package com.gdmss.base;


import com.gdmss.R;
import com.utils.AlarmUtils;
import com.utils.L;
import com.utils.T;
import com.widget.ShowProgress;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;


@SuppressLint("NewApi")
public class BaseActivity extends Activity implements Runnable
{
    public static final int FAIL = 0;

    public static final int SUCCESS = 1;

    public APP app;

    protected Button leftBtn;

    protected Button rightBtn;

    protected TextView tv_title;

    protected ShowProgress progress;

    protected Context context;

    public BaseActivity()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Toolbar toolbar = (Toolbar)
        // getLayoutInflater().inflate(R.layout.layout_toolbar, null);
        // this.setSupportActionBar(toolbar);
        initActivity();
    }

    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);
        initViews();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    private void initActivity()
    {
        app = (APP) getApplication();

        context = getApplicationContext();
        // 透明状态栏
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 透明导航栏
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        // 旋转进度条
        progress = new ShowProgress(this);
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(true);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void initViews()
    {
        leftBtn = (Button) findViewById(R.id.leftBtn);

        rightBtn = (Button) findViewById(R.id.rightBtn);
        // 按钮存在且未设置监听
        if (null != leftBtn && !leftBtn.hasOnClickListeners())
        {
            leftBtn.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onBackClick();
                }
            });
        }

        if (null == tv_title)
        {
            tv_title = (TextView) findViewById(R.id.tv_title);
        }

    }


    @Override
    protected void onResume()
    {
        super.onResume();
        // 每次进入onresume()都重设Log工具类的TAG 方便看打印时知道
        AlarmUtils.initPush(context);
        L.init(getLocalClassName());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        /** 隐藏软键盘 */
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive())
            {
                if (null != getCurrentFocus())// .getWindowToken()
                {
                    inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void startActivity(Intent intent)
    {
        super.startActivity(intent);
    }

    /**
     * 一个点击导航栏返回键的方法（默认为finish（）） 如要进行其他操作在子类重写onbackclick()覆盖即可
     */
    protected void onBackClick()
    {
        finish();
    }

    public void setTitle(String title)
    {
        if (null == tv_title)
        {
            return;
        }
        tv_title.setText(title);
    }

    public void setTitle(int resId)
    {
        if (null == tv_title)
        {
            return;
        }
        tv_title.setText(getString(resId));
    }

    protected void showProgress(String message)
    {
        progress.setMessage(message);
        progress.show();
    }

    protected void showProgress(int msgRes)
    {
        showProgress(getString(msgRes));
    }

    protected void showProgress()
    {
        progress.show();
    }

    protected void dismissProgress()
    {
        progress.dismiss();
    }

    protected void runInThread()
    {

    }

    protected void startThread()
    {
        new Thread(this).start();
    }

    @Override
    public void run()
    {
        runInThread();
    }


    protected Handler msgHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            dismissProgress();
            switch (msg.what)
            {
                case FAIL:
                    T.showS("失败");
                    break;
                case SUCCESS:
                    T.showS("成功");
                    finish();
                    break;
            }
            return false;
        }
    });
}