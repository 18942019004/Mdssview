package com.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gdmss.adapter.DevicelistAdapter;
import com.gdmss.base.MyBaseAdapter;

/**
 * Created by apple on 2016/11/23.
 */

public class RefreshReceiver extends BroadcastReceiver
{
    private DevicelistAdapter adapter;

    public RefreshReceiver()
    {
        super();
    }

    public RefreshReceiver(DevicelistAdapter adapter)
    {
        this.adapter = adapter;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (adapter != null)
        {
            adapter.doRefresh();
        }
    }
}
