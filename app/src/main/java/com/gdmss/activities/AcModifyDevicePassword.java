package com.gdmss.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.Player.Core.PlayerClient;
import com.gdmss.R;
import com.gdmss.base.BaseActivity;
import com.gdmss.entity.PlayNode;
import com.utils.L;
import com.utils.T;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/28.
 */

public class AcModifyDevicePassword extends BaseActivity
{
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_newpwd)
    EditText etNewpwd;
    @BindView(R.id.et_ensurepwd)
    EditText etEnsurepwd;
    @BindView(R.id.btn_sure)
    Button btnSure;
    String pwd, newpwd, ensurepwd;
    PlayNode node;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_modifydevicepassword);
        ButterKnife.bind(this);
        initParam();
        btnSure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getInput();
                checkInput();
            }
        });
    }

    void initParam()
    {
        PlayNode tempNode = (PlayNode) getIntent().getSerializableExtra("node");
        List<PlayNode> list = app.cameraMap.get(tempNode.node.dwNodeId + "");
        node = list.get(0);
    }

    void getInput()
    {
        pwd = etPwd.getText().toString();
        newpwd = etNewpwd.getText().toString();
        ensurepwd = etEnsurepwd.getText().toString();
    }

    void checkInput()
    {
        getInput();
        modifyPwd();
//        if (TextUtils.isEmpty(pwd))
//        {
//            T.showS("请输入完整");
//            return;
//        }
//        if (!app.currentUser.getsPassWord().equals(pwd))
//        {
//            T.showS("密码与原密码不匹配");
//            return;
//        }
//        if (!TextUtils.isEmpty(newpwd))
//        {
//            if (!TextUtils.isEmpty(ensurepwd))
//            {
        if (newpwd.equals(ensurepwd))
        {
//                    app.client.modifyUserPassword(pwd, newpwd, modifyHan);
            return;
        }
        else
        {
            T.showS("两次密码输入不一致");
            return;
        }
//            }
//        }
//        T.showS("请输入完整");
    }

    void modifyPwd()
    {
        showProgress("连接中，请稍候...");
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                PlayerClient client = new PlayerClient();
                L.e(node.node.sDevId);
                L.e(node.getUserName());
                int result = client.CameraSetPassword(node.getDeviceId(),node.getUserName(),pwd,newpwd);
                L.e("result:" + result);
                han.sendEmptyMessage(result);
            }
        }).start();
    }

    private Handler han = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            dismissProgress();
            switch (msg.what)
            {
                case 0:

                    break;
                case 1:
                    T.showS("修改成功");
                    break;
                case -112:

                    break;
            }
            return false;
        }
    });
}