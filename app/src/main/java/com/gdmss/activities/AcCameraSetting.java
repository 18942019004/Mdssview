package com.gdmss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gdmss.R;
import com.gdmss.base.BaseActivity;
import com.gdmss.entity.PlayNode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 通道修改菜单1:修改通道名；<p>2:wifi配置</p>;3:报警设置;4:摄像头参数设
 */

public class AcCameraSetting extends BaseActivity implements View.OnClickListener
{

    @BindView(R.id.tv_modifyname)
    TextView tvModifyname;
    @BindView(R.id.tv_wifisetting)
    TextView tvWifisetting;
    @BindView(R.id.tv_alarm)
    TextView tvAlarm;
    @BindView(R.id.tv_cameraparam)
    TextView tvCameraparam;

    PlayNode node;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_camerasetting);
        ButterKnife.bind(this);
        initParam();
        setListeners();
    }

    void initParam()
    {
        node = (PlayNode) getIntent().getSerializableExtra("node");
    }

    void setListeners()
    {
        tvModifyname.setOnClickListener(this);
        tvWifisetting.setOnClickListener(this);
        tvAlarm.setOnClickListener(this);
        tvCameraparam.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent it = null;
        switch (v.getId())
        {
            case R.id.tv_modifyname:
                it = new Intent(this,AcModifyName.class);
                break;
            case R.id.tv_wifisetting:
                it = new Intent(this,AcWifiList.class);
                break;
            case R.id.tv_alarm:
                it = new Intent(this,AcAlarmSetting.class);
                break;
            case R.id.tv_cameraparam:
                it = new Intent(this,AcCameraParams.class);
                break;
        }
        assert it != null;
        it.putExtra("node",node);
        startActivity(it);
    }
}
