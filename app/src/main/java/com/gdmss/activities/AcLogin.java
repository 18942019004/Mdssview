package com.gdmss.activities;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.Player.web.response.DevItemInfo;
import com.Player.web.response.ResponseCommon;
import com.Player.web.response.ResponseDevList;
import com.gdmss.R;
import com.gdmss.base.BaseActivity;
import com.gdmss.entity.PlayNode;
import com.gdmss.entity.UserInfo;
import com.utils.AlarmUtils;
import com.utils.CommonData;
import com.utils.DataUtils;
import com.utils.L;
import com.utils.T;
import com.utils.Utils;
import com.utils.WriteLogThread;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AcLogin extends BaseActivity implements OnClickListener
{
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_forget)
    TextView tvForget;
    @BindView(R.id.btn_locallogin)
    Button btnLocallogin;
    private Button btn_login;

    private EditText et_user, et_pwd;

    private CheckBox cb_autologin;

    private String sUsername, sPassword;

    private UserInfo user;

    private boolean autoLogin;

    private boolean fromLogout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_login);
        ButterKnife.bind(this);
        AlarmUtils.initPush(context);
        initParameters();
        initViews();
        setViews();
    }


    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        user = app.currentUser;
        et_user.setText(user.getsUserName());
        et_pwd.setText("");
        cb_autologin.setChecked(false);
    }


    /**
     * 初始化参数
     */
    private void initParameters()
    {
        fromLogout = getIntent().getBooleanExtra("logout",false);
        user = readUserInfo();
        if (null == user)
        {
            user = new UserInfo();
        }
    }

    /**
     * 初始化控件
     */
    private void initViews()
    {
        et_user = (EditText) findViewById(R.id.et_username);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        cb_autologin = (CheckBox) findViewById(R.id.cb_autologin);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvForget.setOnClickListener(this);
        btnLocallogin.setOnClickListener(this);
        //下划线
        tvRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvForget.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    private void setViews()
    {
        if (fromLogout)
        {
            user = app.currentUser;
        }
        if (user.getsUserName() != null)
        {
            et_user.setText(user.getsUserName());
            et_user.setSelection(user.getsUserName().length());
        }
        if (user.isbAutoLogin())
        {
            if (user.getsPassWord() != null)
            {
                et_pwd.setText(user.getsPassWord());
            }
            btn_login.performClick();
        }
        else
        {
            et_pwd.setText("");
        }
        cb_autologin.setChecked(user.isbAutoLogin());
    }

    /**
     * 检查输入
     *
     * @return
     */
    private boolean checkInput()
    {
        boolean result = false;
        sUsername = et_user.getText().toString().trim();
        sPassword = et_pwd.getText().toString().trim();
        if (Utils.isEmpty(sUsername))
        {
            et_user.requestFocus();
            T.showS(R.string.plear_enter_username);
        }
        else if (Utils.isEmpty(sPassword))
        {
            et_pwd.requestFocus();
            T.showS(R.string.plear_enter_password);
        }
        else
        {
            result = true;
        }
        return result;
    }

    /**
     * 登陆
     */

    void login(boolean isLocalMode)
    {
        if (isLocalMode)
        {
//            user = new UserInfo(sUsername,sPassword,cb_autologin.isChecked());
//            app.currentUser = user;
            app.currentUser.setsUserName(CommonData.LOCAL_USER);
            app.currentUser.setsPassWord(CommonData.LOCAL_PWD);
            saveUserInfo();
//            app.client.getNodeList(true,0,0,0,loadListHandler);
            app.client.setLocalList(true);
            app.client.loginServerAtUserId(this,"","",loginHandler);
        }
        else
        {
            app.client.loginServerAtUserId(this,sUsername,sPassword,loginHandler);
        }
    }

    private Handler loginHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            ResponseCommon response = (ResponseCommon) msg.obj;
            if (null != response && null != response.h)
            {
                if (response.h.e == 200)
                {
                    user = new UserInfo(sUsername,sPassword,cb_autologin.isChecked());
                    app.client.setPushToken(AlarmUtils.CID);

                    app.currentUser = user;
                    saveUserInfo();
                    app.client.getNodeList(0,0,0,loadListHandler);
                    new WriteLogThread().start();

                    app.client.setUserPush(1,Utils.isZh(app) ? 2 : 1,AlarmUtils.CID,1,0,new Handler()
                    {
                        @Override
                        public void handleMessage(Message msg)
                        {
                            ResponseCommon responseCommon = (ResponseCommon) msg.obj;
                            if (responseCommon != null && responseCommon.h != null && responseCommon.h.e == 200)
                            {
                                L.i("setUserPush","设置用户推送成功");
                            }
                            else
                            {
                                L.i("setUserPush","设置用户推送失败");
                            }
                        }
                    });
                    return true;
                }
                else if (response.h.e == 406)
                {
                    dismissProgress();
                    T.showS(R.string.msg_usr_pwd_error);
                    return true;
                }
            }
            T.showS(R.string.msg_login_fail);
            dismissProgress();
            return false;
        }
    });

    private Handler loadListHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            ResponseDevList response = (ResponseDevList) msg.obj;
            if (null != response && null != response.h)
            {
                int error = response.h.e;
                if (error == 200)
                {
                    List<DevItemInfo> items = response.b.nodes;
                    List<PlayNode> list = new ArrayList<>();
                    for (int i = 0; i < items.size(); i++)
                    {
                        DevItemInfo item = items.get(i);
                        if (null != item)
                        {
                            PlayNode tempNode = PlayNode.DataConversion(items.get(i));
                            list.add(tempNode);
                        }
                    }
                    DataUtils.sortList(app,list);
                }
            }
            dismissProgress();
            Intent it = new Intent(AcLogin.this,AcMain.class);
            //            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK & Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
            finish();
            return false;
        }
    });

    private void saveUserInfo()
    {
        // Utils.saveJsonObject(user,Path.userinfo);
        Utils.saveString(context,"username",sUsername);
        Utils.saveString(context,"password",sPassword);
        Utils.saveBoolean(context,"autologin",cb_autologin.isChecked());
    }

    private UserInfo readUserInfo()
    {
        String username = Utils.ReadString(context,"username");
        String password = Utils.ReadString(context,"password");
        autoLogin = Utils.ReadBoolean(context,"autologin");
        return new UserInfo(username,password,autoLogin);
        // return Utils.readJsonObject(Path.userinfo);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_login:
                if (!checkInput())
                {
                    return;
                }
                showProgress(R.string.msg_connecting_please_wait);
                login(false);
                break;
            case R.id.btn_locallogin:
                showProgress(R.string.msg_connecting_please_wait);
                login(true);
                break;
            case R.id.tv_register:
                startActivityForResult(new Intent(this,AcRegister.class),555);
                break;
            case R.id.tv_forget:
                startActivity(new Intent(this,AcForgetPassword.class).putExtra("user",et_user.getText().toString().trim()));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if (requestCode == 555 && resultCode == RESULT_OK)
        {
            String userName = data.getStringExtra("username");
            if (!TextUtils.isEmpty(userName))
            {
                et_user.setText(userName);
            }
        }
    }
}