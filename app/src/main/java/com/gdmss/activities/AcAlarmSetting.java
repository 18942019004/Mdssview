package com.gdmss.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.Player.Core.Constants;
import com.Player.Core.PlayerClient;
import com.Player.Core.PlayerCore;
import com.Player.Source.TAlarmMotionDetect;
import com.Player.Source.TAlarmSetInfor;
import com.Player.web.response.NotifyStateInfo;
import com.Player.web.response.ResponseCommon;
import com.Player.web.response.ResponseQueryAlarmSettings;
import com.gdmss.R;
import com.gdmss.base.BaseActivity;
import com.gdmss.entity.PlayNode;
import com.utils.AlarmUtils;
import com.utils.L;
import com.utils.T;
import com.utils.ThreadPool;
import com.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */

public class AcAlarmSetting extends BaseActivity
{

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tg_push)
    ToggleButton tgPush;
    @BindView(R.id.tg_alarmtoggle)
    ToggleButton tgAlarmtoggle;
    @BindView(R.id.tv_sensor)
    TextView tvSensor;
    @BindView(R.id.btn_save)
    Button btnSave;

    private PlayNode node;

    private boolean isOpenPush, isOpenAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_alarmsetting);
        ButterKnife.bind(this);
        initParam();
        setViews();
        getParameters();
    }


    void initParam()
    {
        node = (PlayNode) getIntent().getSerializableExtra("node");
        core = new PlayerCore(AcAlarmSetting.this);
        client = new PlayerClient();
    }

    void setViews()
    {
        tvName.setText(node.getName());
        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (isOpenPush != tgPush.toggleOn)
                {
                    showProgress();
                    ThreadPool.getInstance().submit(new setAlarmPush());
                }
                if (isOpenAlarm != tgAlarmtoggle.toggleOn)
                {
                    showProgress();
                    if (null != motion)
                    {
                        motion.bIfEnable = isOpenAlarm ? 0 : 1;
                        ThreadPool.getInstance().submit(new setAlarmDetect());
                    }
                }
            }
        });
    }

    void getParameters()
    {
        showProgress();
        ThreadPool.getInstance(1).submit(new getAlarmPush());
    }

    PlayerCore core;

    PlayerClient client;


    //获取布防参数
    class getAlarmDetect implements Runnable
    {
        @Override
        public void run()
        {
            TAlarmMotionDetect alarmMotion = new TAlarmMotionDetect();
            core.CameraGetAlarmMotionEx(node.getChNo(),node.getDeviceId(),alarmMotion);
            Message msg = Message.obtain();
            msg.obj = alarmMotion;
            motionHandler.sendMessage(msg);
        }
    }

    //获取推送通知参数
    class getAlarmPush implements Runnable
    {
        @Override
        public void run()
        {
            app.client.queryAlarmSettings(node.node.sDevId,pushHandler);
        }
    }

    class setAlarmPush implements Runnable
    {
        @Override
        public void run()
        {
            int[] events = new int[] {1,2,3,4,5};
            String[] devs = new String[] {node.node.sDevId};
            app.client.alarmSettings(isOpenPush ? 2 : 1,AlarmUtils.CID,events,setPushHandler,devs);
        }
    }

    class setAlarmDetect implements Runnable
    {
        @Override
        public void run()
        {
            if (null != motion)
            {
                int result = core.CameraSetAlarmMotion(node.getDeviceId(),motion);
                setAlarmHandler.sendEmptyMessage(result);
            }
        }
    }

    TAlarmMotionDetect motion;
    //处理获取到的布防参数
    private Handler motionHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message message)
        {
            dismissProgress();
            motion = (TAlarmMotionDetect) message.obj;
            if (null == motion)
            {
                T.showS(R.string.msg_get_params_fail);
                finish();
                return true;
            }
            isOpenAlarm = motion.bIfEnable == 1;
            tgAlarmtoggle.setChecked(motion.bIfEnable == 1);
            L.e("level:" + motion.iLevel);
            return false;
        }
    });

    //处理获取到的推送通知参数
    private Handler pushHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message message)
        {
            ResponseQueryAlarmSettings responseQueryAlarmSettings = (ResponseQueryAlarmSettings) message.obj;
            if (responseQueryAlarmSettings != null && responseQueryAlarmSettings.h != null)
            {
                if (responseQueryAlarmSettings.h.e == 200 && responseQueryAlarmSettings.b != null
                        && responseQueryAlarmSettings.b.devs != null
                        && responseQueryAlarmSettings.b.devs.length > 0)
                {
                    TAlarmSetInfor alarmInfo = TAlarmSetInfor.toTAlarmSetInfor(responseQueryAlarmSettings.b.devs[0]);
                    if (alarmInfo.bIfSetAlarm == 1)
                    {
                        isOpenPush = isNotify(alarmInfo);
                        tgPush.setChecked(isOpenPush);
                    }
                    ThreadPool.getInstance().submit(new getAlarmDetect());
                    return true;
                }
            }
            dismissProgress();
            T.showS(R.string.msg_get_params_fail);
            finish();
            return false;
        }
    });

    private Handler setPushHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message message)
        {
            dismissProgress();
            ResponseCommon commonSocketText = (ResponseCommon) message.obj;
            if (commonSocketText != null && commonSocketText.h.e == 200)
            {
                T.showS(R.string.msg_set_params_success);
                finish();
                return true;
            }
            T.showS(R.string.msg_set_params_fail);
            return false;
        }
    });
    private Handler setAlarmHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message message)
        {
            dismissProgress();
            if (message.what == 0)
            {
                T.showS(R.string.msg_set_params_success);
                finish();
                return true;
            }
            T.showS(R.string.msg_set_params_fail);
            return false;
        }
    });

    //根据获取到的推送参数，判断本机是否已经开启推送
    boolean isNotify(TAlarmSetInfor info)
    {
        if (null != info && null != info.notifies)
        {
            for (NotifyStateInfo state : info.notifies)
            {
                if (state.notify_type == Constants.NPC_D_MON_ALARM_NOTIFY_TYPE_PHONE_PUSH)
                {
                    if (state.notify_param.equals(AlarmUtils.CID))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
