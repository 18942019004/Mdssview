package com.widget;

import com.gdmss.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


public class ShowProgress extends Dialog
{

    public Context context;

    public LayoutInflater layoutInflate;

    public View view = null;

    public int width;

    public int height;

    TextView message;

    ProgressBar progress;

    public ShowProgress(Context context)
    {
        super(context,R.style.myProgress);
        this.context = context;
        this.layoutInflate = LayoutInflater.from(context);
        this.view = View.inflate(context,R.layout.myprogress,null);
        message = (TextView) view.findViewById(R.id.tv_message);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        setCanceledOnTouchOutside(false);
    }

    public void setMessage(String sMessage)
    {
        setMessageVisible(sMessage);
        message.setText(sMessage + "");
    }

    public void setMessage(int sMessageId)
    {
        String sMessage = context.getString(sMessageId);
        setMessageVisible(sMessage);
        try
        {
            message.setText(sMessage + "");
        }
        catch (Exception e)
        {

        }
    }

    public void setMessageVisible(String sMessage)
    {
        if (TextUtils.isEmpty(sMessage))
        {
            message.setVisibility(View.GONE);
        }
        else
        {
            message.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (view != null)
        {
            this.setContentView(view);
        }
    }

}
