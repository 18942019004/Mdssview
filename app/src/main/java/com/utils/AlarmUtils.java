package com.utils;

import android.content.Context;

import com.igexin.sdk.PushManager;

/**
 * Created by Administrator on 2016/12/8.
 */

public class AlarmUtils
{
    public static String CID = "";

    public static void initPush(Context context)
    {
        PushManager.getInstance().initialize(context,AlarmService.class);
        PushManager.getInstance().registerPushIntentService(context,AlarmIntentService.class);
        CID = PushManager.getInstance().getClientid(context);

    }
}
