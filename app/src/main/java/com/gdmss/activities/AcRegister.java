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
import com.utils.TextFormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/2.
 */

public class AcRegister extends BaseActivity
{
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_ensurepwd)
    EditText etEnsurepwd;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.btn_register)
    Button btnRegister;

    String username, pwd, ensure_pwd, email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_register);
        ButterKnife.bind(this);
        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkInput();
            }
        });
    }


    @Override
    protected void runInThread()
    {
    }

    void checkInput()
    {
        username = etUsername.getText().toString().trim();
        pwd = etPwd.getText().toString().trim();
        ensure_pwd = etEnsurepwd.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        if (!TextUtils.isEmpty(username))
        {
            if (!TextUtils.isEmpty(pwd))
            {
                if (!TextUtils.isEmpty(ensure_pwd))
                {
                    if (!TextUtils.isEmpty(email))
                    {
                        if (pwd.length() >= 6)
                        {
                            if (TextFormatUtils.isEmail(email))
                            {
                                if (pwd.equals(ensure_pwd))
                                {
                                    showProgress();
                                    app.client.registeredUser(username,pwd,email,"","","",handler);
//                                    startThread();
                                    return;
                                }
                                else
                                {
                                    T.showS(R.string.password_notequal);
                                    return;
                                }
                            }
                            else
                            {
                                T.showS(R.string.msg_email_format_error);
                                return;
                            }
                        }
                        else
                        {
                            T.showS(R.string.msg_pwd_length_error);
                            return;
                        }
                    }
                }
            }
        }
        T.showS(R.string.msg_input_cannot_empty);
    }

    private Handler handler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            dismissProgress();
            ResponseCommon response = (ResponseCommon) msg.obj;
            if (null != response)
            {
                if (null != response.h)
                {
                    if (response.h.e == 200)
                    {
                        T.showS(R.string.msg_register_success);
                        Intent it = new Intent(AcRegister.this,AcLogin.class);
                        it.putExtra("username",username);
                        setResult(RESULT_OK,it);
                        finish();
                        return true;
                    }
                    else if (response.h.e == 409)
                    {
                        T.showS(R.string.msg_username_already_exist);
                        return true;
                    }
                }
            }
            T.showS(R.string.msg_register_fail);
            return false;
        }
    });
}
