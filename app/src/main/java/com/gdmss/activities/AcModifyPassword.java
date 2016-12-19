package com.gdmss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.Player.web.response.ResponseCommon;
import com.gdmss.R;
import com.gdmss.base.BaseActivity;
import com.utils.T;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AcModifyPassword extends BaseActivity
{
    String pwd, newpwd, ensurepwd;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_newpwd)
    EditText etNewpwd;
    @BindView(R.id.et_ensurepwd)
    EditText etEnsurepwd;
    @BindView(R.id.btn_sure)
    Button btnSure;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_modifypassword);
        ButterKnife.bind(this);
        btnSure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                modifyPwd();
            }
        });
    }

    void getInput()
    {
        pwd = etPwd.getText().toString();
        newpwd = etNewpwd.getText().toString();
        ensurepwd = etEnsurepwd.getText().toString();
    }

    void modifyPwd()
    {
        getInput();
        if (TextUtils.isEmpty(pwd))
        {
            T.showS("请输入完整");
            return;
        }
        if (!app.currentUser.getsPassWord().equals(pwd))
        {
            T.showS("密码与原密码不匹配");
            return;
        }
        if (!TextUtils.isEmpty(newpwd))
        {
            if (!TextUtils.isEmpty(ensurepwd))
            {
                if (newpwd.equals(ensurepwd))
                {
                    app.client.modifyUserPassword(pwd,newpwd,modifyHan);
                    return;
                }
                else
                {
                    T.showS("两次密码输入不一致");
                    return;
                }
            }
        }
        T.showS("请输入完整");
    }

    private Handler modifyHan = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            ResponseCommon response = (ResponseCommon) msg.obj;
            if (null != response)
            {
                if (null != response.h)
                {
                    if (response.h.e == 200)
                    {
                        T.showS("修改成功，请重新登录");
                        app.clearData();
                        Intent it = new Intent(AcModifyPassword.this,AcLogin.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);
                        finish();
                    }
                }
            }
            return false;
        }
    });
}