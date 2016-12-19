package com.gdmss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gdmss.R;
import com.gdmss.base.BaseActivity;
import com.utils.Utils;
import com.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/12.
 */

public class AcAccountManage extends BaseActivity implements ToggleButton.OnToggleChanged, View.OnClickListener
{
    boolean autoLogin;
    @BindView(R.id.tg_autologin)
    ToggleButton tgAutologin;
    @BindView(R.id.tv_modifypwd)
    TextView tvModifypwd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_accountmanage);
        ButterKnife.bind(this);
        autoLogin = Utils.ReadBoolean(context,"autologin");
        tgAutologin.setOnToggleChanged(this);
        tvModifypwd.setOnClickListener(this);
    }


    /**
     * @param v
     * @param on
     */
    @Override
    public void onToggle(View v,boolean on)
    {
        Utils.saveBoolean(context,"autologin",on);
    }

    @Override
    public void onClick(View view)
    {
        startActivity(new Intent(context,AcModifyPassword.class));
    }
}
