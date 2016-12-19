

package com.gdmss.activities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.Player.web.response.ResponseCommon;
import com.gdmss.R;
import com.gdmss.base.BaseActivity;
import com.gdmss.entity.OptionInfo;
import com.utils.L;
import com.utils.Path;
import com.utils.Utils;
import com.widget.ToggleButton;

@SuppressLint("ValidFragment")
public class AcLocalSetting extends BaseActivity implements OnClickListener, ToggleButton.OnToggleChanged, OnCheckedChangeListener
{
    private LinearLayout rl_ptz;

    private TextView tv_ptz;

    private TextView tv_modifyPwd;

    private OptionInfo option;

    private ToggleButton tg_scale, tg_disturb, tg_alarmsound;

    private RadioGroup rg_stream;

    private RadioButton rbtn_smooth, rbtn_realTime;

    private Button btn_logout;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fg_localsetting);
        initViews();
        setViews();
    }

    void setViews()
    {
        option = OptionInfo.getInstance();

        tv_ptz.setText(option.getPtzLength() + "x");

        tg_scale.setChecked(option.isScreenScale());

        tg_disturb.setChecked(option.isDisturbMode());

        tg_alarmsound.setChecked(option.isOpenAlarmSound());

        if (option.getPlayMode() == 1)
        {
            rbtn_smooth.setChecked(true);
        }
    }

    private void initViews()
    {
        rl_ptz = (LinearLayout) findViewById(R.id.rl_ptz);

        tv_ptz = (TextView) findViewById(R.id.tv_ptz);

        tg_scale = (ToggleButton) findViewById(R.id.tg_scale);

        tg_disturb = (ToggleButton) findViewById(R.id.tg_disturb);

        tg_alarmsound = (ToggleButton) findViewById(R.id.tg_alarmsound);

        rg_stream = (RadioGroup) findViewById(R.id.rg_stream);

        rbtn_smooth = (RadioButton) findViewById(R.id.rbtn_smooth);

        rbtn_realTime = (RadioButton) findViewById(R.id.rbtn_realtime);

        tv_modifyPwd = (TextView) findViewById(R.id.tv_modifyPwd);

        btn_logout = (Button) findViewById(R.id.btn_logout);


        rl_ptz.setOnClickListener(this);

        tg_scale.setOnToggleChanged(this);

        tg_disturb.setOnToggleChanged(this);

        tg_alarmsound.setOnToggleChanged(this);

        rg_stream.setOnCheckedChangeListener(this);

        tv_modifyPwd.setOnClickListener(this);

        btn_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.rl_ptz:
                startActivityForResult(new Intent(context,AcPtzSetting.class),100);
                break;
            case R.id.btn_logout:
                progress.show();
                app.client.logoutServer(1,logoutHan);
                break;
            case R.id.tv_modifyPwd:
                startActivity(new Intent(this,AcModifyPassword.class));
                break;
        }
    }

    private Handler logoutHan = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            progress.dismiss();
            ResponseCommon response = (ResponseCommon) msg.obj;
            if (null != response && null != response.h)
            {
                L.e("成功注销");
                finish();
                Intent it = new Intent(AcLocalSetting.this,AcLogin.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP & Intent.FLAG_ACTIVITY_NEW_TASK);
                app.clearData();
                it.putExtra("logout",true);
                startActivity(it);
            }
            return false;
        }
    });

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK)
        {
            int length = data.getIntExtra("length",5);
            tv_ptz.setText(length + "x");
            option.setPtzLength(length);
            Utils.saveJsonObject(option,Path.optionInfo);
        }
        super.onActivityResult(requestCode,resultCode,data);
    }


    @Override
    public void onToggle(View v,boolean on)
    {
        switch (v.getId())
        {
            case R.id.tg_scale:
                option.setScreenScale(on);
                break;
            case R.id.tg_disturb:
                option.setDisturbMode(on);
                break;
            case R.id.tg_alarmsound:
                option.setOpenAlarmSound(on);
                break;
        }
        Utils.saveJsonObject(option,Path.optionInfo);
    }

    @Override
    public void onCheckedChanged(RadioGroup group,int checkedId)
    {
        if (checkedId == rbtn_smooth.getId())
        {
            option.setPlayMode(1);
        }
        else
        {
            option.setPlayMode(0);
        }
        Utils.saveJsonObject(option,Path.optionInfo);
    }

}
