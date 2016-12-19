package com.utils;

import android.os.Handler;
import android.util.Log;

import com.Player.Core.PlayerClient;
import com.Player.Source.TDevWifiInfor;

public class WifiSetThread extends Thread
{
    public static final int SET_OK = 0;
    public static final int SET_FALL = 1;
    PlayerClient pc;
    String deviceId;
    TDevWifiInfor devWifiInfo;
    Handler handler;

    public WifiSetThread(PlayerClient pc,String deviceId,TDevWifiInfor devWifiInfo,Handler handler)
    {
        this.pc = pc;
        this.deviceId = deviceId;
        this.devWifiInfo = devWifiInfo;
        this.handler = handler;
    }

    @Override
    public void run()
    {
        Log.d("setWifiInfo",devWifiInfo.toString() + "");
        int ret = pc.CameraSetWIFIConfigEx(deviceId,devWifiInfo);

        if (ret > 0)
        {
            handler.sendEmptyMessage(SET_OK);
        }
        else
        {
            handler.sendEmptyMessage(SET_FALL);
        }

    }
}
