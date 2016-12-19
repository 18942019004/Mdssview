package com.gdmss.activities;

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

/**
 * Created by Administrator on 2016/12/2.
 */

public class AcForgetPassword extends BaseActivity
{
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.btn_retrive)
    Button btnRetrive;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_forgetpassword);
        ButterKnife.bind(this);
        setView();
        btnRetrive.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                retrive();
            }
        });
    }

    void setView()
    {
        String user = getIntent().getStringExtra("user");
        if (!TextUtils.isEmpty(user))
        {
            etUsername.setText(user);
        }
    }

    void retrive()
    {
        String userName = etUsername.getText().toString().trim();
        if (TextUtils.isEmpty(userName))
        {
            T.showS(R.string.msg_input_cannot_empty);
            return;
        }
        showProgress();
        app.client.resetUserPassword(userName,1,han);
    }

    private Handler han = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            dismissProgress();
            ResponseCommon responseCommon = (ResponseCommon) msg.obj;
            if (responseCommon != null && responseCommon.h != null)
            {
                if (responseCommon.h.e == 200)
                {
                    T.showL(R.string.msg_resetemail_has_been_sent);
                    finish();
                }
                else if (responseCommon.h.e == 406)
                {
                }
                else
                {
                }
            }
            else
            {
            }
            return false;
        }
    });
}
