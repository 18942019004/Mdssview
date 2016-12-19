package com.gdmss.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.Player.Core.PlayerClient;
import com.Player.Source.TDateTime;
import com.gdmss.R;
import com.gdmss.base.BaseActivity;
import com.gdmss.entity.PlayNode;
import com.utils.T;

import java.util.Calendar;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 同步设备时间
 */
public class AcModifyDeviceTime extends BaseActivity
{
    @BindView(R.id.tv_phoneTime)
    TextView tvPhoneTime;
    @BindView(R.id.tv_devTime)
    TextView tvDevTime;
    @BindView(R.id.btn_sure)
    Button btnSure;

    PlayNode node;

    TDateTime td_devTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_modifydevicetime);
        ButterKnife.bind(this);
        initParam();
        getDevTime();
        btnSure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                syncDevTime();
            }
        });
    }

    void initParam()
    {
        node = (PlayNode) getIntent().getSerializableExtra("node");
    }

    void getDevTime()
    {
        showProgress("获取设备时间 请稍候...");
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                PlayerClient client = new PlayerClient();
                td_devTime = client.CameraGetDevTime(node.getDeviceId());
                han.sendEmptyMessage(null == td_devTime ? FAIL : SUCCESS);
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
                case FAIL:
                    T.showS("获取设备时间失败");
                    finish();
                    break;
                case SUCCESS:
                    setViews();
                    break;
            }
            return false;
        }
    });

    void setViews()
    {
        String devTime = td_devTime.iYear + "-" + td_devTime.iMonth + "-" + td_devTime.iDay + "   " + td_devTime.iHour + ":" + td_devTime.iMinute;
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String phoneTime = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "   " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);

        tvPhoneTime.setText(phoneTime);
        tvDevTime.setText(devTime);
    }

    void syncDevTime()
    {
        showProgress("设置中 请稍候...");
        startThread();
    }

    @Override
    protected void runInThread()
    {
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        TDateTime dateTime = new TDateTime();
        dateTime.iYear = calendar.get(Calendar.YEAR);
        dateTime.iMonth = calendar.get(Calendar.MONTH) + 1;
        dateTime.iDay = calendar.get(Calendar.DAY_OF_MONTH);
        dateTime.iHour = calendar.get(Calendar.HOUR_OF_DAY);
        dateTime.iMinute = calendar.get(Calendar.MINUTE);
        dateTime.iMinsecond = calendar.get(Calendar.SECOND);
        PlayerClient client = new PlayerClient();
        int result = client.CameraSetDevTime(node.getDeviceId(),dateTime);
        msgHandler.sendEmptyMessage(result);
    }

}
