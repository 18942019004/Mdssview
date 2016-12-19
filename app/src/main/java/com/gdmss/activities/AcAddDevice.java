package com.gdmss.activities;

import com.gdmss.R;
import com.gdmss.base.BaseActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;



public class AcAddDevice extends BaseActivity implements OnClickListener
{
    private TextView rl_p2p, rl_ip, rl_login;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_adddevice);
        initViews();
    }
    
    private void initViews()
    {
        rl_p2p = (TextView) findViewById(R.id.item1);
        rl_ip = (TextView) findViewById(R.id.item4);
        rl_login = (TextView) findViewById(R.id.item6);
        
        rl_p2p.setOnClickListener(this);
        rl_ip.setOnClickListener(this);
        rl_login.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        Class<?> clazz = null;
        
        switch (v.getId())
        {
            case R.id.item1:
                clazz = AcAddP2p.class;
                break;
            case R.id.item4:
                clazz = AcAddIP.class;
                break;
            case R.id.item6:
                clazz = AcAddP2p.class;
                break;
        }
        if(null != clazz)
        {
            startActivity(new Intent(AcAddDevice.this,clazz));
        }
    }
}
