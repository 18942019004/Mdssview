package com.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.gdmss.R;
import com.gdmss.entity.AlarmMessage;
import com.gdmss.entity.AlarmPushMessage;
import com.gdmss.entity.OptionInfo;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;

/**
 * Created by Administrator on 2016/12/7.
 */

public class AlarmIntentService extends GTIntentService
{
    NotificationManager nm;// = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    public AlarmIntentService()
    {

    }

    @Override
    public void onReceiveServicePid(Context context,int i)
    {
        L.e("onReceiveClientId -> " + "clientid = ");
    }

    @Override
    public void onReceiveClientId(Context context,String s)
    {
        AlarmUtils.CID = s;
        L.e("onReceiveClientId -> " + "onReceiveClientId = " + s);
    }

    @Override
    public void onReceiveMessageData(Context context,GTTransmitMessage gtTransmitMessage)
    {
        L.e("onReceiveMessageData -> " + "onReceiveMessageData = ");
        byte[] payload = gtTransmitMessage.getPayload();
        if (null != payload)
        {
            String msg = new String(payload);
            if (!TextUtils.isEmpty(msg))
            {
                AlarmPushMessage alarmMessage;
                L.e("开始转换对象");
                alarmMessage = JSON.parseObject(msg,AlarmPushMessage.class);
                if (null != alarmMessage)
                {
                    if (null != alarmMessage.aps)
                    {
                        push(context,alarmMessage);
                    }
                }
                L.e("转换成功");
            }
        }
    }

    @Override
    public void onReceiveOnlineState(Context context,boolean b)
    {
        L.e("onReceiveClientId -> " + "onReceiveOnlineState = ");
    }

    @Override
    public void onReceiveCommandResult(Context context,GTCmdMessage gtCmdMessage)
    {

    }

    void push(Context context,AlarmPushMessage message)
    {
        if (null == nm)
        {
            nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentTitle(toTitle(context,message));// 设置通知栏标题

        mBuilder.setSmallIcon(R.drawable.icon);// 设置通知小ICON

        mBuilder.setContentText(message.aps.alert.body);

        mBuilder.setTicker(message.aps.alert.body); // 通知首次出现在通知栏，带上升动画效果的

        mBuilder.setWhen(System.currentTimeMillis());// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间

        mBuilder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);


        mBuilder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm));

        OptionInfo optionInfo = Utils.readJsonObject(Path.optionInfo, OptionInfo.class);
    }

    String toTitle(Context context,AlarmPushMessage alarmMessage)
    {
        return String.format(context.getString(R.string.message_title),alarmMessage.CameraName);
    }
}
